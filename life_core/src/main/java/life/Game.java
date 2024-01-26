package life;


public class Game {

    public static final int CELL_SIZE = 20; //  длина квадрата ячейки в пикселях
    public static final int WIDTH = 60;    //  ширина поля в ячейках
    public static final int HEIGHT = 40;   //  высота поля в ячейках
    public static final int RANDOM_CELLS = (int) (WIDTH * HEIGHT * 0.6);    // количество генерируемых рандомных ячеек в при старте
    public static final int FX_TIMER_DELAY = 100; //  задержка игрового таймера, миллисекунды
    public static final boolean LOOPED_FIELD = false;    // true -  игровое поле закольцовано, т.е правая сторона является началом левой, а верх является началом низа
    public static final int MAX_ELEMENTS=30;    // используется для проверки перехода автомата в устойчивое состояние. Лучше не менять :))

    public static void main(String[] args)  {


        Life life=new Life();
        new Window(life);

//        OptionalInt optionalInt=OptionalInt.empty();
//        List<Point2D.Double> list=ValtrAlgorithm.generateRandomConvexPolygon(10);

//        Func.divide(2, 2);

    }
}



