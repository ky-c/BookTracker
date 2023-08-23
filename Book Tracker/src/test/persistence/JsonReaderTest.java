package persistence;

import model.Book;
import model.BooksCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// All code/tests within this class were based on code from the
// JsonReaderTest class in the JsonSerializationDemo project:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java
public class JsonReaderTest extends JsonTest {

    private BooksCollection bc;
    private JsonReader reader;

    @BeforeEach
    void runBefore() {
        bc = new BooksCollection();
    }

    @Test
    void testReaderInvalidFile() {
        reader = new JsonReader("./data/DNEFile.json");
        try {
            bc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCollection() {
        reader = new JsonReader("./data/testReaderEmptyCollection.json");
        try {
            bc = reader.read();
            assertEquals(0, bc.getCollectionSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderOneBookCollection() {
        reader = new JsonReader("./data/testReaderOneBookCollection.json");
        try {
            bc = reader.read();
            List<Book> books = bc.getBooks();
            assertEquals(1, books.size());
            Book b1 = books.get(0);

            checkBook(b1.getTitle(), b1.getAuthor(), b1.getGenre(), b1.getPages(),
                    b1.isFinished(), b1.getReview(), tbr2);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderManyBooksCollection() {
        JsonReader reader = new JsonReader("./data/testReaderManyBooksCollection.json");
        try {
            bc = reader.read();
            List<Book> books = bc.getBooks();
            assertEquals(2, books.size());
            Book b1 = books.get(0);
            Book b2 = books.get(1);
            fin2.addReview(rev1);

            checkBook(b1.getTitle(), b1.getAuthor(), b1.getGenre(), b1.getPages(),
                    b1.isFinished(), b1.getReview(), fin2);
            checkBook(b2.getTitle(), b2.getAuthor(), b2.getGenre(), b2.getPages(),
                    b2.isFinished(), b2.getReview(), fin1);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
