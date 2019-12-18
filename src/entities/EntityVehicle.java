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
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import models.SIScaling;
import models.SharedValues;
import models.stats.ModelCollision;
import models.stats.StatsEventType;

public class EntityVehicle extends Entity implements Collidable, EntityMouseListener {

	private EntityRoad road;
	protected double speed = 0;
	protected double previousSpeed = 0;
	private double distX = 0;
	private double distY = 0;
	private double angle = 0;
	private double hSpeed = 0;
	private double vSpeed = 0;
	private PropertyChangeSupport propertyChangeSupportCounter;
	private Polygon visionArea;
	protected static SIScaling scaling = new SIScaling();
	private Area vehicleBounds;
	private Shape vehicleShape;
	private String vehicleName = "Vehicle";
	private long birthTime = SharedValues.getInstance().getTimeStamp();

	public static final int DELETION_TIMER_CONSTANT = 32 * scaling.getStepsPerSecond();
	private int deletionTimer = DELETION_TIMER_CONSTANT;

	public static final double CROSSING_VELOCITY_MODIFIER = 0.6; // fraction of max velocity when turning
	public static final double DECELERATION = scaling.accelerationPerStep(2.5); //[pixels/step]
	public static final double ACCELERATION = scaling.accelerationPerStep(2.1); //[pixels/step]

	public EntityVehicle(EntityRoad road, PropertyChangeListener listener) {
		setRoad(road);
		addCounter(listener);
	}

	public Polygon getVisionArea() {
		visionArea = new Polygon();

		int centerX = (int) (getXPosition());
		int centerY = (int) (getYPosition());
		double radius = scaling.getPixelsFromMeter(13);
		double visionAngle = Math.toRadians(100 / 2);
		double angleLeft = 0;
		double angleRight = 0;

		if (road.straight) {
			angleLeft = getAngle() - visionAngle;
			angleRight = getAngle() + visionAngle;
		} else if (road.leftCurve) {
			angleLeft = getAngle() - visionAngle - Math.toRadians(15);
			angleRight = getAngle() + visionAngle - Math.toRadians(15);
		} else if (!road.leftCurve) {
			angleLeft = getAngle() - visionAngle + Math.toRadians(15);
			angleRight = getAngle() + visionAngle + Math.toRadians(15);
		}

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

		if (angle1 > angle2 - Math.toRadians(5) && angle1 < angle2 + Math.toRadians(5))
			return 0;
		else if ((angle1 - angleDiff) > angle2 || angle2 < angle1)
			return -1;
		else
			return 1;
	}

	List<Entity> entitiesInSight;

