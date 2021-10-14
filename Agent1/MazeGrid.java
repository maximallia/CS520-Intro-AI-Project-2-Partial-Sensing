import java.lang.Double;

public class MazeGrid  implements Comparable<MazeGrid>{
    private int row;
    private int col;
    private boolean isBlocked;

    //private double h_cost;
    private double g_cost;
    private double f_cost;
    private double h_cost;//heuristic value

    private MazeGrid parent;

    public MazeGrid(int row, int col, double g_cost, MazeGrid parent){
        this.row = row;
        this.col = col;
        this.g_cost = g_cost;
        this.parent = parent;
    }

    public double get_h_cost(){
        return h_cost;
    }
    public void set_h_cost(int row, int col, int g_row, int g_col){
        this.h_cost = h_function(row, col, g_row, g_col);
    }

    public double h_function(int curr_row, int curr_col, int g_row, int g_col){
        double one = Math.abs(curr_row - g_row);
        double two = Math.abs(curr_col - g_col);

        double d_one = curr_row - g_row;
        double d_two = curr_col - g_col;

        double d_row = 0 - g_row;
        double d_col = 0 - g_col;

        //tie breaker
        double cross = Math.abs(d_one*d_col - d_two*d_row);

        double new_h = one+two;

        return new_h += cross*0.001;
    }

    public double get_g_cost(){
        return g_cost;
    }

    public void set_g_cost(double g_cost){
        this.g_cost = g_cost;
    }


    public double get_f_cost(){
        return this.g_cost+ this.h_cost;
    }
    //public void set_f_cost(){
      //  this.f_cost = g_cost+ h_cost;
    //}

    public int get_row(){
        return this.row;
    }
    public int get_col(){
        return this.col;
    }

    public void set_row(int row){
        this.row = row;
    }
    public void set_col(int col){
        this.col=col;
    }

    public boolean isBlocked(){
        return this.isBlocked;
    }
    public void set_blocked(boolean isBlocked){
        this.isBlocked = isBlocked;
    }


    public MazeGrid get_parent(){
        return this.parent;
    }
    public void set_parent(MazeGrid parent){
        this.parent = parent;
    }


    @Override
    public int compareTo(MazeGrid other){

        double f_diff = Double.compare(this.get_f_cost(), other.get_f_cost() );

        if(f_diff == 0){
            return Double.compare(this.get_h_cost(), other.get_h_cost());
        }
        else return Double.compare(this.get_g_cost(), other.get_g_cost());
    }
}

