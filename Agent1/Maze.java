import java.util.ArrayList;

public class Maze{

    public char[][] maze;

    private int row_size;
    private int col_size;

    //set p for block in grid
    private boolean gen_block(double p){
        double rand = Math.random()*100;
        if (rand < p) return true;
        return false;
    }

    //set path and block function
    private char[][]maze_create(int row_dim, int col_dim, double p){
        char[][] maze = new char[row_dim][col_dim];
        
        
        for (int row = 0; row < row_dim; row++){

            for (int col = 0; col < col_dim; col++){
                //start grid
                if(row == 0 && col == 0){
                    //MazeGrid(int row, int col, boolean isBlocked)
                    maze[row][col] = 'S';
                }
                // goal grid
                else if(row== row_dim-1 && col== col_dim-1){
                    maze[row][col] = 'G';
                }
                else{
                    boolean block = gen_block(p);
                    if(block){
                        maze[row][col] = 'X';
                    }
                    else{
                        maze[row][col] = '_';
                    };
                }
            }
            //add temp to maze
        }
        return maze;
    }

    //make the maze
    public Maze(int row_size, int col_size, double p){
        this.row_size = row_size;
        this.col_size = col_size;
        this.maze = maze_create(row_size, col_size, p);
    }

    public void print_maze(char[][] maze){

        for(int num = 0; num< maze.length; num++){
            if(num > 9){
                System.out.print(num-10 + " ");
            }
            else System.out.print(num + " ");
        }
        System.out.println(" ");

        for(int row=0; row< maze.length; row++){
            for(int col=0; col< maze.length; col++){
                System.out.print(maze[row][col]);
                System.out.print(' ');
            }
            System.out.println(row);
        }
    }
}