	@Override
	public void step() {

		previousSpeed = speed;
		entitiesInSight = getEntitiesInsideArea(visionArea);

		entitiesInSight.remove(this);

		modifySpeed(road.getSpeedLimit());

		// Lowers the speed if its close to a intersection
		for (Entity otherEntity : entitiesInSight) {
			if (otherEntity instanceof EntityTrafficLightNode) {
				modifySpeed(scaling.kphToPixelsPerStep(30));

			}
		}

		for (Entity otherEntity : entitiesInSight) {

			int v = entityRelation(otherEntity);

			if (otherEntity instanceof EntityTrafficLight) {
				EntityTrafficLight trafficLight = (EntityTrafficLight) otherEntity;
				Rectangle tLightBounds = trafficLight.getCollisionBounds();
				Rectangle thisBounds = this.getCollisionBounds();

				if (v == 0 && road.straight && !(tLightBounds.intersects(thisBounds))) {
					stopping(DECELERATION);
				}
			} else if (otherEntity instanceof EntityRoadReservation && this instanceof EntityCar) {
				if (Math.toDegrees(Math.abs(((EntityRoadReservation) otherEntity).getAngle() - angle)) > 90) {
					if (((EntityRoadReservation) otherEntity).getReserved() && road.leftCurve) {
						if (!this.getCollisionBounds().intersects(otherEntity.getCollisionBounds())) {

							stopping(DECELERATION);
						}
					}
				} else {

					((EntityRoadReservation) otherEntity).setReserved(true);
				}

			} else if (this instanceof EntityBicycle && otherEntity instanceof EntityVehicle) {

				if (otherEntity instanceof EntityBicycle)
					stopping(DECELERATION);
				else if (v == 0 && Math.toDegrees(getAngle()) % 180 == 0)
					stopping(DECELERATION);

			} else if (otherEntity instanceof EntityBicycle && ((EntityBicycle) otherEntity).getSpeed() > 0) {

				if (!road.straight || road.straight && angleDifference((int) Math.toDegrees(this.getAngle()),
						(int) Math.toDegrees(((EntityBicycle) otherEntity).getAngle())) == 90) {

					stopping(DECELERATION);
				}

			}

			else if (otherEntity instanceof EntityCar) {
				boolean close = distanceToVehicle((EntityVehicle) otherEntity) < scaling.getPixelsFromMeter(1.5);

				boolean inFront = false;

				if (v == 0) {
					inFront = true;
				}

				// Avoiding deadlock with inaccurate collisionboxes
				if (close && inFront || !close) {
					// Checks if the entities are moving in the same general direction +- degrees
					if (angleDifference((int) Math.toDegrees(this.getAngle()),
							(int) Math.toDegrees(((EntityVehicle) otherEntity).getAngle())) < 70) {

						// Checks if this is moving away from the other entity and thusly shouldnt stop
						if (!this.movingAwayFrom((EntityVehicle) otherEntity)) {

							stopping(DECELERATION);
						}
					}
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

		if (getRSUStopSignal()) {
			stopping(DECELERATION);
		}

		hSpeed = speed * Math.cos(angle);
		vSpeed = speed * Math.sin(angle);
		move(hSpeed, vSpeed);

		autoDeletHandeler();
	}

	/**
	 *  modifySpeed: accelerate up to speed set as parameter
	 *  A number higher than speed traveling increases speed in next step
	 *  The opposite if number is lower than speed traveling
	 * @param targetVelocity
	 */
	private void modifySpeed(double targetVelocity) {
		double acceleration = ACCELERATION;
		double deceleration = DECELERATION;

		if (this.previousSpeed < targetVelocity) {
			setSpeed(previousSpeed + acceleration);
			if (this.speed > targetVelocity) {
				setSpeed(targetVelocity);
			}
		} else if (this.previousSpeed > targetVelocity) {
			setSpeed(previousSpeed - deceleration);
			if (this.speed < targetVelocity) {
				setSpeed(targetVelocity);
			}
		}
	}

	private void autoDeletHandeler() {
		if (speed == 0) {
			if (deletionTimer <= 0) {
				instanceDestroy();
			}
			deletionTimer--;
		} else {
			deletionTimer = DELETION_TIMER_CONSTANT;
		}

	}

	/**
	 * stopping: lowers speed/breaks vehicle until complete stop or no obstacle is present
	 * @param deceleration
	 */
	private void stopping(double deceleration) {
		if (this.previousSpeed > 0) {
			setSpeed(previousSpeed - deceleration);
			if (this.speed < 0) {
				setSpeed(0);
			}
		} else if (this.speed != 0) {
			setSpeed(0);
		}
	}

	private boolean movingAwayFrom(EntityVehicle other) {

		// Excepting when this is traveling vertically
		if (Math.round(this.gethSpeed()) == 0 && this.getSpeed() != 0) {
			return false;

		} else if (this.gethSpeed() - other.gethSpeed() < 0 && this.getXPosition() - other.getXPosition() < 0) {
			return true;
		} else if (this.gethSpeed() - other.gethSpeed() > 0 && this.getXPosition() - other.getXPosition() > 0)
			return true;
		else {
			return false;

		}

	}
	/*
	 * setVehicleName: sets name on this vehicle
	 */
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	/**
	 * Cast a property change
	 * @param eventname event to be cast
	 */
	public void castPropertyChange(String eventname) {
		propertyChangeSupportCounter.firePropertyChange(eventname, null, 1);
	}

	/**
	 * Cast a property change with a new CollitionData
	 * @param eventname event to be cast to receiving function
	 * @param vehicle that has collided and should send data
	 */
	public void castPropertyChange(String eventname, EntityVehicle vehicle) {

		ModelCollision mc = new ModelCollision();

		mc.setStatsEventType(eventname);

		mc.setVehicleFirstType(getVehicleName());

		mc.setVehicleFirstSpeed(scaling.pixelsPerStepToKph(getSpeed()));

		mc.setVehicleOtherType(getVehicleName());
		mc.setVehicleOtherSpeed(scaling.pixelsPerStepToKph(vehicle.getSpeed()));

		propertyChangeSupportCounter.firePropertyChange(eventname, null, mc);
	}
	/**
	 * draw: draws a box around vehicle to display hitbox
	 * area that can collide
	 */
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
	/**
	 * collision: Handles collision decision of vehicle
	 * determines if a vehicle has collided
	 */
	@Override
	public void collision(Entity other) {

		if (other instanceof EntityVehicle) {

			Area otherBounds = ((EntityVehicle) other).getVehicleBounds();

			// Checks if inner, more precise bounds intersect
			otherBounds.intersect(this.getVehicleBounds());

			// Also checks that the vehicle haven't just spawned to prevent spawn collision

			if (!otherBounds.isEmpty() && !isNewBorn() && !((EntityVehicle) other).isNewBorn()) {
//
//				if (this instanceof EntityCar && other instanceof EntityCar) {
//					try {
//						wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}

				if (this instanceof EntitySmartCar && other instanceof EntityBicycle) {
					castPropertyChange(StatsEventType.SMARTCAR2BICYCLE.getEventType());
					// cast a property change with collisionData
					castPropertyChange(StatsEventType.COLLISION_DATA.getEventType(), (EntityVehicle) other);
				} else if (this instanceof EntitySmartCar && other instanceof EntitySmartCar) {
					castPropertyChange(StatsEventType.SMARTCAR2SMARTCAR.getEventType());
				} else if (this instanceof EntitySmartCar && other instanceof EntityCar) {
					castPropertyChange(StatsEventType.SMARTCAR2CAR.getEventType());
				} else if (this instanceof EntityCar && other instanceof EntityCar
						&& !(other instanceof EntitySmartCar)) {
					castPropertyChange(StatsEventType.CAR2CAR.getEventType());
				} else if (this instanceof EntityCar && other instanceof EntityBicycle) {
					castPropertyChange(StatsEventType.CAR2BYCYCLE.getEventType());
					// cast a property change with collisionData
					castPropertyChange(StatsEventType.COLLISION_DATA.getEventType(), (EntityVehicle) other);
				}
				instanceDestroy();
			}
		}
	}

	public double getSpeed() {
		return previousSpeed;
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
		if (waitTimer > 0)
			waitTimer--;
		return waitTimer > 0;
	}

	private int waitTimer = 0;

	public void setRSUStopSignal(boolean signal) {
		waitTimer = 120;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (getCollisionBounds().contains(e.getX(), e.getY())) {
			instanceDestroy();
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

	private int angleDifference(int alpha, int beta) {
		int phi = Math.abs(beta - alpha) % 360;
		int distance = phi > 180 ? 360 - phi : phi;
		return distance;
	}

	private double distanceToVehicle(EntityVehicle other) {

		double x1 = other.getXPosition();
		double y1 = other.getYPosition();
		double x2 = this.getXPosition();
		double y2 = this.getYPosition();
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

}
