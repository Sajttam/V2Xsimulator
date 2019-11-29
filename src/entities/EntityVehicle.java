package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import controller.StatsController.EventType;
import entities.EntityNode.Direction;
import entities.EntityRoad.RoadType;
import models.SharedValues;

public class EntityVehicle extends Entity implements Collidable, EntityMouseListener {

	private EntityRoad road;
	private double speed = 0;
	private double distX = 0;
	private double distY = 0;
	private double angle = 0;
	private double hSpeed = 0;
	private double vSpeed = 0;
	private PropertyChangeSupport propertyChangeSupportCounter;
	private Polygon visionArea;
	
	private boolean turningLeft;
	private boolean turningRight;
	
	public EntityVehicle(EntityRoad road,PropertyChangeListener listener) {
		setRoad(road);
		addCounter(listener);
	}
	
	public Polygon getVisionArea() {
		visionArea = new Polygon();
		
		int centerX = (int)(getXPosition());
		int centerY = (int)(getYPosition());		
		double radius = 42;
		double visionAngle = Math.PI/180.0 * 55;		
		double angleLeft = getAngle() - visionAngle;
		double angleRight = getAngle() + visionAngle;
		int points = 5;
		
		visionArea.addPoint(centerX, centerY);
		
		for (int i = 0; i <= points; i++) {
			double angleLeftA = angleLeft+(visionAngle/points)*i;
			int pointX = (int)(centerX +  radius * Math.cos(angleLeftA));
			int pointY = (int)(centerY +  radius * Math.sin(angleLeftA));			
			visionArea.addPoint(pointX, pointY);
		}
		
		
		for (int i = 1; i <= points; i++) {			
			double angleRightA = angleRight-(visionAngle/points)*(points-i);
			int pointX = (int)(centerX +  radius * Math.cos(angleRightA));
			int pointY = (int)(centerY +  radius * Math.sin(angleRightA));			
			visionArea.addPoint(pointX, pointY);
		}
		
		return visionArea;
	}
	
	@Override
	public void move(double x, double y) {
		super.move(x, y);
		visionArea = getVisionArea();
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
		
		visionArea = getVisionArea();

	}
	
	private void addCounter(PropertyChangeListener listener) {
		if (propertyChangeSupportCounter == null)
			propertyChangeSupportCounter = new PropertyChangeSupport(this);
		propertyChangeSupportCounter.addPropertyChangeListener(listener);
	}
	
	/**
	 * 
	 * @param e
	 * @return -1 if entity to the left, 0 if entity in center, 1 if entity to the right
	 */
	public int entityRelation(Entity e) {
		double angle1 = getAngle();
		double angle2 = getAngleBetweenPoints(getXPosition(), getYPosition(), e.getXPosition(), e.getYPosition());
		double angleDiff = (180/Math.PI) * 40;
		
		if (angle1 > angle2-0.1 && angle1 < angle2+0.1)
			return 0;
		else if ((angle1 - angleDiff) > angle2 || angle2 < angle1)
			return -1;
		else
			return 1;
	}
	
	List<Entity> entitiesInSight;
	@Override
	public void step() {
		
		//Entity inSight = getEntityAtPosition((int)(24*Math.cos(angle)+getXPosition()), (int)(24*Math.sin(angle)+getYPosition())); 
		entitiesInSight = getEntitiesInsideArea(visionArea);
		entitiesInSight.remove(this);
		
		hSpeed = speed * Math.cos(angle);
		vSpeed = speed * Math.sin(angle);
		
		
		for (Entity e : entitiesInSight) {
			if (e instanceof EntityVehicle) {
				int v = entityRelation(e);
				if ((!road.straight && e instanceof EntityBicycle) || (!road.straight && v==1) || (road.straight && v == 0)) {
					hSpeed = 0;
					vSpeed = 0;
				}
			}
			if (e instanceof EntityTrafficLight){
				EntityTrafficLight trafficLight = (EntityTrafficLight) e;
				if (entityRelation(trafficLight) == 0) {
					hSpeed = 0;
					vSpeed = 0;
				}
				else {
					hSpeed = speed * Math.cos(angle);
					vSpeed = speed * Math.sin(angle);
				}

			}
		}


		if (Math.abs((int)((getXPosition() - road.getXPosition()))) > Math.abs((int)distX)
				|| Math.abs((int)(getYPosition() - road.getYPosition())) > Math.abs((int)distY)) {
			
			
			EntityRoad nextRoad = road.getNextRoad(Math.random() > 0.5 ? true : false);
			if (nextRoad == null) {
				instanceDestroy();
			} else {
		

				setRoad(nextRoad);

			}
		}
		
		move(hSpeed, vSpeed);


	}

	public void castPropertyChange(String eventname) {
		propertyChangeSupportCounter.firePropertyChange(eventname, null, 1);
	}

	@Override
	public void draw(Graphics g) {
		//g.setColor(Color.YELLOW);
		//g.fillOval((int)(24*Math.cos(angle)+getXPosition())-2, (int)(24*Math.sin(angle)+getYPosition())-2, 4, 4);		
		
		int alpha = 127; // 50% transparent
		Color hitBoxYellow = new Color(235, 229, 52, alpha);
		g.setColor(hitBoxYellow);
		
		g.fillPolygon(visionArea);
	}


	@Override
	public void collision(Entity other) {
		if (other instanceof EntityVehicle) {			
			if (this instanceof EntitySmartCar && other instanceof EntityBicycle) {
				castPropertyChange(EventType.SMARTCAR2BICYCLE.getEventType());
			}
			else if(this instanceof EntitySmartCar && other instanceof EntityCar) {
				castPropertyChange(EventType.SMARTCAR2CAR.getEventType());
			}
			else if(this instanceof EntitySmartCar && other instanceof EntitySmartCar) {
				castPropertyChange(EventType.SMARTCAR2SMARTCAR.getEventType());
			}
			else if(this instanceof EntityCar && other instanceof EntityCar) {
				castPropertyChange(EventType.CAR2CAR.getEventType());
			}
			else if(this instanceof EntityCar && other instanceof EntityBicycle) {
				castPropertyChange(EventType.CAR2BYCYCLE.getEventType());
			}
			instanceDestroy();
		}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (getCollisionBounds().contains(e.getX(), e.getY())) {
			//instanceDestroy();
			System.out.println(entitiesInSight + ": " + road.straight);
		}
	}

}
