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
    NoteModel note = new NoteModel("","","");

    public void startNewNote() {
        note = new NoteModel("","","");
        title.setText(null);
        author.setText(null);
        content.setText(null);
    }

    public void saveNote() {
        note = new NoteModel(title.getText(), author.getText(), content.getText());
        int id = noteDAO.createNote(note.getTitle(), /*note.getAuthor()*/"user", note.getContent());
        note.setId(id);
    }

    public void deleteNote() {
        if (note.getId() != null) {
            noteDAO.deleteNote(note.getId());
        }
        startNewNote();
    }
}
