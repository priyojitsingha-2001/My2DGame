package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
//    Screen settings
    final int originalTileSize = 16; // default size of any npc, mopb or player size [16*16]
    final int scale = 3;

    final int tileSize = originalTileSize * 3; // 48*48 actual tile size
    final int maxScreenColumn = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenColumn; // 768 pixles
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

//    FPS
    int FPS = 60;
    KeyHandler keyHandler = new KeyHandler();

//    set player's default position
    int playerPositionX = 100;
    int playerPositionY = 100;

    int playerSpeed = 4;

    Thread gameThread;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight)); // sets the size of this class (JPanel)
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // with this, this GamePanel can be "focused" to receive key input
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){ // gameloop
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta > 1){
    //            1. UPDATE: update information such as character positions
                update();
    //            2. DRAW: draw the screen with updated information
                repaint(); // by this we are basically calling the paintComponent
                delta --;
            }
        }

    }

    public void update(){
        if(keyHandler.upPressed == true){
            playerPositionY -= playerSpeed;
        }
        else if(keyHandler.downPressed == true){
            playerPositionY += playerSpeed;
        }
        else if (keyHandler.rightPressed == true) {
            playerPositionX += playerSpeed;
        }
        else if (keyHandler.leftPressed == true) {
            playerPositionX -= playerSpeed;
        }
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(playerPositionX,playerPositionY,tileSize,tileSize);
        graphics2D.dispose();
    }
}
