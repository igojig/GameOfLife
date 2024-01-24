package life;

import java.awt.*;

public enum CellStatus {
    NONE(Color.black),
    BORN(Color.GREEN),
    LIVE(Color.white),
    DEAD(Color.GRAY);

    private final Color color;

    public Color getColor() {
        return color;
    }


    CellStatus(Color color) {
        this.color = color;
    }

//    CellStatus step_1(int count) {
//        return switch (this) {
//            case NONE -> count == 3 ? BORN : NONE;
//            case LIVE -> (count == 2 || count == 3) ? LIVE : DEAD;
//            default -> this;
//        };
//    }


//    CellStatus step_2(Cell cell) {
//        switch (this) {
//            case LIVE:
//                cell.generationCount++;
//                return this;
//            case BORN:
//                cell.generationCount =1;
//                Life.live_cells_count++;
//                return LIVE;
//            case DEAD:
//                cell.generationCount =-1;
//                Life.live_cells_count--;
//                return NONE;
//            default:
//                return this;
//        }
//    }
}
