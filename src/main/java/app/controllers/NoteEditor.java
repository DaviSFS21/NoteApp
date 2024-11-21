package app.controllers;

import app.DAOs.NoteDAO;
import app.models.NoteModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NoteEditor {
    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private TextArea content;

    NoteDAO noteDAO = new NoteDAO();

    public void startNewNote() {
        title.setText(null);
        author.setText(null);
        content.setText(null);
    }

    public void saveNote() {
        NoteModel note = new NoteModel(title.getText(),author.getText(),content.getText());
        System.out.println(note);
        noteDAO.createNote(note.getTitle(), note.getAuthor(), note.getContent());
    }

    public void deleteNote() {
        int noteID = 0;
        noteDAO.deleteNote(noteID);
    }
}
