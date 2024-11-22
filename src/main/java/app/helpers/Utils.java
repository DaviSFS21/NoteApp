package app.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utils {
    private static Alert.AlertType setAlertType(String type) {
        return switch (type.toUpperCase()) {
            case "INFORMATION" -> Alert.AlertType.INFORMATION;
            case "ERROR" -> Alert.AlertType.ERROR;
            case "WARNING" -> Alert.AlertType.WARNING;
            case "CONFIRMATION" -> Alert.AlertType.CONFIRMATION;
            default -> Alert.AlertType.NONE;
        };
    }

    public static void setAlert(String type, String title, String text, Runnable onOkAction) {
        Alert alert = new Alert(setAlertType(type));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(_ -> {
                    if (onOkAction != null) {
                        onOkAction.run();
                    }
                });
    }

    public static void setAlert(String type, String title, String text) {
        Alert alert = new Alert(setAlertType(type));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
