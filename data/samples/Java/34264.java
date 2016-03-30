/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Map class keeps track objects on the playing grid, helper methods to make movement decisions, and export/import methods for the editor
 * 
 * @author Ramsey Kant
 */
public class Map {

    private int mapWidth;

    private int mapHeight;

    public final int CELL_SIZE;

    private byte collideMap[][];

    private Item itemMap[][];

    private ArrayList<Actor> actorList;

    private Player player;

    private int dotsRemaining;

    /**
	 * Class constructor, inits a blank map based on a width, height, and cell size
	 * Used in the editor
	 * 
	 * @param w Width of the map
	 * @param h Height of the map
	 * @param cs Size of individual cells in pixels
	 */
    public Map(int w, int h, int cs) {
        mapWidth = w;
        mapHeight = h;
        CELL_SIZE = cs;
        dotsRemaining = 0;
        collideMap = new byte[mapWidth][mapHeight];
        itemMap = new Item[mapWidth][mapHeight];
        actorList = new ArrayList<Actor>();
    }

    /**
	 * Class Constructor that reads the map data from filename
	 * 
	 * @param filename The file name of the map to read contents from
	 * @param cs Size of individual cells in pixels. This is something that should be deteremined by graphics, not the mapfile
	 */
    public Map(String filename, int cs) {
        CELL_SIZE = cs;
        read(filename);
    }

    /**
	 * The width of the map originally set in the constructor
	 *
	 * @return The width of the map
	 */
    public int getWidth() {
        return mapWidth;
    }

    /**
	 * The height of the map originally set in the constructor
	 * 
	 * @return The height of the map
	 */
    public int getHeight() {
        return mapHeight;
    }

    /**
	 * Get the number of actorList on the map (the size of the actorList ArrayList)
	 * 
	 * @return Number of actorList
	 */
    public int getNumActors() {
        return actorList.size();
    }

    /**
	 * Return the collidable map (a 2d array of bytes which correspond to the collidable types defined in GameObject)
	 * 
	 * @return collidable map (collideMap)
	 */
    public byte[][] getCollidableMap() {
        return collideMap;
    }

    /**
	 * Return the item map (a 2D array of Item objects)
	 * 
	 * @return item map (itemMap)
	 */
    public Item[][] getItemMap() {
        return itemMap;
    }

    /**
	 * Return the number of dots remaining on the map. This is tracked by the dotsRemaining local var (not a loop and count in itemMap)
	 * 
	 * @return dots remaining
	 */
    public int getDotsRemaining() {
        return dotsRemaining;
    }

