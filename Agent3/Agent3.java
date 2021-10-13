// AUTHOR: Zachary Tarman (zpt2)

import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Point;

public class Agent3 {

	public Maze maze;
	public int rows; // ROW DIMENSION OF MAZE
	public int cols; // COLUMN DIMENSION OF MAZE

	public int collisions = 0; // THE NUMBER OF BLOCKS THE AGENT PHYSICALLY HITS
    public int trajectoryLength = 0; // THE TRAJECTORY LENGTH OF THE AGENT
    public int shortestPathFound = 0; // THE LENGTH OF THE SHORTEST PATH FOUND BY THE AGENT WHILE TRAVERSING THE MAZE
    public long runtime = 0; // THE RUNTIME OF THE PROGRAM TO FIND A PATH TO THE GOAL




    // USED TO PRINT THE ABOVE STATS FOR THE PROJECT
    public void printStats() {
    	System.out.println("Statistics for Maze Solution");
    	System.out.println("Shortest Path Found: " + shortestPathFound);
    	System.out.println("Trajectory Length: " + trajectoryLength);
    	System.out.println("Runtime: " + runtime);
    	System.out.println("Collisions: " + collisions);
    	return;
    }




	// PLANNING METHOD (AKA A SINGLE ITERATION OF A* WITHOUT PHYSICALLY MOVING THE AGENT THROUGH THE MAZE)
	public LinkedList<CellInfo> plan(CellInfo start) {

		LinkedList<CellInfo> plannedPath = new LinkedList<CellInfo>(); // TO STORE THE NEW PLANNED PATH
		ArrayList<CellInfo> toExplore = new ArrayList<CellInfo>(); // TO STORE THE CELLS TO BE EXPLORED
		ArrayList<CellInfo> doneWith = new ArrayList<CellInfo>();
		
		CellInfo curr = start;
		Point curr_position;
		int x, y;
		boolean addUp, addDown, addLeft, addRight = false;

		Point up = new Point();
		Point down = new Point();
		Point left = new Point();
		Point right = new Point();

		// System.out.println("We're in the planning method, about to enter the backtracking loop.");
		
		// LOOP TO TAKE CARE OF BACKTRACKING IF NO VIABLE MOVES "FORWARD" (I.E. AT END OF LONG HALLWAY)
		do {
			curr_position = curr.getPos();
			x = (int) curr_position.getX();
			y = (int) curr_position.getY();
			addUp = true;
			addDown = true; 
			addLeft = true;
			addRight = true;
			
			// System.out.println("We're evaluating " + curr.getPos().toString() + " for viable neighbors.");

			up.setLocation(x, y - 1); // NORTH
			down.setLocation(x, y + 1); // SOUTH
			left.setLocation(x - 1, y); // WEST
			right.setLocation(x + 1, y); // EAST

			if (!inBounds(up)) { 
				addUp = false; 
			} else if (maze.getCell((int) up.getX(), (int) up.getY()).isBlocked()) {
				addUp = false;
			} else if (!(maze.getCell((int) up.getX(), (int) up.getY()).isVisited())) {
				// System.out.println("We've hit a break north condition in the backtracking.");
				break;
			}
			
			if (!inBounds(down)) { 
				addDown = false; 
			} else if (maze.getCell((int) down.getX(), (int) down.getY()).isBlocked()) {
				addDown = false;
			} else if (!(maze.getCell((int) down.getX(), (int) down.getY()).isVisited())) {
				// System.out.println("We've hit a break south condition in the backtracking.");
				break;
			}
			
			if (!inBounds(left)) { 
				addLeft = false; 
			} else if (maze.getCell((int) left.getX(), (int) left.getY()).isBlocked()) {
				addLeft = false;
			} else if (!(maze.getCell((int) left.getX(), (int) left.getY()).isVisited())) {
				// System.out.println("We've hit a break west condition in the backtracking.");
				break;
			}
			
			if (!inBounds(right)) { 
				addRight = false; 
			} else if (maze.getCell((int) right.getX(), (int) right.getY()).isBlocked()) {
				addRight = false;
			} else if (!(maze.getCell((int) right.getX(), (int) right.getY()).isVisited())) {
				// System.out.println("We've hit a break east condition in the backtracking.");
				break;
			}

			// IF WE'RE STILL HERE, THEN WE KNOW THAT THERE ARE NO VIABLE NEIGHBORS THAT WE HAVEN'T ALREADY VISITED
			if (x == 0 && y == 0) { // WE'RE STILL AT THE START NODE HERE, AND THERE'S NOWHERE TO GO
				return null;
			}

			curr = curr.getParent(); // WE'VE BACKTRACKED AND WE WILL START THIS PROCESS AGAIN
			trajectoryLength++; // WE INCLUDE BACKTRACKING IN THE TRAJECTORY LENGTH

		} while (true);


		// System.out.println("We've made it out of the initial loop for backtracking.");
		
		CellInfo first = curr;
		toExplore.add(first);
		boolean goalFound = false; // INDICATOR AS TO IF THE GOAL HAS BEEN FOUND

		// BEGIN LOOP UNTIL REACHING GOAL (VIRTUALLY OF COURSE)
		while (toExplore.size() > 0) {

			curr = toExplore.remove(0); // CURRENT CELL THAT WE'RE EXPLORING
			// System.out.println("We're currently figuring out where to plan to go to next from " + curr.getPos().toString());
			if (contains(curr, doneWith)) {
				// System.out.println("We've already seen this cell and its directions.");
				continue;
			}

			curr_position = curr.getPos(); // COORDINATE OF THE CELL WE'RE CURRENTLY EXPLORING
			x = (int) curr_position.getX(); // X COORDINATE
			y = (int) curr_position.getY(); // Y COORDINATE
			doneWith.add(curr);
						
			if (x == cols - 1 && y == rows - 1) {
				goalFound = true;
				break;
			}

			// DETERMINE POSSIBLE PLACES TO MOVE FROM CURRENT POSITION
			up.setLocation(x, y - 1); // NORTH
			down.setLocation(x, y + 1); // SOUTH
			left.setLocation(x - 1, y); // WEST
			right.setLocation(x + 1, y); // EAST
			addUp = true;
			addDown = true; 
			addLeft = true;
			addRight = true;

			// CHECK FOR CELLS WE CAN'T / SHOULDN'T EXPLORE OR MOVE INTO ON OUR WAY TO THE GOAL
			if (!inBounds(up)) { 
				addUp = false; 
			} else if (maze.getCell((int) up.getX(), (int) up.getY()).isBlocked()) {
				addUp = false;
			} else if (maze.getCell((int) up.getX(), (int) up.getY()).isVisited()) {
				addUp = false;
			} else if (contains(maze.getCell((int) up.getX(), (int) up.getY()), doneWith)) {
				addUp = false;
			}
			
			if (!inBounds(down)) { 
				addDown = false; 
			} else if (maze.getCell((int) down.getX(), (int) down.getY()).isBlocked()) {
				addDown = false;
			} else if (maze.getCell((int) down.getX(), (int) down.getY()).isVisited()) {
				addDown = false;
			} else if (contains(maze.getCell((int) down.getX(), (int) down.getY()), doneWith)) {
				addDown = false;
			}
			
			if (!inBounds(left)) { 
				addLeft = false; 
			} else if (maze.getCell((int) left.getX(), (int) left.getY()).isBlocked()) {
				addLeft = false;
			} else if (maze.getCell((int) left.getX(), (int) left.getY()).isVisited()) {
				addLeft = false;
			} else if (contains(maze.getCell((int) left.getX(), (int) left.getY()), doneWith)) {
				addLeft = false;
			}
			
			if (!inBounds(right)) { 
				addRight = false; 
			} else if (maze.getCell((int) right.getX(), (int) right.getY()).isBlocked()) {
				addRight = false;
			} else if (maze.getCell((int) right.getX(), (int) right.getY()).isVisited()) {
				addRight = false;
			} else if (contains(maze.getCell((int) right.getX(), (int) right.getY()), doneWith)) {
				addRight = false;
			}

			// ADD ALL UNCONFIRMED OR UNBLOCKED CELLS TO PRIORITY QUEUE + SET PARENTS AND G-VALUES
			CellInfo temp;
			double curr_g = curr.getG(); // THE G_VALUE OF THE CURRENT CELL IN THE PLANNING PROCESS
			if (addUp) { 
				temp = maze.getCell((int) up.getX(), (int) up.getY());
				temp.setG(curr_g + 1);
				temp.setParent(curr);
				toExplore = insertCell(temp, toExplore);
				/* System.out.println("Inserting the north cell " + temp.getPos().toString() + 
						" into the priority queue. Its parent is " + temp.getParent().getPos().toString()); */
			}
			if (addLeft) { 
				temp = maze.getCell((int) left.getX(), (int) left.getY());
				temp.setG(curr_g + 1);
				temp.setParent(curr);
				toExplore = insertCell(temp, toExplore);
				/* System.out.println("Inserting the west cell " + temp.getPos().toString() + 
						" into the priority queue. Its parent is " + temp.getParent().getPos().toString()); */
			}
			if (addDown) { 
				temp = maze.getCell((int) down.getX(), (int) down.getY());
				temp.setG(curr_g + 1);
				temp.setParent(curr);
				toExplore = insertCell(temp, toExplore);
				/* System.out.println("Inserting the south cell " + temp.getPos().toString() + 
						" into the priority queue. Its parent is " + temp.getParent().getPos().toString()); */
				
			}
			if (addRight) { 
				temp = maze.getCell((int) right.getX(), (int) right.getY());
				temp.setG(curr_g + 1);
				temp.setParent(curr);
				toExplore = insertCell(temp, toExplore);
				/* System.out.println("Inserting the east cell " + temp.getPos().toString() + 
						" into the priority queue. Its parent is " + temp.getParent().getPos().toString()); */
			}

		}

		// MAKE SURE THE LAST COORDINATE IN THE PLANNED PATH IS THE GOAL CELL
		// IF NOT, THEN WE KNOW THE MAZE IS NOT SOLVABLE
		if (goalFound) {
			// FOLLOW POINTERS BACK UP UNTIL THE START CELL TO ADD TO THE LINKED LIST
			CellInfo goal = maze.getCell(cols - 1, rows - 1);
			CellInfo ptr = goal;
			while (ptr.getPos().getX() != first.getPos().getX() || ptr.getPos().getY() != first.getPos().getY()) {
				// System.out.println("Adding " + ptr.getPos().toString() + " to the path.");
				plannedPath.addFirst(ptr);
				ptr = ptr.getParent();
			}
			plannedPath.addFirst(ptr);
			return plannedPath;
		}

		return null; // MAZE IS UNSOLVABLE ):
	}






