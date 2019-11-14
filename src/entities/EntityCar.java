package entities;

import java.awt.Color;
import java.awt.Graphics;

public class EntityCar extends Entity {
	EntityRoad road;
	int speed = 1;
	
	public EntityCar (EntityRoad road) {
		setXPosition(road.getXPosition());
		setYPosition(road.getYPosition());
		this.road = road;
	}
	
	@Override
	public void step() {
		if (getXPosition() < road.x2)
			setXPosition(getXPosition()+speed);
		else
			setXPosition(getXPosition()-speed);
		
		if (getXPosition() == road.x2)
			instanceDestroy();
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(getXPosition()-8, getYPosition()-8, 16, 16);
	}
}
