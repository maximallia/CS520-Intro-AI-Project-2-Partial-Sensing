import java.util.Vector;
import java.util.Comparator;
import java.util.LinkedList;

public class MazeInfo{
    
    Maze maze;
    //MazeGrid curr_grid;
    //Vector<MazeGrid> path;
    int maze_col;
    int maze_row;


    double trajectoryLen;
    double discovered_len; //based on discovered
    double full_len; //full grid maze

    double blind_trajectoryLen;
    double sight_trajectoryLen;
    double infer_trajectoryLen;
    double own_trajectoryLen;

    long runtime;

    double grids_processed;



    double backtracked;

    //init Maze Info
    public MazeInfo(Maze maze, int row, int col){
        this.maze_row = row;
        this.maze_col = col;
        this.maze = maze;

    }



    //trajectory length
    public double get_trajectoryLen(){
        return trajectoryLen;
    }
    public void set_trajectoryLen(double trajectoryLen){
        this.trajectoryLen = trajectoryLen;
    }
    public void add_trajectoryLen(double add_trajectory){
        this.trajectoryLen += add_trajectory;
    }

    public void set_discoveredLen(double discovered){
        this.discovered_len = discovered;
    }
    public double get_discoveredLen(){
        return discovered_len;
    }

    public void set_blind_trajectoryLen(double blind){
        this.blind_trajectoryLen = blind;
    }
    public double get_blind_trajectoryLen(){
        return blind_trajectoryLen;
    }

    //grids processed
    public double get_grids_processed(){
        return grids_processed;
    }
    public void set_grids_processed(double grids_processed){
        this.grids_processed = grids_processed;
    }
    public void add_grads_processed(double add_grids){
        this.grids_processed = add_grids;
    }

    //runtime
    public long get_runtime(){
        return runtime;
    }
    public void set_runtime(long runtime){
        this.runtime = runtime;
    }




}
