import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.Collections;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
//import org.javatuples.Pair;




public class Agent1{
    
    public PriorityQueue<MazeGrid> close = new PriorityQueue<MazeGrid>();
    public Vector<MazeGrid> open = new Vector<MazeGrid>();
    public int[][] block = new int[1100][2];

    MazeGrid start_grid = new MazeGrid(0, 0, 0, null);
    //MazeGrid goal_grid = new MazeGrid(0, 0, )
    MazeGrid curr_grid = new MazeGrid(0, 0, 0, null);

    int row_dim;
    int col_dim;

    int dead_end = 0;

    double grids_popped = 0;
    double grids_traveled = 0;

    int possible_directions = 0;

    int block_idx = 0;

    public static void main(String[] args){

        int row_dim = Integer.parseInt(args[0]);
        int col_dim = Integer.parseInt(args[1]);

        int g_row = row_dim-1;
        int g_col = col_dim-1;

        double p = Double.parseDouble(args[2]);

        //Maze maze;

        Maze maze = new Maze(row_dim, col_dim, p);

        //Agent1 agent;
        
        //System.out.println(maze);
        maze.print_maze(maze.maze);

        Agent1 agent1 = new Agent1();

        int counter = -1;

        // continuously add path to open
        while(true){

            int temp_idx = agent1.block_idx;

            System.out.println("main");

            System.out.println("curr_grid1: "+ agent1.curr_grid.get_row()+ " "+ agent1.curr_grid.get_col());

            LinkedList<MazeGrid> path = agent1.forward_astar( g_row, g_col, maze);

            Iterator<MazeGrid> value = path.iterator();

            //int i = 0;
            while(value.hasNext()){
                MazeGrid temp_grid = value.next();

                // same grid added, add new one
                if(agent1.open == null){
                    agent1.open.add(temp_grid);
                    continue;
                }

                /*if(agent1.open.contains(temp_grid)){
                    int idx = agent1.open.indexOf(temp_grid);

                    if(agent1.open.get(idx).get_g_cost() < temp_grid.get_g_cost()){
                        agent1.open.set(idx, temp_grid);
                    }
                }*/
                //else agent1.open.add(temp_grid);
                agent1.open.add(temp_grid);
            }
            
            if(agent1.curr_grid != null){
                System.out.println("curr_grid2: "+ agent1.curr_grid.get_row()+ " "+ agent1.curr_grid.get_col());
            }
            else{
                //System.out.println("Maze is unsolvable.");
                //break;
                agent1.curr_grid = agent1.open.lastElement().get_parent();
            }
            if(temp_idx == agent1.block_idx){
                counter++;
                
            }
            if(counter == 100){
                System.out.println("main counter ended");
                break;
            }
            if(agent1.curr_grid.get_row() == g_row && agent1.curr_grid.get_col()== g_col) break;

        }

        //for(int i= 0; i< agent1.block.length; i++){
          //  System.out.print("["+agent1.block[i][0]+","+agent1.block[i][1]+ "] ");
           
        //}
        System.out.println(" ");

        char[][] new_maze = agent1.input_path(maze);

        maze.print_maze(new_maze);

        System.out.println("grids traveled: "+ agent1.grids_traveled);
        System.out.println("grids processed: "+ agent1.grids_popped);

    }

    public char[][] input_path(Maze maze){
        char[][] new_maze = maze.maze;

        Iterator<MazeGrid> value = open.iterator();

        while(value.hasNext()){
            MazeGrid temp_grid = value.next();
            int temp_row = temp_grid.get_row();
            int temp_col = temp_grid.get_col();

            if((temp_row == 0 && temp_col == 0) ||
            (temp_row == row_dim-1 && temp_col == col_dim-1)){
                continue;
            }
            else new_maze[temp_row][temp_col] = 'O';
            
        }
        return new_maze;
    }

    // manhattan
    public boolean move_valid(Maze maze, int curr_row, int curr_col, int[][] path_idx){

        if(curr_row < 0 || curr_col < 0) return false;

        if(curr_row >= maze.maze.length || curr_col >= maze.maze[0].length) return false;

        //int[] cords = {curr_row, curr_col};
        if( maze.maze[curr_row][curr_col] == 'X'){
            //hit block before
            int[] cords = new int[]{curr_row, curr_col};
            //System.out.println("cords: "+ cords[0] +" "+ cords[1]);
            //System.out.println(block);
            
            //java's way of finding a subarray in 2d array
            boolean in_block = Arrays.stream(block).anyMatch(line->Arrays.equals(line, cords));
            if(in_block ){
                dead_end = 1;
                //System.out.println("found in block: " + cords[0] + " " + cords[1]);

                return false;
            }
            //first hit
            else{
                //block.add(cords);
                return true;
            }
        }
        int[] cords = new int[]{curr_row, curr_col};
        boolean in_path = Arrays.stream(path_idx).anyMatch(line->Arrays.equals(line, cords));

        if(in_path && (curr_row!=0 && curr_col!= 0)){
            //System.out.println("found in currnet path: " + cords[0] + " " + cords[1]);
            dead_end=2;
            return false;
        }

        if(curr_grid.get_parent() != null && dead_end != 1){
            if(curr_grid.get_parent().get_col()== curr_col && curr_grid.get_parent().get_row() == curr_row){
            return false;
           }
        }

        return true;
    }

