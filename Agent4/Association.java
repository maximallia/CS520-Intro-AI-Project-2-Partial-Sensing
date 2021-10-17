// AUTHOR: Zachary Tarman (zpt2)


import java.util.ArrayList;

// TREAT THIS ENTIRE OBJECT TYPE AS A SYSTEM OF EQUATIONS
	// WE WANT OUR AGENT TO KEEP A LIST OF THESE SO THAT HE MIGHT BE ABLE TO
	// DETECT NEW INFORMATION OR COMBINE EQUATIONS TO YIELD INTERESTING RESULTS
public class Association {
	
	// THIS INNER CLASS IS ESSENTIALLY THE "NODE" OF THIS PARTICULAR DATA STRUCTURE
	public static class Unit {
		
		// FIELDS
		private CellInfo c; // STORES THE cellInfo OBJECT THAT WILL BE USED TO IDENTIFY THIS UNIT OF THE ASSOCIATION
			// IN TERMS OF THE SYSTEMS OF EQUATIONS ANALOGY, CELLS CAN ONLY BE OF VALUE 0 OR 1 (EMPTY OR BLOCKED RESPECTIVELY)
		private int factor; // THE FACTOR OF THIS TERM OF THE ASSOCIATION (THINK SYSTEMS OF EQUATIONS)
		
		// CONSTRUCTOR
		public Unit(CellInfo c, int factor) {
			this.c = c;
			this.factor = factor;
		}
		
		
		// METHODS
		public CellInfo getCell() { // RETURNS THE CELL OF THIS TERM
			return this.c;
		}
		
		public int getFactor() { // RETURNS THE FACTOR OF THIS TERM
			return this.factor;
		}
		
		public void updateFactor(int u) { // UPDATES THE FACTOR OF A GIVEN TERM
			// THIS IS PARTICULARLY USEFUL WHEN CALCULATING A NEW EQUATION / FACT TO ADD TO OUR DATABASE
			this.factor = u;
			return;
		}
		
	}
	
	
	// FIELDS
	public ArrayList<Association.Unit> allUnknowns; // STORES ALL OF THE UNCONFIRMED NEIGHBORS OF A CELL AND THEIR ASSOCIATED FACTOR WITHIN THE "EQUATION"
		// FOR EXAMPLE, WHEN DOING A SYSTEM OF EQUATIONS, YOU MAY END UP WITH NEGATIVE TERMS OR TERMS OF FACTORS GREATER THAN 1
		// THIS IS TO KEEP A CONSISTENT EQUATION WHEN TRYING TO REDUCE TERMS
	public int totalBlocks; // THIS WILL KEEP THE TOTAL NUMBER OF BLOCKS FOR THIS CERTAIN ASSOCIATION

	
	
	
	// CONSTRUCTOR
	public Association(ArrayList<Association.Unit> allUnknowns, int totalBlocks) {
		this.allUnknowns = allUnknowns;
		this.totalBlocks = totalBlocks;
	}
	

	
	
	// UPDATE THE BLOCK COUNT OF A GIVEN ASSOCIATION
	public void updateBlockCount(int b) {
		this.totalBlocks = b;
		return;
	}
	
	// RETURNS THE CELLS THAT ARE A PART OF THIS ASSOCIATION
	public ArrayList<CellInfo> getCells() {
		ArrayList<CellInfo> variables = new ArrayList<CellInfo>();
		for (int i = 0; i < this.allUnknowns.size(); i++) {
			variables.add(this.allUnknowns.get(i).getCell());
		}
		return variables;
	}
	
	// TELLS US IF A CERTAIN CELL IS WITHIN THIS ASSOCIATION
	// MOSTLY USED WHEN WE'RE TRYING TO REMOVE CONFIRMED CELLS AS A VARIABLE
	// FROM OUR DATABASE OF FACTS
	public int contains(CellInfo c) {
		
		for (int i = 0; i < this.allUnknowns.size(); i++ ) {
			if (allUnknowns.get(i).c.getPos().getX() == c.getPos().getX() &&
					allUnknowns.get(i).c.getPos().getY() == c.getPos().getY()) {
				return i;
			}
		}
		
		return -1;
		
	}
	
