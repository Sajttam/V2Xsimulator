package entities;

import java.awt.Color;
import java.awt.Graphics;

import entities.EntityNode.Direction;
import entities.EntityRoad.RoadType;

public class EntityVehicle extends Entity implements Collidable {
	private EntityRoad road;
	private double speed = 0.75;
	private double distX = 0;
	private double distY = 0;
	private double angle = 0;
	private double hSpeed = 0;
	private double vSpeed = 0;


	public EntityVehicle(EntityRoad road) {
		setRoad(road);
	}
	

	public void setRoad(EntityRoad road) {
		setXPosition((int) road.getXPosition());
		setYPosition((int) road.getYPosition());

		this.road = road;

		distX = road.x2 - road.getXPosition();
		distY = road.y2 - road.getYPosition();
		
		angle = road.getAngle();

		hSpeed = speed * Math.cos(angle);
		vSpeed = speed * Math.sin(angle);
	}

	@Override
	public void step() {
		
		Entity inSight = getEntityAtPosition((int)(24*Math.cos(angle)+getXPosition()), (int)(24*Math.sin(angle)+getYPosition())); 
		
		if (inSight != null) {
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
		g.setColor(Color.YELLOW);
		g.fillOval((int)(24*Math.cos(angle)+getXPosition())-2, (int)(24*Math.sin(angle)+getYPosition())-2, 4, 4);

	}


	@Override
	public void collision(Entity other) {
		System.out.println("Collision");
		if (other instanceof EntityVehicle)
			instanceDestroy();
	}
	
	public double getSpeed() {
		return speed;
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}


	public double getDistX() {
		return distX;
	}


	public void setDistX(double distX) {
		this.distX = distX;
	}


	public double getDistY() {
		return distY;
	}


	public void setDistY(double distY) {
		this.distY = distY;
	}


	public double getAngle() {
		return angle;
	}


	public void setAngle(double angle) {
		this.angle = angle;
	}


	public double gethSpeed() {
		return hSpeed;
	}


	public void sethSpeed(double hSpeed) {
		this.hSpeed = hSpeed;
	}


	public double getvSpeed() {
		return vSpeed;
	}


	public void setvSpeed(double vSpeed) {
		this.vSpeed = vSpeed;
	}


	public EntityRoad getRoad() {
		return road;
	}

}
