import java.util.*;
import java.util.function.Predicate; 

public class Main{
    
    public static void main(String[] args){



        int row = Integer.parseInt(args[0]);
        int col = Integer.parseInt(args[1]);
        double p = Double.parseDouble(args[2]);

        System.out.println(row);
        System.out.println(col);
        System.out.println(p);

        Maze maze;

        maze = new Maze(row, col, p);
        
        System.out.println(maze);
    }
}
