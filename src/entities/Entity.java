package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;

import controller.*;
import view.*;

/**
 * Abstract superclass for all Entities. Used by all all Entities, and have
 * methods for position, image, animation, direction and collisionbounds. Also
 * extends Observable, which enables all Entities to notify the Controller of
 * changes to be made.
 *
 * @author Mattias Källström
 * @version 2.0
 */

public abstract class Entity {

	private double xPosition;
	private double yPosition;
	private Rectangle collisionBounds;
	private BufferedImage sprite;
	private Animation anim;
	private int collisionBoundsXOffset;
	private int collisionBoundsYOffset;
	private PropertyChangeSupport propertyChangeSupport;
	private Controller controller;

	public Entity() {
		setCollisionBounds();
	}
	
	public Entity(int width, int height) {
		setCollisionBounds(width, height);
	}

	/**
	 * Creates an instance of an Entity at the given coordinates with the given
	 * image as a sprite.
	 *
	 * @param xPosition, initial x coordinates for the player character.
	 * @param yPosition, initial y coordinates for the player character.
	 * @param sprite, chosen image for the Entity.
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
	 * @param xPosition, initial x coordinates for the player character.
	 * @param yPosition, initial y coordinates for the player character.
	 * @param sprite, chosen image for the Entity.
	 * @param anim, chosen animations for the Entity.
	 */
	public Entity(int xPosition, int yPosition, BufferedImage sprite, Animation anim) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.sprite = sprite;
		this.anim = anim;
		setCollisionBounds();
	}

	public void addObserver(PropertyChangeListener listener) {
		if (propertyChangeSupport == null)
			propertyChangeSupport = new PropertyChangeSupport(this);
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void draw(Graphics g) {
		
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public Entity getEntityAtPosition(int x, int y) {
		if (controller == null) return null;
		return controller.getEntityAtPosition(x, y);
	}
	
	public List<Entity> getEntitiesInsideArea(Polygon area) {
		if (controller == null) return null;
		return controller.getEntitiesInsideArea(area);
	}

	/**
	 * Updates the Entities position, given how much the Entity is supposed to move
	 *
	 * @param x, how many pixels Entity is supposed to be moved in x-Axis.
	 * @param y, how many pixels Entity is supposed to be moved in y-Axis.
	 */
	public void move(double x, double y) {
		xPosition += x;
		yPosition += y;
		setCollisionBoundsLocation();
	}
	
	/**
	 * Draws the image given a Graphics2D object and an image to draw. Image to be
	 * used is gotten from the Entity
	 *
	 * @param g2d, a Graphics2D object for animation and tileset.
	 */

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
	
	public void instanceCreate(Entity entity) {
		propertyChangeSupport.firePropertyChange("INSTANCE CREATE", null, entity);
	}

	/*
	 * public void instanceCreate() { }
	 */

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
	 * new collisionbounds position accordingly
	 *
	 * @param x, the new value for the xPosition.
	 */
	public void setXPosition(double x1) {
		xPosition = x1;
		setCollisionBoundsLocation();
	}

	/**
	 * setYPosition changes yPosition coordinate of the Entity to y, and sets the
	 * new collisionbounds position accordingly
	 *
	 * @param y, the new value for the yPosition.
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
	 * setSprite changes the image of the Entity to sprite,
	 *
	 * @param sprite, the new image for the sprite.
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
		setCollisionBounds(new Rectangle((int)getXPosition(), (int)getYPosition(), width, height), 0, 0);
	}
	
	public void setCollisionBounds(int width, int height, int xOffset, int yOffset) {
		setCollisionBounds(width, height);
		setCollisionBounds(getCollisionBounds(), xOffset, yOffset);
	}
	
	public void setCollisionBounds(int width, int height) {
		collisionBoundsXOffset = 0;
		collisionBoundsYOffset = 0;
		setCollisionBounds(new Rectangle((int)getXPosition(), (int)getYPosition(), width, height), 0, 0);
	}

	/**
	 * setCollisionBounds with parameters for offset and base collisionBounds
	 *
	 * @param collisionBounds, used to set a new collisionBounds using the old
	 * @param xOffset, offset in x-Axis
	 * @param yOffset, offset in y-Axis
	 */
	public void setCollisionBounds(Rectangle collisionBounds, int xOffset, int yOffset) {
		collisionBoundsXOffset = xOffset;
		collisionBoundsYOffset = yOffset;
		setCollisionBoundsLocation();
		this.collisionBounds = collisionBounds;
	}

	/**
	 * setCollisionBoundsLocation sets the location of collisionBounds with the
	 * attributes from the entity, as long as there exists a collisionBounds for the
	 * Entity.
	 */
	public void setCollisionBoundsLocation() {
		if (collisionBounds != null) {
			collisionBounds.setLocation((int)getXPosition() + collisionBoundsXOffset, (int)getYPosition() + collisionBoundsYOffset);
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
	
	public double getAngleBetweenPoints(double x1, double y1, double x2, double y2) {
		double delta_x = x2 - x1;
		double delta_y = y2 - y1;
		double theta_radians = Math.atan2(delta_y, delta_x);
		return theta_radians;
	}

	/**
	 * drawFlippedImage draws an image in reversed, used in animation to get the
	 * image in the other way
	 *
	 * @param g2d, the Graphics2D object used to draw the image.
	 * @param image, the BufferedImage sprite to be drawn.
	 */
	public void drawFlippedImage(Graphics2D g2d, BufferedImage image) {
		g2d.drawImage(image, (int)(getXPosition() + image.getWidth()), (int)getYPosition(), -image.getWidth(), image.getHeight(),
				null);
	}
}
