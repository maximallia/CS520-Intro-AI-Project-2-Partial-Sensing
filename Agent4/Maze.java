// AUTHOR: Daniel Ying (INSERT NETID) and Zachary Tarman (zpt2)

import java.util.ArrayList;
import java.awt.Point;

public class Maze {
	
	private ArrayList<ArrayList<CellInfo>> maze_structure; // THE BIG OBJECT THAT WILL STORE ALL THE CELL INFORMATION AT THE SPECIFIED COORDINATES
    private int rows; // THE NUMBER OF ROWS
    private int cols; // THE NUMBER OF COLUMNS


    // THIS RETRIEVES A CELL'S INFORMATION GIVEN THE COORDINATE
    public CellInfo getCell(int x, int y) {
    	return maze_structure.get(x).get(y);
    }

    //RANDOMLY ASSIGN A BLOCKED OR FREE STATUS TO THE CELL
    private boolean gen_block(double p) {
        double rand = Math.random();
        if (rand < p) { return true; }
        return false;
    }
    
    public boolean inBounds(Point coor) { // CHECK IF A NEIGHBORING CELL'S COORDINATES ARE IN BOUNDS (DON'T CHECK NON-EXISTENT CELLS)
		if (coor.getX() < 0 || coor.getX() >= cols || coor.getY() < 0 || coor.getY() >= rows) {
			return false;
		}
		return true;
	}

    // SETTING UP ALL THE INDIVIDUAL CELLS FOR ALL ROWS AND COLUMNS
    private ArrayList<ArrayList<CellInfo>> maze_create(int rows, int cols, double prob) {
        
        maze_structure = new ArrayList<ArrayList<CellInfo>>();
        
        for (int col = 0; col < cols; col++){
            
            ArrayList<CellInfo> temp = new ArrayList<CellInfo>();
            
            for (int row = 0; row < rows; row++) {
                
                // STORE THE CELL'S POSITION WITHIN THE MAZE
            	Point pos = new Point(col, row);

                // VERIFYING HOW MANY NEIGHBORS THIS CURRENT CELL ACTUALLY HAS
                int neighbors;
                if ((row == 0 && col == 0) || (row == 0 && col == cols - 1) || (row == rows - 1 && col == 0) || (row == rows - 1 && col == cols - 1)) {
                	neighbors = 3;
                } else if (row == 0 || col == 0 || row == rows - 1 || col == cols - 1) {
                	neighbors = 5;
                } else {
                	neighbors = 8;
                }

                // VERIFYING HOW MUCH THE HEURISTIC ESTIMATE IS FROM THIS GIVEN CELL
                	// GIVEN THAT THE MANHATTAN HEURISTIC YIELDED THE FASTEST RUNTIME IN PROJECT 1,
                	// WE'RE GOING TO BE USING THAT AS THE HEURISTIC HERE AS WELL
                double one = Math.abs(row - (rows - 1));
                double two = Math.abs(col - (cols - 1));
                double d_one = row - (rows - 1);
                double d_two = col - (cols - 1);
                double d_row = 0 - (rows - 1);
                double d_col = 0 - (cols - 1);
                double cross = Math.abs(d_one*d_col - d_two*d_row);
                double new_h = one + two;
                new_h += cross * 0.001;
                
                
                // THE POINT OF THIS IS TO HAVE THE CELL KEEP TRACK OF ALL UNCONFIRMED CELLS AROUND IT
                // IT CAN THEN "SPEAK" TO OTHER CELLS AROUND IT AND COMMUNICATE THIS INFORMATION
                	// IN THE HOPES THAT THEY SHARE KNOWLEDGE TO INFER NEW THINGS
                Point n = new Point(col, row - 1);
        		Point nw = new Point(col - 1, row - 1);
        		Point w = new Point(col - 1, row);
        		Point sw = new Point(col - 1, row + 1);
        		Point s = new Point(col, row + 1);
        		Point se = new Point(col + 1, row + 1);
        		Point e = new Point(col + 1, row);
        		Point ne = new Point(col + 1, row - 1);

        		ArrayList<Point> neighborsList = new ArrayList<Point>();
        		neighborsList.add(n);
        		neighborsList.add(nw);
        		neighborsList.add(w);
        		neighborsList.add(sw);
        		neighborsList.add(s);
        		neighborsList.add(se);
        		neighborsList.add(e);
        		neighborsList.add(ne);
        		
        		ArrayList<Association.Unit> unconfNeighbors = new ArrayList<Association.Unit>();
        		for (int i = 0; i < neighborsList.size(); i++) {
        			if (inBounds(neighborsList.get(i))) {
        				Association.Unit tempUnit = new Association.Unit(getCell((int) neighborsList.get(i).getX(), (int) neighborsList.get(i).getY()), 1);
        				unconfNeighbors.add(tempUnit);
        			}
        		}
        		Association unconf = new Association(unconfNeighbors, -1);

                // NEW CELL BEING INSERTED INTO THE MAZE WITH THE CORRECT NUMBER OF NEIGHBORS, THE RIGHT HEURISTIC ESTIMATE, AND RANDOMIZED "BLOCKED" STATUS
                CellInfo temp2 = new CellInfo(pos, neighbors, new_h, gen_block(prob), unconf);
                temp.add(temp2);
            }

            maze_structure.add(temp);
        }

        // MAKING SURE THAT THE START AND GOAL CELLS ARE UNBLOCKED FOR US
        getCell(0, 0).setActualBlockStatus(false);
        getCell(cols - 1, rows - 1).setActualBlockStatus(false);
        
        return maze_structure;
    }

    // CONSTRUCTOR 
    public Maze(int rows, int cols, double p) {
        this.rows = rows;
        this.cols = cols;
        this.maze_structure = maze_create(rows, cols, p);
    }

    // TODO ADD TO THIS METHOD TO MAKE THE MAZE REALLY CLEAR
    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder("-----------Maze-----------\n");

        for (int i = 0; i < rows; i++) {
        	
        	for (int j = 0; j < cols; j++) {

        		if (i == 0 && j == 0) {
        			builder.append("S"); // MARK START CELL
        			continue;
        		} else if (i == rows - 1 && j == cols - 1) { // MARK GOAL CELL
        			builder.append("G");
        			continue;
        		}

        		if (getCell(j, i).isActuallyBlocked()) {
        			builder.append("x");
        		} else if (getCell(j, i).isOnShortestPath()) {
        			builder.append(">");
        		} else if (getCell(j, i).isVisited()) {
        			builder.append("-");
        		} else {
        			builder.append(":");
        		}

        	}

        	builder.append("\n");
        }

        return builder.toString();
    }
}