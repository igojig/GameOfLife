package life;

import java.util.Objects;

public class Cell {

    CellStatus cellStatus;
    Coordinate coordinate;

    Statistics statistics;


//    List<Cell> nearCellsList = new ArrayList<>(8);
    Cell[] nearCellsList=new Cell[8];


//    public Cell(Cell cell) {
//        cellStatus = cell.cellStatus;
////        coordinate = new Coordinate(cell.coordinate);
//        statistics = new Statistics(cell.statistics);
////        nearCellsList=null;
//    }

    public Cell(Coordinate coordinate, CellStatus cellStatus) {
        this.coordinate = coordinate;
        this.cellStatus = cellStatus;
//        generationCount = -1;
//        fillNearCells();
        Statistics statistics = new Statistics();
    }

    public Cell(int x, int y, CellStatus cellStatus) {
        this.coordinate = new Coordinate(x, y);
        this.cellStatus = cellStatus;
//        generationCount = -1;
//        fillNearCells();
        Statistics statistics = new Statistics();
    }

    public Cell(int x, int y) {
        this.coordinate = new Coordinate(x, y);
        this.cellStatus = CellStatus.NONE;
//        generationCount = -1;
        this.statistics = new Statistics();
    }

    void fill(Cell[][] cells) {

        int[][] coords_rule_flase = {
                {-1, -1}, {0, -1}, {1, -1},
                {-1, 0}, {}, {1, 0},
                {-1, 1}, {0, 1}, {1, 1},
        };

        int[][] coords_rule_true = {
                {-1, -1}, {0, -1}, {1, -1},
                {-1, 0}, {}, {1, 0},
                {-1, 1}, {0, 1}, {1, 1},
        };

//        for (int i = 0; i < coords_rule_flase.length; i++) {
//            int dx =  coords_rule_flase[i][0];
//            int dy =  coords_rule_flase[i][1];
//
//        }

        int x = coordinate.x;
        int y = coordinate.y;
        int index=0;
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++) {
                if (!Game.LOOPED_FIELD) {
                    if (isValidCoordinates(x + dx, y + dy) && !(dx == 0 && dy == 0)){
                        nearCellsList[index++]=cells[x + dx][y + dy];
//                        nearCellsList.add(cells[x + dx][y + dy]);

                    }
                } else {
                    if (!(dx == 0 && dy == 0)) {
                        nearCellsList[index++]=cells[(x + dx + Game.WIDTH) % Game.WIDTH][(y + dy + Game.HEIGHT) % Game.HEIGHT];
//                        nearCellsList.add(cells[(x + dx + Game.WIDTH) % Game.WIDTH][(y + dy + Game.HEIGHT) % Game.HEIGHT]);
                    }
                }
            }
    }

    boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < Game.WIDTH && y >= 0 && y < Game.HEIGHT;
    }

    int calculateNearLiveCells() {
        int count = 0;
        for (Cell c : nearCellsList) {
            if (c!=null && (c.cellStatus == CellStatus.LIVE || c.cellStatus == CellStatus.DEAD))
                ++count;
        }
        return count;
    }


    void step_1() {

        int count = calculateNearLiveCells();
        cellStatus = switch (cellStatus) {
            case NONE -> count == 3 ? CellStatus.BORN : CellStatus.NONE;
            case LIVE -> (count == 2 || count == 3) ? CellStatus.LIVE : CellStatus.DEAD;
            default -> cellStatus;
        };
    }

    void step_2() {

        cellStatus = switch (cellStatus) {
            case LIVE -> {
                statistics.generationCount++;
                yield CellStatus.LIVE;
            }
            case BORN -> {
                statistics.generationCount = 1;
                statistics.reborn++;
//                Life.live_cells_count++;
                yield CellStatus.LIVE;
            }
            case DEAD -> {
                statistics.generationCount = -1;
//                Life.live_cells_count--;
                yield CellStatus.NONE;
            }
            default -> CellStatus.NONE;
        };
    }


//    Color getColor() {
////        Color c = Color.RED;
//
////        if (statistics.reborn >1 && cellStatus==CellStatus.LIVE)
////            return Color.GREEN;
////
////        if (statistics.generationCount == Life.Stat.max_generation)
////            return Color.RED;
////        if (statistics.generationCount > Life.Stat.max_generation - 2)
////            return Color.ORANGE;
////        if (statistics.generationCount > Life.Stat.max_generation - 3)
////            return Color.PINK;
//
//        return cellStatus.color;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return Objects.equals(coordinate, cell.coordinate);
    }

    @Override
    public int hashCode() {
//        return Objects.hash(cellStatus, coordinate);
        return Objects.hash(coordinate);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "cellStatus=" + cellStatus +
                ", coordinate=" + coordinate +
//                ", nearCellsList=" + nearCellsList +
                '}' + '\n';
    }
}
