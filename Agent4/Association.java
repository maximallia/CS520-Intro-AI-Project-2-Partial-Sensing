// AUTHOR: Zachary Tarman (zpt2)


import java.util.ArrayList;



public class Association {
	
	// THIS INNER CLASS IS ESSENTIALLY THE "NODE" OF THIS PARTICULAR DATA STRUCTURE
	public static class Unit {
		
		private CellInfo c;
		private int factor;
		
		public Unit(CellInfo c, int factor) {
			this.c = c;
			this.factor = factor;
		}
		
		public CellInfo getCell() {
			return this.c;
		}
		
		public int getFactor() {
			return this.factor;
		}
		
		public void updateFactor(int u) {
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
	

	public void updateBlockCount(int b) {
		this.totalBlocks = b;
		return;
	}
	
	
	// TELLS US IF A CERTAIN CELL IS WITHIN THIS ASSOCIATION
	public boolean contains(CellInfo c) {
		
		for (int i = 0; i < this.allUnknowns.size(); i++ ) {
			if (allUnknowns.get(i).c.getPos().getX() == c.getPos().getX() &&
					allUnknowns.get(i).c.getPos().getY() == c.getPos().getY()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	// THIS ATTEMPTS TO REDUCE TWO ASSOCIATIONS
	// RETURNS A NEW ASSOCIATION IF A REDUCED ONE CAN BE FOUND FROM THE TWO
		// RETURNS NULL IF NOTHING HAS ACTUALLY BEEN REDUCED
	public Association reduce(Association other) {
		
		int updatedBlocks;
		boolean otherFirst = false; // THIS INDICATES WHICH OF THE EXISTING ASSOCIATIONS WILL BE THE SUBTRACTED "TERM"
	
		// WE WANT TO KEEP THE NUMBER OF BLOCKS IN THE ASSOCIATION NON-NEGATIVE JUST FOR SIMPLICITY'S SAKE
		if (this.totalBlocks - other.totalBlocks >= 0) {
			updatedBlocks = this.totalBlocks - other.totalBlocks;
		} else {
			updatedBlocks = other.totalBlocks - this.totalBlocks;
			otherFirst = true;
		}
		
		ArrayList<Association.Unit> updatedUnknowns = new ArrayList<Association.Unit>();
		
		for (int i = 0; i < this.allUnknowns.size(); i++) {
			if (otherFirst) {
				this.allUnknowns.get(i).updateFactor(this.allUnknowns.get(i).getFactor() * -1);
			}
			updatedUnknowns.add(this.allUnknowns.get(i));
		}
		for (int i = 0; i < other.allUnknowns.size(); i++) {
			if (!otherFirst) {
				other.allUnknowns.get(i).updateFactor(other.allUnknowns.get(i).getFactor() * -1);
			}
			updatedUnknowns.add(other.allUnknowns.get(i));
		}
		
		updatedUnknowns = combineLikeTerms(updatedUnknowns);
		
		// IF THIS IS TRUE, WE KNOW WE HAVEN'T REALLY EFFECTIVELY REDUCED ANYTHING
		// IT'S PROBABLY NOT USEFUL TO PUT THIS ASSOCIATION IN OUR KNOWLEDGE BASE
		if (updatedUnknowns.size() >= this.allUnknowns.size() && updatedUnknowns.size() >= other.allUnknowns.size()) {
			return null;
		}
		
		Association result = new Association(updatedUnknowns, updatedBlocks);
		return result;
				
		
	}
	
	// THIS IS ESSENTIALLY AN EXTENSION OF THE INFERENCE AGENT
	// THIS WILL TELL US IF THERE IS SOMETHING WE CAN INFER ABOUT THE CELLS THAT WE DON'T SEE OURSELVES
	// RETURNS THE CELLS THAT HAVE CHANGED SO WE CAN UPDATE SURROUDING CELLS ON THE AGENT SIDE
		// RETURN AN EMPTY LIST IF NOTHING WAS INFERRED
	public ArrayList<CellInfo> process() {
		
		ArrayList<CellInfo> cellsThatChanged = new ArrayList<CellInfo>();
		
		if (this.allUnknowns.size() == 1) {
			// WE KNOW THE IDENTITY OF THIS CELL BECAUSE IT IS ATOMIC
			CellInfo temp = this.allUnknowns.get(0).getCell();
			if (this.totalBlocks == 1) {
				temp.setBlocked();
				cellsThatChanged.add(temp);
			} else if (this.totalBlocks == 0) {
				temp.setEmpty();
				cellsThatChanged.add(temp);
			}
			
			return cellsThatChanged;
		}
		
		int sum = 0;
		for (int a = 0; a < this.allUnknowns.size(); a++) {
			sum += this.allUnknowns.get(a).getFactor();
		}
		if (sum == this.allUnknowns.size() && sum == this.totalBlocks) {
			// WE KNOW THAT ALL OF THESE CELLS ARE ACTUALLY BLOCKED
			for (int i = 0; i < this.allUnknowns.size(); i++) {
				CellInfo temp = this.allUnknowns.get(i).getCell();
				temp.setBlocked();
				cellsThatChanged.add(temp);
			}
		}
		
		return cellsThatChanged;
		
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
				}
			}
		}
		
		return a;
	}
	
	
}