package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
//    Screen settings
    final int originalTileSize = 16; // default size of any npc, mopb or player size [16*16]
    final int scale = 3;

    public final int tileSize = originalTileSize * 3; // 48*48 actual tile size
    final int maxScreenColumn = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenColumn; // 768 pixles
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

//    FPS
    int FPS = 60;
    KeyHandler keyHandler = new KeyHandler();
    Player player = new Player(this,keyHandler);
    TileManager tileManager = new TileManager(this);

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
        double nextDrawTime = System.nanoTime() + drawInterval; // when internal screenTime hits this time next frame will be drawn

        while(gameThread != null){ // gameloop
//            1. UPDATE: update information such as character positions
            update();
//            2. DRAW: draw the screen with updated information
            repaint(); // by this we are basically calling the paintComponent

            try {
                long remainingTime = (long) ((nextDrawTime - System.nanoTime()) / 1_000_000);

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep(remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        tileManager.draw(graphics2D);
        player.draw(graphics2D);
        graphics2D.dispose();
    }
}
