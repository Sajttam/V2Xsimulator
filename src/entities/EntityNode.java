package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import entities.EntityRoad.RoadType;

public class EntityNode extends Entity {

	public enum Direction {
		WEST, EAST, NORTH, SOUTH;
	}
	
	public class RoadPair {
		EntityRoad road1, road2;
		
		public RoadPair (EntityRoad road1, EntityRoad road2) {
			this.road1 = road1;
			this.road2 = road2;
		}
		
		public EntityRoad getRoad1() {
			return road1;
		}
		
		public EntityRoad getRoad2() {
			return road2;
		}
		
		public void setRoad1(EntityRoad road1) {
			this.road1 = road1;
		}
		
		public void setRoad2(EntityRoad road2) {
			this.road2 = road2;
		}
	}

	private ArrayList<EntityRoad> roadsWest = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsEast = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsNorth = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsSouth = new ArrayList<EntityRoad>();

	private Map<EntityRoad, RoadPair> roadConnections = new HashMap<EntityRoad, RoadPair>();;
	
	private boolean spawning = false;

	public EntityNode(int x, int y) {
		setXPosition(x);
		setYPosition(y);
	}

	public boolean isSpawning() {
		return spawning;
	}

	public void setSpawning(boolean spawning) {
		this.spawning = spawning;
	}

	int wait = (int) (20 + (Math.random() * 400));

	@Override
	public void step() {
		if (spawning) {
			for (EntityRoad r : getAllRoads()) {
				if (wait == 0) {
					if (equals(r.getEntryNode()))
						instanceCreate(new EntityVehicle(r));
					wait = (int) (40 + (Math.random() * 400));
				} else
					wait--;
			}
		}
	}

	public List<EntityRoad> getAllRoads() {
		List<EntityRoad> allRoads = new ArrayList<EntityRoad>(roadsWest);
		allRoads.addAll(roadsWest);
		allRoads.addAll(roadsEast);
		allRoads.addAll(roadsNorth);
		allRoads.addAll(roadsSouth);
		return allRoads;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect((int)getXPosition(), (int)getYPosition(), getWidth(), getHeight());
	}

	public Direction getOppositDirection(Direction direction) {
		switch (direction) {
		case WEST:
			return Direction.EAST;
		case EAST:
			return Direction.WEST;
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		}
		return direction;
	}
	
	public EntityRoad getNextRoad(EntityRoad road, Boolean turn) {
		if (roadConnections.containsKey(road)) {
			RoadPair rp = roadConnections.get(road);
			return turn ? rp.getRoad1() : rp.getRoad2();
		}
		else {
			return null;
		}
	}
	
	public void doInternalConnections(List<EntityRoad> roads) {
		for (EntityRoad r : roads) {
			if (equals(r.getExitNode())) {
				//roadConnections.put(r, new RoadPair(null, null));
				List<EntityRoad> allOtherRoads = getAllRoads();
				allOtherRoads.removeAll(roads);

				for (EntityRoad otherRoad : allOtherRoads) {
					if (equals(otherRoad.getEntryNode()) && otherRoad.getRoadType() == r.getRoadType()) {
						
						EntityRoad newRoad = new EntityRoad(this, this, r.getDirection(), r.getRoadType());
						
						
						
						newRoad.setPosition(r.x2, r.y2, (int)otherRoad.getXPosition(),
								(int)otherRoad.getYPosition());

						if (roadConnections.containsKey(r)) {
							RoadPair rp = roadConnections.get(r);
							rp.setRoad2(newRoad);
						}
						else {
							roadConnections.put(r, new RoadPair(newRoad, newRoad));
						}
						roadConnections.put(newRoad, new RoadPair(otherRoad, otherRoad));
						instanceCreate(newRoad);
					}
				}
			}
		}
		
	}
	
	public void doInternalConnections() {
		doInternalConnections(roadsWest);
		doInternalConnections(roadsEast);
		doInternalConnections(roadsNorth);
		doInternalConnections(roadsSouth);
	}

	public void addConnectionTo(EntityNode other, Direction direction, RoadType roadType) {
		EntityRoad road = new EntityRoad(this, other, direction, roadType);
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
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition(), (int)getYPosition() + i * 24 + 24);
			break;
		case EAST:
			roads = roadsEast;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + getWidth(), (int)getYPosition() + i * 24 + 24);
			break;
		case NORTH:
			roads = roadsNorth;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + i * 48 + 24, (int)getYPosition());
			break;
		case SOUTH:
			roads = roadsSouth;
			roads.add(road);
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + i * 48 + 24, (int)getYPosition() + getHeight());
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
