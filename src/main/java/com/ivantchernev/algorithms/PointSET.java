package com.ivantchernev.algorithms;

import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET() {
        treeSet = new TreeSet<>(Point2D.X_ORDER);
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        treeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        double minx= Double.MAX_VALUE;
        double miny = Double.MAX_VALUE;
        double maxx = -Double.MAX_VALUE;
        double maxy = -Double.MAX_VALUE;

        for (Point2D p : treeSet) {
            if (p.x() < minx) minx = p.x();
            if (p.y() < miny) miny = p.y();
            if (p.x() > maxx) maxx = p.x();
            if (p.y() > maxy) maxy = p.y();

            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> stack = new Stack<>();
        for (Point2D p: treeSet) {
            if (rect.contains(p)) stack.push(p);
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D nearestPoint = null;
        double closestDistanceSquared = Double.MAX_VALUE;
        for (Point2D storedPoint: treeSet) {
            if (p.distanceSquaredTo(storedPoint) < closestDistanceSquared) {
                nearestPoint = storedPoint;
                closestDistanceSquared = p.distanceSquaredTo(storedPoint);
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();

        set.insert(new Point2D(0.2,0.7));
        set.insert(new Point2D(0.5,0.5));
        set.insert(new Point2D(0.8,0.2));

        set.draw();
    }
}
