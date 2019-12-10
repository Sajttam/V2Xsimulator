package entities;

import java.awt.Graphics;

import entities.EntityTrafficLight.LightCycle;

/**
 * The Class EntityRoadReservation is used to add a reservation to a part of the
 * road. Mainly used in crossings at a stop light to prevent cars going the
 * opposite way from crossing a lane when a car is coming.
 */
public class EntityRoadReservation extends Entity {

	private double angle;
	private boolean reserved = false;

	private boolean oldReserved = false;

	EntityTrafficLight tLight;

	/**
	 * Instantiates a new entity road reservation.
	 *
	 * @param xPosition the x position
	 * @param yPosition the y position
	 * @param width     the width
	 * @param height    the height
	 * @param angle     the angle
	 * @param tLight    the t light
	 */
	public EntityRoadReservation(double xPosition, double yPosition, int width, int height, double angle,
			EntityTrafficLight tLight) {
		super(xPosition, yPosition, width, height);
		this.angle = angle;
		this.tLight = tLight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see entities.Entity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
//
//		g.setColor(Color.blue);
//		g.drawRect((int) getCollisionBounds().getX(), (int) getCollisionBounds().getY(),
//				(int) getCollisionBounds().getWidth(), (int) getCollisionBounds().getHeight());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see entities.Entity#step()
	 */
	@Override
	public void step() {

		oldReserved = false;
		// Sets the reservation to last step rounds reservations if Light is not red
		if (!tLight.getLightCycle().equals(LightCycle.STOP)) {
			oldReserved = reserved;
		}
		reserved = false;

	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * Gets the reservation state.
	 *
	 * @return the reservations
	 */
	public boolean getReserved() {
		return oldReserved;
	}

	/**
	 * reserves this roadpart.
	 *
	 * @param reserved the new reserved
	 */
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

}
