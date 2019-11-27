package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityCar extends EntityVehicle {

	public EntityCar(EntityRoad road, PropertyChangeListener listener,String entitytype) {
		super(road, listener);

		setParameters(road, listener, entitytype);

	}

	private synchronized void setParameters(EntityRoad road, PropertyChangeListener listener, String entity) {

		setSpeed(1.25);
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		super.castPropertyChange(entity);

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.GREEN);

		g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);

	}

	@Override
	public void collision(Entity other) {
		super.collision(other);

		SharedValues.getInstance().incrementCarCounter();
	}

}
