import java.util.ArrayList;

public class MazeInfo {
    
    double trajectoryLen;
    double discovered_len; //based on discovered
    double full_len; //full grid maze

    double blind_trajectoryLen;
    double sight_trajectoryLen;
    double infer_trajectoryLen;
    double own_trajectoryLen;

    long runtime;

    int grids_processed;

    double h_value;//heuristic value

    double backtracked;

    ArrayList<ArrayList<Integer>> path;

    //init Maze Info
    public MazeInfo(double trajectoryLen, int grids_processed, ArrayList<ArrayList<Integer>> path){
        this.trajectoryLen = trajectoryLen;
        this.grids_processed = grids_processed;
        this.path = path;

        this.blind_trajectoryLen = -1;
        this.sight_trajectoryLen = -1;
        this.infer_trajectoryLen = -1;
        this.own_trajectoryLen = -1;

        this.discovered_len= -1 ;
        this.full_len = -1;

        this.runtime = -1;

        this.backtracked = 0;
        
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

    //grids processed
    public double get_grids_processed(){
        return grids_processed;
    }
    public void set_grids_processed(int grids_processed){
        this.grids_processed = grids_processed;
    }
    public void add_grads_processed(int add_grids){
        this.grids_processed = add_grids;
    }

    //runtime
    public long get_runtime(){
        return runtime;
    }
    public void set_runtime(long runtime){
        this.runtime = runtime;
    }

    //backtracked
    public double get_backtracked(){
        return backtracked;
    } 
    public void set_backtracked(double backtracked){
        this.backtracked = backtracked;
    }

    // h_value
    public double get_h_value(){
        return h_value;
    }
    public void set_h_value(double h_value){
        this.h_value= h_value;
    }

    //path
    public ArrayList<ArrayList<Integer>> get_path(){
        return path;
    }
    public void set_path(ArrayList<ArrayList<Integer>> path){
        this.path = path;
    }



}
