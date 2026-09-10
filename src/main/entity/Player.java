package main.entity;

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
        x = 100;
        y = 100;
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
        if(keyHandler.upPressed == true){
            direction = "up";
            y -= speed;
        }
        else if(keyHandler.downPressed == true){
            direction = "down";
            y += speed;
        }
        else if (keyHandler.rightPressed == true) {
            direction = "right";
            x += speed;
        }
        else if (keyHandler.leftPressed == true) {
            direction = "left";
            x -= speed;
        }
    }

    public void draw(Graphics2D graphics2D){
//        to draw a basic white square
//        graphics2D.setColor(Color.white);
//        graphics2D.fillRect(x,y,gamePanel.tileSize,gamePanel.tileSize);

//        Now we are drawing an image
        BufferedImage image = null;
        switch (direction){
//            rendering image based on direction the player is moving
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "right":
                image = right1;
                break;
            case "left":
                image = left1;
                break;
        }
        graphics2D.drawImage(image, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
