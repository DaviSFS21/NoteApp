package app.DAOs;

import app.helpers.DBConnection;
import app.helpers.Utils;
import app.models.NoteModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class NoteDAO {
    public int createNote(NoteModel note) {
        String sql = "INSERT INTO note(title, author, content) VALUES (?,?,?)";
        int id = 0;

        try {
            id = DBConnection.executeUpdate(sql, note.getTitle(), note.getAuthor(), note.getContent());
        } catch (SQLIntegrityConstraintViolationException e) {
            Utils.setAlert("ERROR", "Create note", "This note already exists...");
        } catch (SQLException e) {
            System.out.println("Error creating a note: " + e);
        } finally {
            DBConnection.closeResources();
        }

        return id;
    }

    public int editNote(NoteModel note) {
        int rowsAffected = 0;

        String sql = "UPDATE note SET title = ?, author = ?, content = ? WHERE id = ?";

        try {
            rowsAffected = DBConnection.executeUpdate(sql, note.getTitle(), note.getAuthor(), note.getContent(), note.getId());
        } catch (SQLIntegrityConstraintViolationException e) {
            Utils.setAlert("ERROR", "Create note", "This note already exists...");
        } catch (SQLException e) {
            System.out.println("Error editing a note: " + e);
        } finally {
            DBConnection.closeResources();
        }

        return rowsAffected;
    }

    public NoteModel getNoteByName(String title) {
        String sql = "SELECT * FROM note WHERE title = ?";
        NoteModel note = null;

        try (ResultSet rs = DBConnection.executeQuery(sql,title)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String content = rs.getString("content");

                note = new NoteModel(id, title, author, content);
            }
        } catch (SQLException e) {
            System.out.println("Error reading notes from database: " + e);
        } finally {
            DBConnection.closeResources();
        }
        return note;
    }

    public void deleteNote(int id) {
        String sql = "DELETE FROM note WHERE id = ?";

        try {
            DBConnection.executeUpdate(sql, id);
        } catch (SQLException e) {
            System.out.println("Error deleting a note: " + e);
        } finally {
            DBConnection.closeResources();
        }
    }
}
