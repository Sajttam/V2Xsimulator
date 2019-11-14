package view;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The class Animation keeps track of a list of Images. Every time an image is
 * returned a step counter increases. A period decides for how many steps an
 * image will be returned. When a period ends the next image in the list will be
 * returned. When the animation reaches the end of its cycle it will restart.
 * 
 * @author Mattias Sikvall Källström
 * @version 2019-03-10
 */
public class Animation {
	private List<BufferedImage> imageList;
	private int cycle;
	private int steps;

	/**
	 * Creates a new empty Animation.
	 * 
	 * @param cycle the total amount of steps that has to be performed before the
	 *              animation restarts.
	 * @param steps What step the animation will start on.
	 */
	public Animation(int cycle, int steps) {
		imageList = new ArrayList<BufferedImage>();
		this.steps = steps;
		setCycle(cycle);
	}

	/**
	 * Loads an animation from the class TileSet. An image is to be select from
	 * TileSet and the constructor will then load the next x images to the right of
	 * the given tile.
	 * 
	 * @param imageStartX The column of the first animation image in the tile set.
	 * @param imageStartY The row of the first animation image in the tile set.
	 * @param size        The number of images that are to be loaded.
	 * @param cycle       The total amount of steps that has to be performed before
	 *                    the animation restarts.
	 * @param steps       What step the animation will start on.
	 */
	public Animation(int imageStartX, int imageStartY, int size, int cycle, int steps) {
		imageList = new ArrayList<BufferedImage>(size);
		this.steps = steps;
		setCycle(cycle);

		for (int i = 0; i < size; i++) {
			addImage(TileSet.getTile(imageStartX + i, imageStartY));
		}
	}
	
	/**
	 * Adds an image to the animation
	 * @param img the image to be added.
	 */
	public void addImage(BufferedImage img) {
		imageList.add(img);
	}
	
	/**
	 * Get current image of the animation
	 * @return the current image of the animation
	 */
	public BufferedImage getImage() {
		steps++;
		if (steps >= getCycle())
			steps = 0;
		int temp = steps / (getCycle() / imageList.size());
		return imageList.get(temp);
	}
	
	/**
	 * Sets the cycle length of the animation.
	 * @param cycle the cycle length of the animation
	 */
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	/**
	 * Get the length of the animations cycle.
	 * @return the length of the animations cycle
	 */
	public int getCycle() {
		return cycle;
	}

}
