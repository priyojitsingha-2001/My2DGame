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

    Thread gameThread;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight)); // sets the size of this class (JPanel)
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        while(gameThread != null){ // gameloop
//            1. UPDATE: update information such as character positions
            update();
//            2. DRAW: draw the screen with updated information
            repaint(); // by this we are basically calling the paintComponent
        }

    }

    public void update(){

    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(100,100,tileSize,tileSize);
        graphics2D.dispose();
    }
}
