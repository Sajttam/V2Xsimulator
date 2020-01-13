package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class EntityBikeDetector.
 */
public class EntityBikeDetector extends Entity implements Collidable {

	
	public static final Color STOP_RED = new Color(222, 22, 22, 100);
	public static final Color GO_GREEN = new Color(0, 255, 0, 100);
	private double viewingAngle;
	private boolean lightIsRed;
	private Set<EntityBicycle> bicycles;
	private List<Point> pointsChecked; // DEBUG LIST FOR CHECKED POINTS

	/**
	 * Instantiates a new entity bike detector.
	 *
	 * @param xPos the x pos
	 * @param yPos the y pos
	 * @param width the width
	 * @param height the height
	 * @param angle the angle
	 */
	public EntityBikeDetector(double xPos, double yPos, int width, int height, double angle) {
		super(xPos, yPos, width, height);
		bicycles = new TreeSet<EntityBicycle>();
		pointsChecked = new Vector<Point>();
		viewingAngle = angle;
	}

	Set<EntityBicycle> remove = new TreeSet<EntityBicycle>();;
	private int wait = 0;
	private int waitTime = 30;

	/* 
	 *BikeDetector step-function. Clears the list of bikes in it after the given waittime in steps
	 */
	@Override
	public void step() {
		if (wait <= 0) {
			bicycles.clear();
			wait = waitTime;
		} else {
			wait--;
		}
	}

	/**
	 * Gets the bicycles.
	 *
	 * @return the bicycles
	 */
	public Set<EntityBicycle> getBicycles() {
		return bicycles;
	}

	/**
	 * Gets the boolean lightIsRed,
	 *
	 * @return the light is red
	 */
	public boolean getLightIsRed() {
		return lightIsRed;
	}

	/**
	 * Sets the light is red.
	 *
	 * @param lightIsRed the new light is red
	 */
	public void setLightIsRed(boolean lightIsRed) {
		this.lightIsRed = lightIsRed;
	}

	/**
	 * Adds the checked point. Used for debugging.
	 *
	 * @param p the p
	 */
	public void addCheckedPoint(Point p) {
		pointsChecked.add(p);
	}

	/**
	 * Gets the viewing angle of the bikedetector i.e. from which angle is the bikes coming from.
	 *
	 * @return the viewing angle
	 */
	public double getViewingAngle() {
		return viewingAngle;
	}

	/**
	 * Sets the viewing angle of the bikedetector i.e. from which angle is the bikes coming from.
	 *
	 * @param viewingAngle the new viewing angle
	 */
	public void setViewingAngle(double viewingAngle) {
		this.viewingAngle = viewingAngle;
	}

	/* (non-Javadoc)
	 * @see entities.Entity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (bicycles.isEmpty())
			g.setColor(GO_GREEN);
		else
			g.setColor(STOP_RED);
		g.fillRect((int) getXPosition(), (int) getYPosition(), getWidth(), getHeight());
		// DEBUG
		g.setColor(Color.RED);
		/*
		 * for (Point p : pointsChecked) { g.fillRect(p.x-2, p.y-2, 4, 4); }
		 */

	}

	/* 
	 * Adds bicycles to the collection of bicycles and monitors if the light turns red.
	 */
	@Override
	public void collision(Entity other) {
		if (other instanceof EntityBicycle) {

			EntityBicycle bike = (EntityBicycle) other;
			if (bike.getSpeed() > 0 && !lightIsRed)
				bicycles.add((EntityBicycle) other);
		}
		if (other instanceof EntityTrafficLight) {

			lightIsRed = true;

		}
	}
}
