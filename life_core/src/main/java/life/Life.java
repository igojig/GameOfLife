package life;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

class Func implements Runnable {
    int x_start;
    int x_end;
    int y_start;
    int y_end;
    Cell[][] cells;
    Consumer<Cell> cellConsumer;


    public Func(int x_start, int x_end, int y_start, int y_end, Cell[][] cells, Consumer<Cell> cellConsumer) {
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        this.cells = cells;
        this.cellConsumer=cellConsumer;
    }

    @Override
    public void run() {
        for (int x = x_start; x < x_end; x++)
            for (int y = y_start; y < y_end; y++) {
//                cells[x][y].step_1();
                cellConsumer.accept(cells[x][y]);
            }
    }
}

class Func2 implements Runnable {
    int x_start;
    int x_end;
    int y_start;
    int y_end;
    Cell[][] cells;

    public Func2(int x_start, int x_end, int y_start, int y_end, Cell[][] cells) {
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        this.cells = cells;
    }

    @Override
    public void run() {
        for (int x = x_start; x < x_end; x++)
            for (int y = y_start; y < y_end; y++) {
                cells[x][y].step_2();
            }
    }
}



public class Life {

//    static int dimension_x;
//    static int dimension_y;
//
//    static int random_count;

    ExecutorService executor = Executors.newFixedThreadPool(8);


    Check check = new Check();

    Cell[][] cellArray;

    List<Runnable> runnableList1=new ArrayList<>();
    List<Runnable> runnableList2=new ArrayList<>();

    static class Info {
        // макс. кол-во поколений, когда клетка непрерывно жива
        static int max_generation = 1;
        // количество клеток, имеющих наибольший счетчик поколений
        static int count_of_max_generations = 1;
        static int live_cells_count = 0;
        static int life_step = 1;

        static OptionalInt isRepeated = OptionalInt.empty();

        static int restart_count = 1;

        static void reset() {
            // макс. кол-во поколений, когда клетка непрерывно жива
            max_generation = 1;
            // количество клеток, имеющих наибольший счетчик поколений
            count_of_max_generations = 1;
            live_cells_count = 0;
            life_step = 1;
            restart_count++;
        }

    }

      void divide( int parts_x, int parts_y){
        List<Integer> xVect=new ArrayList<>();
        List<Integer> yVect=new ArrayList<>();

        int len_x=Config.X_S/parts_x;
        int len_y=Config.Y_S/parts_y;

        for(int i=0;i<=parts_x;i++){
            xVect.add(i*len_x);
        }
        if(Config.X_S%parts_x!=0){
            xVect.set(xVect.size()-1, Config.X_S);
        }

        for(int i=0;i<=parts_y;i++){
            yVect.add(i*len_y);
        }
        if(Config.Y_S%parts_y!=0){
            yVect.set(yVect.size()-1, Config.Y_S);
        }


        for(int i=1;i<xVect.size();i++)
            for(int j=1;j<yVect.size();j++){
                runnableList1.add(new Func(xVect.get(i-1),xVect.get(i), yVect.get(j-1), yVect.get(j), cellArray, Cell::step_1));
                runnableList2.add(new Func(xVect.get(i-1),xVect.get(i), yVect.get(j-1), yVect.get(j), cellArray,  Cell::step_2));
            }

    }


        Life(int dimension_x, int dimension_y, int random_count) {
//        Life.dimension_x = dimension_x;
//        Life.dimension_y = dimension_y;
//        Life.random_count = random_count;

        initArrays();
        fillCellArray();
        fillNear();

// генерация того, что нам надо - обязательно
        generateRandomLiveCells();
//        generate_symmetric();
//        generateSimmetricFigure();
//        generate_symmetric_x();
//        generate_1();

        divide(2,2);

        getStatistics();
    }

    void restart() {
        check = new Check();
        clearArray();
        generate_symmetric();
//        generateRandomLiveCells();
//        generateSimmetricFigure();
//        generate_symmetric_x();
//        generate_1();

        getStatistics();
    }

    void clearArray() {
        Info.reset();
        Arrays.stream(cellArray)
                .flatMap(Arrays::stream)
                .forEach(cell -> {
                    cell.statistics.reset();
                    cell.cellStatus = CellStatus.NONE;
                });
    }


    private void initArrays() {
        cellArray = new Cell[Config.X_S][Config.Y_S];

    }

    private void fillCellArray() {
        for (int x = 0; x < Config.X_S; x++)
            for (int y = 0; y < Config.Y_S; y++) {
                cellArray[x][y] = new Cell(x, y);
            }
    }

    void fillNear() {
        for (int x = 0; x < Config.X_S; x++)
            for (int y = 0; y < Config.Y_S; y++) {
                cellArray[x][y].fill(cellArray);
            }
    }


