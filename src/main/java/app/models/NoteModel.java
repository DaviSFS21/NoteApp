package app.models;

public class NoteModel {
    Integer id;
    String title;
    String author;
    String content;

    public NoteModel(String title, String author, String content) {
        this.id = 0;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public NoteModel(Integer id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("""
               %s - %s (ID: %d)
               %s
               """, title, author, id, content);
    }
}
