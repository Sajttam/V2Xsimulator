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

	private boolean spawning;
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
	

	public EntityNode(int x, int y) {
		setXPosition(x);
		setYPosition(y);
		setSpawning(false);
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
						if (r.getRoadType() == RoadType.BICYCLE) {
						instanceCreate(new EntityBicycle(r, CollisionObserver.getInstance()));
						}
						else {
							instanceCreate(new EntityCar(r, CollisionObserver.getInstance()));
						}
						
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
						
						Boolean left = false;
						
						int x1 = r.x2;
						int y1 = r.y2;
						int x2 = (int)otherRoad.getXPosition();
						int y2 = (int)otherRoad.getYPosition();
						
						
						
						EntityRoad newRoad = new EntityRoad(this, this, r.getRoadType(),false, false, false); // False due to it should only be able to spawn on exit from node						
						newRoad.setPosition(x1, y1, x2, y2);
						
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
		EntityRoad road = new EntityRoad(this, other, roadType,spawning);
		addRoad(road, direction);
		other.addRoad(road, getOppositDirection(direction));
		instanceCreate(road);
	}

	public void addRoad(EntityRoad road, Direction direction) {
		ArrayList<EntityRoad> roads = null;
		int roadOffset = (int) ( getHeight() * 0.15);
		int seperation = (int) ((getHeight() - (2*roadOffset)));
		switch (direction) {
		case WEST:
			roads = roadsWest;
			roads.add(road);
			if (roads.size() > 1)
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition(),  (int)getYPosition() + i * (seperation/(roads.size()-1)) + roadOffset);
			break;
		case EAST:
			roads = roadsEast;
			roads.add(road);
			if (roads.size() > 1)
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + getWidth(), (int)getYPosition() + i * (seperation/(roads.size()-1))  + roadOffset);
			break;
		case NORTH:
			roads = roadsNorth;
			roads.add(road);
			if (roads.size() > 1)
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + i * (seperation/(roads.size()-1))  + roadOffset, (int)getYPosition());
			break;
		case SOUTH:
			roads = roadsSouth;
			roads.add(road);
			if (roads.size() > 1)
			for (int i = 0; i < roads.size(); i++)
				roads.get(i).setPosition(this, (int)getXPosition() + i * (seperation/(roads.size()-1))  + roadOffset, (int)getYPosition() + getHeight());
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