	// SENSING METHOD
	public void sense(CellInfo pos) {

		System.out.println("We're currently in the sensing phase.");
		
		// SENSING PORTION
		int blocked_neighbors = 0;
		Point coor = pos.getPos();
		int x = (int) coor.getX();
		int y = (int) coor.getY();

		Point n = new Point(x, y - 1);
		Point nw = new Point(x - 1, y - 1);
		Point w = new Point(x - 1, y);
		Point sw = new Point(x - 1, y + 1);
		Point s = new Point(x, y + 1);
		Point se = new Point(x + 1, y + 1);
		Point e = new Point(x + 1, y);
		Point ne = new Point(x + 1, y - 1);

		ArrayList<Point> neighbors = new ArrayList<Point>();
		neighbors.add(n);
		neighbors.add(nw);
		neighbors.add(w);
		neighbors.add(sw);
		neighbors.add(s);
		neighbors.add(se);
		neighbors.add(e);
		neighbors.add(ne);
		
		for (int i = 0; i < neighbors.size(); i++) {
			// System.out.println("Currently sensing cell " + neighbors.get(i).toString());
			if (inBounds(neighbors.get(i))) { // ONLY CHECK NEIGHBORING CELLS THAT ARE ACTUALLY IN BOUNDS (I.E. A LEGITIMATE CELL)
				// System.out.println("Cell " + neighbors.get(i).toString() + " is within the boundaries of the maze.");
				if (maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).isActuallyBlocked()) { // CHECK ACTUAL "BLOCKED" STATUS
					// System.out.println("We've sensed " + neighbors.get(i).toString() + " to be blocked.");
					blocked_neighbors++;
				}
			}
		}

