package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Book;
import model.BooksCollection;
import model.Review;
import org.json.*;

// All code (and specifications) in this class was taken and modified from
// the JsonReader class in the JsonSerializationDemo project:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
// Represents a reader that reads books collection from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads books collection from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BooksCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBooksCollection(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses book collection from JSON object and returns it
    private BooksCollection parseBooksCollection(JSONObject jsonObject) {
        BooksCollection bc = new BooksCollection();
        addBooks(bc, jsonObject);
        return bc;
    }

    // MODIFIES: bc
    // EFFECTS: parses books from JSON object and adds them to books collection
    private void addBooks(BooksCollection bc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("books");

        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addBook(bc, nextBook);
        }
    }

    // MODIFIES: bc
    // EFFECTS: parses book from JSON object and adds it to books collection
    private void addBook(BooksCollection bc, JSONObject jsonObject) {
        String t = jsonObject.getString("title");
        String a = jsonObject.getString("author");
        String g = jsonObject.getString("genre");
        int p = jsonObject.getInt("pages");
        boolean i = jsonObject.getBoolean("isFinished");
        Book book = new Book(t, a, g, p, i);

        if (jsonObject.getBoolean("hasReview")) {
            String l = jsonObject.getString("like");
            String d = jsonObject.getString("dislike");
            int r = jsonObject.getInt("rating");
            Review bookReview = new Review(l, d, r);
            book.addReview(bookReview);
        }

        bc.addToCollection(book);
    }
}