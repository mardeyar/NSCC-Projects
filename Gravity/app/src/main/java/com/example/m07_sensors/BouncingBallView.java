package com.example.m07_sensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Random;

public class BouncingBallView extends View implements SensorEventListener {

    private ArrayList<Ball> balls = new ArrayList<Ball>(); // list of Balls
    private Ball ball_1;  // use this to reference first ball in arraylist
    private Box box;

    // Status message to show Ball's (x,y) position and speed.
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);
    private Paint paint;
    private int string_line = 1;  //
    private int string_x = 10;
    private int string_line_size = 40;  // pixels to move down one line
    private ArrayList<String> debug_dump1 = new ArrayList();
    private String[] debug_dump2 = new String[200];

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    // Rectangle for collision: hit the rectangle get points
    private Rectangle rectangle;
    private int scoreCount = 0; // Initialize scoreCount to 0
    String scoreBox = "Your score: " + scoreCount;
    double ax = 0;   // Store here for logging to screen
    double ay = 0;   //
    double az = 0;   //


    // Constructor
    public BouncingBallView(Context context) {
        super(context);

        // Init the array
        for (int i = 1; i < 200; i++) {
            debug_dump2[i] = "  ";
        }

        // create the box
        box = new Box(Color.rgb(70, 70, 70));  // ARGB

        // Add the rectangle to the screen
        rectangle = new Rectangle(Color.rgb(59, 185, 210), 100, 10, 70, 20);

        balls.add(new Ball(Color.rgb(210, 35, 190)));
        ball_1 = balls.get(0);  // points ball_1 to the first; (zero-ith) element of list
        Log.w("BouncingBallLog", "Just added a bouncing ball");

        balls.add(new Ball(Color.rgb(210, 35, 190)));
        Log.w("BouncingBallLog", "Just added another bouncing ball");

        // Set up status message on paint object
        paint = new Paint();

        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(32);
        paint.setColor(Color.CYAN);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the components
        box.draw(canvas);
        rectangle.draw(canvas);
        canvas.drawText(scoreBox, 10, 2140, paint);

        // This code block is how to get the rectangle moving across the screen
        float currentRectX = rectangle.getRectX();
        float currentRectDirection = rectangle.getRectDirection();
        currentRectX += currentRectDirection * 30; // Sets the speed of rectangle movement

        // Check to see if the rectangle has hit the wall, if so then reverse direction
        if (currentRectX + rectangle.getRectWidth() >= box.xMax || currentRectX <= 0) {
            rectangle.setRectDirection(currentRectDirection * -1);
        }

        // Set the value of currentRectX to the Rectangle object
        rectangle.setRectX(currentRectX);

        // New arraylist to add the balls to be removed, avoiding ConcurrentModificationException
        ArrayList<Ball> remove = new ArrayList<>();

        for (Ball b : balls) {
            if (isCollision(b, rectangle)) {
                handleCollision();
                remove.add(b);
            }
            b.draw(canvas);  //draw each ball in the list
            b.moveWithCollisionDetection(box);  // Update the position of the ball
        }

        // Remove collected balls
        balls.removeAll(remove);

        // inc-rotate string_line
        if (string_line * string_line_size > box.yMax) {
            string_line = 1;  // first line is status
            debug_dump1.clear();
        } else {
            string_line++;
        }

        // inc-rotate string_x
        if (string_x > box.xMax) {
            string_x = 10;  // first line is status
        } else {
            string_x++;
        }

        // Array of String (uses more mem, but changes less)
        debug_dump2[string_line] = "Acc(" + ax + ", " + ay + " ," + az + ")";
        for (int i = 1; i < debug_dump2.length; i++) {
            // un-comment to print debug code on screen
            //canvas.drawText(debug_dump2[i], string_x, i * string_line_size, paint);
        }

        // Force a re-draw
        this.invalidate();
    }

    // Method to check ball collisions with the moving rectangle
    public boolean isCollision(Ball b, Rectangle rectangle) {
        // Calculates the distance between the center of ball and rectangle
        double distanceX = Math.abs(b.x - rectangle.getRectX() - rectangle.getRectWidth() / 2);
        double distanceY = Math.abs(b.y - rectangle.getRectY() - rectangle.getRectHeight() / 2);

        // If the distance along x is greater than sum of half rectWidth and ball radius then there is no collision
        if (distanceX > (rectangle.getRectWidth() / 2 + b.radius)) {
            return false;
        }

        // If the distance along x is greater than sum of half rectHeight and ball radius then there is no collision
        if (distanceY > (rectangle.getRectHeight() / 2 + b.radius)) {
            return false;
        }
        //If x & y are NOT greater than sums, then there has been a collision so return true
        return true;
    }

    // What happens on ball collision with the moving rectangle?
    public void handleCollision() {
        scoreCount++; // Increment the score on each collision
        scoreBox = "Your score: " + scoreCount; // Update value of scoreBox to reflect new score
        Log.w("Collision", "Ball has collided with the moving rectangle");
        Log.w("Collision", "Current score: " + scoreCount);
        invalidate(); // Redraw to update the score on screen
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Limit the amount of balls at any time to 10, instead of clearing when limit is reached
        // simply prevent any more from being added. **DOESN'T WORK HOW I THINK IT SHOULD
        if (balls.size() < 11) {
            float currentX = event.getX();
            float currentY = event.getY();
            float deltaX, deltaY;
            float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
            float slow_down_speed_factor = 10.0f;
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // Modify rotational angles according to movement
                    deltaX = (currentX - previousX) / slow_down_speed_factor;
                    deltaY = (currentY - previousY) / slow_down_speed_factor;
                    ball_1.speedX += deltaX * scalingFactor;
                    ball_1.speedY += deltaY * scalingFactor;
                    Log.w("BouncingBallLog", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);
                    balls.add(new Ball((Color.rgb(210, 35, 190)), previousX, previousY, deltaX, deltaY));  // add ball at every touch event
                }
                // Save current x, y
                previousX = currentX;
                previousY = currentY;
            }
        return true;  // Event handled
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        // Lots of sensor types...get which one, unpack accordingly
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = -event.values[0];  // turns out x is backwards...on my screen?
            ay = event.values[1];   // y component of Accelerometer
            az = event.values[2];   // z component of Accelerometer

            for (Ball b : balls) {
                b.setAcc(ax, ay, az);  //draw each ball in the list
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("onAccuracyChanged", "event=" + sensor.toString());
    }
}
