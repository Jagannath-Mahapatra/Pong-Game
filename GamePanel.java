package PongGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{
    static final int PANEL_WIDTH = 1000;
    static final int PANEL_HEIGHT = (int)(PANEL_WIDTH * (0.5555));
    static final Dimension DIMENSION = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread thread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle pad1;
    Paddle pad2;
    Ball ball;
    Score score;

    GamePanel(){
        newPaddles();
        newBall();
        score = new Score(PANEL_WIDTH, PANEL_HEIGHT);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ActListener());
        this.setPreferredSize(DIMENSION);

        thread = new Thread(this);
        thread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((PANEL_WIDTH /2)-(BALL_DIAMETER/2),random.nextInt(PANEL_HEIGHT -BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }
    public void newPaddles() {
        pad1 = new Paddle(0,(PANEL_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        pad2 = new Paddle(PANEL_WIDTH -PADDLE_WIDTH-15,(PANEL_HEIGHT /2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }
    public void paint(Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw(Graphics g) {
        pad1.draw(g);
        pad2.draw(g);
        ball.draw(g);
        score.draw(g);

    }
    public void move() {
        pad1.move();
        pad2.move();
        ball.move();
    }
    public void checkCollision() {
        if(ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= PANEL_HEIGHT - BALL_DIAMETER - 40) {
            ball.setYDirection(-ball.yVelocity);
        }

        if(ball.intersects(pad1)) {
            ball.xVelocity = -ball.xVelocity;
            ball.xVelocity++;

            if(ball.yVelocity > 0)
                ball.yVelocity++;

            else
                ball.yVelocity--;

            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(pad2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;

            if(ball.yVelocity>0)
                ball.yVelocity++;

            else
                ball.yVelocity--;

            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if(pad1.y<=0)
            pad1.y=0;

        if(pad1.y >= (PANEL_HEIGHT - PADDLE_HEIGHT))
            pad1.y = PANEL_HEIGHT - PADDLE_HEIGHT;

        if(pad2.y<=0)
            pad2.y=0;

        if(pad2.y >= (PANEL_HEIGHT - PADDLE_HEIGHT))
            pad2.y = PANEL_HEIGHT - PADDLE_HEIGHT;

        if(ball.x <=0) {
            score.player2Score++;
            newPaddles();
            newBall();
        }

        if(ball.x >= PANEL_WIDTH -BALL_DIAMETER) {
            score.player1Score++;
            newPaddles();
            newBall();
        }
    }

    public void checkWinner(){
        if(score.player1Score > 20){
            int choice = JOptionPane.showConfirmDialog(null, "Player 1 Wins. Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if(choice == 0){
                score.player1Score = 0;
                score.player2Score = 0;
                repaint();
            }
            else{
                System.exit(0);
            }
        }
        else if(score.player2Score > 20){
            int choice = JOptionPane.showConfirmDialog(null, "Player 2 Wins. Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if(choice == 0){
                score.player1Score = 0;
                score.player2Score = 0;
                repaint();
            }
            else{
                System.exit(0);
            }
        }
    }

    public void run() {
        long last = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now - last)/ns;
            last = now;
            if(delta >=1) {
                checkWinner();
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public class ActListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            pad1.keyPressed(e);
            pad2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            pad1.keyReleased(e);
            pad2.keyReleased(e);
        }
    }
}