package com.example.m07_sensors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;
public class Ball {

    double radius = 50;      // Ball's radius
    double x;                // Ball's center (x,y)
    double y;
    double speedX;           // Ball's speed
    double speedY;
    double speed_resistance = 0.99f; //amount of slow-down
    double acc_resistance = 0.99f; //amount of slow-down
    private RectF bounds;   // Needed for Canvas.drawOval
    private Paint paint;    // The paint style, color used for drawing

    // Add Acceleration
    private double ax, ay, az = 0; // Acceleration from different axis

    public void setAcc(double ax, double ay, double az) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    Random r = new Random();  // seed random number generator

    // Constructor
    public Ball(int color) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // random position and speed
        x = radius + r.nextInt(800);
        y = radius + r.nextInt(800);
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    // Constructor
    public Ball(int color, float x, float y, float speedX, float speedY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // use parameter position and speed
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x = (x + speedX);
        y = (y + speedY);

        // Add acceleration to speed
        speedX = (speedX) * speed_resistance + ax * acc_resistance;
        speedY = (speedY) * speed_resistance + ay * acc_resistance;

        // Detect collision and react
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
        }
    }

    public void draw(Canvas canvas) {
        // convert to float for bounds
        bounds.set((float) (x - radius),
                (float) (y - radius),
                (float) (x + radius),
                (float) (y + radius));
        canvas.drawOval(bounds, paint);
    }

}
