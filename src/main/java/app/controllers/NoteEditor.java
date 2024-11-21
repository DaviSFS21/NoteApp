package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NoteEditor {
    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private TextArea content;

    public void startNewNote() {
        title.setText(null);
        author.setText(null);
        content.setText(null);
    }

    public void saveNote() {}

    public void deleteNote() {}
}
