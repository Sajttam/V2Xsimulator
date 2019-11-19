package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import entities.EntityNode.Direction;
import entities.EntityRoad.RoadType;

public class EntityVehicle extends Entity implements Collidable {
	EntityRoad road;
	double speed = 0;
	double distX = 0;
	double distY = 0;
	double angle = 0;
	double hSpeed = 0;
	double vSpeed = 0;
	private PropertyChangeSupport propertyChangeSupportCounter;
	

	public EntityVehicle(EntityRoad road,PropertyChangeListener listener) {
		setRoad(road);
		addCounter(listener);
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
	
	private void addCounter(PropertyChangeListener listener) {
		if (propertyChangeSupportCounter == null)
			propertyChangeSupportCounter = new PropertyChangeSupport(this);
		propertyChangeSupportCounter.addPropertyChangeListener(listener);
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

	public void castPropertyChange(String eventname) {
		propertyChangeSupportCounter.firePropertyChange(eventname, null, 1);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int)(24*Math.cos(angle)+getXPosition())-2, (int)(24*Math.sin(angle)+getYPosition())-2, 4, 4);

	}


	@Override
	public void collision(Entity other) {
		if (other instanceof EntityVehicle)
			castPropertyChange("COLLISION");
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
