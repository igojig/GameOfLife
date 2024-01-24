package life;


public class Game {

    public static final int SIZE = 10;
    public static final int X_S = 100;
    public static final int Y_S = 100;

    public static final int RND = (int) (X_S * Y_S * 0.6);
//    static int RND = 150;

    public static final int DELAY = 5;

    public static final boolean RULE = true;

    public static final int MAX_ELEMENTS=30;

    public static void main(String[] args)  {


        Life life=new Life(Config.X_S, Config.Y_S, Config.RND);
        new Window(life);

//        OptionalInt optionalInt=OptionalInt.empty();
//        List<Point2D.Double> list=ValtrAlgorithm.generateRandomConvexPolygon(10);

//        Func.divide(2, 2);

    }
}



