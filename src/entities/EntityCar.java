package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityCar extends EntityVehicle {

	protected double length = scaling.getPixelsFromMeter(4.83);
	protected double width = scaling.getPixelsFromMeter(1.8);

	public EntityCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);
		setVehicleName("Car");
		setParameters(road, listener, entitytype);

	}

	private synchronized void setParameters(EntityRoad road, PropertyChangeListener listener, String entity) {

		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		setVehicleShape(width, length);
		super.castPropertyChange(entity);

	}

	@Override
	public void step() {
		super.step();

		setVehicleShape(width, length);

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);

		drawVehicleShape(g, Color.green);

	}

	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		if (this instanceof EntitySmartCar)
			SharedValues.getInstance().incrementSmartCarCounter();
		else
			SharedValues.getInstance().incrementCarCounter();

	}
}
