package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

public class GameField extends JPanel implements ActionListener {
    private final int size = 320;
    private final int dot_size = 16;
    private final int all_dots = 400;
    private Image dot;
    private Image apple;
    private int apple_X;
    private int apple_Y;
    private int[] x = new int[all_dots];
    private int[] y = new int[all_dots];
    private int dots;
    private Timer timer;
    private boolean left;
    private boolean right = true;
    private boolean up;
    private boolean down;
    private boolean inGame = true;



    public GameField(){
        setBackground(Color.white);
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame(){
        dots = 3;
        for(int i = 0; i < dots; i++){
            x[i] = 48 - i * dot_size;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    public void createApple(){
        apple_Y = new Random().nextInt(20) * dot_size;
        apple_X = new Random().nextInt(20) * dot_size;
    }


    public void loadImage(){
        ImageIcon apple_Image = new ImageIcon("first.png");
        apple = apple_Image.getImage();
        ImageIcon dot_image = new ImageIcon("second.png");
        dot = dot_image.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple, apple_X, apple_Y, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }else{
            String end_text = "Game over";
            g.setColor(Color.white);
            g.drawString(end_text, 125, size/2);
        }
    }

    public void move_snake(){
        for(int i = dots; i > 0; i--){
            x[i]= x[i-1];
            y[i] = y[i-1];
        }

        if(left){
            x[0] -= dot_size;
        }
        if(right){
            x[0] += dot_size;
        }
        if(up){
            y[0] -= dot_size;
        }
        if(down){
            y[0] += dot_size;
        }
    }

    public void checkApple(){
        if (x[0] == apple_X && y[0] == apple_Y){
            dots++;
            createApple();
            checkCollisions();
        }
    }

    public void checkCollisions(){
        for(int i = dots; i > 0; i--){
            if(i >4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(x[0] > size) inGame = false;
        if(x[0] < 0) inGame = false;
        if(y[0] > size) inGame = false;
        if(y[0] < 0) inGame = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move_snake();
        }
        repaint();
    }

   class FieldKeyListener extends KeyAdapter{
       @Override
       public void keyPressed(KeyEvent e) {
           super.keyPressed(e);
           int key = e.getKeyCode();
           if(key == KeyEvent.VK_LEFT && !right){
               left = true;
               up = false;
               down = false;
           }else if(key == KeyEvent.VK_RIGHT && !left){
               right = true;
               up = false;
               down = false;
           }else if(key == KeyEvent.VK_DOWN && !up){
               right = false;
               down = true;
               left = false;
           }else if(key == KeyEvent.VK_UP && !down){
               right = false;
               up = true;
               left = false;
           }
       }
   }
}
