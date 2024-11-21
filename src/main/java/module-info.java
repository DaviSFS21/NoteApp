module app.controllers {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens app.controllers to javafx.fxml;
    exports app.controllers;
    exports app.launcher;
    opens app.launcher to javafx.fxml;
}