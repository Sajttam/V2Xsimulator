package models;

import java.awt.geom.Point2D;
import java.io.Serializable;

import controller.Controller;

@SuppressWarnings("serial")
public class V2XMessage implements Serializable {

	private double speed;
	private double direction;
	private Point2D position;
	private int id;
	private long timeStamp = Controller.GLOBAL.getTimeStamp();
	private int listenerPort;

	public V2XMessage(int id, double speed, double direction, Point2D position, int listenerPort) {
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.position = position;
		this.listenerPort = listenerPort;
	}
	
	public int getId() {
		return id;
	}

	public int getListenerPort() {
		return listenerPort;
	}

	public void setListenerPort(int listenerPort) {
		this.listenerPort = listenerPort;
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

		return "{id: " + id + ", speed: " + Math.round(getSpeed()) + ", direction: " + Math.round(Math.toDegrees(getDirection()))
				+ ", position: " + "[" + Math.round(getPosition().getX()) + ", " + Math.round(getPosition().getY())
				+ "]" + ", timestamp: " + timeStamp + "}";

	}

	public long getTimeStamp() {
		return timeStamp;
	}
}
