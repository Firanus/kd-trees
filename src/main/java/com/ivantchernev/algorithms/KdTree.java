package com.ivantchernev.algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static class Node {
        private static final boolean X_ORIENTED = true;
        private static final boolean Y_ORIENTED = false;

        private final Point2D p;      // the point
        private final boolean isXOriented; // is this node x or y oriented?
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, boolean isXOriented) {
            this.p = p;
            this.isXOriented = isXOriented;
        }
    }

    Node root;

    // construct an empty set of points
    public KdTree() { }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : 1 + size(node.lb) + size(node.rt);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        root = insert(root, p, false);
    }

    private Node insert(Node node, Point2D p, boolean parentIsXOriented) {
        if(node == null) return new Node(p, !parentIsXOriented);

        boolean insertIntoLeftBottom = node.isXOriented ? p.x() < node.p.x() : p.y() < node.p.y();
        if (insertIntoLeftBottom) node.lb = insert(node.lb, p, node.isXOriented);
        else                      node.rt = insert(node.rt, p, node.isXOriented);

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (node.p.equals(p)) return true;

        boolean searchLeftBottom = node.isXOriented ? p.x() < node.p.x() : p.y() < node.p.y();
        if (searchLeftBottom) return contains(node.lb, p);
        else                  return contains(node.rt, p);
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        return null;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();

        // create initial tree from file

        In in = new In(args[0]);
        while(in.hasNextLine()) {
            double x = in.readDouble();
            double y = in.readDouble();
            tree.insert(new Point2D(x, y));
        }

        // Drawing

        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);

        tree.draw();
    }
}
