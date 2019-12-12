package models;

import java.awt.geom.Point2D;

import controller.Controller;
/**
 *  Used to save data from vehicle when it collides 
 * @author
 *
 */
public class CollisionData {

	private double speed;
	private double direction;
	private Point2D position;
	private long timeStamp = Controller.GLOBAL.getTimeStamp();
	
	
	/**
	 * 
	 * @param speed  a vehicles speed
	 * @param direction  a vehicles direction
	 * @param position  a vehicles position
	 */
	public CollisionData (double speed,double direction,Point2D position) {
		this.speed = speed;
		this.direction = direction;
		this.position = position;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
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

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String toString() {
		return timeStamp + "," + speed + "," + direction + "," + position.toString();
	}

}
