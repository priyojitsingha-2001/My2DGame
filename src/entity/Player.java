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

    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        setDefaultvalues();
        getPLayerImage();
    }

    public void setDefaultvalues(){
        playerXCoordinate = 100;
        playerYCoordinate = 100;
        speed = 4;
        direction = "down"; // at the start player will be facing to this direction
    }

    public void getPLayerImage(){
        try {
//            loading player images
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
    public void update(){

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

    public void draw(Graphics2D graphics2D){
//        to draw a basic white square
//        graphics2D.setColor(Color.white);
//        graphics2D.fillRect(x,playerYCoordinate,gamePanel.tileSize,gamePanel.tileSize);

//        Now we are drawing an image
        BufferedImage image = null;
        switch (direction){
//            rendering image based on direction the player is moving
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
        graphics2D.drawImage(image, playerXCoordinate, playerYCoordinate, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
