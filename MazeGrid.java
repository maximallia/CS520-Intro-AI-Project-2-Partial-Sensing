public class MazeGrid {
    private int row;
    private int col;
    private boolean isBlocked;
    private double cost;
    private double h_cost;
    private MazeGrid parent;

    public MazeGrid(int row, int col, boolean isBlocked){
        this.row = row;
        this.col = col;
        this.isBlocked = isBlocked;
        this.cost = 0;
        this.parent = null;
    }

    public double get_h_cost(){
        return h_cost;
    }
    public void set_h_cost(double h_cost){
        this.h_cost = h_cost;
    }

    public int get_row(){
        return row;
    }
    public int get_col(){
        return col;
    }

    public void set_row(int row){
        this.row = row;
    }
    public void set_col(int col){
        this.col=col;
    }

    public boolean isBlocked(){
        return isBlocked;
    }
    public void set_blocked(boolean isBlocked){
        this.isBlocked = isBlocked;
    }

    public double get_cost(){
        return cost;
    }
    public void set_cost(double cost){
        this.cost = cost;
    }

    public MazeGrid get_parent(){
        return parent;
    }
    public void set_parent(MazeGrid parent){
        this.parent = parent;
    }

    @Override
    public String toString(){
        String str = "MazeGrid{ row= "+ row+ ", col= "+ col+ ", isBlocked= "+ isBlocked+
        ", cost= "+ cost+ "}";
        return str;
    }
}

