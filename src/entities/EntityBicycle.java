package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityBicycle extends EntityVehicle {

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
	 * (non-Javadoc)
	 * 
	 * @see entities.Entity#step()
	 */
	@Override
	public void step() {
		super.step();

		setVehicleShape(scaling.getPixelsFromMeter(0.5), scaling.getPixelsFromMeter(2));

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawVehicleShape(g, Color.ORANGE);

	}
	
	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		SharedValues.getInstance().incrementBicycleCounter();
	}
}
