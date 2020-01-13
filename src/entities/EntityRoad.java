package entities;

import java.awt.Color;
import java.awt.Graphics;

import controller.StatsController;
import models.SIScaling;
import models.SharedValues;

public class EntityRoad extends Entity {
	
	
	public enum RoadType {
		
		CAR, BICYCLE;
	}

	protected EntityNode enterNode;
	protected EntityNode exitNode;
	double x2;
	double y2;
	private RoadType roadType;
	protected double angle = 0;
	private double distX = 0;
	private double distY = 0;
	private Boolean spawning;
	private double speedLimit = 2;
	private SIScaling scaler = new SIScaling();
	boolean drawn;
	public boolean straight = true;
	public boolean leftCurve = false;

	/**
	 * Instantiates a new entity road.
	 *
	 * @param enterNode the enter node
	 * @param exitNode the exit node
	 * @param roadType the road type
	 * @param spawning the spawning
	 */
	public EntityRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType, Boolean spawning) {
		this.enterNode = enterNode;
		this.exitNode = exitNode;
		this.roadType = roadType;
		this.spawning = spawning;

		switch (roadType) {
		case BICYCLE:
			setSpeedLimit(scaler.kphToPixelsPerStep(20));
			break;
		case CAR:
			setSpeedLimit(scaler.kphToPixelsPerStep(60));
			break;
		default:
			setSpeedLimit(scaler.kphToPixelsPerStep(60));
			break;
		}
	}

	/**
	 * Sets the position of the road.
	 *
	 * @param x1 the x-position for the start of the road.
	 * @param y1 the y-position for the start of the road.
	 * @param nextX2 the x-position for the end of the road.
	 * @param nextY2 the y-position for the end of the road.
	 */
	public void setPosition(double x1, double y1, double nextX2, double nextY2) {
		setXPosition(x1);
		setYPosition(y1);
		this.x2 = nextX2;
		this.y2 = nextY2;

		distX = nextX2 - x1;
		distY = nextY2 - y1;

		angle = Math.atan(distY / distX);
		if (distX < 0)
			angle += Math.PI;
	}
	
	/**
	 * Returns the type of road
	 * available : BICYCLE, CAR.
	 *
	 * @return type of Road
	 */
	public RoadType getRoadType() {
		return roadType;
	}

	/**
	 * Sets the position of the road in reference to a node.
	 *
	 * @param node the node
	 * @param x the x
	 * @param y the y
	 */
	public void setPosition(EntityNode node, int x, int y) {
		if (node.equals(enterNode)) {
			setPosition(x, y, x2, y2);
		} else {
			setPosition((int) getXPosition(), (int) getYPosition(), x, y);
		}
	}
	

	// Random delay between vehicle spawning
	int wait = (int) (50 + (Math.random() * 400));
	
	/*
	 * Step-function for entityroad. Controls spawning of vehicles.
	 */
	@Override
	public void step() {
		if (spawning)
			if (wait == 0) {
				if (roadType == RoadType.BICYCLE) {
					if (SharedValues.getInstance().getBicycleCounter() > 0) {
						instanceCreate(new EntityBicycle(this, StatsController.getInstance(), "Bicycle"));
						SharedValues.getInstance().decrementBicycleCounter();
					}
				} else {

					boolean smartVehicle = Math.random() > 0.5 ? true : false;

					if (smartVehicle && SharedValues.getInstance().getSmartCarCounter() > 0) {

						instanceCreate(new EntitySmartCar(this, StatsController.getInstance(), "Smartcar"));

						SharedValues.getInstance().decrementSmartCarCounter();

					} else if (SharedValues.getInstance().getCarCounter() > 0) {

						instanceCreate(new EntityCar(this, StatsController.getInstance(), "Car"));

						SharedValues.getInstance().decrementCarCounter();
					}
				}
				wait = scaler.getStepsPerSecond() * 4;
			}
		wait--;
	}
	
	/**
	 * Returns the angle the road is pointing towards
	 * relative horizontal axis.
	 *
	 * @return angle of road
	 */
	public double getAngle() {
		return angle;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see entities.Entity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (!drawn) {
			switch (roadType) {
			case CAR:

				g.setColor(Color.GREEN);
				break;
			case BICYCLE:
				g.setColor(Color.ORANGE);
				break;
			}

			g.drawLine((int) getXPosition(), (int) getYPosition(), (int) x2, (int) y2);
			// g.fillRect(x2,y2-4,8,8);
			g.setColor(Color.WHITE);
			// g.drawString((angle*(180.0/Math.PI)) + "�",
			// (int)(getXPosition() + (distX/2)),
			// (int)(getYPosition() + (distY/2));

		}

	}

	/**
	 * Gets the distance of the road on the x-axis.
	 *
	 * @return the dist X
	 */
	public double getDistX() {
		return distX;
	}

	/**
	 * Sets the distance of the road on the x-axis.
	 *
	 * @param distX the new dist X
	 */
	public void setDistX(double distX) {
		this.distX = distX;
	}

	/**
	 * Gets the distance of the road on the y-axis.
	 *
	 * @return the dist Y
	 */
	public double getDistY() {
		return distY;
	}

	/**
	 * Sets the distance of the road on the x-axis.
	 *
	 * @param distY the new dist Y
	 */
	public void setDistY(double distY) {
		this.distY = distY;
	}

	/**
	 * Gets the next road.
	 *
	 * @param turn the turn
	 * @return the next road
	 */
	public EntityRoad getNextRoad(Boolean turn) {
		return exitNode.getNextRoad(this, turn);
	}

	/**
	 * Gets the entry node.
	 *
	 * @return the entry node
	 */
	public EntityNode getEntryNode() {
		return enterNode;
	}

	/**
	 * Gets the exit node.
	 *
	 * @return the exit node
	 */
	public EntityNode getExitNode() {
		return exitNode;
	}

	/**
	 * Gets the other node.
	 *
	 * @param node the node
	 * @return the other node
	 */
	public EntityNode getOtherNode(EntityNode node) {
		if (node.equals(enterNode))
			return exitNode;
		else
			return enterNode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("EntityRoad: " + "x1:" + getXPosition() + " y1:" + getYPosition() + " x2:" + x2 + " y2:" + y2);
		return s.toString();
	}

	/**
	 * Gets the speed limit.
	 *
	 * @return the speed limit
	 */
	public double getSpeedLimit() {
		return speedLimit;
	}

	/**
	 * Sets the speed limit.
	 *
	 * @param speedLimit the new speed limit
	 */
	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}
}
