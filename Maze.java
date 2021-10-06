import java.util.ArrayList;

public class Maze{

    private ArrayList<ArrayList<MazeGrid>> maze;

    private int row_size;
    private int col_size;

    //set p for block in grid
    private boolean gen_block(double p){
        double rand = Math.random()*100;
        if (rand < p) return true;
        return false;
    }

    //set path and block function
    private ArrayList<ArrayList<MazeGrid>> maze_create(int row_dim, int col_dim, double p){
        ArrayList<ArrayList<MazeGrid>> maze = new ArrayList<>();
        
        
        for (int row = 0; row < row_dim; row++){

            ArrayList<MazeGrid> temp = new ArrayList<>();

            for (int col = 0; col < col_dim; col++){
                //start grid
                if(row == 0 && col == 0){
                    //MazeGrid(int row, int col, boolean isBlocked)
                    temp.add(new MazeGrid(row, col, true));
                }
                // goal grid
                else if(row== row_dim-1 && col== col_dim-1){
                    temp.add(new MazeGrid(row, col, true));
                }
                else{
                    temp.add(new MazeGrid(row, col, gen_block(p)));
                }
            }
            //add temp to maze
            maze.add(temp);
        }
        return maze;
    }

    //make the maze
    public Maze(int row_size, int col_size, double p){
        this.row_size = row_size;
        this.col_size = col_size;
        this.maze = maze_create(row_size, col_size, p);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("-----------Maze-----------\n");

        for(int row=0; row<row_size; row++){
            builder.append("\n");

            for(int col=0; col<col_size; col++){

                //start
                if(row==0 && col==0){
                    builder.append("S");
                }
                else if(row==row_size-1 && col==col_size-1){
                    builder.append("G");
                }
                else{
                    MazeGrid curr_grid = maze.get(row).get(col);
                    if(curr_grid.isBlocked() ){
                        builder.append('H');
                    }
                    else{
                        builder.append("O");
                    }
                }
            }
        }
        builder.append("\n");
        builder.append("-----------------------------");

        return builder.toString();
    }
}