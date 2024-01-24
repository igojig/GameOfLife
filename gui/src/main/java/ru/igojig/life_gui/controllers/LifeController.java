package ru.igojig.life_gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import ru.igojig.life_gui.nodes.LifeCanvas;

import java.net.URL;
import java.util.ResourceBundle;

public class LifeController implements Initializable {


    @FXML
    public Button btnStart;
    LifeCanvas lifeCanvas=new LifeCanvas();
    @FXML
    public Pane canvasPane;

    @FXML
    public Button btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvasPane.getChildren().add(lifeCanvas);
        lifeCanvas.heightProperty().bind(canvasPane.heightProperty());

        lifeCanvas.widthProperty().bind(canvasPane.widthProperty());
    }


    @FXML
    public void onStart(ActionEvent actionEvent) {
        lifeCanvas.drawGrid();
    }

    @FXML
    public void onAction(ActionEvent actionEvent) {
        lifeCanvas.clear();
    }
}
