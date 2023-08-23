package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// the code for checking the contents of the event log was taken from
// the EventLogTest class in the AlarmSystem project:
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/main/src/test/ca/ubc/cpsc210/alarm/
// test/EventLogTest.java
// Tests for BooksCollection class.
public class BooksCollectionTest {

    private EventLog el = EventLog.getInstance();
    private BooksCollection testCollection;
    private Book tbr1;
    private Book tbr2;
    private Book fin1;
    private Book fin2;

    @BeforeEach
    void runBefore() {
        testCollection = new BooksCollection();
        tbr1 = new Book("tbr1", "auth1", "fantasy", 392, false);
        tbr2 = new Book("tbr2", "auth2", "action", 178, false);
        fin1 = new Book("fin1", "auth3", "sci-fi", 467, true);
        fin2 = new Book("fin2", "auth4", "horror", 250, true);
    }

    @Test
    void testConstructor() {
        assertEquals(0, testCollection.getBooks().size());
    }

    @Test
    void testAddOneBook() {
        el.clear();
        testCollection.addToCollection(tbr1);
        assertEquals(1, testCollection.getCollectionSize());
        assertEquals(tbr1, testCollection.getBook(0));
        List<Event> l = new ArrayList<Event>();

        for (Event next : el) {
            l.add(next);
        }
        assertEquals(2, l.size());
        assertEquals("tbr1 was added to the tracker.", l.get(1).getDescription());
    }

    @Test
    void testAddTwoDiffBooks() {
        el.clear();
        testCollection.addToCollection(tbr2);
        testCollection.addToCollection(fin1);
        assertEquals(2, testCollection.getCollectionSize());
        assertEquals(tbr2, testCollection.getBook(0));
        assertEquals(fin1, testCollection.getBook(1));
        List<Event> l = new ArrayList<Event>();

        for (Event next : el) {
            l.add(next);
        }
        assertEquals(3, l.size());
        assertEquals("tbr2 was added to the tracker.", l.get(1).getDescription());
        assertEquals("fin1 was added to the tracker.", l.get(2).getDescription());
    }

    @Test
    void testAddTwoSameBooks() {
        testCollection.addToCollection(fin2);
        testCollection.addToCollection(fin2);
        assertEquals(2, testCollection.getCollectionSize());
        assertEquals(fin2, testCollection.getBook(0));
        assertEquals(fin2, testCollection.getBook(1));
    }

    @Test
    void testGetUnfinishedEmpty() {
        assertEquals(0, testCollection.getTBR().size());

        testCollection.addToCollection(fin1);
        testCollection.addToCollection(fin2);
        assertEquals(0, testCollection.getTBR().size());
    }

    @Test
    void testGetUnfinishedNonEmpty() {
        testCollection.addToCollection(tbr1);
        assertEquals(1, testCollection.getTBR().size());
        assertTrue(testCollection.getTBR().contains(tbr1));

        testCollection.addToCollection(fin1);
        assertEquals(1, testCollection.getTBR().size());
        assertTrue(testCollection.getTBR().contains(tbr1));

        testCollection.addToCollection(tbr2);
        assertEquals(2, testCollection.getTBR().size());
        assertTrue(testCollection.getTBR().contains(tbr1));
        assertTrue(testCollection.getTBR().contains(tbr2));

        testCollection.addToCollection(fin2);
        assertEquals(2, testCollection.getTBR().size());
        assertTrue(testCollection.getTBR().contains(tbr1));
        assertTrue(testCollection.getTBR().contains(tbr2));
    }

    @Test
    void testGetFinishedEmpty() {
        assertEquals(0, testCollection.getFinished().size());

        testCollection.addToCollection(tbr1);
        testCollection.addToCollection(tbr2);
        assertEquals(0, testCollection.getFinished().size());
    }