	// THIS ATTEMPTS TO COMBINE TWO ASSOCIATIONS TO SEE IF NEW INFORMATION CAN BE YIELDED
		// RETURNS A NEW ASSOCIATION IF AN EQUATION CAN BE FOUND FROM THE TWO
		// RETURNS NULL IF WE END UP WITH NOTHING AT THE END (IF ALL TERMS CANCEL)
	public Association synthesize(Association other) {
		
		// JUST IN CASE WE ACCIDENTALLY SEND AN ASSOCIATION THAT IS ALREADY COMPLETELY DIMINISHED
		if (this.allUnknowns.size() == 0 || other.allUnknowns.size() == 0) {
			return null;
		}
		
		// FOR DEBUGGING PURPOSES
		System.out.print("Currently trying to synthesize... ");
		this.printAssociation();
		System.out.print(", ");
		other.printAssociation();
		
		int updatedBlocks; // THIS WILL GIVE US THE NEW BLOCK COUNT OF THE SYNTHESIZED ASSOCIATION (RHS OF EQUATION)
		boolean otherFirst = false; // THIS INDICATES WHICH OF THE EXISTING ASSOCIATIONS WILL BE SUBTRACTED FROM THE OTHER
	
		// WE WANT TO KEEP THE NUMBER OF BLOCKS IN THE ASSOCIATION NON-NEGATIVE JUST FOR SIMPLICITY'S SAKE
		if (this.totalBlocks - other.totalBlocks >= 0) {
			updatedBlocks = this.totalBlocks - other.totalBlocks;
		} else {
			updatedBlocks = other.totalBlocks - this.totalBlocks;
			otherFirst = true;
		}
		
		ArrayList<Association.Unit> updatedUnknowns = new ArrayList<Association.Unit>(); // THIS STORES THE NEW ASSOCIATION TERMS (LHS OF EQUATION)
		
		// LOOP THROUGH BOTH ASSOCIATIONS AND APPEND THEIR TERMS TO THE NEW ASSOCIATION
		for (int i = 0; i < this.allUnknowns.size(); i++) {
			if (otherFirst) {
				updatedUnknowns.add(new Association.Unit(this.allUnknowns.get(i).getCell(), this.allUnknowns.get(i).getFactor() * -1));
			} else {
				updatedUnknowns.add(new Association.Unit(this.allUnknowns.get(i).getCell(), this.allUnknowns.get(i).getFactor()));
			}
		}
		for (int i = 0; i < other.allUnknowns.size(); i++) {
			if (!otherFirst) {
				updatedUnknowns.add(new Association.Unit(other.allUnknowns.get(i).getCell(), other.allUnknowns.get(i).getFactor() * -1));
			} else {
				updatedUnknowns.add(new Association.Unit(other.allUnknowns.get(i).getCell(), other.allUnknowns.get(i).getFactor()));
			}
		}
		
		// COMBINE LIKE TERMS IN THE NEW EQUATION (REDUCE AS MUCH AS POSSIBLE)
		updatedUnknowns = combineLikeTerms(updatedUnknowns);
		
		if (updatedUnknowns.size() == 0) {
			// System.out.println("This was not a useful reduction.");
			return null;
		}
		
		Association result = new Association(updatedUnknowns, updatedBlocks);
		
		// FOR DEBUGGING PURPOSES
		System.out.println("We were able to find a new possibly useful association.");
		result.printAssociation();
		
		return result;
				
		
	}
	
