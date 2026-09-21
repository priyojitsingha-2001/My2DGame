package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gamePanel;
    KeyHandler keyHandler;

    // Screen (viewport) position: fixed pixel where the player is drawn on the window.
    // Kept centered so the world scrolls around the player (camera-follow effect).
    // Contrast with playerXCoordinate / playerYCoordinate (inherited from Entity),
    // which is the player's position in the full world map.
    public final int screenXCoordinate;
    public final int screenYCoordinate;

    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        // Center the player on screen. Subtract half a tile so the
        // tile-sized sprite itself is centered, not its top-left corner.
        // These values never change after this (final) - only the world coords move.
        screenXCoordinate = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenYCoordinate = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        // initializing the players collision box values
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultvalues();
        getPLayerImage();
    }

    // Sets the player's spawn state in WORLD coordinates (pixels on the full map,
    // not pixels on the window). TileManager uses these to offset every tile:
    // screenX = worldX - playerXCoordinate + screenXCoordinate.
    public void setDefaultvalues(){
        // Spawn at world tile (col 23, row 21) converted to pixels.
        playerXCoordinate = gamePanel.tileSize * 23;
        playerYCoordinate = gamePanel.tileSize * 21;
        speed = 4; // world pixels moved per update tick
        direction = "down"; // at the start player will be facing to this direction
    }

    // Loads the 8 directional walk sprites (2 frames per direction for animation).
    public void getPLayerImage(){
        try {
//            loading player images from /res/player/
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    // Called once per frame. Moves the player in WORLD space and advances walk animation.
    // Note: screenXCoordinate/screenYCoordinate are untouched here - the player sprite
    // stays centered on screen while playerXCoordinate/playerYCoordinate moves through the world.
    public void update(){

        // Only move/animate when a movement key is held.
        if(keyHandler.upPressed == true || keyHandler.downPressed == true || keyHandler.rightPressed == true || keyHandler.leftPressed == true){
            if(keyHandler.upPressed == true){
                direction = "up";
                playerYCoordinate -= speed;
            }
            else if(keyHandler.downPressed == true){
                direction = "down";
                playerYCoordinate += speed;
            }
            else if (keyHandler.rightPressed == true) {
                direction = "right";
                playerXCoordinate += speed;
            }
            else if (keyHandler.leftPressed == true) {
                direction = "left";
                playerXCoordinate -= speed;
            }

//            Initially we mark player's collision flag as false and then we check collision
            collisionOn = false;
            gamePanel.collisionChecker.checkTile(this);

//        This is the main logic where we are making the sprite animation/ simulating its walk animation

/*        The gave is running at FPS(60).
          We are trying to render one of the two Sprite images alterbatively on every 12 frames.
          Hence we are checking with the help of SpriteCounter(which is basically how many framers are being drawn) to count till 12th frame.
          Once 12 frames are rendered we are changing the SpriteNum 1 if 2 and vice versa. Later we are using this SpriteNum to determine which version of the sprite image to be drawn

          ** we have nested all of this logic inside of a if which checks if either of the key for any of the respective movements are pressed or not, so that our animation/sprite switching happens only when the key press is triggered or player is moving.
 */
            SpriteCounter++;
            if(SpriteCounter > 12){
                if(SpriteNum == 1)
                    SpriteNum = 2;
                else if(SpriteNum == 2)
                    SpriteNum = 1;
                SpriteCounter = 0;
            }
        }
    }

    // Draws the player at its fixed SCREEN position (center of window).
    // The world (tiles) moves behind it based on playerXCoordinate/playerYCoordinate.
    public void draw(Graphics2D graphics2D){
        BufferedImage image = null;
        switch (direction){
//            Pick sprite based on facing direction + current walk frame (SpriteNum).
            case "up":
                // for each sprite we are alternatively rendering two versions just to mimic an animation effect
                if(SpriteNum == 1)
                    image = up1;
                if(SpriteNum == 2)
                    image = up2;
                break;
            case "down":
                if(SpriteNum == 1)
                    image = down1;
                if(SpriteNum == 2)
                    image = down2;
                break;
            case "right":
                if(SpriteNum == 1)
                    image = right1;
                if(SpriteNum == 2)
                    image = right2;
                break;
            case "left":
                if(SpriteNum == 1)
                    image = left1;
                if(SpriteNum == 2)
                    image = left2;
                break;
        }
        // Always drawn at screen center; world position affects tiles, not this call.
        graphics2D.drawImage(image, screenXCoordinate, screenYCoordinate, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
