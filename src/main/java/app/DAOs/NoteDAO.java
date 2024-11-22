package app.DAOs;

import app.helpers.DBConnection;
import app.models.NoteModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteDAO {
    public int createNote(String title, String author, String content) {
        String sql = "INSERT INTO note(title, author, content) VALUES (?,?,?)";
        int id = 0;

        try {
            id = DBConnection.executeUpdate(sql,title, author, content);
        } catch (SQLException e) {
            System.out.println("Error creating a note: " + e);
        } finally {
            DBConnection.closeResources();
        }

        return id;
    }

    public void editNote(NoteModel note) {
        String sql = "UPDATE note SET title = ?, author = ?, content = ? WHERE id = ?";

        try {
            DBConnection.executeUpdate(sql, note.getTitle(), note.getAuthor(), note.getContent(), note.getId());
        } catch (SQLException e) {
            System.out.println("Error editing a note: " + e);
        } finally {
            DBConnection.closeResources();
        }
    }

    public NoteModel getNote(int id) {
        String sql = "SELECT * FROM note WHERE id = ?";
        NoteModel note = null;

        try (ResultSet rs = DBConnection.executeQuery(sql,id)) {
            while (rs.next()) {
                String title = rs.getString("title");
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
