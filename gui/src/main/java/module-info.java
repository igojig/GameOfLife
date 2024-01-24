module ru.igojig.life_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens ru.igojig.life_gui to javafx.fxml;
    exports ru.igojig.life_gui;
    exports ru.igojig.life_gui.nodes;
    opens ru.igojig.life_gui.nodes to javafx.fxml;
    exports ru.igojig.life_gui.controllers;
    opens ru.igojig.life_gui.controllers to javafx.fxml;
}