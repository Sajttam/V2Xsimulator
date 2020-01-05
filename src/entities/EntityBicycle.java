package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;

/**
 * The Class EntityBicycle which controls the behavior of the bicycle entities.
 * 
 * @author Mattias Källström
 * @version 2.0
 */
public class EntityBicycle extends EntityVehicle {

	/**
	 * Instantiates a new entity bicycle.
	 *
	 * @param road the road
	 * @param listener the listener
	 * @param entitytype the entitytype
	 */
	public EntityBicycle(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);
		setVehicleName("Bicycle");
		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(12, 12);
		setCollisionBounds((getCollisionBounds()), -6, -6);
		setVehicleShape(scaling.getPixelsFromMeter(0.5), scaling.getPixelsFromMeter(2));
		super.castPropertyChange(entitytype);

	}
	/*
	 * 
	 * @see entities.Entity#step()
	 */
	@Override
	public void step() {
		super.step();

		setVehicleShape(scaling.getPixelsFromMeter(0.5), scaling.getPixelsFromMeter(2));

	}

	/* 
	 * @see entities.EntityVehicle#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawVehicleShape(g, Color.ORANGE);

	}
	
	/* 
	 * @see entities.Entity#instanceDestroy()
	 */
	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		SharedValues.getInstance().incrementBicycleCounter();
	}
}
