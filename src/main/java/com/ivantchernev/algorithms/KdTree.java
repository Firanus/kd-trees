package com.ivantchernev.algorithms;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class KdTree {

    private Node root;

    private enum Color { BLACK, RED, BLUE }

    private static class Node {
        private final Point2D p;      // the point
        private final boolean isXOriented; // is this node x or y oriented?
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, boolean isXOriented) {
            this.p = p;
            this.isXOriented = isXOriented;
        }
    }

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
        if(contains(p)) return;

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
        drawLine(0, 0, 1, 0, Color.BLACK);
        drawLine(0, 0, 0, 1, Color.BLACK);
        drawLine(0, 1, 1, 1, Color.BLACK);
        drawLine(1, 0, 1, 1, Color.BLACK);
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV limitRectangle) {
        if (node == null) return;

        // drawing code
        drawPoint(node.p);

        Color lineColor = node.isXOriented ? Color.RED : Color.BLUE;
        if (node.isXOriented) drawLine(node.p.x(), limitRectangle.ymax(), node.p.x(), limitRectangle.ymin(), lineColor);
        else                  drawLine(limitRectangle.xmax(), node.p.y(), limitRectangle.xmin(), node.p.y(), lineColor);

        // recurse through tree
        draw(node.lb, getLeftBottomLimitRectangle(node, limitRectangle));
        draw(node.rt, getRightTopLimitRectangle(node, limitRectangle));
    }

    private void drawPoint(Point2D p) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        p.draw();
    }

    private void drawLine(double x0, double y0, double x1, double y1, Color color) {
        setPenColor(color);
        StdDraw.setPenRadius(0.001);
        StdDraw.line(x0,y0,x1,y1);
    }

    private void setPenColor(Color color) {
        switch (color) {
            case RED:
                StdDraw.setPenColor(StdDraw.RED);
                break;
            case BLUE:
                StdDraw.setPenColor(StdDraw.BLUE);
                break;
            case BLACK:
                StdDraw.setPenColor(StdDraw.BLACK);
                break;
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> intersectionPoints = new Stack<>();
        return range(root, intersectionPoints, rect);
    }

    private Stack<Point2D> range(Node node, Stack<Point2D> intersectionPoints, RectHV rect) {
        if (node == null) return intersectionPoints;
        if (rect.contains(node.p)) intersectionPoints.push(node.p);

        boolean searchLeftBottom = node.isXOriented ? rect.xmin() < node.p.x() : rect.ymin() < node.p.y();
        boolean searchRightTop = node.isXOriented ? rect.xmax() > node.p.x() : rect.ymax() > node.p.y();

        if (searchLeftBottom) intersectionPoints = range(node.lb, intersectionPoints, rect);
        if (searchRightTop) intersectionPoints = range(node.rt, intersectionPoints, rect);

        return intersectionPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return nearest(root, new RectHV(0, 0, 1, 1), null, p);
    }

    private Point2D nearest(Node node, RectHV nodeRect, Point2D closestPoint, Point2D queryPoint) {
        if (node == null) return closestPoint;

        if   (closestPoint == null) closestPoint = node.p;
        else if (node.p.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint))
            closestPoint = node.p;

        closestPoint = nearestQuerySubtrees(node, nodeRect, closestPoint, queryPoint);

        return closestPoint;
    }

    private Point2D nearestQuerySubtrees(Node node, RectHV nodeRect, Point2D closestPoint, Point2D queryPoint) {
        if (node.isXOriented ? queryPoint.x() < node.p.x() : queryPoint.y() < node.p.y()) {
            closestPoint = nearestOnLeftBottomSubTree(node, nodeRect, closestPoint, queryPoint);
            closestPoint = nearestOnRightTopSubTree(node, nodeRect, closestPoint, queryPoint);
        } else {
            closestPoint = nearestOnRightTopSubTree(node, nodeRect, closestPoint, queryPoint);
            closestPoint = nearestOnLeftBottomSubTree(node, nodeRect, closestPoint, queryPoint);
        }

        return closestPoint;
    }

    private Point2D nearestOnLeftBottomSubTree(Node node, RectHV nodeRect, Point2D closestPoint, Point2D queryPoint) {
        RectHV leftBottomLimitRectangle = getLeftBottomLimitRectangle(node, nodeRect);
        if (leftBottomLimitRectangle.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint)) {
            closestPoint = nearest(node.lb, leftBottomLimitRectangle, closestPoint, queryPoint);
        }
        return closestPoint;
    }

    private Point2D nearestOnRightTopSubTree(Node node, RectHV nodeRect, Point2D closestPoint, Point2D queryPoint) {
        RectHV rightTopLimitRectangle = getRightTopLimitRectangle(node, nodeRect);
        if (rightTopLimitRectangle.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint)) {
            closestPoint = nearest(node.rt, rightTopLimitRectangle, closestPoint, queryPoint);
        }
        return closestPoint;
    }

    private RectHV getLeftBottomLimitRectangle(Node node, RectHV limitRectangle) {
        return node.isXOriented ?
                new RectHV(limitRectangle.xmin(), limitRectangle.ymin(), node.p.x(), limitRectangle.ymax()) :
                new RectHV(limitRectangle.xmin(), limitRectangle.ymin(), limitRectangle.xmax(), node.p.y());
    }

    private RectHV getRightTopLimitRectangle(Node node, RectHV limitRectangle) {
        return node.isXOriented ?
                new RectHV(node.p.x(), limitRectangle.ymin(), limitRectangle.xmax(), limitRectangle.ymax()) :
                new RectHV(limitRectangle.xmin(), node.p.y(), limitRectangle.xmax(), limitRectangle.ymax());
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();

        // create initial tree from file

        In in = new In(args[0]);
        while (in.hasNextLine()) {
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
