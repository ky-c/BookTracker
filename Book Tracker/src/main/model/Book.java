package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a book with a title, author, genre, number of pages, and status
// indicating whether it is finished or not.
public class Book implements Writable {

    private final String title;
    private final String author;
    private final String genre;
    private final int pages;
    private boolean isFinished;
    private Review bookReview;

    // EFFECTS: creates a book with title, author, genre, pages,
    // status of finished (or not), and no book review.
    public Book(String t, String a, String g, int p, boolean i) {
        this.title = t;
        this.author = a;
        this.genre = g;
        this.pages = p;
        this.isFinished = i;
    }

    // MODIFIES: this
    // EFFECTS: adds review to book if book has no review and
    // is finished; returns true if review is added, false otherwise
    public boolean addReview(Review r) {
        if (!hasReview() && isFinished) {
            this.bookReview = r;
            return true;
        } else {
            return false;
        }
    }

    // The following code is taken and modified from the Thingy class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/model/Thingy.java
    // EFFECTS: puts book into json with corresponding review
    // if the book has one
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("author", author);
        json.put("genre", genre);
        json.put("pages", pages);
        json.put("isFinished", isFinished);
        if (hasReview()) {
            json.put("hasReview", true);
            bookReview.toJson(json);
        } else {
            json.put("hasReview", false);
        }
        return json;
    }

    // MODIFIES: this
    // EFFECTS: sets the finished status of the book to be true
    public void setFinished() {
        this.isFinished = true;
    }

    // EFFECTS: returns true if a review exists for book,
    // false otherwise
    public boolean hasReview() {
        return (bookReview != null);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getPages() {
        return pages;
    }

    public Review getReview() {
        return bookReview;
    }

}