	// THIS IS ESSENTIALLY AN EXTENSION OF THE NEW AND IMPROVED INFERENCE AGENT
	// THIS WILL TELL US IF THERE IS SOMETHING WE CAN INFER ABOUT THE CELLS THAT THE AGENT COULDN'T SEE BEFORE
	// RETURNS THE CELLS THAT HAVE CHANGED SO WE CAN UPDATE SURROUDING CELLS ON THE AGENT SIDE
		// RETURN AN EMPTY LIST IF NOTHING WAS INFERRED
	public ArrayList<CellInfo> process() {
		
		ArrayList<CellInfo> cellsThatChanged = new ArrayList<CellInfo>(); // STORES NEWLY CONFIRMED CELLS
		
		// FOR DEBUGGING PURPOSES
		System.out.println("Currently processing...");
		this.printAssociation();
		
		// INFERENCE RULE 1
		if (this.allUnknowns.size() == 1) {
			// WE KNOW THE IDENTITY OF THIS CELL BECAUSE THERE'S ONLY ONE TERM IN THE EQUATION
			// VERY POWERFUL INFERENCE TOOL, BUT NOT TOO COMMON TO OCCUR
			System.out.println("We've entered IR1.");
			CellInfo temp = this.allUnknowns.get(0).getCell();
			if (this.totalBlocks == this.allUnknowns.get(0).getFactor()) {
				temp.setBlocked();
				cellsThatChanged.add(temp);
				System.out.println("IR1: We processed an association and found that " +
						temp.getPos().getX() + ", " + temp.getPos().getY() +
						" was able to be confirmed as blocked.");
			} else if (this.totalBlocks == 0) {
				temp.setEmpty();
				cellsThatChanged.add(temp);
				System.out.println("IR1: We processed an association and found that " +
						temp.getPos().getX() + ", " + temp.getPos().getY() +
						" was able to be confirmed as empty.");
			}
			
			return cellsThatChanged;
		}
		
		int positiveFactors = 0; // STORES THE NUMBER OF POSITIVE TERMS IN THE ASSOCIATION
		int sum = 0; // SUMS UP ALL THE FACTORS OF THE VARIOUS TERMS IN THE EQUATION
		int positiveSum = 0; // STORES THE SUM FROM THE POSITIVE TERMS
		for (int a = 0; a < this.allUnknowns.size(); a++) {
			if (this.allUnknowns.get(a).getFactor() > 0) {
				positiveFactors++;
				positiveSum += this.allUnknowns.get(a).getFactor();
			}
			sum += this.allUnknowns.get(a).getFactor();
		}
		
		System.out.println("Positive factors: " + positiveFactors + ", sum: " + sum + ", positive sum: " + positiveSum + ", size: " + this.allUnknowns.size());
		
		// INFERENCE RULE 2
		if (sum == 0 && this.allUnknowns.size() == 2 && this.totalBlocks == Math.abs(this.allUnknowns.get(0).getFactor())) {
			// ONE OF THESE TWO VARIABLES MUST BE BLOCKED
				// WE SEE THE DIFFERENCE IN THEIR FACTORS MAKES ZERO, SO THE CELL WITH THE POSITIVE FACTOR
				// MUST BE BLOCKED TO SATISFY THE EQUATION
			System.out.println("We've entered IR2.");
			if (this.allUnknowns.get(0).getFactor() == 1) {
				cellsThatChanged.add(this.allUnknowns.get(0).getCell());
				System.out.println("IR2: We processed an association and found that " +
						this.allUnknowns.get(0).getCell().getPos().getX() + ", " + 
						this.allUnknowns.get(0).getCell().getPos().getY() +
						" was able to be confirmed as empty.");
			} else {
				cellsThatChanged.add(this.allUnknowns.get(1).getCell());
				System.out.println("IR2: We processed an association and found that " +
						this.allUnknowns.get(1).getCell().getPos().getX() + ", " + 
						this.allUnknowns.get(1).getCell().getPos().getY() +
						" was able to be confirmed as empty.");
			}
			
			return cellsThatChanged;
		}
		
		// INFERENCE RULE 3
		if (positiveSum == this.totalBlocks) {
			// WE KNOW THE FACTORS OF ALL THE SAME SIGN (POSITIVE) MUST BE BLOCKED
				// THAT WOULD BE THE ONLY WAY TO STATISFY THIS ASSOCIATION
			System.out.println("We've entered IR3.");
			for (int i = 0; i < this.allUnknowns.size(); i++) {
				CellInfo temp = this.allUnknowns.get(i).getCell();
				if (this.allUnknowns.get(i).getFactor() > 0) {
					temp.setBlocked();
					cellsThatChanged.add(temp);
					System.out.println("IR3: We processed an association and found that " +
							temp.getPos().getX() + ", " + temp.getPos().getY() +
							" was able to be confirmed as blocked.");
				}
			}
			
			return cellsThatChanged;
		}
		
		// INFERENCE RULE 4
		if ((sum - positiveSum < 0) && (sum - positiveSum == this.totalBlocks)) {
			// WE KNOW THE FACTORS OF ALL THE SAME SIGN (NEGATIVE) MUST BE BLOCKED
				// THAT WOULD BE THE ONLY WAY TO SATISFY THIS ASSOCIATION
			System.out.println("We've entered IR4.");
			for (int i = 0; i < this.allUnknowns.size(); i++) {
				CellInfo temp = this.allUnknowns.get(i).getCell();
				if (this.allUnknowns.get(i).getFactor() < 0) {
					temp.setBlocked();
					cellsThatChanged.add(temp);
					System.out.println("IR4: We processed an association and found that " +
							temp.getPos().getX() + ", " + temp.getPos().getY() +
							" was able to be confirmed as blocked.");
				}
			}
			
			return cellsThatChanged;
		}
		
		// INFERENCE RULE 5
		if ((this.allUnknowns.size() == positiveFactors || this.allUnknowns.size() - positiveFactors == 0) && this.totalBlocks == 0) {
			// WE KNOW THAT ALL OF THE CELLS MUST BE EMPTY
				// ALL TERMS ARE OF THE SAME SIGN AND THE TOTAL BLOCKS BETWEEN THEM ARE STILL ZERO
				// THE ONLY POSSIBLE CONCLUSION IS THAT ALL CELLS MUST BE ZERO (I.E. THEY'RE EMPTY AND NOT BLOCKED)
			System.out.println("We've entered IR5.");
			for (int i = 0; i < this.allUnknowns.size(); i++) {
				CellInfo temp = this.allUnknowns.get(i).getCell();
				temp.setEmpty();
				cellsThatChanged.add(temp);
				System.out.println("IR5: We processed an association and found that " +
						temp.getPos().getX() + ", " + temp.getPos().getY() +
						" was able to be confirmed as empty.");
			}
		}
		
		return cellsThatChanged;
		
	}
	
	
	// FOR DEBUGGING PURPOSES
	public void printAssociation() {
		System.out.print("Association: ");
		for (int i = 0; i < this.allUnknowns.size(); i++) {
			System.out.print(this.allUnknowns.get(i).getFactor() + " * " + (int)this.allUnknowns.get(i).getCell().getPos().getX()
					+ "," + (int)this.allUnknowns.get(i).getCell().getPos().getY());
			System.out.print(" + ");
		}
		System.out.println(" = " + this.totalBlocks);
	}
	
	
	
	// THIS COMBINES LIKE TERMS WHEN REDUCING AN ASSOCIATION
	private ArrayList<Association.Unit> combineLikeTerms(ArrayList<Association.Unit> a) {
		
		for (int i = 0; i < a.size(); i++) {
			for (int j = i + 1; j < a.size(); j++) {
				
				CellInfo temp = a.get(i).getCell();
				CellInfo temp2 = a.get(j).getCell();
				
				if (temp.getPos().getX() == temp2.getPos().getX() &&
						temp.getPos().getY() == temp2.getPos().getY()) {
					
					a.get(i).updateFactor(a.get(i).getFactor() + a.get(j).getFactor());
					a.remove(j);
					if (a.get(i).getFactor() == 0) {
						a.remove(i);
						i--;
					}
					break;
				}
			}
		}
		
		return a;
	}
	
	
}