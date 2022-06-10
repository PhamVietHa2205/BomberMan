package com.Bomberman.controller;

public class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * root: top left.
     * axis X: top {left->right}
     * axis Y: left {top->bottom}
     * @param x coordinates x.
     * @param y coordinates y.
     * @param width width of rectangle.
     * @param height height of rectangle.
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int overlapArea(Rectangle rec) {
        int left = Math.max(this.x, rec.x);
        int right = Math.min(this.x + this.width, rec.x + rec.width);
        int top = Math.min(this.y + this.height, rec.y + rec.height);
        int bottom = Math.max(this.y, rec.y);

        int wOverlap = right - left;
        int hOverlap = top - bottom;

        if (wOverlap < 0 || hOverlap < 0) return 0;

        return wOverlap * hOverlap;
    }

}
