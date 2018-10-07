package com.ivantchernev.algorithms;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KdTreeTest {

    // isEmpty Tests

    @Test
    void isEmptyReturnsTrueWhenSetEmpty() {
        assertTrue(new KdTree().isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenSetContainsItems() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0,0));

        assertFalse(set.isEmpty());
    }

    // size tests

    @Test
    void sizeReturnsZeroForEmptySet() {
        assertEquals(0, new KdTree().size());
    }

    @Test
    void sizeReturnsCorrectNumberWhenSetContainsItems() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0,0));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(1,1));

        assertEquals(3, set.size());
    }

    // insert tests

    @Test
    void insertThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new KdTree().insert(null));
    }

    @Test
    void insertAllowsItemsWithIdenticalXElementsToBeAdded() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0.3,0.9));
        set.insert(new Point2D(0.3,0.1));
        set.insert(new Point2D(0.3,0.2));
        set.insert(new Point2D(0.3,0.8));
        set.insert(new Point2D(0.3,0.4));
        set.insert(new Point2D(0.3,0.7));
        set.insert(new Point2D(0.3,0.5));

        assertEquals(7, set.size());
    }

    @Test
    void insertOnlyAllowsOneCopyOfIdenticalItemsToBeAdded() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0.3,0.9));
        set.insert(new Point2D(0.3,0.9));
        set.insert(new Point2D(0.3,0.9));

        assertEquals(1, set.size());
    }

    // contains tests

    @Test
    void containsThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new KdTree().contains(null));
    }


    @Test
    void containsReturnsFalseOnEmptySet() {
        assertFalse(new KdTree().contains(new Point2D(0,0)));
    }

    @Test
    void containsReturnsFalseWhenItemNotInSet() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0,0));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(1,1));

        assertFalse(set.contains(new Point2D(2,2)));
    }

    @Test
    void containsReturnsTrueWhenItemInSet() {
        KdTree set = new KdTree();
        Point2D p = new Point2D(1, 1);

        set.insert(p);

        assertTrue(set.contains(p));
    }

    @Test
    void containsPerformsAValueTypeComparison() {
        KdTree set = new KdTree();

        set.insert(new Point2D(1,1));

        assertTrue(set.contains(new Point2D(1,1)));
    }

    // range tests

    @Test
    void rangeThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new KdTree().range(null));
    }

    @Test
    void rangeContainsAllValuesInRect() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0,0));
        set.insert(new Point2D(0.25,0.25));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(0.75,0.75));
        set.insert(new Point2D(1,1));

        Iterable<Point2D> range = set.range(new RectHV(0.4, 0.4, 0.6, 0.6));

        range.forEach(point2D -> {
            assertEquals(point2D, new Point2D(0.5,0.5));
        });
    }

    // nearest tests

    @Test
    void nearestThrowsIllegalArgumentExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new KdTree().nearest(null));
    }

    @Test
    void nearestReturnsNullIfSetEmpty() {
        assertNull(new KdTree().nearest(new Point2D(1,1)));
    }

    @Test
    void nearestReturnsNearestElement() {
        KdTree set = new KdTree();

        set.insert(new Point2D(0.25,0.25));
        set.insert(new Point2D(0,0));
        set.insert(new Point2D(1,1));
        set.insert(new Point2D(0.75,0.75));
        set.insert(new Point2D(0.5,0.5));

        Point2D nearest = set.nearest(new Point2D(0.6,0.6));

        assertEquals(nearest, new Point2D(0.5,0.5));
    }
    
}