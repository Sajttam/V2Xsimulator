package entities;

import java.awt.Color;
import java.awt.Graphics;

import entities.EntityNode.Direction;

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
		
		switch (road.getDirection()) {
		case WEST:
			setXPosition(getXPosition()-speed);
			if (getXPosition() == road.x2)
				instanceDestroy();
			break;
		case EAST:
			setXPosition(getXPosition()+speed);
			if (getXPosition() == road.x2)
				instanceDestroy();
			break;
		case NORTH:
			setYPosition(getYPosition()-speed);
			if (getYPosition() == road.y2)
				instanceDestroy();
			break;
		case SOUTH:
			setYPosition(getYPosition()+speed);
			if (getYPosition() == road.y2)
				instanceDestroy();
			break;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(getXPosition()-8, getYPosition()-8, 16, 16);
	}
}