    /**
	 * Check's if a coordinate pair is within valid bounds for this map
	 * 
	 * @param x X-coordinate component to check
	 * @param y Y-coordinate component to check
	 * @return boolean True if the coordinate pair is valid, false if otherwise
	 */
    public boolean inBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) return false;
        return true;
    }

    /**
	 * Add a collidable (by type) to the collideMap
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param t Type of collidable
	 * @return True if successful
	 */
    public boolean addCollidable(int x, int y, byte t) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) return false;
        if (collideMap[x][y] > 0) return false;
        collideMap[x][y] = t;
        return true;
    }

    /**
	 * Put a new item to the item map
	 * 
	 * @param item Item
	 * @return True if successful
	 */
    public boolean addItem(Item item) {
        if (item == null) return false;
        int x = item.getX();
        int y = item.getY();
        if (!inBounds(x, y)) return false;
        if (item.getType() == GameObject.OBJECT_DOT) dotsRemaining++;
        itemMap[x][y] = item;
        return true;
    }

    /**
	 * Put a new actor in the map (actorList ArrayList)
	 * 
	 * @param act Actor
	 * @return True if successful
	 */
    public boolean addActor(Actor act) {
        if (act == null) return false;
        int x = act.getX();
        int y = act.getY();
        if (!inBounds(x, y)) return false;
        actorList.add(act);
        if (act.getType() == GameObject.OBJECT_PLAYER) player = (Player) act;
        return true;
    }

    /**
	 * Return a value at (x,y) in the collision map
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Integer that represents the collision object. -1 if invalid coordinate provided
	 */
    public byte getCollidable(int x, int y) {
        if (!inBounds(x, y)) return -1;
        return collideMap[x][y];
    }

    /**
	 * Return an item at coordinate (x,y) from within the item map (itemMap)
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Item the item that is found at (x,y). 
	 */
    public Item getItem(int x, int y) {
        if (!inBounds(x, y)) return null;
        return itemMap[x][y];
    }

    /**
	 * Return an actor at index in the actorList ArrayList
	 * 
	 * @param idx Index in actorList
	 * @return Actor (null if non-existent)
	 */
    public Actor getActor(int idx) {
        Actor act = null;
        try {
            act = actorList.get(idx);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return act;
    }

    /**
	 * Return the current reference to the player object on the map
	 * 
	 * @return The player object
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * Return an actor at coordinate (x,y)
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param notPlayer If true, ignore a "Player" actor at (x,y)
	 * @return Actor (null if an actor doesn't exist at the position)
	 */
    public Actor getActor(int x, int y, boolean notPlayer) {
        if (!inBounds(x, y)) return null;
        for (Actor g : actorList) {
            if (notPlayer && g.getType() == GameObject.OBJECT_PLAYER) continue;
            if (g.getX() == x && g.getY() == y) {
                return g;
            }
        }
        return null;
    }

    /**
	 * Remove an actor from actorList based on index. Be careful when using this! Just because an actor isn't in the map doesn't mean it's not 'alive'
	 * This is primarily for the editor
	 * 
	 * @param idx Index of the actor
	 */
    public void removeActor(int idx) {
        actorList.remove(idx);
    }

    /**
	 * Remove an item from the item array by coordinate (x, y)
	 * 
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
    public void removeItem(int x, int y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) return;
        if (itemMap[x][y].getType() == GameObject.OBJECT_DOT) dotsRemaining--;
        itemMap[x][y] = null;
    }

    /**
	 * Remove everything at coordiante (x,y)
	 * Used by the editor only
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return boolean True if something was removed, false if otherwise
	 */
    public boolean removeAnyAt(int x, int y) {
        boolean rm = false;
        if (!inBounds(x, y)) return false;
        if (collideMap[x][y] != 0) {
            collideMap[x][y] = 0;
            rm = true;
        }
        if (itemMap[x][y] != null) {
            itemMap[x][y] = null;
            rm = true;
        }
        for (int i = 0; i < actorList.size(); i++) {
            Actor a = actorList.get(i);
            if (a.getX() == x && a.getY() == y) {
                actorList.remove(i);
                a = null;
                i--;
                rm = true;
            }
        }
        return rm;
    }

    /**
	 * Find the distance (Manhattan) between two objects
	 * 
	 * @param start GameObject at the initial position
	 * @param end GameObject at the end position
	 * @return Distance (integer)
	 */
    public int findDistance(GameObject start, GameObject end) {
        return (int) Math.sqrt(Math.pow(Math.abs(start.getX() - end.getX()), 2) + Math.pow(Math.abs(start.getY() - end.getY()), 2));
    }

    /**
	 * Check if a coordinate is completely empty (void of actorList, items, and collissions)
	 * Used by the editor
	 * 
	 * @param x A x coordinate to move to
	 * @param y A y coordinate to move to
	 * @return True if empty. False if otherwise
	 */
    public boolean isEmpty(int x, int y) {
        if (!inBounds(x, y)) return false;
        if (getCollidable(x, y) != 0) return false;
        if (getItem(x, y) != null) return false;
        if (getActor(x, y, false) != null) return false;
        return true;
    }

    /**
	 * Move attempt method. Changes the position the map of the game object if there are no obstructions
	 * 
	 * @param act The actor object trying to move
	 * @param x A x coordinate to move to
	 * @param y A y coordinate to move to
	 * @return True if the move succeeded. False if otherwise
	 */
    public boolean canMove(Actor act, int x, int y) {
        if (act == null) return false;
        if (!inBounds(x, y)) return false;
        if (getCollidable(x, y) != 0) return false;
        return true;
    }

    /**
	 * Get the cost of moving through the given tile. This can be used to 
	 * make certain areas more desirable. A simple and valid implementation
	 * of this method would be to return 1 in all cases.
	 * 
	 * @param mover The mover that is trying to move across the tile
	 * @param sx The x coordinate of the tile we're moving from
	 * @param sy The y coordinate of the tile we're moving from
	 * @param tx The x coordinate of the tile we're moving to
	 * @param ty The y coordinate of the tile we're moving to
	 * @return The relative cost of moving across the given tile
	 */
    public float getCost(Actor mover, int sx, int sy, int tx, int ty) {
        return 1;
    }

    /**
	 * Write the contents of this map to a file in the correct format
	 * 
	 * @param filename File name of the map 
	 */
    public void write(String filename) {
        FileOutputStream fout;
        DataOutputStream data;
        try {
            fout = new FileOutputStream(filename);
            data = new DataOutputStream(fout);
            data.writeUTF("RKPACMAP");
            data.writeInt(mapWidth);
            data.writeInt(mapHeight);
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    data.write(collideMap[x][y]);
                }
            }
            Item item = null;
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    item = itemMap[x][y];
                    if (item == null) {
                        data.writeBoolean(false);
                        continue;
                    }
                    data.writeBoolean(true);
                    data.writeInt(item.getType());
                    data.writeInt(item.getX());
                    data.writeInt(item.getY());
                    data.writeInt(item.getColor().getRGB());
                    if (item.getType() == GameObject.OBJECT_TELEPORT) {
                        data.writeInt(item.getTeleportX());
                        data.writeInt(item.getTeleportY());
                    }
                }
            }
            data.writeInt(actorList.size());
            for (Actor a : actorList) {
                data.writeInt(a.getType());
                data.writeInt(a.getX());
                data.writeInt(a.getY());
                data.writeInt(a.getColor().getRGB());
                if (a.getType() == GameObject.OBJECT_GHOST) {
                    data.writeBoolean(((Ghost) a).isTrapped());
                }
            }
            data.close();
            fout.close();
        } catch (IOException e) {
            System.out.println("Failed to write map file: " + e.getMessage());
        }
    }

    /**
	 * Read a file with map contents and set the properties in this map
	 * Called by the constructor.
	 * 
	 * @param filename File name of the map
	 */
    private void read(String filename) {
        FileInputStream fin;
        DataInputStream data;
        try {
            fin = new FileInputStream(filename);
            data = new DataInputStream(fin);
            if (!data.readUTF().equals("RKPACMAP")) {
                System.out.println("Not a map file!");
                return;
            }
            mapWidth = data.readInt();
            mapHeight = data.readInt();
            dotsRemaining = 0;
            collideMap = new byte[mapWidth][mapHeight];
            itemMap = new Item[mapWidth][mapHeight];
            actorList = new ArrayList<Actor>();
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    addCollidable(x, y, data.readByte());
                }
            }
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    if (!data.readBoolean()) continue;
                    int t = data.readInt();
                    int ix = data.readInt();
                    int iy = data.readInt();
                    Color c = new Color(data.readInt());
                    addItem(new Item(t, c, this, ix, iy));
                    if (t == GameObject.OBJECT_TELEPORT) {
                        int teleX = data.readInt();
                        int teleY = data.readInt();
                        itemMap[ix][iy].setTeleport(teleX, teleY);
                    }
                }
            }
            int nActorsSize = data.readInt();
            for (int i = 0; i < nActorsSize; i++) {
                int t = data.readInt();
                int ix = data.readInt();
                int iy = data.readInt();
                Color c = new Color(data.readInt());
                if (t == GameObject.OBJECT_PLAYER) {
                    addActor(new Player(this, ix, iy));
                } else if (t == GameObject.OBJECT_GHOST) {
                    boolean trap = data.readBoolean();
                    addActor(new Ghost(c, this, ix, iy, trap));
                } else {
                    addActor(new Actor(t, c, this, ix, iy));
                }
            }
            data.close();
            fin.close();
        } catch (IOException e) {
            System.out.println("Failed to read map file: " + e.getMessage());
        }
    }
}
