package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;

import controller.StatsController.EventType;
import models.SIScaling;
import models.SharedValues;

public class EntityVehicle extends Entity implements Collidable, EntityMouseListener {

	private EntityRoad road;
	protected double speed = 0;
	private double distX = 0;
	private double distY = 0;
	private double angle = 0;
	private double hSpeed = 0;
	private double vSpeed = 0;
	private PropertyChangeSupport propertyChangeSupportCounter;
	private Polygon visionArea;
	protected SIScaling scaling = new SIScaling();
	private Area vehicleBounds;
	private Shape vehicleShape;

	private boolean rsuStopSignal = false;

	private HashMap<Entity, RelationLog> relationLogs = new HashMap<Entity, RelationLog>();
	private long birthTime = SharedValues.getInstance().getTimeStamp();

	public static final double CROSSING_VELOCITY_MODIFIER = 0.6; // fraction of max velocity when turning

	public EntityVehicle(EntityRoad road, PropertyChangeListener listener) {
		setRoad(road);
		addCounter(listener);
	}

	public Polygon getVisionArea() {
		visionArea = new Polygon();

		int centerX = (int) (getXPosition());
		int centerY = (int) (getYPosition());
		double radius = scaling.getPixelsFromMeter(13);
		double visionAngle = Math.PI / 180.0 * 55;
		double angleLeft = getAngle() - visionAngle;
		double angleRight = getAngle() + visionAngle;
		int points = 5;

		visionArea.addPoint(centerX, centerY);

		for (int i = 0; i <= points; i++) {
			double angleLeftA = angleLeft + (visionAngle / points) * i;
			int pointX = (int) (centerX + radius * Math.cos(angleLeftA));
			int pointY = (int) (centerY + radius * Math.sin(angleLeftA));
			visionArea.addPoint(pointX, pointY);
		}

		for (int i = 1; i <= points; i++) {
			double angleRightA = angleRight - (visionAngle / points) * (points - i);
			int pointX = (int) (centerX + radius * Math.cos(angleRightA));
			int pointY = (int) (centerY + radius * Math.sin(angleRightA));
			visionArea.addPoint(pointX, pointY);
		}

		return visionArea;

	}

	private class RelationLog {
		private double angleToVehicle;
		private double distanceToVehicle;

		public RelationLog(double angleToVehicle, double distanceToVehicle) {

			this.angleToVehicle = angleToVehicle;
			this.distanceToVehicle = distanceToVehicle;

		}

		public double getAngleToVehicle() {
			return angleToVehicle;
		}

		public void setAngleToVehicle(double angleToVehicle) {
			this.angleToVehicle = angleToVehicle;
		}

		public double getDistanceToVehicle() {
			return distanceToVehicle;
		}

		public void setDistanceToVehicle(double distanceToVehicle) {
			this.distanceToVehicle = distanceToVehicle;
		}

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
	 * @return -1 if entity to the left, 0 if entity in center, 1 if entity to the
	 *         right
	 */
	public int entityRelation(Entity e) {
		double angle1 = getAngle();
		double angle2 = getAngleBetweenPoints(getXPosition(), getYPosition(), e.getXPosition(), e.getYPosition());

		double angleDiff = (180 / Math.PI) * 40;

		if (angle1 > angle2 - Math.toRadians(8) && angle1 < angle2 + Math.toRadians(8))
			return 0;
		else if ((angle1 - angleDiff) > angle2 || angle2 < angle1)
			return -1;
		else
			return 1;
	}

	List<Entity> entitiesInSight;

