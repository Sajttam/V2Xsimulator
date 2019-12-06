package models;

import java.awt.Point;
import java.awt.geom.Point2D;

public class CarData  {
	private V2XMessage oldMessage = null;
	private V2XMessage newMessage = null;
	private double angularVelocity	= 0;
	
	public CarData(V2XMessage message) {
		oldMessage = message;
		newMessage = message;
	}
	
	public void updateMessage(V2XMessage message) {
		oldMessage = newMessage;
		newMessage = message;
		
		if (newMessage.getDirection() != 0)
			setAngularVelocity(oldMessage.getDirection() / newMessage.getDirection());
		else
			setAngularVelocity(0);
	}
	
	/**
	 * Position of the car in n number of steps
	 * @param steps the number future steps 
	 * @return future position of the car
	 */
	public Point getPositionAfterSteps(int steps) {
		double angle = newMessage.getDirection() + getAngularVelocity() * steps;
		double speed = newMessage.getSpeed();
		double currentX = newMessage.getPosition().getX();
		double currentY = newMessage.getPosition().getY();		
		
		double newX = currentX + (steps * speed * Math.cos(angle));
		double newY = currentY + (steps * speed * Math.sin(angle));
		
		Point futurePosition = new Point((int)newX,(int)newY);
		
		return futurePosition;		
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
	
	public V2XMessage getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(V2XMessage newMessage) {
		this.newMessage = newMessage;
	}
}
