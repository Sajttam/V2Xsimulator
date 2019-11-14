package view;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * The classed is used to load the games sprite sheet in to memory and
 * distribute image tiles of that image to entities. This class uses the
 * Singleton design pattern. The top left tile is at position x=0 and y=0, all
 * increments are by integers of one.
 * 
 * @author Mattias Sikvall Källström
 * @version 2019-03-10
 */
public class TileSet {
	private String fileName = "/graphics/spritesheet.png";
	private BufferedImage BufferedTileSet;
	private int xOffset = 2;
	private int yOffset = 2;
	private int tileSize = 21;

	private static TileSet tileSet;

	/**
	 * Returns the sprite at the given position in the tile set spritesheet.png.
	 * Note that loadTileSet needs to be called prior to using this method.
	 * @param x x position of the tile that is to be loaded
	 * @param y y position of the tile that is to be loaded
	 * @return the sprite at the given position
	 */
	public static BufferedImage getTile(int x, int y) {
		if (tileSet == null)
			return null;
		BufferedImage temp = tileSet.getTileThis(x, y);
		return temp;
	}
	
	/**
	 * Calls on the constructor if a TileSet doesn't exist.
	 * @throws Exception thrown if the spritesheet.png file can't be loaded
	 */
	public static void loadTileSet() throws Exception {
		if (tileSet == null)
			tileSet = new TileSet();
	}
	
	/**
	 * Creates the TileSet from spritesheet.png
	 * @throws Exception thrown if the spritesheet.png file can't be loaded
	 */
	private TileSet() throws Exception {
		BufferedTileSet = getImage(fileName);
	}
	
	/**
	 * Returns a subimage of the BufferedTileset
	 * @param column the column of the image that is to be loaded
	 * @param row the row of the image that is to be loaded
	 * @return
	 */
	public BufferedImage getTileThis(int column, int row) {
		return BufferedTileSet.getSubimage(3 + (tileSize + xOffset) * column, 3 + (tileSize + yOffset) * row, tileSize,
				tileSize);
	}
	
	/**
	 * Loads an image from a filename and returns it as a BufferedImage
	 * @param filename the name of the file that is to be loaded
	 * @return a BufferedImage
	 * @throws Exception thrown if the spritesheet.png file can't be loaded
	 */
	private BufferedImage getImage(String filename) throws Exception {
		// Grab the InputStream for the image.
		InputStream in = getClass().getResourceAsStream(filename);
		if (in == null)
			throw new Exception("spritesheet could not be loaded");
		// Then read it in.
		return ImageIO.read(in);
	}
}