	@Override
	public void step() {
		// Entity inSight =
		// getEntityAtPosition((int)(24*Math.cos(angle)+getXPosition()),
		// (int)(24*Math.sin(angle)+getYPosition()));
		entitiesInSight = getEntitiesInsideArea(visionArea);
		entitiesInSight.remove(this);

		modifySpeed(road.getSpeedLimit());

		// setSpeed(SharedValues.getInstance().getMaxSpeed(this));

		for (Entity otherEntity : entitiesInSight) {

			if (otherEntity instanceof EntityRoadReservation && this instanceof EntityCar) {

				if (Math.toDegrees(Math.abs(((EntityRoadReservation) otherEntity).getAngle() - angle)) > 90) {

					if (((EntityRoadReservation) otherEntity).getReserved() && road.leftCurve) {

						stopping();

					}
				} else {

					// System.out.println("Adds reservation");

					((EntityRoadReservation) otherEntity).setReserved(true);

				}
			} else if (otherEntity instanceof EntityBicycle && this instanceof EntityBicycle) {

				stopping();

			}

			else if (otherEntity instanceof EntityVehicle) {
				int v;

				Area otherBounds = ((EntityVehicle) otherEntity).getVehicleBounds();
				Area vArea = new Area(visionArea);
				otherBounds.intersect(vArea);
				if (!otherBounds.isEmpty()) {
					v = entityRelation(otherEntity);

					double relativeAngle = getAngleBetweenPoints(getXPosition(), getYPosition(),
							otherEntity.getXPosition(), otherEntity.getYPosition());

					// System.out.println(relativeAngle);

					double distance = Point2D.distance(getXPosition(), getYPosition(), otherEntity.getXPosition(),
							otherEntity.getYPosition());
					// System.out.println(distance);
					if (road.straight && v == 0) {

						stopping();
					}

					else if (relationLogs.containsKey(otherEntity)) {

						if (Math.abs(relativeAngle - relationLogs.get(otherEntity).getAngleToVehicle()) < Math
								.toRadians(20)) {

							if (relationLogs.get(otherEntity).getDistanceToVehicle() > distance
									+ scaling.getPixelsFromMeter(0.5)) {

								stopping();

								// System.out.println("Stopping..." + temp++);

							}

						}

					}

					relationLogs.put(otherEntity, new RelationLog(relativeAngle, distance));

//					if ((!road.straight && otherEntity instanceof EntityBicycle) || (!road.straight && v > -1)
//							|| (road.straight && v == 0)) {
//						stopping();
//
//					}
				}

			}

			if (otherEntity instanceof EntityTrafficLight) {
				EntityTrafficLight trafficLight = (EntityTrafficLight) otherEntity;
				Rectangle tLightBounds = trafficLight.getCollisionBounds();
				Rectangle thisBounds = this.getCollisionBounds();

				if (entityRelation(trafficLight) == 0 && road.straight && !(tLightBounds.intersects(thisBounds))) {
					stopping();
				}
			}
		}

		if (Math.abs((int) ((getXPosition() - road.getXPosition()))) > Math.abs((int) distX)
				|| Math.abs((int) (getYPosition() - road.getYPosition())) > Math.abs((int) distY)) {

			EntityRoad nextRoad = road.getNextRoad(Math.random() > 0.5 ? true : false);
			if (nextRoad == null) {
				instanceDestroy();
			} else {

				setRoad(nextRoad);

			}
		}

		if (rsuStopSignal) {
			stopping();
		}

		hSpeed = speed * Math.cos(angle);
		vSpeed = speed * Math.sin(angle);

		move(hSpeed, vSpeed);

	}

	// accelerate up to targetspeed
	private void modifySpeed(double targetVelocity) {
		double acceleration = 0.005;
		double deceleration = 0.1;

		if (this.speed < targetVelocity) {
			setSpeed(this.speed += acceleration);
			if (this.speed > targetVelocity) {
				setSpeed(targetVelocity);
			}
		} else if (this.speed > targetVelocity) {
			setSpeed(this.speed -= deceleration);
			if (this.speed < targetVelocity) {
				setSpeed(targetVelocity);
			}
		}
	}

