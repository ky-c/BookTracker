package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Book class.
class BookTest {

    private Book tbr1;
    private Book fin1;
    private Review rev;

    @BeforeEach
    void runBefore() {
        tbr1 = new Book("tbr1", "mc", "comic", 1209, false);
        fin1 = new Book("fin1", "lh", "manga", 310, true);
        rev = new Review("i like this", "i hate this", 4);
    }

    @Test
    void testConstructor() {
        assertEquals("tbr1", tbr1.getTitle());
        assertEquals("mc", tbr1.getAuthor());
        assertEquals("comic", tbr1.getGenre());
        assertEquals(1209, tbr1.getPages());
        assertFalse(tbr1.isFinished());
        assertFalse(tbr1.hasReview());

        assertEquals("fin1", fin1.getTitle());
        assertEquals("lh", fin1.getAuthor());
        assertEquals("manga", fin1.getGenre());
        assertEquals(310, fin1.getPages());
        assertTrue(fin1.isFinished());
        assertFalse(fin1.hasReview());
    }

    @Test
    void testAddReviewFailThenPass() {
        assertFalse(tbr1.addReview(rev));
        assertFalse(tbr1.hasReview());
        assertNull(tbr1.getReview());

        tbr1.setFinished();
        assertTrue(tbr1.addReview(rev));
        assertTrue(tbr1.hasReview());
        assertEquals(rev, tbr1.getReview());

        assertFalse(tbr1.addReview(rev));
        assertTrue(tbr1.hasReview());
        assertEquals(rev, tbr1.getReview());
    }

    @Test
    void testAddReviewSuccess() {
        assertTrue(fin1.addReview(rev));
        assertTrue(fin1.hasReview());
        assertEquals(rev, fin1.getReview());
    }

    @Test
    void testAddAnotherReview() {
        Review rev2 = new Review("love it", "couldn't put down", 5);
        fin1.addReview(rev);
        assertFalse(fin1.addReview(rev2));
        assertTrue(fin1.hasReview());
        assertEquals(rev, fin1.getReview());
    }

    @Test
    void testSetFinished() {
        assertTrue(fin1.isFinished());
        fin1.setFinished();
        assertTrue(fin1.isFinished());
        tbr1.setFinished();
        assertTrue(tbr1.isFinished());
    }

}