    public LinkedList<MazeGrid> possible_paths(int curr_row, int curr_col, Maze maze, int[][] path_idx){

        LinkedList<MazeGrid> directions = new LinkedList<MazeGrid>();

        //int paths = 0;

        int up = curr_row-1;
        int down = curr_row+1;
        int left = curr_col-1;
        int right = curr_col+1;

        possible_directions = 0;

        if(move_valid(maze, curr_row, right, path_idx)){
            directions.add(new MazeGrid(curr_row, right, curr_grid.get_g_cost()+1, curr_grid));
            possible_directions++;
        }

        if(move_valid(maze, down, curr_col, path_idx)){
            directions.add(new MazeGrid(down, curr_col, curr_grid.get_g_cost()+1, curr_grid));
            possible_directions++;
        }

        if(move_valid(maze, curr_row, left, path_idx)){
            directions.add(new MazeGrid(curr_row, left, curr_grid.get_g_cost()+1, curr_grid));
            possible_directions++;
        }

        if(move_valid(maze, up, curr_col, path_idx)){
            directions.add(new MazeGrid(up, curr_col, curr_grid.get_g_cost()+1, curr_grid));
            possible_directions++;
        }

        return directions;
    }

    // stop when hit block
    public LinkedList<MazeGrid> forward_astar(int g_row, int g_col, Maze maze){

        LinkedList<MazeGrid> path = new LinkedList<MazeGrid>();
        int[][] path_cords = new int[1100][2];
        PriorityQueue<MazeGrid> fringe = new PriorityQueue<MazeGrid>();

        //int[] goal = {g_row, g_col};

        fringe.add(curr_grid);

        //need to check if block is hit
        int i =-1 ;
        int stopper = 0;
        while(fringe != null){

            i++;
            possible_directions = 0;
            stopper++;

            curr_grid = fringe.poll();

            if(curr_grid == null){
                System.out.println("unsolvable");
                return path;
            }

            int curr_row = curr_grid.get_row();
            int curr_col = curr_grid.get_col();
            grids_popped++;

            //System.out.println("fringe: "+ curr_row+ " "+ curr_col);

            if(curr_row == g_row && curr_col == g_col){
                //path.add(curr_grid);
                grids_traveled++;
                return path;
            }

            //should be order by f=g+h due to mazegrid's comparable
            LinkedList<MazeGrid> directions = possible_paths(curr_row, curr_col, maze, path_cords);

            Collections.sort(directions);

            Iterator<MazeGrid> value = directions.iterator();

            if(possible_directions==0 && dead_end == 1){
                //path=null;
                return path;
            }


            while(value.hasNext()){
                
                MazeGrid temp_grid = value.next();

                int temp_row = temp_grid.get_row();
                int temp_col = temp_grid.get_col();

                //hit a new block
                if( (maze.maze[temp_row][temp_col] == 'X' )
                ){
                    //System.out.println("store into block");
                    int[] cords = new int[2];
                    cords[0] = temp_row;
                    cords[1] = temp_col ;
                    
                    System.out.println("hit block cords: "+ cords[0] +" "+ cords[1]);

                    block[block_idx][0] = cords[0];
                    block[block_idx][1] = cords[1];
                    block_idx++;
                
                    path.add(curr_grid);

                    //dead_end = 0;

                    return path;
                }
                // for dead end
                
                else if((possible_directions == 1  && dead_end != 2) && temp_grid != null) {
                    //if(curr_grid.get_g_cost()+1 < temp_grid.get_g_cost()){
                    int[] cords = new int[2];
                    cords[0] = curr_row;
                    cords[1] = curr_col ;

                    System.out.println("dead end block cords: "+ cords[0] +" "+ cords[1]);

                    block[block_idx][0] = cords[0];
                    block[block_idx][1] = cords[1];
                    block_idx++;

                    path_cords[i][0] = cords[0];
                    path_cords[i][1] = cords[1];
                    i++;
                
                    path.add(curr_grid);
                    grids_popped++;


                    fringe.add(temp_grid);
                    path.add(curr_grid);

                    System.out.println("temp_grid cords: "+ temp_row + ", " + temp_col);

                    //curr_grid = temp_grid;
                    grids_traveled++;

                    dead_end = 0;

                    //curr_grid = curr_grid.get_parent();

                    break;
                    //}
                }

                //already traveled to the grid in current iteration
                //if((path_cords[i][0] == temp_row && path_cords[i][1] == temp_col) || path_cords== null) continue;
                //MazeGrid parent = temp_grid.get_parent();
                //if(temp_grid.get_row() == parent.get_row() && temp_grid.get_col() == parent.get_col()) continue;
            
                //System.out.println("path_cords " + i + ": "+ path_cords[i][0] + " "+ path_cords[i][1]);

                if(temp_grid != null){
                    //if(curr_grid.get_g_cost()+1 < temp_grid.get_g_cost()){
                    path_cords[i][0] = curr_row;
                    path_cords[i][1] = curr_col;

                    int[] cords = new int[] {curr_row, curr_col};

                    fringe.add(temp_grid);
                    path.add(curr_grid);

                    System.out.println("temp_grid cords: "+ temp_row + ", " + temp_col);

                    //curr_grid = temp_grid;
                    grids_traveled++;
                    break;
                    //}
                }

                //break;

            }
            if(stopper ==4 ) break;
        }
        return path;
    
    }


}
