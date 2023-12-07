package com.example.m07_sensors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Rectangle {
    // Variables we need to set up the rectangle
    private float rectX;
    private float rectY;
    private float rectWidth;
    private float rectHeight;
    private Paint paint;
    private float rectDirection = 1; // Value of 1 for moving right, value of -1 for moving left

    // Constructor for the rectangle
    public Rectangle(int color, float rectX, float rectY, float rectWidth, float rectHeight) {
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        paint = new Paint();
        paint.setColor(color);
    }

    // Method to draw the rectangle on the screen
    public void draw(Canvas canvas) {
        RectF rect = new RectF(rectX, rectY, rectX + rectWidth, rectY + rectHeight);
        canvas.drawRect(rect, paint);
    }

    // Getters
    public float getRectX() {
        return rectX;
    }

    public float getRectY() {
        return rectY;
    }

    public float getRectWidth() {
        return rectWidth;
    }

    public float getRectHeight() {
        return rectHeight;
    }

    public float getRectDirection() {
        return rectDirection;
    }

    public void setRectX(float rectX) {
        this.rectX = rectX;
    }

    public void setRectDirection(float direction) {
        rectDirection = direction;
    }
}