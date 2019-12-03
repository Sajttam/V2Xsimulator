package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityBicycle extends EntityVehicle {

	public EntityBicycle(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);
		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(12, 12);
		setCollisionBounds(((Rectangle) getCollisionBounds()), -6, -6);
		super.castPropertyChange(entitytype);

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawVehicle(Color.ORANGE, g, scaling.getPixelsFromMeter(0.5), scaling.getPixelsFromMeter(2));

	}

	@Override
	public void collision(Entity other) {
		super.collision(other);
		SharedValues.getInstance().incrementBicycleCounter();
	}
}
