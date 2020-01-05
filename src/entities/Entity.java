package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import controller.Controller;
import view.Animation;

/**
 * Abstract superclass for all Entities. Used by all Entities, and have
 * methods for position, image, animation, direction and collisionbounds. Also
 * extends Observable, which enables all Entities to notify the Controller of
 * changes to be made.
 *
 * @author Mattias Källström
 * @version 2.0
 */

public abstract class Entity implements Comparable<Object> {

	
	private double xPosition;

	private double yPosition;
	private Rectangle collisionBounds;
	private BufferedImage sprite;
	private Animation anim;
	private int collisionBoundsXOffset;
	private int collisionBoundsYOffset;
	private PropertyChangeSupport propertyChangeSupport;
	private Controller controller;
	private int depth = 0;

	/**
	 * Instantiates a new entity.
	 */
	public Entity() {
		setCollisionBounds();
	}

	/**
	 * Instantiates a new entity.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public Entity(int width, int height) {
		setCollisionBounds(width, height);
	}

	/**
	 * Instantiates a new entity.
	 *
	 * @param xPosition the x position
	 * @param yPosition the y position
	 * @param width the width
	 * @param height the height
	 */
	public Entity(double xPosition, double yPosition, int width, int height) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		setCollisionBounds(width, height);
	}

	/**
	 * Creates an instance of an Entity at the given coordinates with the given
	 * image as a sprite.
	 *
	 * @param xPosition the x position
	 * @param yPosition the y position
	 * @param sprite the sprite
	 */
	public Entity(int xPosition, int yPosition, BufferedImage sprite) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.sprite = sprite;
		setCollisionBounds();
	}

	/**
	 * Creates an instance of an Entity at the given coordinates. Has both image and
	 * animation.
	 *
	 * @param xPosition the x position
	 * @param yPosition the y position
	 * @param sprite the sprite
	 * @param anim the anim
	 */
	public Entity(int xPosition, int yPosition, BufferedImage sprite, Animation anim) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.sprite = sprite;
		this.anim = anim;
		setCollisionBounds();
	}
	
	/**
	 * addObserver : Makes i possible to observe changes of objects.
	 *
	 * @param listener the listener
	 */
	public void addObserver(PropertyChangeListener listener) {
		if (propertyChangeSupport == null)
			propertyChangeSupport = new PropertyChangeSupport(this);
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * Draw this instance. Is overriden in the children of this class.
	 *
	 * @param g the graphics
	 */
	public void draw(Graphics g) {

	}
	
	/**
	 * Sets the controller.
	 *
	 * @param controller the new controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * getEntityAtPosition : .
	 *
	 * @param x position, where the
	 * @param y position,
	 * @return returns entity at the coordinates x position and y position
	 */
	public Entity getEntityAtPosition(int x, int y) {
		if (controller == null)
			return null;
		return controller.getEntityAtPosition(x, y);
	}

	/**
	 * Gets the entities inside area.
	 *
	 * @param area the area
	 * @return the entities inside area
	 */
	public List<Entity> getEntitiesInsideArea(Polygon area) {
		if (controller == null)
			return null;
		return controller.getEntitiesInsideArea(area);
	}

	/**
	 * Updates the Entities position, given how much the Entity is supposed to move.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void move(double x, double y) {
		xPosition += x;
		yPosition += y;
		setCollisionBoundsLocation();
	}

	/**
	 * Draws a rectangle that depicts the collisionBounds limits. Used for debugging
	 * purposes to show when objects intersects.
	 *
	 * @param g2d, a Graphics2D object for animation and tileset.
	 */
	public void drawCollisionBounds(Graphics2D g2d) {

		g2d.drawRect((int) getCollisionBounds().getX(), (int) getCollisionBounds().getY(),
				(int) getCollisionBounds().getWidth(), (int) getCollisionBounds().getHeight());

	}

	/**
	 * step is to be overridden with calculations depending on the Entity. Some
	 * Entities may move, some may do nothing and not require a step.
	 */
	public void step() {

	}

	/**
	 * Used to destroy the instance of this object. Notifies observers (Controller)
	 * about this to be handled.
	 */
	public void instanceDestroy() {
		propertyChangeSupport.firePropertyChange("INSTANCE DESTROY", this, null);
	}

	/**
	 * Alerts a propertychange to create this instance
	 *
	 * @param entity the entity to create
	 */
	public void instanceCreate(Entity entity) {
		propertyChangeSupport.firePropertyChange("INSTANCE CREATE", null, entity);
	}



	/**
	 * getXPosition returns the value of xPosition.
	 *
	 * @return value of xPosition.
	 */
	public double getXPosition() {
		return xPosition;
	}

	/**
	 * getYPosition returns the value of yPosition.
	 *
	 * @return value of yPosition.
	 */
	public double getYPosition() {
		return yPosition;
	}

	/**
	 * getWidth returns the width of the collisionbounds typecasted to int.
	 *
	 * @return width of the collisionbounds.
	 */
	public int getWidth() {
		// return (int) getCollisionBounds().getWidth();
		return (int) getCollisionBounds().getWidth() + collisionBoundsXOffset;
	}

	/**
	 * getHeight returns the height of the collisionbounds typecasted to int.
	 *
	 * @return height of the collisionbounds.
	 */
	public int getHeight() {
		// return (int) getCollisionBounds().getHeight();
		return (int) getCollisionBounds().getHeight() + collisionBoundsYOffset;
	}

	/**
	 * setXPosition changes xPosition coordinate of the Entity to x, and sets the
	 * new collisionbounds position accordingly.
	 *
	 * @param x1 the new x position
	 */
	public void setXPosition(double x1) {
		xPosition = x1;
		setCollisionBoundsLocation();
	}

	/**
	 * setYPosition changes yPosition coordinate of the Entity to y, and sets the
	 * new collisionbounds position accordingly.
	 *
	 * @param y1 the new y position
	 */
	public void setYPosition(double y1) {
		yPosition = y1;
		setCollisionBoundsLocation();
	}

	/**
	 * getSprite returns the sprite image.
	 *
	 * @return the sprite image.
	 */
	public BufferedImage getSprite() {
		return sprite;
	}

	/**
	 * setSprite changes the image of the Entity to sprite,.
	 *
	 * @param sprite the new sprite
	 */
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	/**
	 * setCollisionBounds initializes the collisionBounds as a new Rectangle, with
	 * Entitys given position, width and height.
	 *
	 */
	public void setCollisionBounds() {
		collisionBoundsXOffset = 0;
		collisionBoundsYOffset = 0;
		int width = 0;
		int height = 0;
		if (getSprite() != null) {
			width = getSprite().getWidth();
			height = getSprite().getHeight();
		}
		setCollisionBounds(new Rectangle((int) getXPosition(), (int) getYPosition(), width, height), 0, 0);
	}

	/**
	 * Sets the collision bounds.
	 *
	 * @param width the width
	 * @param height the height
	 * @param xOffset the x offset
	 * @param yOffset the y offset
	 */
	public void setCollisionBounds(int width, int height, int xOffset, int yOffset) {
		setCollisionBounds(width, height);
		setCollisionBounds(getCollisionBounds(), xOffset, yOffset);
	}

	/**
	 * Sets the collision bounds.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setCollisionBounds(int width, int height) {
		collisionBoundsXOffset = 0;
		collisionBoundsYOffset = 0;
		setCollisionBounds(new Rectangle((int) getXPosition(), (int) getYPosition(), width, height), 0, 0);
	}

	/**
	 * setCollisionBounds with parameters handling offset and base collisionBounds.
	 *
	 * @param collisionBounds the collision bounds
	 * @param xOffset the x offset
	 * @param yOffset the y offset
	 */
	public void setCollisionBounds(Rectangle collisionBounds, int xOffset, int yOffset) {
		collisionBoundsXOffset = xOffset;
		collisionBoundsYOffset = yOffset;
		setCollisionBoundsLocation();
		this.collisionBounds = collisionBounds;
	}

	/**
	 * setCollisionBoundsLocation sets the location of collisionBounds with the
	 * attributes from the entity provided there exists a collisionBounds for the
	 * Entity.
	 */
	public void setCollisionBoundsLocation() {
		if (collisionBounds != null) {
			collisionBounds.setLocation((int) getXPosition() + collisionBoundsXOffset,
					(int) getYPosition() + collisionBoundsYOffset);
		}
	}

	/**
	 * getCollisionBounds returns the collisionBounds.
	 *
	 * @return collisionBounds.
	 */
	public Rectangle getCollisionBounds() {
		return collisionBounds;
	}

	/**
	 * Gets the angle between two given points.
	 *
	 * @param x1 the x-coordinate of the first point
	 * @param y1 the y-coordinate of the first point
	 * @param x2 the x-coordinate of the second point
	 * @param y2 the y-coordinate of the second point
	 * @return the angle between points
	 */
	public double getAngleBetweenPoints(double x1, double y1, double x2, double y2) {
		double delta_x = x2 - x1;
		double delta_y = y2 - y1;
		double theta_radians = Math.atan2(delta_y, delta_x);
		return theta_radians;
	}

	/**
	 * drawFlippedImage draws an image in reversed, used in animation to get the mirrored
	 * image
	 * Flipped image around the vertical axis.
	 *
	 * @param g2d the g 2 d
	 * @param image the image
	 */
	public void drawFlippedImage(Graphics2D g2d, BufferedImage image) {
		g2d.drawImage(image, (int) (getXPosition() + image.getWidth()), (int) getYPosition(), -image.getWidth(),
				image.getHeight(), null);
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object arg0) {
		if (!(arg0 instanceof Entity))
			return 0;
		Entity other = (Entity) arg0;
		if (getDepth() > other.getDepth())
			return 1;
		else if (getDepth() < other.getDepth())
			return -1;
		else
			return 0;
	}
}