    void getStatistics() {
//        long t1=System.currentTimeMillis();

        long countLiveCells = Arrays.stream(cellArray)
                .flatMap(Arrays::stream)
                .filter(o -> o.cellStatus == CellStatus.LIVE)
                .count();


        Optional<Integer> maxGeneration = Arrays.stream(cellArray)
                .flatMap(Arrays::stream)
                .map(o -> o.statistics.generationCount)
                .filter(o -> o > -1)
                .max(Comparator.naturalOrder());


        long maxGenerationCount
                = Arrays.stream(cellArray)
                .flatMap(Arrays::stream)
                .filter(o -> o.statistics.generationCount == maxGeneration.orElse(0))
                .count();
//                .collect(Collectors.groupingBy(o->max_generation_count));
//
        Info.live_cells_count = (int) countLiveCells;
        Info.max_generation = maxGeneration.orElse(0);
        Info.count_of_max_generations = (int) maxGenerationCount;

        check.add((int) countLiveCells);
//        System.out.println(check);
        Info.isRepeated = check.test();
//        if(Stat.isRepeated.isPresent())
//            System.out.println("match");

//        System.out.println("Step: "+ life_step.get()+" Lives: " + countLiveCells + " Max. generation: " + maxGeneration.get() + " count: " + maxGenerationCount);
//
//        long t2=System.currentTimeMillis();
//        System.out.println("Statisticss: " + (t2-t1));

    }

    void lifeCycle() {

//        long t1=System.currentTimeMillis();
//        parallel_step_1();
//        long t2=System.currentTimeMillis();
//        System.out.println("Parallel step 1: " + (t2-t1));

//        long t1=System.currentTimeMillis();
        step_1();
//        long t2=System.currentTimeMillis();
//        System.out.println("Step 1: " + (t2-t1));
//        t1=System.currentTimeMillis();
//        parallel_step_2();
//        t2=System.currentTimeMillis();
//        System.out.println("Parallel step 2: " + (t2-t1));
//        t1=System.currentTimeMillis();
        step_2();
//        t2=System.currentTimeMillis();
//        System.out.println("Step 2: " + (t2-t1));


        ++Info.life_step;
        getStatistics();
//        System.out.println("Step: " + Stat.life_step);


    }

