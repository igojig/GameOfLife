package life;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.*;


public class Life {

    Check check = new Check();

    Cell[][] cellArray;

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



    Life() {


        initArrays();
        fillCellArray();
        fillNear();

// генерация того, что нам надо - обязательно
        generateRandomLiveCells();
//        generate_symmetric();
//        generateSimmetricFigure();
//        generate_symmetric_x();
//        generate_1();

//        divide(2, 2);

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

//        getStatistics();
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
        cellArray = new Cell[Game.WIDTH][Game.HEIGHT];

    }

    private void fillCellArray() {
        for (int x = 0; x < Game.WIDTH; x++)
            for (int y = 0; y < Game.HEIGHT; y++) {
                cellArray[x][y] = new Cell(x, y);
            }
    }

    void fillNear() {
        for (int x = 0; x < Game.WIDTH; x++)
            for (int y = 0; y < Game.HEIGHT; y++) {
                cellArray[x][y].fill(cellArray);
            }
    }


    void getStatistics() {

//        long countLiveCells = Arrays.stream(cellArray)
//                .flatMap(Arrays::stream)
//                .filter(o -> o.cellStatus == CellStatus.LIVE)
//                .count();
        int countLiveCells = 0;
        int maxGeneration = -1;
        int max = -1;
        int maxGenerationCount =-1;

        for (int x = 0; x < Game.WIDTH; ++x) {
            for (int y = 0; y < Game.HEIGHT; ++y) {
                if (cellArray[x][y].cellStatus == CellStatus.LIVE) {
                    ++countLiveCells;
                }


                if (cellArray[x][y].statistics.generationCount > max) {
                    max = cellArray[x][y].statistics.generationCount;
                }


            }
        }
        maxGeneration = max;

        for (int x = 0; x < Game.WIDTH; ++x) {
            for (int y = 0; y < Game.HEIGHT; ++y) {
                if(maxGeneration!=-1){
                    if(cellArray[x][y].statistics.generationCount==maxGeneration){
                        ++maxGenerationCount;
                    }
                }

            }
        }


//        Optional<Integer> maxGeneration = Arrays.stream(cellArray)
//                .flatMap(Arrays::stream)
//                .map(o -> o.statistics.generationCount)
//                .filter(o -> o > -1)
//                .max(Comparator.naturalOrder());


//        long maxGenerationCount
//                = Arrays.stream(cellArray)
//                .flatMap(Arrays::stream)
//                .filter(o -> o.statistics.generationCount == maxGeneration.orElse(0))
//                .count();
//                .collect(Collectors.groupingBy(o->max_generation_count));
//
        Info.live_cells_count = (int) countLiveCells;
        Info.max_generation = maxGeneration;
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



    void step_1() {
        for (int x = 0; x < Game.WIDTH; x++)
            for (int y = 0; y < Game.HEIGHT; y++) {
                cellArray[x][y].step_1();
            }
//        Arrays.stream(cellArray).
//                flatMap((Function<Cell[], Stream<Cell>>) Arrays::stream).
//                parallel().
//                forEach(Cell::step_1);
    }

    void step_2() {
        for (int x = 0; x < Game.WIDTH; x++)
            for (int y = 0; y < Game.HEIGHT; y++) {
                cellArray[x][y].step_2();
            }
//        Arrays.stream(cellArray).
//                flatMap((Function<Cell[], Stream<Cell>>) Arrays::stream).
//                parallel().
//                forEach(Cell::step_2);
    }


    void generateRandomLiveCells() {

        int count = 0;
        while (count < Game.RANDOM_CELLS) {

            int x = ThreadLocalRandom.current().nextInt(Game.WIDTH);
            int y = ThreadLocalRandom.current().nextInt(Game.HEIGHT);
            if (cellArray[x][y].cellStatus != CellStatus.LIVE) {
                cellArray[x][y].cellStatus = CellStatus.LIVE;
                cellArray[x][y].statistics.generationCount = 1;
                ++count;
            }
        }
        Info.live_cells_count = Game.RANDOM_CELLS;
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
        int dx = Game.WIDTH / 2 - len_x / 2;
        int dy = Game.HEIGHT / 2 - len_y / 2;

        for (Coordinate c : arr) {
            cellArray[dx + c.x][dy + c.y].cellStatus = CellStatus.LIVE;
            cellArray[dx + c.x][dy + c.y].statistics.generationCount = 1;

            int new_x = (Game.WIDTH / 2) * 2 - 1 - c.x - dx;
            int new_y = (Game.HEIGHT / 2) * 2 - 1 - c.y - dy;

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

        int dx = Game.WIDTH / 2 - x_dimension / 2;
        int dy = Game.HEIGHT / 2 - y_dimension / 2;

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
        while (count < Game.RANDOM_CELLS / 10) {

            int x = ThreadLocalRandom.current().nextInt(Game.WIDTH / 2);
            int y = ThreadLocalRandom.current().nextInt(Game.HEIGHT / 2);
            if (cellArray[x][y].cellStatus != CellStatus.LIVE) {
                cellArray[x][y].cellStatus = CellStatus.LIVE;
                cellArray[x][y].statistics.generationCount = 1;
                int new_x = (Game.WIDTH / 2) * 2 - 1 - x;
                int new_y = (Game.HEIGHT / 2) * 2 - 1 - y;

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


        Info.live_cells_count = Game.RANDOM_CELLS;

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