		// BUT WE DON'T REPORT THE LOCATIONS OF BLOCKS, JUST THE TOTAL NUMBER SURROUNDING THE CURRENT CELL
		maze.getCell(x, y).setBlocksSensed(blocked_neighbors);
		System.out.println("We sensed " + blocked_neighbors + " blocks around us in this cell.");
		
		int blocked = 0;
		int empty = 0;
		int unconfirmed = 0;
		
		for (int i = 0; i < neighbors.size(); i++) {
			if (inBounds(neighbors.get(i))) {
				if (maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).isBlocked()) {
					blocked++;
				} else if (!maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).isUnconfirmed()) {
					empty++;
				} else {
					unconfirmed++;
				}
			}
		}
		
		pos.setNeighborsBlocked(blocked);
		pos.setNeighborsEmpty(empty);
		pos.setNeighborsUnconfirmed(unconfirmed);
		
		return;
	}







	// INFERENCE METHOD (ACTS RECURSIVELY IF REQUIRED BY GIVEN UPDATES TO THE KNOWLEDGE BASE)
	public void infer(CellInfo pos) {

		System.out.println("We're currently in the inference phase for cell " + pos.getPos().getX() + ", " + pos.getPos().getY());
		
		LinkedList<CellInfo> propagate = new LinkedList<CellInfo>(); // TO BE USED IN PROPAGATING INFERENCES
		Point coor = pos.getPos();
		int x = (int) coor.getX();
		int y = (int) coor.getY();

		boolean restBlocked = false;
		boolean restEmpty = false;

		System.out.println("Neighbors blocked: " + pos.getNeighborsBlocked() 
			+ ". Neighbors empty: " + pos.getNeighborsEmpty() 
			+ ". Neighbors unconfirmed: " + pos.getNeighborsUnconfirmed());
		
		if (pos.getNeighborsUnconfirmed() != 0) { // IF IT DID EQUAL 0, THERE WOULD BE NOTHING MORE TO INFER (WE JUST NEED TO UPDATE NEIGHBORS)

			if (pos.getNeighborsBlocked() == pos.getNeighbors()) {
				restEmpty = true;
			} else if (pos.getNeighborsEmpty() == pos.getNeighbors() - pos.getBlocksSensed()) {
				restBlocked = true;
			}

		}

		Point n = new Point(x, y - 1);
		Point nw = new Point(x - 1, y - 1);
		Point w = new Point(x - 1, y);
		Point sw = new Point(x - 1, y + 1);
		Point s = new Point(x, y + 1);
		Point se = new Point(x + 1, y + 1);
		Point e = new Point(x + 1, y);
		Point ne = new Point(x + 1, y - 1);

		ArrayList<Point> neighbors = new ArrayList<Point>();
		neighbors.add(n);
		neighbors.add(nw);
		neighbors.add(w);
		neighbors.add(sw);
		neighbors.add(s);
		neighbors.add(se);
		neighbors.add(e);
		neighbors.add(ne);
		
		for (int i = 0; i < neighbors.size(); i++) {
			
			if (inBounds(neighbors.get(i))) { // ONLY CHECK NEIGHBORING CELLS THAT ARE ACTUALLY IN BOUNDS (I.E. A LEGITIMATE CELL)
				
				// WE CAN INFER / CONFIRM THE NEIGHBOR'S STATUS
				if (maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).isUnconfirmed() && (restBlocked || restEmpty)) {
					if (restBlocked) {
						maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).setBlocked();
					} else if (restEmpty) {
						maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).setEmpty();
					}
					propagate.add(maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY())); // WILL ALSO UPDATE THIS NEIGHBOR BELOW THIS LOOP
				}

				int neighbor_x = (int) neighbors.get(i).getX();
				int neighbor_y = (int) neighbors.get(i).getY();

				Point neighbor_n = new Point(neighbor_x, neighbor_y - 1);
				Point neighbor_nw = new Point(neighbor_x - 1, neighbor_y - 1);
				Point neighbor_w = new Point(neighbor_x - 1, neighbor_y);
				Point neighbor_sw = new Point(neighbor_x - 1, neighbor_y + 1);
				Point neighbor_s = new Point(neighbor_x, neighbor_y + 1);
				Point neighbor_se = new Point(neighbor_x + 1, neighbor_y + 1);
				Point neighbor_e = new Point(neighbor_x + 1, neighbor_y);
				Point neighbor_ne = new Point(neighbor_x + 1, neighbor_y - 1);

				// SCAN THE KNOWLEDGE BASE SURROUNDING THE NEIGHBORS TO SEE IF ANYTHING SHOULD BE UPDATED (NOT EQUIVALENT TO SENSING)
				ArrayList<Point> neighborsOfNeighbor = new ArrayList<Point>();
				neighborsOfNeighbor.add(neighbor_n);
				neighborsOfNeighbor.add(neighbor_nw);
				neighborsOfNeighbor.add(neighbor_w);
				neighborsOfNeighbor.add(neighbor_sw);
				neighborsOfNeighbor.add(neighbor_s);
				neighborsOfNeighbor.add(neighbor_se);
				neighborsOfNeighbor.add(neighbor_e);
				neighborsOfNeighbor.add(neighbor_ne);
				
				int blocked = 0, empty = 0, unconfirmed = 0;
				for (int j = 0; j < neighborsOfNeighbor.size(); j++) {
					
					if (inBounds(neighborsOfNeighbor.get(j))) {
						if (maze.getCell((int) neighborsOfNeighbor.get(j).getX(), (int) neighborsOfNeighbor.get(j).getY()).isBlocked()) {
							blocked++;
						} else if (!maze.getCell((int) neighborsOfNeighbor.get(j).getX(), (int) neighborsOfNeighbor.get(j).getY()).isUnconfirmed()) {
							empty++;
						} else {
							unconfirmed++;
						}
					}
				}

				maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).setNeighborsBlocked(blocked);
				maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).setNeighborsEmpty(empty);
				maze.getCell((int) neighbors.get(i).getX(), (int) neighbors.get(i).getY()).setNeighborsUnconfirmed(unconfirmed);

			}
		}

		while (propagate.peek() != null) { // RECURSIVE FUNCTION TO MAKE SURE WE UPDATE ALL CELLS ACCORDINGLY
			infer(propagate.poll());
		}

		// NOW WE'RE DONE INFERRING
		return;
	}






	// HELPER METHODS FOR THE DIFFERENT STEPS OF THE AGENT'S ALGORITHM
	public boolean canMove(CellInfo pos, LinkedList<CellInfo> path) { // CHECK IF A CELL CAN ACTUALLY MOVE TO THE NEXT CELL OR NOT
				
		if (path.peekFirst().isActuallyBlocked()) { 
			// System.out.println("Cell " + path.peekFirst().getPos().getX() + ", " + path.peekFirst().getPos().getY() + " is blocked. We cannot move here.");
			return false; 
		}
		return true;
	}

	public boolean inBounds(Point coor) { // CHECK IF A NEIGHBORING CELL'S COORDINATES ARE IN BOUNDS (DON'T CHECK NON-EXISTENT CELLS)
		if (coor.getX() < 0 || coor.getX() >= cols || coor.getY() < 0 || coor.getY() >= rows) {
			return false;
		}
		return true;
	}
	
	public boolean contains(CellInfo newCell, ArrayList<CellInfo> doneWith) {
		for (int i = 0; i < doneWith.size(); i++) {
			if (newCell.getPos().getX() == doneWith.get(i).getPos().getX() 
					&& newCell.getPos().getY() == doneWith.get(i).getPos().getY()) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<CellInfo> insertCell(CellInfo newCell, ArrayList<CellInfo> toExplore) {
		
		// FIRST CELL TO BE ADDED TO AN EMPTY LIST
		if (toExplore.isEmpty()) {
			toExplore.add(newCell);
			return toExplore;
		}
		
		double cell_f = newCell.getF();
		for (int i = 0; i < toExplore.size(); i++) {
			if (cell_f < toExplore.get(i).getF()) {
				toExplore.add(i, newCell);
				return toExplore;
			} else if (cell_f <= toExplore.get(i).getF()) {
				double cell_g = newCell.getG();
				if (cell_g <= toExplore.get(i).getG()) {
					toExplore.add(i, newCell);
					return toExplore;
				}
			}
		}
		
		toExplore.add(toExplore.size() - 1, newCell); // ADDING IT TO THE END OF THE LIST
		
		return toExplore;
		
	}






	public static void main (String args[]) {

		Agent3 mazeRunner = new Agent3();
		
		// READING FROM INPUT
		mazeRunner.rows = Integer.parseInt(args[0]); // THE NUMBER OF ROWS THAT WE WANT IN THE CONSTRUCTED MAZE
		mazeRunner.cols = Integer.parseInt(args[1]); // THE NUMBER OF COLUMNS THAT WE WANT IN THE CONSTRUCTED MAZE
		double p = Double.parseDouble(args[2]); // VALUE BETWEEN 0.0 AND 1.0

		// SET UP MAZE
		mazeRunner.maze = new Maze(mazeRunner.rows, mazeRunner.cols, p);
		boolean badPath = false; // USED FOR DETERMINING WITH WE'VE FOUND A BAD PATH BY INFERENCE
		
		long begin = System.nanoTime();
		CellInfo start = mazeRunner.maze.getCell(0, 0);
		LinkedList<CellInfo> plannedPath = mazeRunner.plan(start); // STORES OUR BEST PATH THROUGH THE MAZE

		// System.out.println("We've made it through the first planning phase.");
		
		// MAIN LOOP FOR AGENT
		while (true) {

			CellInfo currCell = plannedPath.poll();
			System.out.println("Agent is currently in " + currCell.getPos().getX() + ", " + currCell.getPos().getY());
			
			if (currCell.getPos().getX() == mazeRunner.cols - 1 && currCell.getPos().getY() == mazeRunner.rows - 1) { // WE'VE HIT THE GOAL CELL
				break;
			}

			// WE HAVEN'T HIT THE GOAL CELL, SO WE CONTINUE ONWARD
			currCell.setEmpty(); // THE CELL WE ARE CURRENTLY IS EMPTY / UNBLOCKED
			currCell.setVisited();
			mazeRunner.sense(currCell);
			mazeRunner.infer(currCell);

			// CHECK IF WE'VE HAD AN UPDATE (BLOCK) IN THE PATH THAT REQUIRES US TO REPLAN
			for (int i = 0; i < plannedPath.size(); i++) {
				CellInfo temp = plannedPath.poll();
				if (temp.isBlocked()) { // THIS IS CHECKING THE INFERRED / OBSERVED BLOCKS, NOT SNEAKING A PEEK AT THE LEGITIMATE STATUS
					badPath = true;
					break;
				}
				plannedPath.addLast(temp); // CYCLE THIS TO THE BACK (IT WILL EVENTUALLY END UP IN ITS CORRECT POSITION)
			}

			// WE'VE FOUND A BLOCK IN OUR PATH WITHOUT ACTUALLY RUNNING INTO IT
			if (badPath) {
				System.out.println("WE'VE DISCOVERED A BAD PATH WITHOUT COLLISION.");
				plannedPath = mazeRunner.plan(currCell);
				if (plannedPath == null) {
					System.out.println("Maze is unsolvable.");
					System.out.println(mazeRunner.maze.toString());
					return;
				}
				badPath = false;
				continue;
			}

			// ATTEMPT TO EXECUTE EXACTLY ONE CELL MOVEMENT
			if (!mazeRunner.canMove(currCell, plannedPath)) {
				// WE'VE HIT A BLOCK
				CellInfo obstruction = plannedPath.peekFirst();
				obstruction.setBlocked();
				System.out.println("We've hit a block at coordinate " + obstruction.getPos().toString());
				mazeRunner.infer(obstruction); // WE CAN UPDATE OUR KNOWLEDGE BASE (CAN'T SENSE THOUGH SINCE WE CAN'T OCCUPY THAT CELL)

				// AND WE NEED TO REPLAN AS WELL
				mazeRunner.collisions++;
				plannedPath = mazeRunner.plan(currCell);
				if (plannedPath == null) {
					System.out.println("Maze is unsolvable.");
					System.out.println(mazeRunner.maze.toString());
					return;
				}
				continue;
			}

			mazeRunner.trajectoryLength++;


		}
		
		// IF WE BREAK FROM THE LOOP (AKA WE'RE HERE AND HAVEN'T RETURNED YET), WE KNOW WE FOUND THE GOAL.
		long end = System.nanoTime();
		mazeRunner.runtime = end - begin;
		// System.out.println("We made it to the goal, and we're out of the main method's big loop.");
		
		
		CellInfo ptr = mazeRunner.maze.getCell(mazeRunner.cols - 1, mazeRunner.rows - 1);
		while (ptr.getPos().getX() != 0 || ptr.getPos().getY() != 0) {
			ptr = ptr.getParent();
			mazeRunner.shortestPathFound++;
		}

		System.out.println("Path Found!");
		System.out.println(mazeRunner.maze.toString()); // TODO TO BE ADDED TO
		mazeRunner.printStats();

		return;
	}

}