    @Test
    void testGetFinishedNonEmpty() {
        testCollection.addToCollection(fin1);
        assertEquals(1, testCollection.getFinished().size());
        assertTrue(testCollection.getFinished().contains(fin1));

        testCollection.addToCollection(tbr1);
        assertEquals(1, testCollection.getFinished().size());
        assertTrue(testCollection.getFinished().contains(fin1));

        testCollection.addToCollection(fin2);
        assertEquals(2, testCollection.getFinished().size());
        assertTrue(testCollection.getFinished().contains(fin1));
        assertTrue(testCollection.getFinished().contains(fin2));

        testCollection.addToCollection(tbr2);
        assertEquals(2, testCollection.getFinished().size());
        assertTrue(testCollection.getFinished().contains(fin1));
        assertTrue(testCollection.getFinished().contains(fin2));
    }

    @Test
    void testGetFinishedUnfinishedFirst() {
        testCollection.addToCollection(tbr1);
        testCollection.addToCollection(tbr1);
        assertEquals(0, testCollection.getFinished().size());
        testCollection.addToCollection(fin2);
        testCollection.addToCollection(fin1);
    }

    @Test
    void testRemoveValidBook() {
        el.clear();
        testCollection.addToCollection(tbr2);
        testCollection.removeBook(tbr2);
        assertEquals(0, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(tbr2));
        List<Event> l = new ArrayList<Event>();

        for (Event next : el) {
            l.add(next);
        }
        assertEquals(3, l.size());
        assertEquals("tbr2 was added to the tracker.", l.get(1).getDescription());
        assertEquals("tbr2 was removed from the tracker.", l.get(2).getDescription());
    }

    @Test
    void testRemoveBooksFromCollection() {
        el.clear();
        testCollection.addToCollection(tbr1);
        testCollection.addToCollection(fin2);
        testCollection.addToCollection(fin1);
        testCollection.addToCollection(tbr2);

        testCollection.removeBook(fin1);
        assertEquals(3, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(fin1));
        testCollection.removeBook(tbr1);
        assertEquals(2, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(tbr1));
        testCollection.removeBook(tbr1);
        assertEquals(2, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(tbr1));
        testCollection.removeBook(tbr2);
        assertEquals(1, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(tbr2));
        testCollection.removeBook(fin2);
        assertEquals(0, testCollection.getCollectionSize());
        assertFalse(testCollection.containsBook(fin2));
        List<Event> l = new ArrayList<Event>();
        for (Event next : el) {
            l.add(next);
        }
        assertEquals(10, l.size());
        assertEquals("fin1 was added to the tracker.", l.get(3).getDescription());
        assertEquals("tbr2 was removed from the tracker.", l.get(8).getDescription());
        assertEquals("tbr1 was removed from the tracker.", l.get(6).getDescription());
    }

    @Test
    void testGetTitlesEmpty() {
        String[] titles = testCollection.getAllTitles();
        assertEquals(0, titles.length);
    }

    // I followed the following tutorial to convert an Array to a List instead
    // for the purposes of checking the contents of getTitles();
    // https://stackabuse.com/java-check-if-array-contains-value-or-element/
    @Test
    void testGetOneTitle() {
        testCollection.addToCollection(tbr1);
        String[] titles = testCollection.getAllTitles();
        assertEquals(1, titles.length);

        ArrayList<String> listTitles = new ArrayList<>(Arrays.asList(titles));
        listTitles.contains("tbr1");
    }

    @Test
    void testGetMultipleTitles() {
        testCollection.addToCollection(tbr2);
        testCollection.addToCollection(fin1);
        testCollection.addToCollection(fin2);
        String[] titles = testCollection.getAllTitles();
        assertEquals(3, titles.length);

        ArrayList<String> listTitles = new ArrayList<>(Arrays.asList(titles));
        assertEquals("tbr2", listTitles.get(0));
        assertEquals("fin1", listTitles.get(1));
        assertEquals("fin2", listTitles.get(2));
    }
}
