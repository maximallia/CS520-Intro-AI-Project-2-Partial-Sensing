public class H_Calc {
    
    //return double

    public static double euclidean(int s_row, int s_col, int g_row, int g_col){
        double one = Math.pow(s_row - g_row, 2);
        double two = Math.pow(s_col - g_col, 2);
        return Math.sqrt(one + two);
    }

    public static double manhattan(int s_row, int s_col, int g_row, int g_col){
        double one = Math.abs(s_row - g_row);
        double two = Math.abs(s_col - g_col);
        return one+two;
    }

    public static double chebyshev(int s_row, int s_col, int g_row, int g_col){
        double one = Math.abs(s_row-g_row);
        double two = Math.abs(s_col- g_col);
        return Math.max(one, two);
    }
}
