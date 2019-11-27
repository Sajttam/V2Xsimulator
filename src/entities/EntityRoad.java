package entities;

import java.awt.Color;
import java.awt.Graphics;

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

	public boolean straight = true;
	public boolean leftCurve = false;

	public EntityRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType, Boolean spawning) {
		this.enterNode = enterNode;
		this.exitNode = exitNode;
		this.roadType = roadType;
		this.spawning = spawning;
	}

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

	public RoadType getRoadType() {
		return roadType;
	}

	public void setPosition(EntityNode node, int x, int y) {
		if (node.equals(enterNode)) {
			setPosition(x, y, x2, y2);
		} else {
			setPosition((int) getXPosition(), (int) getYPosition(), x, y);
		}
	}

	int wait = (int) (50 + (Math.random() * 400));

	@Override
	public void step() {
		if (spawning)
			if (wait == 0) {
				if (roadType == RoadType.BICYCLE) {
					if (SharedValues.getInstance().getBicycleCounter() > 0) {
						instanceCreate(new EntityBicycle(this, StatisticsObserver.getInstance()));
						SharedValues.getInstance().decrementBicycleCounter();
					}

				} else {
					if (SharedValues.getInstance().getCarCounter() > 0) {
						boolean smartVehicle = Math.random() > 0.1 ? true : false;

						if (smartVehicle) {
							instanceCreate(new EntitySmartCar(this, StatisticsObserver.getInstance()));
						} else {

							instanceCreate(new EntityCar(this, StatisticsObserver.getInstance()));

						}

						SharedValues.getInstance().decrementCarCounter();

					}
				}
				wait = (int) (50 + (Math.random() * 400));
			}
		wait--;
	}

	public double getAngle() {
		return angle;
	}

	@Override
	public void draw(Graphics g) {
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
		// g.drawString((angle*(180.0/Math.PI)) + "°", (int)(getXPosition()+(distX/2)),
		// (int)(getYPosition()+distY/2));

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

	public EntityRoad getNextRoad(Boolean turn) {
		return exitNode.getNextRoad(this, turn);
	}

	public EntityNode getEntryNode() {
		return enterNode;
	}

	public EntityNode getExitNode() {
		return exitNode;
	}

	public EntityNode getOtherNode(EntityNode node) {
		if (node.equals(enterNode))
			return exitNode;
		else
			return enterNode;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("EntityRoad: " + "x1:" + getXPosition() + " y1:" + getYPosition() + " x2:" + x2 + " y2:" + y2);
		return s.toString();
	}
}
