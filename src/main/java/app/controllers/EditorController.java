package app.controllers;

import app.DAOs.NoteDAO;
import app.helpers.DBConnection;
import app.helpers.Utils;
import app.models.NoteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.Popup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class EditorController {
    @FXML
    private TextField searchText;
    @FXML
    private Label status;
    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private TextArea content;
    private ListView<String> suggestionList;
    private Popup popup;

    private final NoteDAO noteDAO = new NoteDAO();
    private NoteModel note = new NoteModel("","","");
    ObservableList<String> suggestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize components
        suggestionList = new ListView<>();
        popup = new Popup();
        status.setText("Note");

        // Configure components
        suggestionList.setMaxHeight(200);
        suggestionList.setPrefWidth(200);
        popup.getContent().add(suggestionList);  // Move this here

        // Add text change listener
        searchText.textProperty().addListener((_, _, newValue) -> {
            if (!newValue.isEmpty()) {
                searchNotes();
            } else {
                popup.hide();
            }
        });

        // Add focus listener to hide popup when focus is lost
        searchText.focusedProperty().addListener((_, _, newValue) -> {
            if (!newValue) {
                popup.hide();
            }
        });

        suggestionList.setOnMouseClicked(_ -> {
            String selectedItem = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                searchText.setText(selectedItem);
                popup.hide();
            }
        });
    }

    public void setNote() {
        status.setText(note.getTitle());
        title.setText(note.getTitle());
        author.setText(note.getAuthor());
        content.setText(note.getContent());
    }

    public void searchNotes() {
        // Update suggestion list items
        suggestionList.getItems().clear();
        suggestionList.setItems(getSuggestions(searchText.getText()));

        // Show popup if not already showing and there are suggestions
        if (!popup.isShowing() && !suggestionList.getItems().isEmpty()) {
            Bounds bounds = searchText.localToScreen(searchText.getBoundsInLocal());
            popup.show(searchText, bounds.getMinX(), bounds.getMaxY());
        } else if (suggestionList.getItems().isEmpty()) {
            popup.hide();
        }

        suggestionList.setOnMouseClicked(_ -> {
            String selectedItem = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                note = noteDAO.getNoteByName(selectedItem);
                searchText.setText(selectedItem);
                setNote();
                popup.hide();
            }
        });
    }

    public ObservableList<String> getSuggestions(String search) {
        String sql = "SELECT title FROM note WHERE title LIKE ?";

        try (ResultSet rs = DBConnection.executeQuery(sql,'%' + search + '%')) {
            while (rs.next()) {
                String title = rs.getString("title");
                suggestions.add(title);
            }
        } catch (SQLException e) {
            System.out.println("Error reading notes from database: " + e);
        } finally {
            DBConnection.closeResources();
        }
        return suggestions;
    }

    public boolean checkChanges() {
        return !Objects.equals(title.getText(), note.getTitle()) ||
                !Objects.equals(author.getText(), note.getAuthor()) ||
                !Objects.equals(content.getText(), note.getContent());
    }

    public void startNewNote() {
        Runnable newNote = () -> {
            note = new NoteModel("","","");
            status.setText("New note");
            title.setText(null);
            author.setText(null);
            content.setText(null);
        };

        if (checkChanges()) {
            Utils.setAlert("CONFIRMATION","Start new note", "This action will discard any unsaved changes...", newNote);
        } else {
            newNote.run();
        }
    }

    public void saveNote() {
        boolean changed = false;

        if (note.getId() == 0) {
            note = new NoteModel(title.getText(), author.getText(), content.getText());
            if(note.setId(noteDAO.createNote(note)) != 0) {
                changed = true;
            }
        } else {
            note = new NoteModel(note.getId(), title.getText(), author.getText(), content.getText());
            if (noteDAO.editNote(note) != 0) {
                changed = true;
            }
        }

        if (changed) { Utils.setAlert("CONFIRMATION", "NoteApp", "The note was saved!"); }
    }

    public void deleteNote() {
        Utils.setAlert("CONFIRMATION","Delete note", "Are you sure you want to delete this note?", () -> {
            if (note.getId() != null) {
                noteDAO.deleteNote(note.getId());
            }
            startNewNote();
        });
    }
}
