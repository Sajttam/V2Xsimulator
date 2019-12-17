package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.EntityRoad.RoadType;
import models.SIScaling;
import models.SharedValues;

public class EntityNode extends Entity {

	private boolean spawning;
	public static final int MAX_SPEED_TURNS_CARS = 20;
	public static final int MAX_SPEED_BIKE = 20;

	public enum Direction {
		WEST, EAST, NORTH, SOUTH;
	}

	public class RoadPair {
		EntityRoad road1, road2;

		public RoadPair(EntityRoad road1, EntityRoad road2) {
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
		setCollisionBounds(getWidth(), getHeight(), 0, 0);

	}

	public boolean isSpawning() {
		return spawning;
	}

	public void setSpawning(boolean spawning) {
		this.spawning = spawning;
	}

	int wait = (int) (20 + (Math.random() * 400));

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
		int count = 0;
		if (roadsWest.isEmpty())
			count++;
		if (roadsEast.isEmpty())
			count++;
		if (roadsNorth.isEmpty())
			count++;
		if (roadsSouth.isEmpty())
			count++;

		boolean starightRoads = !(count == 2);

		if (starightRoads)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLUE);
		g.drawRect((int) getXPosition(), (int) getYPosition(), getWidth(), getHeight());
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
		} else {
			return null;
		}
	}

	public void doInternalConnections(List<EntityRoad> roads) {
		int count = 0;
		if (roadsWest.isEmpty())
			count++;
		if (roadsEast.isEmpty())
			count++;
		if (roadsNorth.isEmpty())
			count++;
		if (roadsSouth.isEmpty())
			count++;

		boolean starightRoads = !(count == 2);

		// if (starightRoads) System.out.println("YES");

		for (EntityRoad r : roads) {

			if (equals(r.getExitNode())) {

				List<EntityRoad> allOtherRoads = getAllRoads();
				allOtherRoads.removeAll(roads);

				for (EntityRoad otherRoad : allOtherRoads) {
					if (equals(otherRoad.getEntryNode()) && otherRoad.getRoadType() == r.getRoadType()) {

						Boolean left = false;

						double x1 = r.x2;
						double y1 = r.y2;
						double x2 = otherRoad.getXPosition();
						double y2 = otherRoad.getYPosition();

						// System.out.print("Angle: " + difAngle);

						// EntityRoad newRoad = new EntityRoad(this, this, r.getRoadType(),false); //
						// False due to it should only be able to spawn on exit from node
						EntityCurvedRoad newRoad = new EntityCurvedRoad(this, this, r.getRoadType(), r.getAngle(),
								otherRoad.getAngle()); // False due to it should only be able to spawn on exit from node

						SIScaling scaler = new SIScaling();
						if (newRoad.straight)
							newRoad.setSpeedLimit(SharedValues.getInstance().getMaxSpeed("CAR"));
						else {
							newRoad.setSpeedLimit(scaler.kphToPixelsPerStep(MAX_SPEED_TURNS_CARS));

						}
						newRoad.setPosition(r.x2, r.y2, otherRoad.getXPosition(), otherRoad.getYPosition());

						if (roadConnections.containsKey(r)) {
							RoadPair rp = roadConnections.get(r);
							rp.setRoad2(newRoad);
						} else {
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
		EntityRoad road = new EntityRoad(this, other, roadType, spawning);

		addRoad(road, direction);
		other.addRoad(road, getOppositDirection(direction));

		instanceCreate(road);
	}

	public void addRoad(EntityRoad road, Direction direction) {
		ArrayList<EntityRoad> roads = null;
		int roadXOffset = (int) (getWidth() * 0.15);
		int roadYOffset = (int) (getHeight() * 0.15);
		int ySeparation = ((getHeight() - (2 * roadYOffset)));
		int xSeparation = ((getWidth() - (2 * roadXOffset)));

		switch (direction) {
		case WEST:
			roads = roadsWest;
			roads.add(road);
			if (roads.size() > 1)
				for (int i = 0; i < roads.size(); i++)
					roads.get(i).setPosition(this, (int) getXPosition(),
							(int) getYPosition() + i * (ySeparation / (roads.size() - 1)) + roadYOffset);

			break;
		case EAST:
			roads = roadsEast;
			roads.add(road);
			if (roads.size() > 1)
				for (int i = 0; i < roads.size(); i++)
					roads.get(i).setPosition(this, (int) getXPosition() + getWidth(),
							(int) getYPosition() + i * (ySeparation / (roads.size() - 1)) + roadYOffset);
			break;
		case NORTH:
			roads = roadsNorth;
			roads.add(road);
			if (roads.size() > 1)
				for (int i = 0; i < roads.size(); i++)
					roads.get(i).setPosition(this,
							(int) getXPosition() + i * (xSeparation / (roads.size() - 1)) + roadXOffset,
							(int) getYPosition());
			break;
		case SOUTH:
			roads = roadsSouth;
			roads.add(road);
			if (roads.size() > 1)
				for (int i = 0; i < roads.size(); i++)
					roads.get(i).setPosition(this,
							(int) getXPosition() + i * (xSeparation / (roads.size() - 1)) + roadXOffset,
							(int) getYPosition() + getHeight());
			break;
		}

	}

	public boolean roadIsExit(EntityRoad road) {
		return equals(road.getExitNode());
	}

	public void addRoadReservation(EntityRoad r, EntityTrafficLight l) {

		int resHeight = getWidth();
		EntityRoadReservation rRes = null;

		double angle = Math.toDegrees(r.getAngle());

		if (angle == 0.0) {
			rRes = new EntityRoadReservation(r.x2, r.y2, this.getWidth(), getHeight() / 10, r.getAngle());
		} else if (angle == 180.0) {
			rRes = new EntityRoadReservation(r.x2, r.y2, this.getWidth(), getHeight() / 10, r.getAngle());

		} else if (angle == -90.0) {
			rRes = new EntityRoadReservation(r.x2, r.y2, this.getWidth() / 10, getHeight(), r.getAngle());

		} else if (angle == 90.0) {
			rRes = new EntityRoadReservation(r.x2, r.y2, this.getWidth() / 10, getHeight(), r.getAngle());

		} else {
			rRes = new EntityRoadReservation(r.x2 - this.getWidth() * 1.6, r.y2 - resHeight / 3, this.getWidth() * 2,
					resHeight, r.getAngle());

		}
		instanceCreate(rRes);

	}

	@Override
	public int getWidth() {
		return (int) SharedValues.getInstance().getNodeWidth();
	}

	@Override
	public int getHeight() {
		return (int) SharedValues.getInstance().getNodeHeight();
	}

}
