package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import models.SharedValues;

public class EntityCar extends EntityVehicle{

	private boolean smartVehicle = false;
	
	
	
	public EntityCar(EntityRoad road,PropertyChangeListener listener) {
		super(road,listener);
		
		setParameters(road, listener);
	
	}
	private synchronized void setParameters(EntityRoad road,PropertyChangeListener listener) {
		
		setSpeed(1.25);
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		smartVehicle = Math.random() > 0.8 ? true : false;	
		super.castPropertyChange("CAR CREATED");
		
		
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
			g.setColor(Color.GREEN);
			if (smartVehicle) {
				g.setColor(Color.BLUE);
			}
			g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);
			
			
	}
	
	@Override
	public void collision(Entity other) {
		super.collision(other);

		SharedValues.getInstance().incrementCarCounter();
	}
	
	
}
