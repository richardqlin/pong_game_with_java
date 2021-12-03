package main;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.nio.Buffer;

import static java.awt.BasicStroke.*;


public class Game extends Canvas implements Runnable{

    private static final long serialVersionUID = 1L;

    public static final int WIDTH =1000;
    public static final int HEIGHT = WIDTH *9/16;

    public boolean running = false;

    private Thread gameThread;

    private Ball ball;
    private Paddle paddle1;
    private Paddle paddle2;

    public Game() {

        canvasSetup();

        init();

        new Window("Simple Pong",this);

        this.addKeyListener(new KeyInput(paddle1, paddle2));
        this.setFocusable(true);
    }


    public static int ensureRange(int val, int min, int max) {

        return Math.min(Math.max(val, min), max);
    }

    private void init() {
        ball = new Ball();

        paddle1 = new Paddle(Color.green, true);
        paddle2 = new Paddle(Color.red, false);
    }

    private void canvasSetup() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
    }

    @Override
    public void run() {
        this.requestFocus();

        long lastTime = System.nanoTime();
        double amountOfTick = 60.0;
        double ns = 1000000000 / amountOfTick;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now -lastTime) /ns;
            lastTime = now;
            while (delta >-1){
                update();
                delta--;
            }
            if (running)
                draw();
            frames ++;

            if(System.currentTimeMillis() - timer >1000){
                timer += 1000;
                System.out.println("FPS: "+frames);
                frames++;
            }
        }
        stop();
    }

    private void draw() {
        // init
        BufferStrategy buffer = this.getBufferStrategy();

        if (buffer == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();

        // draw background

        drawBackground(g);
        
        // ball

        ball.draw(g);

        // draw paddles and score

        paddle1.draw(g);
        paddle2.draw(g);

        g.dispose();
        buffer.show();
    }

    private void drawBackground(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0, new float[]{10},0);
        g2d.setStroke(dashed);
        g2d.drawLine(WIDTH/2,0,WIDTH/2,HEIGHT);
    }


    private void update() {
        // update ball
        ball.update(paddle1, paddle2);

        // update paddles
       paddle1.update(ball);
       paddle2.update(ball);

    }


    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public void stop(){
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static int sign(double d){
        if (d <= 0)
            return -1;
        return 1;

    }

    public static void main (String[] args){
        new Game();
    }
}
