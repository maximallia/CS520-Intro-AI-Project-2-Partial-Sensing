// AUTHOR: Zachary Tarman (zpt2)

import java.awt.Point;

public class CellInfo {
	
	private int num_neighbors; // NUMBER OF NEIGHBORS THIS CELL HAS
	private int blocks_sensed; // NUMBER OF NEIGHBORS SENSED TO BE BLOCKED
	private int neighbors_block; // NUMBER OF NEIGHBORS CONFIRMED TO BE BLOCKED
	private int neighbors_empty; // NUMBER OF NEIGHBORS CONFIRMED TO BE EMPTY
	private int neighbors_unconfirmed; // NUMBER OF NEIGHBORS STILL UNCONFIRMED AT THIS POINT

	private boolean visited; // HAS THIS CELL BEEN VISITED DURING EXECUTION YET?
	private boolean blocked; // HAS THIS CELL BEEN INFERRED / CONFIRMED TO BE BLOCKED? (EMPTY IF BLOCKED = FALSE && UNCONFIRMED = FALSE)
	private boolean unconfirmed; // IS THE CELL'S STATUS STILL UNCERTAIN?
	private boolean actually_blocked; // WHAT IS THIS CELL'S LEGITIMATE STATUS ASSIGNED AT MAZE BUILD TIME? (ONLY USED WHEN PHYSICALLY TRYING TO MOVE INTO IT)

	private Point position; // COORDINATE OF THE CURRENT CELL
	private CellInfo parent; // COORDINATE OF THE CELL'S PARENT (THE PREVIOUS CELL IN THE PATH WE USED TO GET HERE)
	private double g_value; // THE NUMBER OF STEPS IT'S TAKEN TO GET TO THIS CELL
	private double h_estimate; // THE HEURISTIC ESTIMATE
	
	private boolean onShortestPath; // IDENTIFIER FOR IF THIS CELL IS ON THE SHORTEST PATH
	// TO BE USED FOR PRINTING OUT THE MAZE DURING DEBUGGING

	// CONSTRUCTOR
	public CellInfo(Point position, int num_neighbors, double h_estimate, boolean actually_blocked) {
		this.position = position; // GIVES THE COORDINATE OF THE CELL IN THE MAZE
		this.num_neighbors = num_neighbors; // maze.java CHECKS FOR IF THE CURRENT CELL IS ON A BORDER OR IN A CORNER
		this.blocks_sensed = -1;
		this.neighbors_block = 0;
		this.neighbors_empty = 0;
		this.neighbors_unconfirmed = num_neighbors;
		this.visited = false;
		this.blocked = false;
		this.unconfirmed = true; // ALL CELLS START OUT AS UNCONFIRMED (AND WE'LL MAKE SURE WE EDIT THE START NODE AND BEGIN SEARCHING)
		this.actually_blocked = actually_blocked; // GENERATED BY THE RANDOMIZED FUNCTION IN maze.java
		this.parent = null; // DEFAULT PARENT (UNTIL CHANGED TO THE ACTUAL PARENT)
		this.g_value = 0; // DEFAULT VALUE, WILL BE CHANGED WHEN CELLS ARE SEARCHED
		this.h_estimate = h_estimate;
		this.onShortestPath = false;
	}




	// GET METHODS
	public Point getPos() {
		return this.position;
	}

	public CellInfo getParent() { // GIVES THE CORRDINATE OF THE "PARENT" OR THE CELL THAT WE USED TO GET TO HERE
		return this.parent;
	}

	public int getNeighbors() { // GIVES THE NUMBER OF NEIGHBORS THIS CELL HAS
		return this.num_neighbors;
	}

	public int getBlocksSensed() { // GIVES THE NUMBER OF NEIGHBORS SENSED TO BE BLOCKED
		return this.blocks_sensed;
	}
	
	public int getNeighborsBlocked() { // GIVES THE NUMBER OF NEIGHBORS THAT ARE CONFIRMED TO BE BLOCKED
		return this.neighbors_block;
	}

