package ru.igojig.life_gui.nodes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.igojig.life_gui.LifeApp;

public class LifeCanvas extends Canvas {


    private final GraphicsContext context;

    public LifeCanvas() {
        this.context = this.getGraphicsContext2D();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
//        super.resize(width, height);
//        drawGrid(width, height);

    }

    public void drawGrid(){
        double width=getWidth();
        double height=getHeight();
        drawGrid(width, height);
    }

    public void drawGrid(double width, double height) {
//        int width = (int) lifeCanvas.getWidth();
//        int height = (int) lifeCanvas.getHeight();

        double minX=this.getBoundsInLocal().getMinX();
        double minY=getBoundsInLocal().getMinY();
        context.clearRect(0, 0, width, height);
        fillBackground();
        grid(width, height);
    }

    private void grid(double width, double height) {
        context.setStroke(Color.WHITE);
        context.setFill(Color.BLUE);
        context.setLineWidth(LifeApp.LINE_WIDTH);

        for (int y = 0; y < height; y += LifeApp.BOX_LEN) {
            context.strokeLine(0, y, width, y);
        }
        for (int x = 0; x < width; x+=LifeApp.BOX_LEN) {
            context.strokeLine(x, 0, x, height);
        }

//        context.fillOval(width / 2, height / 2, 50, 50);
//        for (int y = 0; y < LifeApp.Y_LEN*LifeApp.BOX_LEN; y += LifeApp.BOX_LEN) {
//            context.strokeLine(0, y, width, y);
//        }
//        for (int x = 0; x < LifeApp.X_LEN*LifeApp.BOX_LEN; x+=LifeApp.BOX_LEN) {
//            context.strokeLine(x, 0, x, height);
//        }
    }

    public void drawField(){

    }

    public void fillBackground(){
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, getWidth(), getHeight());
    }

    public void clear(){
        context.setFill(Color.GRAY);
        context.clearRect(0, 0, getWidth(), getHeight());
    }
}
