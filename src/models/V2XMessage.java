package models;

import java.awt.geom.Point2D;
import java.io.Serializable;

@SuppressWarnings("serial")
public class V2XMessage implements Serializable {

	private double speed;
	private double direction;
	private Point2D position;

	public V2XMessage(double speed, double direction, Point2D position) {

		this.speed = speed;
		this.direction = direction;
		this.position = position;

	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public String toString() {

		return "{speed: " + getSpeed() + ", direction: " + Math.round(Math.toDegrees(getDirection())) + ", position: "
				+ "[" + Math.round(getPosition().getX()) + ", " + Math.round(getPosition().getY()) + "]}";

	}

}
