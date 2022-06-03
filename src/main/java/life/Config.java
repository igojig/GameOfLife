package life;

public interface Config {
    int SIZE = 5;
    int X_S = 200;
    int Y_S = 200;

    int RND = (int) (X_S * Y_S * 0.5);
//    static int RND = 150;

    int DELAY = 5;

    boolean RULE = true;

    int MAX_ELEMENTS=30;


}
