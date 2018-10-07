package com.ivantchernev.algorithms;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointSETTest {

    // isEmpty Tests

    @Test
    void isEmptyReturnsTrueWhenSetEmpty() {
        assertTrue(new PointSET().isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenSetContainsItems() {
        PointSET set = new PointSET();

        set.insert(new Point2D(0,0));

        assertFalse(set.isEmpty());
    }

    // size tests

    @Test
    void sizeReturnsZeroForEmptySet() {
        assertEquals(0, new PointSET().size());
    }

    @Test
    void sizeReturnsCorrectNumberWhenSetContainsItems() {
        PointSET set = new PointSET();

        set.insert(new Point2D(0,0));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(1,1));

        assertEquals(3, set.size());
    }

    // insert tests

    @Test
    void insertThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PointSET().insert(null));
    }

    // contains tests

    @Test
    void containsThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PointSET().contains(null));
    }


    @Test
    void containsReturnsFalseOnEmptySet() {
        assertFalse(new PointSET().contains(new Point2D(0,0)));
    }

    @Test
    void containsReturnsFalseWhenItemNotInSet() {
        PointSET set = new PointSET();

        set.insert(new Point2D(0,0));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(1,1));

        assertFalse(set.contains(new Point2D(2,2)));
    }

    @Test
    void containsReturnsTrueWhenItemInSet() {
        PointSET set = new PointSET();
        Point2D p = new Point2D(1, 1);

        set.insert(p);

        assertTrue(set.contains(p));
    }

    @Test
    void containsPerformsAValueTypeComparison() {
        PointSET set = new PointSET();

        set.insert(new Point2D(1,1));

        assertTrue(set.contains(new Point2D(1,1)));
    }

    // range tests

    @Test
    void rangeThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PointSET().range(null));
    }

    @Test
    void rangeContainsAllValuesInRect() {
        PointSET set = new PointSET();

        set.insert(new Point2D(1,1));
        set.insert(new Point2D(2,2));
        set.insert(new Point2D(3,3));
        set.insert(new Point2D(4,4));

        Iterable<Point2D> range = set.range(new RectHV(2.5, 2.5, 3.5, 3.5));

        range.forEach(point2D -> {
            assertEquals(point2D, new Point2D(3,3));
        });
    }

    // nearest tests

    @Test
    void nearestThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PointSET().nearest(null));
    }

    @Test
    void nearestReturnsNullIfSetEmpty() {
        assertNull(new PointSET().nearest(new Point2D(1,1)));
    }

    @Test
    void nearestReturnsNearestElement() {
        PointSET set = new PointSET();

        set.insert(new Point2D(1,1));
        set.insert(new Point2D(2,2));
        set.insert(new Point2D(3,3));
        set.insert(new Point2D(4,4));

        Point2D nearest = set.nearest(new Point2D(3.2,2.8));

        assertEquals(nearest, new Point2D(3,3));
    }
}