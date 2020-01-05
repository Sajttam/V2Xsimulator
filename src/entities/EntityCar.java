package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;


/**
 * The Class EntityCar which controlls the behavior and visuals of the cars.
 */
public class EntityCar extends EntityVehicle {

	protected double length = scaling.getPixelsFromMeter(4.83);

	protected double width = scaling.getPixelsFromMeter(1.8);

	/**
	 * Instantiates a new entity car.
	 *
	 * @param road the road
	 * @param listener the PropertyChangeListener for the simulation
	 * @param entitytype the entitytype
	 */
	public EntityCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);
		setVehicleName("Car");
		setParameters(road, listener, entitytype);

	}

	/**
	 * Sets the parameters.
	 *
	 * @param road the road
	 * @param listener the PropertyChangeListener for the simulation
	 * @param entity the entity
	 */
	private synchronized void setParameters(EntityRoad road, PropertyChangeListener listener, String entity) {

		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		setVehicleShape(width, length);
		super.castPropertyChange(entity);

	}

	/* 
	 * @see entities.EntityVehicle#step()
	 */
	@Override
	public void step() {
		super.step();

		setVehicleShape(width, length);

	}

	/*
	 * @see entities.EntityVehicle#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);

		drawVehicleShape(g, Color.green);

	}

	/* 
	 * @see entities.Entity#instanceDestroy()
	 */
	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		if (this instanceof EntitySmartCar)
			SharedValues.getInstance().incrementSmartCarCounter();
		else
			SharedValues.getInstance().incrementCarCounter();

	}
}
