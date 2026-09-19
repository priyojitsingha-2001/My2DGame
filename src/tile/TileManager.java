package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Manages background tiles: the tile palette, the world layout loaded from a text map,
// and drawing only the screen-visible slice around the player (camera-follow).
public class TileManager {
    GamePanel gamePanel;
    // Tile palette: index = tile ID used inside the map file (e.g. 0 = grass, see getTileImage).
    Tile[] tiles;
    // World layout: stores tile ID per world cell as [col][row] (x then y).
    int mapTileNum [][];

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tiles = new Tile[10]; // we are going to have 10 types of
        // One entry per world cell (50x50 from GamePanel world settings).
        mapTileNum = new int[gamePanel.maxWorldColumn][gamePanel.maxWorldRow];
        getTileImage(); // load palette first so draw() has images
        loadMap("/maps/world01.txt"); // current level file; swapping this path swaps the level
    }

    // Loads the tile palette images from /res/tiles/.
    // Tile ID table (must match numbers used in /maps/world01.txt):
    // 0 = grass, 1 = wall, 2 = water, 3 = earth, 4 = tree, 5 = sand.
    public void getTileImage(){
//        we are going to load the tile images here
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));

            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

            tiles[4] = new Tile();
            tiles[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));

            tiles[5] = new Tile();
            tiles[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // Reads the text map into mapTileNum[col][row].
    // Map file format: one text line per world row, with space-separated tile IDs per column
    // (e.g. "0 0 1 0 ..." for grass grass wall grass ...). Size must match maxWorldColumn x maxWorldRow.
    public void loadMap(String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            // Outer loop walks rows line-by-line; inner loop fills each column in that row.
            while(col < gamePanel.maxWorldColumn && row < gamePanel.maxWorldRow){

                String line = br.readLine(); // one world row, e.g. 50 numbers for 50 columns

                while(col < gamePanel.maxWorldColumn){
                    String numbers[] = line.split(" "); // split row into its tile ID strings
                    int num = Integer.parseInt(numbers[col]); // tile ID for this column
                    mapTileNum[col][row] = num; // store as [x][y]
                    col ++;
                }

                if(col == gamePanel.maxWorldColumn){
                    col = 0; // start next row at first column
                    row++;
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Draws the world with a camera centered on the player.
    // worldX/worldY = tile's fixed position on the full map (pixels).
    // screenX/screenY = where to draw it on the window: world pos shifted by
    // (player world pos -> player screen center), so the world scrolls around the fixed player.
    public void draw(Graphics2D graphics2D){
        int worldColum  = 0; // current world column (x tile index)
        int worldRow = 0; // current world row (y tile index)

        // Walk every world cell column-by-column, row-by-row.
        while(worldColum < gamePanel.maxWorldColumn && worldRow < gamePanel.maxWorldRow){

            int tileNumber = mapTileNum[worldColum][worldRow]; // tile ID for this cell (see ID table above)

            int worldX = worldColum * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.playerXCoordinate + gamePanel.player.screenXCoordinate;
            int screenY = worldY - gamePanel.player.playerYCoordinate + gamePanel.player.screenYCoordinate;

//            only draw tiles that are within the visible screen area
            // Skip tiles outside the screen-sized window around the player, so off-screen
            // map areas are not drawn. Bounds are player world pos +/- player screen offset.
            if(worldX + gamePanel.tileSize > gamePanel.player.playerXCoordinate - gamePanel.player.screenXCoordinate &&
               worldX - gamePanel.tileSize < gamePanel.player.playerXCoordinate + gamePanel.player.screenXCoordinate &&
               worldY + gamePanel.tileSize > gamePanel.player.playerYCoordinate - gamePanel.player.screenYCoordinate &&
               worldY - gamePanel.tileSize < gamePanel.player.playerYCoordinate + gamePanel.player.screenYCoordinate){

                graphics2D.drawImage(tiles[tileNumber].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            }
//            graphics2D.drawImage(tiles[tileNumber].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            worldColum ++;

            if(worldColum == gamePanel.maxWorldColumn){
                worldColum = 0; // wrap to first column of next row
                worldRow++;
            }
        }
    }
}
