package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public int playerXCoordinate, playerYCoordinate;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int SpriteCounter = 0;
    public int SpriteNum = 1;

    public Rectangle solidArea; // players hit box, there area within the player which is going to interact upon collision
    public boolean collisionOn = false; // flag to denote if player got hit or not
}
