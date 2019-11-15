package entities;

import java.awt.Color;
import java.awt.Graphics;

import entities.EntityNode.Direction;

public class EntityVehicles extends Entity {
	EntityRoad road;
	int speed = 1;
	
	public EntityVehicles (EntityRoad road) {
		setXPosition((int)road.getXPosition());
		setYPosition((int)road.getYPosition());
		this.road = road;
	}
	
	@Override
	public void step() {
		
		switch (road.getDirection()) {
		case WEST:
			setXPosition((int)getXPosition()-speed);
			if (getXPosition() == road.x2)
				instanceDestroy();
			break;
		case EAST:
			setXPosition((int)getXPosition()+speed);
			if (getXPosition() == road.x2)
				instanceDestroy();
			break;
		case NORTH:
			setYPosition((int)getYPosition()-speed);
			if (getYPosition() == road.y2)
				instanceDestroy();
			break;
		case SOUTH:
			setYPosition((int)getYPosition()+speed);
			if (getYPosition() == road.y2)
				instanceDestroy();
			break;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		switch (road.getRoadType()) {
		case CAR:
			g.setColor(Color.GREEN);
			g.fillOval((int)getXPosition()-8, (int)getYPosition()-8, 16, 16);
			break;
		case BICYCLE:
			g.setColor(Color.ORANGE);
			g.fillOval((int)getXPosition()-6, (int)getYPosition()-6, 12, 12);
			break;
		}
		
	}
}
