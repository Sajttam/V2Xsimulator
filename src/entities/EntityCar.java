package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;

import models.SharedValues;

public class EntityCar extends EntityVehicle {

	public EntityCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener);

		setParameters(road, listener, entitytype);

	}

	private synchronized void setParameters(EntityRoad road, PropertyChangeListener listener, String entity) {

		setSpeed(SharedValues.getInstance().getMaxSpeed(this));
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		super.castPropertyChange(entity);

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawCar(Color.green, g);

	}
	
	@Override
	public void instanceDestroy() {
		super.instanceDestroy();
		SharedValues.getInstance().incrementCarCounter();
	}

	protected void drawCar(Color color, Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();

		Rectangle2D rNormal;
		Shape rRotated;

		int width = 20;
		int length = 10;

		double x = (int) getXPosition();
		double y = (int) getYPosition();

		int pointX = (int) (x + ((length / 2) * Math.sin(getAngle())) - (width / 2) * Math.cos(getAngle()));
		int pointY = (int) (y - ((length / 2) * Math.cos(getAngle())) - (width / 2) * Math.sin(getAngle()));

		rNormal = new Rectangle2D.Double(pointX, pointY, width, length);
		AffineTransform at = new AffineTransform();

		at.rotate(getAngle(), pointX, pointY);
		rRotated = at.createTransformedShape(rNormal);
		g2d.setColor(color);
		g2d.draw(rRotated);
		g2d.fill(rRotated);
	}
}