	public int getNeighborsEmpty() { // GIVES THE NUMBER OF NEIGHBORS THAT ARE CONFIRMED TO BE EMPTY
		return this.neighbors_empty;
	}

	public int getNeighborsUnconfirmed() { // GIVES THE NUMBER OF NEIGHBORS WHOSE STATUS IS UNCERTAIN
		return this.neighbors_unconfirmed;
	}

	public boolean isVisited() { // GIVES IF THE CELL HAS ALREADY BEEN "VISITED" / PROCESSED
		return this.visited;
	}

	public boolean isBlocked() { // GIVES IF THE CELL IS CONFIRMED TO BE BLOCKED (IF FALSE AND UNCONFIRMED IS FALSE, THEN THIS CELL IS EMPTY)
		return this.blocked;
	}

	public boolean isUnconfirmed() { // GIVES IF THE CELL'S BLOCKED/UNBLOCKED STATUS IS UNCERTAIN
		return this.unconfirmed;
	}

	public double getF() { // GIVES THE F-VALUE TO FIND WHICH CELL TO EXPLORE NEXT IN THE PLANNING PHASE
		return this.g_value + this.h_estimate;
	}

	public double getG() {
		return this.g_value;
	}

	public double getH() { // GIVES THE HEURISTIC ESTIMATE FOR THE CELL
		return this.h_estimate;
	}


	// TO BE USED ONLY IN THE EVENT OF THE AGENT ATTEMPTING TO MOVE THAT CELL
	public boolean isActuallyBlocked() { // GIVES THE REAL STATUS OF THE CELL BEING BLOCKED OR UNBLOCKED
		return this.actually_blocked;
	}
	
	public boolean isOnShortestPath() { // TO BE USED WHEN PRINTING OUT THE MAZE
		return this.onShortestPath;
	}





	// SET METHODS
	public void setBlocksSensed(int s) {
		this.blocks_sensed = s;
		return;
	}
	
	public void setNeighborsBlocked(int b) { // SET THE NUMBER OF NEIGHBORS THAT ARE CONFIRMED TO BE BLOCKED
		this.neighbors_block = b;
		return;
	}

	public void setNeighborsEmpty(int e) { // SET THE NUMBER OF NEIGHBORS THAT ARE CONFIRED TO BE EMPTY
		this.neighbors_empty = e;
		return;
	}

	public void setNeighborsUnconfirmed(int u) { // SET THE NUMBER OF NEIGHBORS WHOSE STATUS ARE UNCONFIRMED AT THIS POINT
		this.neighbors_unconfirmed = u;
		return;
	}

	public void setVisited() { // SET THE CELL TO HAVE BEEN VISITED (DONE DURING EXECUTION PHASE)
		this.visited = true;
		return;
	}

	public void setBlocked() { // SET IF THE CELL HAS BEEN INFERRED / CONFIRMED TO BE BLOCKED
		this.blocked = true;
		this.unconfirmed = false;
		return;
	}

	public void setEmpty() { // SET IF THE CELL HAS BEEN INFERRED / CONFIRMED TO BE EMPTY
		this.blocked = false;
		this.unconfirmed = false;
		return;
	}

	public void setParent(CellInfo prev) { // SET THE CELL'S PARENT AS THE PREVIOUS CELL IN THE PATH
		this.parent = prev;
		return;
	}

	public void setG(double g) { // SET THE DISTANCE FROM THE START NODE ON THE PATH (DON'T CHANGE IF YOU HAPPEN TO BACKTRACK)
		this.g_value = g;
		return;
	}

	public void setActualBlockStatus(boolean s) { // ONLY TO BE USED DURING MAZE CONSTRUCTION
		// USED TO ENSURE THAT START AND GOAL NODE ARE UNBLOCKED
		this.actually_blocked = s;
		return;
	}
	
	public void setOnShortestPath() {
		this.onShortestPath = true;
		return;
	}
	

}