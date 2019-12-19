package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class EntityBikeDetector extends Entity implements Collidable {

	public static final Color STOP_RED = new Color(222, 22, 22, 100);
	public static final Color GO_GREEN = new Color(0, 255, 0, 100);
	private double viewingAngle;
	private boolean lightIsRed;

	private Set<EntityBicycle> bicycles;
	private List<Point> pointsChecked; // DEBUG LIST FOR CHECKED POINTS

	public EntityBikeDetector(double xPos, double yPos, int width, int height, double angle) {
		super(xPos, yPos, width, height);
		bicycles = new TreeSet<EntityBicycle>();
		pointsChecked = new Vector<Point>();
		viewingAngle = angle;
	}

	Set<EntityBicycle> remove = new TreeSet<EntityBicycle>();;
	private int wait = 0;
	private int waitTime = 30;

	@Override
	public void step() {
		if (wait <= 0) {
			bicycles.clear();
			wait = waitTime;
		} else {
			wait--;
		}
	}

	public Set<EntityBicycle> getBicycles() {
		return bicycles;
	}

	public boolean getLightIsRed() {
		return lightIsRed;
	}

	public void setLightIsRed(boolean lightIsRed) {
		this.lightIsRed = lightIsRed;
	}

	public void addCheckedPoint(Point p) {
		pointsChecked.add(p);
	}

	public double getViewingAngle() {
		return viewingAngle;
	}

	public void setViewingAngle(double viewingAngle) {
		this.viewingAngle = viewingAngle;
	}

	@Override
	public void draw(Graphics g) {
		if (bicycles.isEmpty())
			g.setColor(GO_GREEN);
		else
			g.setColor(STOP_RED);
		g.fillRect((int) getXPosition(), (int) getYPosition(), getWidth(), getHeight());
		// DEBUG
		g.setColor(Color.RED);
		/*
		 * for (Point p : pointsChecked) { g.fillRect(p.x-2, p.y-2, 4, 4); }
		 */

	}

	@Override
	public void collision(Entity other) {
		if (other instanceof EntityBicycle) {

			EntityBicycle bike = (EntityBicycle) other;
			if (bike.getSpeed() > 0 && !lightIsRed)
				bicycles.add((EntityBicycle) other);
		}
		if (other instanceof EntityTrafficLight) {

			lightIsRed = true;

		}
	}
}
