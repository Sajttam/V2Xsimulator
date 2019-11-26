package models;

import com.sun.javafx.scene.paint.GradientUtils.Point;

public class V2XMessage {


	private int speed;
	private double direction;
	private Point position;
	
	public V2XMessage(int speed, double direction, Point position) {
		
		this.speed = speed;
		this.direction = direction;
		this.position = position;
		
		
	}
	
	public int getSpeed() {
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

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	

}
