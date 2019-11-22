package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import entities.EntityNode.Direction;
import entities.EntityRoad.RoadType;

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
		
		visionArea.addPoint(centerX, centerY);
		
		visionArea.addPoint((int)(centerX +  radius * Math.cos(angleLeft)), (int)(centerY +  radius * Math.sin(angleLeft)));
		visionArea.addPoint((int)(centerX +  radius * Math.cos(getAngle())), (int)(centerY +  radius * Math.sin(getAngle())));
		visionArea.addPoint((int)(centerX +  radius * Math.cos(angleRight)), (int)(centerY +  radius * Math.sin(angleRight)));
		
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
	
	public double getAngleBetweenPoints(double x1, double y1, double x2, double y2) {
		double delta_x = x2 - x1;
		double delta_y = y2 - y1;
		double theta_radians = Math.atan2(delta_y, delta_x);
		return theta_radians;
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
		
		if (angle1 > angle2-0.05 && angle1 < angle2+0.05)
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
				if (v != -1 && !road.straight || road.straight && v == 0) {
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
			castPropertyChange("COLLISION");
			
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
			instanceDestroy();
			System.out.println(entitiesInSight);
		}
	}

}
