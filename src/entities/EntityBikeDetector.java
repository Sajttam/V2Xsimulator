package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.TreeSet;

public class EntityBikeDetector extends Entity implements Collidable {
	
	public static final Color STOP_RED = new Color(222, 22, 22, 100);
	public static final Color GO_GREEN = new Color(0, 255, 0, 100);
	
	private Set<EntityBicycle> bicycles;
	
	public EntityBikeDetector(double xPos, double yPos, int width, int height) {
		super(xPos, yPos, width, height);
		bicycles = new TreeSet<EntityBicycle>();
	}
	
	Set<EntityBicycle> remove = new TreeSet<EntityBicycle>();;
	@Override
	public void step() {
		bicycles.clear();
	}
	
	public Set<EntityBicycle> getBicycles() {
		return bicycles;
	}
	
	@Override
	public void draw(Graphics g) {
		if (bicycles.isEmpty())
			g.setColor(GO_GREEN);
		else
			g.setColor(STOP_RED);
		g.fillRect((int)getXPosition(), (int)getYPosition(), getWidth(), getHeight());
	}

	@Override
	public void collision(Entity other) {
		if (other instanceof EntityBicycle) {
			bicycles.add((EntityBicycle)other);
		}
	}
}
