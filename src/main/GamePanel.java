package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    // Screen (viewport) settings: what the window shows.
    // The window displays 16x12 tiles at a time; the rest of the world is off-screen.
    final int originalTileSize = 16; // default size of any npc, mob or player sprite [16x16]
    final int scale = 3; // scale factor to make 16px sprites visible on modern displays

    public final int tileSize = originalTileSize * scale; // 48x48 actual tile size on screen
    public final int maxScreenColumn = 16; // visible tiles horizontally
    public final int maxScreenRow = 12; // visible tiles vertically
    public final int screenWidth = tileSize * maxScreenColumn; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall

    // World settings: the full game map, much bigger than the screen.
    // Camera (see TileManager) shows only the screen-sized slice around the player.
    // Contrast with maxScreenColumn/Row above (viewport) vs maxWorldColumn/Row here (full map).
    public final int maxWorldColumn = 50; // total world tiles horizontally
    public final int maxWorldRow = 50; // total world tiles vertically
    public final int worldWidth = tileSize * maxWorldColumn; // full world width in pixels (2400)
    public final int worldHeight = tileSize * maxWorldRow; // full world height in pixels (2400)
//    FPS: target frames per second for the game loop
    int FPS = 60;
    KeyHandler keyHandler = new KeyHandler();
    // Player holds world coords (playerX/Y) + fixed screen anchor (screenX/Y).
    // TileManager uses both to convert world -> screen: screenX = worldX - playerX + screenCenterX.
    public Player player = new Player(this,keyHandler);
    TileManager tileManager = new TileManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    Thread gameThread; // game loop runs on this separate thread

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight)); // sets the size of this class (JPanel)
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // enables off-screen buffer for smoother rendering (no flicker)
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // with this, this GamePanel can be "focused" to receive key input
    }

    // Starts the game loop on a new thread so rendering doesn't block the UI thread setup.
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Fixed-timestep game loop: update + repaint every 1/FPS seconds, then sleep the remainder.
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // nanoseconds per frame (1e9 / 60)
        double nextDrawTime = System.nanoTime() + drawInterval; // when internal screenTime hits this time next frame will be drawn

        while(gameThread != null){ // gameloop
//            1. UPDATE: update information such as character positions
            update();
//            2. DRAW: draw the screen with updated information
            repaint(); // by this we are basically calling the paintComponent

            try {
                // Sleep leftover time in this frame to hold steady FPS; 0 if update+draw overran.
                long remainingTime = (long) ((nextDrawTime - System.nanoTime()) / 1_000_000);

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep(remainingTime);

                nextDrawTime += drawInterval; // schedule next frame
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // Forwards world-state updates (currently just the player; enemies/NPCs would go here).
    public void update(){
        player.update();
    }

    // Render layer. Order matters: tiles first (background), player on top (foreground).
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        tileManager.draw(graphics2D); // draws only the screen-visible world slice (camera in TileManager)
        player.draw(graphics2D); // draws player fixed at screen center
        graphics2D.dispose(); // frees graphics resources
    }
}
