package entities;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.EntityTrafficLight.LightCycle;

public class EntityRoadReservation extends Entity {

	private double angle;
	private ArrayList<Integer> newReservations = new ArrayList<Integer>();
	private ArrayList<Integer> oldReservations = new ArrayList<Integer>();
	EntityTrafficLight tLight;

	public EntityRoadReservation(double xPosition, double yPosition, int width, int height, double angle,
			EntityTrafficLight tLight) {
		super(xPosition, yPosition, width, height);
		this.angle = angle;
		this.tLight = tLight;
	}

	public void addReservation(int reservationHash) {

		newReservations.add(reservationHash);

	}

	@Override
	public void draw(Graphics g) {
//
//		g.setColor(Color.blue);
//		g.drawRect((int) getCollisionBounds().getX(), (int) getCollisionBounds().getY(),
//				(int) getCollisionBounds().getWidth(), (int) getCollisionBounds().getHeight());

	}

	@Override
	public void step() {

		oldReservations.clear();
		if (!tLight.getLightCycle().equals(LightCycle.STOP)) {
			oldReservations.addAll(newReservations);
		}
		newReservations.clear();

	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public ArrayList<Integer> getReservations() {
		return oldReservations;
	}

	public void setReservations(ArrayList<Integer> reservations) {
		this.newReservations = reservations;
	}

}
