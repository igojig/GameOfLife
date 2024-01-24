package ru.igojig.life_gui;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.igojig.life_gui.controllers.LifeController;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class LifeApp extends Application {
    public static final int X_LEN=50;
    public static final int Y_LEN=50;
    public static final int BOX_LEN=10;
    public static final int LINE_WIDTH=2;

    int[][] arr =new int[X_LEN][Y_LEN];


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LifeApp.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        LifeController lifeController=fxmlLoader.getController();



        Service<String> service= new Service<>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected void updateProgress(long l, long l1) {
                        System.out.println("1");
                    }

                    @Override
                    protected void updateValue(String s) {
                        System.out.println(2);
                    }

                    @Override
                    protected String call() throws Exception {
                        for (int x = 0; x < X_LEN; x++) {
                            for (int y = 0; y < Y_LEN; y++) {
                                arr[x][y] = ThreadLocalRandom.current().nextInt(0, 2);
                            }
                        }
                        return null;
                    }
                };
            }
        };


        service.start();
        System.out.println(service.getValue());
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println(workerStateEvent.getSource().getValue());
            }
        });




//        AnimationTimer animationTimer=new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                System.out.println(now);
//            }
//        };
//        animationTimer.start();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}