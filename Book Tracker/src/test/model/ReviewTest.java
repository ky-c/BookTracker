package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests for Review class.
public class ReviewTest {

    private Review review;

    @BeforeEach
    void runBefore() {
        review = new Review("like", "dislike", 0);
    }

    @Test
    void testConstructor() {
        assertEquals("like", review.getLike());
        assertEquals("dislike", review.getDislike());
        assertEquals(0, review.getRating());
    }
}
