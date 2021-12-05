package main;


import java.awt.*;

public class Ball {

    public static final int SIZE = 25;

    private int x, y;
    private int xVel, yVel; // value either 1 or -1

    private int speed = 4;


    public Ball() {

        reset();

    }

    private void reset() {

        x = Game.WIDTH / 2 - SIZE / 2;
        y = Game.HEIGHT / 2 - SIZE / 2;

        // initial velocities

        xVel = Game.sign(Math.random() * 2.0 - 1);
        yVel = Game.sign(Math.random() * 2.0 - 1);

    }

    public void changeYDir() {
        yVel *= -1;
    }

    public void changeXDir() {
        xVel *= -1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, SIZE, SIZE);

    }

    public void update(Paddle p1, Paddle p2) {
        //update movement
        x += xVel * speed;
        y += yVel * speed;

        // collision
        if (y + SIZE >= Game.HEIGHT || y <= 0)
            changeYDir();

            // with walls
            if (x + SIZE >= Game.WIDTH) {
                p1.addPoint();
                reset();
            }
            if (x <= 0) {
                p2.addPoint();
                reset();
            }
        }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
