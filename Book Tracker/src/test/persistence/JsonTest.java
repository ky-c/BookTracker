package persistence;

import model.Book;
import model.Review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// This code was modified from code from the JsonTest class in the JsonSerializationDemo:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonTest.java
// checks if book information is equal to the book given
public class JsonTest {

    protected Book tbr1 = new Book("tbr1", "auth1", "fantasy", 392, false);
    protected Book tbr2 = new Book("tbr2", "auth2", "action", 178, false);
    protected Book fin1 = new Book("fin1", "auth3", "sci-fi", 467, true);
    protected Book fin2 = new Book("fin2", "auth4", "horror", 250, true);
    protected Review rev = new Review("i like this", "i hate this", 4);
    protected Review rev1 = new Review("bop it", "twist it", 2);

    protected void checkBook(String title, String author, String genre, int pages,
                             boolean isFinished, Review r, Book b) {
        Review ogRev = b.getReview();

        assertEquals(title, b.getTitle());
        assertEquals(author, b.getAuthor());
        assertEquals(genre, b.getGenre());
        assertEquals(pages, b.getPages());
        assertEquals(isFinished, b.isFinished());

        if (ogRev == null) {
            assertNull(r);
        } else {
            assertEquals(r.getLike(), ogRev.getLike());
            assertEquals(r.getDislike(), ogRev.getDislike());
            assertEquals(r.getRating(), ogRev.getRating());
        }
    }
}
