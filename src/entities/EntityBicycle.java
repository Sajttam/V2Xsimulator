package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EntityBicycle extends EntityVehicle {
	

	
	public EntityBicycle(EntityRoad road, PropertyChangeListener listener) {
		super(road,listener);
		setSpeed(0.75);
		setCollisionBounds(12, 12);
		setCollisionBounds(getCollisionBounds(), -6, -6);
		super.castPropertyChange("BICYCLE CREATED");

	}
	
	
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.ORANGE);
		g.fillOval((int) getXPosition() - 6, (int) getYPosition() - 6, 12, 12);
	}
}
