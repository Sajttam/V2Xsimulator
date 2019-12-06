package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityBicycle extends EntityVehicle {

	public EntityBicycle(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);
		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(12, 12);
		setCollisionBounds(getCollisionBounds(), -6, -6);
		super.castPropertyChange(entitytype);

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.ORANGE);
		g.fillOval((int) getXPosition() - 6, (int) getYPosition() - 6, 12, 12);
	}
	
	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		SharedValues.getInstance().incrementBicycleCounter();
	}
}