    void parallel_step_1() {
//        int x1 = 0;
//        int x2 = Config.X_S / 2;
//        int y1 = 0;
//        int y2 = Config.Y_S / 2;

//        Runnable r1 = new Func(0, Config.X_S / 2, 0, Config.Y_S / 2, cellArray);
//        Runnable r2 = new Func(Config.X_S / 2, Config.X_S, 0,  Config.Y_S / 2, cellArray);
//        Runnable r3 = new Func(0, Config.X_S / 2, Config.Y_S / 2, Config.Y_S, cellArray);
//        Runnable r4 = new Func(Config.X_S / 2, Config.X_S, Config.Y_S / 2, Config.Y_S, cellArray);

        List<Future<?>> fl=new ArrayList<>();
        for(Runnable r: runnableList1){
            fl.add(executor.submit(r));
        }


        try {
            for(Future<?> f: fl){
                f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void parallel_step_2(){

        List<Future<?>> fl=new ArrayList<>();
        for(Runnable r: runnableList2){
            fl.add(executor.submit(r));
        }

        try {
            for(Future<?> f: fl){
                f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void step_1() {
        for (int x = 0; x < Config.X_S; x++)
            for (int y = 0; y < Config.Y_S; y++) {
                cellArray[x][y].step_1();
            }
//        Arrays.stream(cellArray).
//                flatMap((Function<Cell[], Stream<Cell>>) Arrays::stream).
//                parallel().
//                forEach(Cell::step_1);
    }

    void step_2() {
        for (int x = 0; x < Config.X_S; x++)
            for (int y = 0; y < Config.Y_S; y++) {
                cellArray[x][y].step_2();
            }
//        Arrays.stream(cellArray).
//                flatMap((Function<Cell[], Stream<Cell>>) Arrays::stream).
//                parallel().
//                forEach(Cell::step_2);
    }


    void generateRandomLiveCells() {

        int count = 0;
        while (count < Config.RND) {

            int x = ThreadLocalRandom.current().nextInt(Config.X_S);
            int y = ThreadLocalRandom.current().nextInt(Config.Y_S);
            if (cellArray[x][y].cellStatus != CellStatus.LIVE) {
                cellArray[x][y].cellStatus = CellStatus.LIVE;
                cellArray[x][y].statistics.generationCount = 1;
                ++count;
            }
        }
        Info.live_cells_count = Config.RND;
    }

    void generate_1() {
        int DELTA = 30;
        List<Coordinate> polygon = ValtrAlgorithm.generateRandomConvexPolygon(20, 30);

//        Polygon p=new Polygon();
//        for(Point2D.Double d:list){
//            p.addPoint((int)d.x, (int)d.y);
//        }


        for (Coordinate coordinate : polygon) {
            cellArray[coordinate.x + DELTA][coordinate.y + DELTA].cellStatus = CellStatus.LIVE;
            cellArray[coordinate.x + DELTA][coordinate.y + DELTA].statistics.generationCount = 1;
        }

        for (int i = 0; i < 500; i++) {
            int x = ThreadLocalRandom.current().nextInt(50);
            int y = ThreadLocalRandom.current().nextInt(50);
            if (ValtrAlgorithm.isInsideByEvenOddRule(x, y, polygon)) {
//                System.out.println("Point x: " + x + " y: " + y +" is in");
                cellArray[x + DELTA][y + DELTA].cellStatus = CellStatus.LIVE;
                cellArray[x + DELTA][y + DELTA].statistics.generationCount = 1;
            } else {
//                System.out.println("Point x: " + x + " y: " + y +" is out");
            }
        }

    }

    void generate_symmetric_x() {

        int len_x = 30;
        int len_y = 30;
        int delta = 3;

        List<Coordinate> arr = new ArrayList<>();

        for (int i = len_x; i > 0; i--) {
            int count = ThreadLocalRandom.current().nextInt(i + 1);
            while (count-- > 0) {
                int d = ThreadLocalRandom.current().nextInt(delta);
                int sign = ThreadLocalRandom.current().nextInt(100) > 50 ? 1 : -1;
                d *= sign;
                int effective_x = i + d;

                d = ThreadLocalRandom.current().nextInt(delta);
                sign = ThreadLocalRandom.current().nextInt(100) > 50 ? 1 : -1;
                d *= sign;
                int effective_y = i + d;

                arr.add(new Coordinate(effective_x, effective_y));
            }
        }
        int dx = Config.X_S / 2 - len_x / 2;
        int dy = Config.Y_S / 2 - len_y / 2;

        for (Coordinate c : arr) {
            cellArray[dx + c.x][dy + c.y].cellStatus = CellStatus.LIVE;
            cellArray[dx + c.x][dy + c.y].statistics.generationCount = 1;

            int new_x = (Config.X_S / 2) * 2 - 1 - c.x - dx;
            int new_y = (Config.Y_S / 2) * 2 - 1 - c.y - dy;

            cellArray[new_x][dy + c.y].cellStatus = CellStatus.LIVE;
            cellArray[new_x][dy + c.y].statistics.generationCount = 1;

            cellArray[dx + c.x][new_y].cellStatus = CellStatus.LIVE;
            cellArray[dx + c.x][new_y].statistics.generationCount = 1;

            cellArray[new_x][new_y].cellStatus = CellStatus.LIVE;
            cellArray[new_x][new_y].statistics.generationCount = 1;

        }
    }

    void generateSimmetricFigure() {
        int x_dimension = 40;
        int y_dimension = 40;

        boolean[][] figure = new boolean[x_dimension][y_dimension];

//        for(int x=0;x<x_dimension;x++){
//            for(int y=0;y<y_dimension;y++){
//                figure[x][y]=new Cell(x,y);
//            }
//        }

        int count = 0;
        while (count < 170) {

            int x = ThreadLocalRandom.current().nextInt(x_dimension / 2);
            int y = ThreadLocalRandom.current().nextInt(y_dimension / 2);

            if (!figure[x][y]) {
                int new_x = (x_dimension / 2) * 2 - 1 - x;
                int new_y = (y_dimension / 2) * 2 - 1 - y;
                figure[x][y] = true;
                figure[new_x][y] = true;
//                figure[x][new_y] = true;
//                figure[new_x][new_y] = true;

//                cellArray[new_x][y]=new Cell(cellArray[x][y]);
                ++count;
            }
        }

        int dx = Config.X_S / 2 - x_dimension / 2;
        int dy = Config.Y_S / 2 - y_dimension / 2;

        for (int x = 0; x < x_dimension; x++) {
            for (int y = 0; y < y_dimension; y++) {
                if (figure[x][y]) {
                    cellArray[x + dx][y + dy].cellStatus = CellStatus.LIVE;
                    cellArray[x + dx][y + dy].statistics.generationCount = 1;
                }
            }
        }

    }


    void generate_symmetric() {

        int count = 0;
        while (count < Config.RND/10 ) {

            int x = ThreadLocalRandom.current().nextInt(Config.X_S / 2);
            int y = ThreadLocalRandom.current().nextInt(Config.Y_S / 2);
            if (cellArray[x][y].cellStatus != CellStatus.LIVE) {
                cellArray[x][y].cellStatus = CellStatus.LIVE;
                cellArray[x][y].statistics.generationCount = 1;
                int new_x = (Config.X_S / 2) * 2 - 1 - x;
                int new_y = (Config.Y_S / 2) * 2 - 1 - y;

                cellArray[new_x][y].cellStatus = CellStatus.LIVE;
                cellArray[new_x][y].statistics.generationCount = 1;

                cellArray[x][new_y].cellStatus = CellStatus.LIVE;
                cellArray[x][new_y].statistics.generationCount = 1;

                cellArray[new_x][new_y].cellStatus = CellStatus.LIVE;
                cellArray[new_x][new_y].statistics.generationCount = 1;


//                cellArray[new_x][y]=new Cell(cellArray[x][y]);
                ++count;
            }
        }


        Info.live_cells_count = Config.RND;

    }

    @Override
    public String toString() {
        return "Step: " + Info.life_step
                + "\t" +
                "Cells: " + Info.live_cells_count + "\t" +
                "Max. generation: " + Info.max_generation + "\t" +
                "Generatio count: " + Info.count_of_max_generations + "\t";
//                "Delta: " + (life_step - max_generation);
    }


}
