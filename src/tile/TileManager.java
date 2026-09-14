package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    GamePanel gamePanel;
    Tile[] tiles;

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tiles = new Tile[10]; // we are going to have 10 types of
        getTileImage();
    }

    public void getTileImage(){
//        we are going to load the tile images here
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(tiles[0].image, 0,0, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
