package models;

import java.awt.Point;

public class CarData {
	private V2XMessage oldMessage = null;
	private V2XMessage newMessage = null;
	private SIScaling scaling = new SIScaling();
	private double angularVelocity = 0;

	public CarData(V2XMessage message) {
		oldMessage = message;
		newMessage = message;
	}

	public void updateMessage(V2XMessage message) {
		oldMessage = newMessage;
		newMessage = message;

		setAngularVelocity((newMessage.getDirection() - oldMessage.getDirection()) / getTimeDiff());

	}

	/**
	 * Position of the car in n number of steps
	 * 
	 * @param steps the number future steps
	 * @return future position of the car
	 */
	public Point getPositionAfterSteps(int steps) {
		double angle = newMessage.getDirection() + getAngularVelocity() * steps;
		double speed = scaling.kphToPixelsPerStep(newMessage.getSpeed()) + getAcceleration() * steps;
		double currentX = newMessage.getPosition().getX();
		double currentY = newMessage.getPosition().getY();
		// System.out.println("Angle: " + angle + " Direction "+
		// newMessage.getDirection() + "AngleV " + getAngularVelocity());
		double newX = currentX + (steps * speed * Math.cos(angle));
		double newY = currentY + (steps * speed * Math.sin(angle));

		Point futurePosition = new Point((int) newX, (int) newY);

		return futurePosition;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public double getAcceleration() {
		double acceleration = scaling.kphToPixelsPerStep((newMessage.getSpeed() - oldMessage.getSpeed()))
				/ getTimeDiff();
		if (acceleration < 0) {
			acceleration = 0;
		}
		return acceleration;
	}

	public long getTimeDiff() {
		return newMessage.getTimeStamp() - oldMessage.getTimeStamp();
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
