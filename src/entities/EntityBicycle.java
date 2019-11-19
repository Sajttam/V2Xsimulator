package entities;

import java.awt.Color;
import java.awt.Graphics;

public class EntityBicycle extends EntityVehicle {

	
	public EntityBicycle(EntityRoad road) {
		super(road);
		setSpeed(0.75);
		setCollisionBounds(12, 12);
		setCollisionBounds(getCollisionBounds(), -6, -6);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) getXPosition() - 6, (int) getYPosition() - 6, 12, 12);
	}
}
