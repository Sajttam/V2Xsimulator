package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class EntityNode extends Entity {

	public enum Direction {
		WEST, EAST, TOP, BOTTOM;
	}

	private ArrayList<EntityRoad> roadsWest = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsEast = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsTop = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsBottom = new ArrayList<EntityRoad>();

	private int wait = 0;

	public EntityNode(int x, int y) {
		setXPosition(x);
		setYPosition(y);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(getXPosition(), getYPosition(), getWidth(), getHeight());
	}

	public Direction getOppositDirection(Direction direction) {
		switch (direction) {
		case WEST:
			return Direction.EAST;
		case EAST:
			return Direction.WEST;
		case TOP:
			return Direction.BOTTOM;
		case BOTTOM:
			return Direction.TOP;
		}
		return direction;
	}

	public void addConnectionTo(EntityNode other, Direction direction) {
		EntityRoad road = new EntityRoad(this, other);
		addRoad(road, direction);
		other.addRoad(road, getOppositDirection(direction));
		instanceCreate(road);
	}

	public void addRoad(EntityRoad road, Direction direction) {
		ArrayList<EntityRoad> roads = null;
		switch (direction) {
		case WEST:
			roads = roadsWest;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++) roads.get(i).setPosition(this, getXPosition(), getYPosition() + i * 24 + 24);
			break;
		case EAST:
			roads = roadsEast;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++) roads.get(i).setPosition(this, getXPosition() + getWidth(), getYPosition() + i * 24 + 24);
			break;
		case TOP:
			roads = roadsTop;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++) roads.get(i).setPosition(this, getXPosition() + i * 48 + 24, getYPosition());
			break;
		case BOTTOM:
			roads = roadsBottom;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++) roads.get(i).setPosition(this, getXPosition() + i * 48 + 24, getYPosition() + getHeight());
			break;
		}
	}

	public boolean roadIsExit(EntityRoad road) {
		return equals(road.getExitNode());
	}

	public int getWidth() {
		return 96;
	}

	public int getHeight() {
		return 96;
	}
}