	// break until complete stop or no obstacle is present
	private void stopping() {
		double deceleration = 0.08;
		if (this.speed > 0) {
			setSpeed(this.speed -= deceleration);
			if (this.speed < 0) {
				setSpeed(0);
			}
		}
	}

	public void castPropertyChange(String eventname) {
		propertyChangeSupportCounter.firePropertyChange(eventname, null, 1);
	}

	@Override
	public void draw(Graphics g) {
		// g.setColor(Color.YELLOW);
		// g.fillOval((int)(24*Math.cos(angle)+getXPosition())-2,
		// (int)(24*Math.sin(angle)+getYPosition())-2, 4, 4);

		int alpha = 60; // 50% transparent
		Color hitBoxYellow = new Color(235, 229, 52, alpha);
		g.setColor(hitBoxYellow);

		g.fillPolygon(visionArea);
	}

	@Override
	public void collision(Entity other) {

		if (other instanceof EntityVehicle) {

			Area otherBounds = ((EntityVehicle) other).getVehicleBounds();

			// Checks if inner, more precise bounds intersect
			otherBounds.intersect(this.getVehicleBounds());

			// Also checks thgat the vehicle haven't just spawned to prevent spawn collision

			if (!otherBounds.isEmpty() && !isNewBorn()) {

				if (this instanceof EntitySmartCar && other instanceof EntityBicycle) {
					castPropertyChange(EventType.SMARTCAR2BICYCLE.getEventType());
				} else if (this instanceof EntitySmartCar && other instanceof EntitySmartCar) {
					castPropertyChange(EventType.SMARTCAR2SMARTCAR.getEventType());
				} else if (this instanceof EntitySmartCar && other instanceof EntityCar) {
					castPropertyChange(EventType.SMARTCAR2CAR.getEventType());
				} else if (this instanceof EntityCar && other instanceof EntityCar) {
					castPropertyChange(EventType.CAR2CAR.getEventType());
				} else if (this instanceof EntityCar && other instanceof EntityBicycle) {
					castPropertyChange(EventType.CAR2BYCYCLE.getEventType());

				}

				instanceDestroy();
			}
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

	public boolean getRSUStopSignal() {
		return rsuStopSignal;
	}

	public void setRSUStopSignal(boolean signal) {
		this.rsuStopSignal = signal;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (getCollisionBounds().contains(e.getX(), e.getY())) {
			// instanceDestroy();
		}
	}

	public Shape getVehicleShape() {
		return vehicleShape;

	}

	protected void setVehicleShape(double width, double length) {

		Rectangle2D rNormal;
		Shape rRotated;

		double x = (int) getXPosition();
		double y = (int) getYPosition();

		int pointX = (int) (x + ((width / 2) * Math.sin(getAngle())) - (length / 2) * Math.cos(getAngle()));
		int pointY = (int) (y - ((width / 2) * Math.cos(getAngle())) - (length / 2) * Math.sin(getAngle()));

		rNormal = new Rectangle2D.Double(pointX, pointY, length, width);
		AffineTransform at = new AffineTransform();

		at.rotate(getAngle(), pointX, pointY);
		rRotated = at.createTransformedShape(rNormal);

		setVehicleBounds(rRotated);
		setCollisionBounds(rRotated.getBounds(), (int) -(rRotated.getBounds().getWidth() / 2),
				(int) -rRotated.getBounds().getHeight() / 2);

		vehicleShape = rRotated;

	}

	protected void drawVehicleShape(Graphics g, Color color) {
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setColor(color);
		g2d.draw(getVehicleShape());
		g2d.fill(getVehicleShape());

	}

	private void setVehicleBounds(Shape bounds) {

		vehicleBounds = new Area(bounds);
	}

	public Area getVehicleBounds() {

		return vehicleBounds;

	}

	// Checks if the entity is newly spawned
	private boolean isNewBorn() {

		return birthTime > SharedValues.getInstance().getTimeStamp() - scaling.getStepsPerSecond() * 2;

	}

}
