package entities;

import java.awt.Graphics;

import entities.EntityTrafficLight.LightCycle;
import models.SIScaling;

/**
 * The Class EntityRoadReservation is used to add a reservation to a part of the
 * road. Mainly used in crossings at a stop light to prevent cars going the
 * opposite way from crossing a lane when a car is coming.
 */
public class EntityRoadReservation extends Entity implements Collidable {

	private double angle;
	private boolean reserved = false;
	private int waitTimeout = 5;

	private boolean oldReserved = false;

	EntityTrafficLight tLight;
	SIScaling scaling = new SIScaling();

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

	public EntityRoadReservation(double xPosition, double yPosition, int width, int height, double angle) {
		super(xPosition, yPosition, width, height);
		this.angle = angle;
		this.tLight = null;
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
//		(int) getCollisionBounds().getWidth(), (int) getCollisionBounds().getHeight());

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
		if (tLight != null && tLight.getLightCycle().equals(LightCycle.DRIVE)) {
			oldReserved = reserved;
			waitTimeout = scaling.getStepsPerSecond() * 4;

		} else if (waitTimeout > 0) {
			oldReserved = reserved;
			waitTimeout--;

		} else {
			reserved = false;
		}

	}

	/**
	 * @return Returns the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the angle.
	 * @param angle the new angle to be set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * Gets the reservation state.
	 * @return Returns the reservations
	 */
	public boolean getReserved() {
		return oldReserved;
	}

	/**
	 * reserves this roadpart.
	 * @param reserved the new reserved
	 */
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	@Override
	public void collision(Entity other) {

		if (other instanceof EntityCar) {
			setReserved(true);

		}

	}

}
