package persistence;

import model.Book;
import model.BooksCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// All code/tests within this class were based on code from the
// JsonWriterTest class in the JsonSerializationDemo project:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
public class JsonWriterTest extends JsonTest {

    private BooksCollection bc;
    private JsonWriter writer;

    @BeforeEach
    void runBefore() {
        bc = new BooksCollection();
    }

    @Test
    void testWriterInvalid() {
        try {
            writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was the expected result");
        } catch (IOException e) {
            // should pass
        }
    }

    @Test
    void testWriterEmptyCollection() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCollection.json");
            writer.open();
            writer.write(bc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCollection.json");
            bc = reader.read();
            assertEquals(0, bc.getCollectionSize());
        } catch (IOException e) {
            fail("IOException should not have occurred");
        }
    }

    @Test
    void testWriterOneBookCollection() {
        try {
            writer = new JsonWriter("./data/testWriterOneBookCollection.json");
            bc.addToCollection(tbr1);
            writer.open();
            writer.write(bc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterOneBookCollection.json");
            bc = reader.read();
            Book b = bc.getBook(0);
            assertEquals(1, bc.getCollectionSize());
            checkBook("tbr1", "auth1", "fantasy", 392, false, b.getReview(), tbr1);
        } catch (IOException e) {
            fail("IOException should not have occurred");
        }
    }

    @Test
    void testWriterManyBooksCollection() {
        try {
            writer = new JsonWriter("./data/testWriterManyBooksCollection.json");
            bc.addToCollection(fin1);
            fin2.addReview(rev);
            bc.addToCollection(fin2);
            writer.open();
            writer.write(bc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterManyBooksCollection.json");
            bc = reader.read();
            Book b1 = bc.getBook(0);
            Book b2 = bc.getBook(1);
            assertEquals(2, bc.getCollectionSize());
            checkBook("fin1", "auth3", "sci-fi", 467, true, b1.getReview(), fin1);
            checkBook("fin2", "auth4", "horror", 250, true, b2.getReview(), fin2);
        } catch (IOException e) {
            fail("IOException should not have occurred");
        }
    }
    }
