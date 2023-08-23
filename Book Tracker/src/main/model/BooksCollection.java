package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a collection of books with methods to retrieve
// number of books in collection, and finished or unfinished books
public class BooksCollection implements Writable {

    private ArrayList<Book> books;
    private EventLog changeLog;

    // EFFECTS: Creates new book collection with no books within it.
    public BooksCollection() {
        books = new ArrayList<>();
        changeLog = changeLog.getInstance();
    }

    // The following code is from the solution to question 10
    // in practice midterm one (Rev1.10):
    // https://ca.prairielearn.com/pl/course_instance/3694/instance_question/59979417/
    // EFFECTS: returns list with all Books that have
    // status of unfinished in the collection
    public ArrayList<Book> getTBR() {
        ArrayList<Book> tbr = new ArrayList<>();
        for (Book b : this.books) {
            if (!b.isFinished()) {
                tbr.add(b);
            }
        }
        return tbr;
    }

    // The following code is from the solution to question 10
    // in practice midterm one (Rev1.10):
    // https://ca.prairielearn.com/pl/course_instance/3694/instance_question/59979417/
    // EFFECTS: returns list with all Books that have
    // status of Finished
    public ArrayList<Book> getFinished() {
        ArrayList<Book> finished = new ArrayList<>();
        for (Book b : this.books) {
            if (b.isFinished()) {
                finished.add(b);
            }
        }
        return finished;
    }

    // EFFECTS: returns an array with all the Book titles in collection;
    public String[] getAllTitles() {
        String[] titles = new String[books.size()];
        for (int i = 0; i < books.size(); i++) {
            titles[i] = books.get(i).getTitle();
        }
        return titles;
    }

    // MODIFIES: this
    // EFFECTS: removes b from book collection and adds event to change log
    public void removeBook(Book b) {
        books.remove(b);
        changeLog.logEvent(new Event(b.getTitle() + " was removed from the tracker."));
    }

    // The following code is taken from the WorkRoom class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/model/WorkRoom.java
    // EFFECTS: puts collection into json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("books", booksToJson());
        return json;
    }

    // The following code is taken from the WorkRoom class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/model/WorkRoom.java
    // EFFECTS: returns books in collection as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book b : books) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }

    // REQUIRES: book is not already in collection
    // MODIFIES: this
    // EFFECTS: adds book to collection and adds event to change log
    public void addToCollection(Book b) {
        books.add(b);
        changeLog.logEvent(new Event(b.getTitle() + " was added to the tracker."));
    }

    // EFFECTS: returns true if book is in collection
    public boolean containsBook(Book b) {
        return books.contains(b);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book getBook(int i) {
        return books.get(i);
    }

    public int getCollectionSize() {
        return books.size();
    }
}
