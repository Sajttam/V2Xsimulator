package entities;

import java.awt.Color;
import java.awt.Graphics;

import entities.EntityNode.Direction;
import entities.EntityRoad.RoadType;

public class EntityVehicle extends Entity implements Collidable {
	EntityRoad road;
	double speed = 0.75;
	double distX = 0;
	double distY = 0;
	double angle = 0;
	double hSpeed = 0;
	double vSpeed = 0;

	boolean smartVehicle = false;

	public EntityVehicle(EntityRoad road) {
		if (road.getRoadType() == RoadType.CAR) {
			setCollisionBounds(16, 16);
			setCollisionBounds(getCollisionBounds(), -8, -8);
		}
		else {
			setCollisionBounds(12, 12);
			setCollisionBounds(getCollisionBounds(), -6, -6);
		}
		setRoad(road);
		setSpeed();
		smartVehicle = Math.random() > 0.8 ? true : false;		
	}
	
	public void setSpeed() {
		if (road.getRoadType() == RoadType.CAR) speed = 1.25;
		else speed = 0.75;
	}

	public void setRoad(EntityRoad road) {
		setXPosition((int) road.getXPosition());
		setYPosition((int) road.getYPosition());

		this.road = road;

		distX = road.x2 - road.getXPosition();
		distY = road.y2 - road.getYPosition();

		angle = Math.PI * 1 / 2;
		if (distX != 0) {
			angle = Math.atan(distY / distX);
		}
		if (distX < 0 || (distY < 0 && road.getDirection() == Direction.NORTH))
			angle += Math.PI;

		hSpeed = speed * Math.cos(angle);
		vSpeed = speed * Math.sin(angle);
	}

	@Override
	public void step() {

		if (getEntityAtPosition((int)(24*Math.cos(angle)+getXPosition()), (int)(24*Math.sin(angle)+getYPosition())) != null) {
			hSpeed = 0;
			vSpeed = 0;
		}
		else {
			hSpeed = speed * Math.cos(angle);
			vSpeed = speed * Math.sin(angle);
		}

		move(hSpeed, vSpeed);

		if (Math.abs((getXPosition() - road.getXPosition())) >= Math.abs(distX)
				&& Math.abs(getYPosition() - road.getYPosition()) >= Math.abs(distY)) {
			EntityRoad nextRoad = road.getNextRoad(Math.random() > 0.5 ? true : false);
			if (nextRoad == null) {
				instanceDestroy();
			} else {
				setRoad(nextRoad);
			}
		}

	}

	@Override
	public void draw(Graphics g) {
		switch (road.getRoadType()) {
		case CAR:
			g.setColor(Color.GREEN);
			if (smartVehicle)
				g.setColor(Color.BLUE);
			g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);
			break;
		case BICYCLE:
			g.setColor(Color.ORANGE);
			g.fillOval((int) getXPosition() - 6, (int) getYPosition() - 6, 12, 12);
			break;
		}

	}

	@Override
	public void collision(Entity other) {
		System.out.println("Collision");
		if (other instanceof EntityVehicle)
			instanceDestroy();
	}
}
