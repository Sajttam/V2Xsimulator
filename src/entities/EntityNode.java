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

/**
 * The Class EntityNode which sets the behaviour of the traffic Nodes.
 */
public class EntityNode extends Entity {

	
	private boolean spawning;
	public static final int MAX_SPEED_TURNS_CARS = 20;
	public static final int MAX_SPEED_BIKE = 20;

	/**
	 * The Enum Direction.
	 */
	public enum Direction {
		WEST, EAST, NORTH, SOUTH;
	}

	/**
	 * The Class RoadPair.
	 */
	public class RoadPair {
		
		EntityRoad road1, road2;

		/**
		 * Instantiates a new road pair.
		 *
		 * @param road1 the road 1
		 * @param road2 the road 2
		 */
		public RoadPair(EntityRoad road1, EntityRoad road2) {
			this.road1 = road1;
			this.road2 = road2;
		}

		/**
		 * Gets the first road.
		 *
		 * @return the road 1
		 */
		public EntityRoad getRoad1() {
			return road1;
		}

		/**
		 * Gets the second road.
		 *
		 * @return the road 2
		 */
		public EntityRoad getRoad2() {
			return road2;
		}

		/**
		 * Sets the first road.
		 *
		 * @param road1 the new road 1
		 */
		public void setRoad1(EntityRoad road1) {
			this.road1 = road1;
		}

		/**
		 * Sets the second road.
		 *
		 * @param road2 the new road 2
		 */
		public void setRoad2(EntityRoad road2) {
			this.road2 = road2;
		}
	}

	private ArrayList<EntityRoad> roadsWest = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsEast = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsNorth = new ArrayList<EntityRoad>();
	private ArrayList<EntityRoad> roadsSouth = new ArrayList<EntityRoad>();
	private Map<EntityRoad, RoadPair> roadConnections = new HashMap<EntityRoad, RoadPair>();;

	/**
	 * Instantiates a new entity node.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public EntityNode(int x, int y) {
		setXPosition(x);
		setYPosition(y);
		setSpawning(false);
		setCollisionBounds(getWidth(), getHeight(), 0, 0);

	}
	
	/**
	 * Tells if a node can spawn vehicles.
	 *
	 * @return Returns whether the node is spawning
	 */
	public boolean isSpawning() {
		return spawning;
	}
	
	/**
	 * Sets a node to spawn vehicles.
	 *
	 * @param spawning the new spawning
	 */
	public void setSpawning(boolean spawning) {
		this.spawning = spawning;
	}

	/** The wait. */
	int wait = (int) (20 + (Math.random() * 400));

	/**
	 * Gets the all roads.
	 *
	 * @return the all roads
	 */
	public List<EntityRoad> getAllRoads() {
		List<EntityRoad> allRoads = new ArrayList<EntityRoad>(roadsWest);
		allRoads.addAll(roadsWest);
		allRoads.addAll(roadsEast);
		allRoads.addAll(roadsNorth);
		allRoads.addAll(roadsSouth);
		return allRoads;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see entities.Entity#draw(java.awt.Graphics)
	 */
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

	/**
	 * Gets the opposite direction.
	 *
	 * @param direction the direction
	 * @return the opposite direction
	 */
	public Direction getOppositeDirection(Direction direction) {
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
	
	/**
	 * Return the next road in the pair if there is one
	 * otherwise null.
	 *
	 * @param road the road
	 * @param turn the turn
	 * @return returns the next road
	 */
	public EntityRoad getNextRoad(EntityRoad road, Boolean turn) {
		if (roadConnections.containsKey(road)) {
			RoadPair rp = roadConnections.get(road);
			return turn ? rp.getRoad1() : rp.getRoad2();
		} else {
			return null;
		}
	}
	
	/**
	 * Creates the network of roads
	 * links them together to make all the roads
	 * connect to each-other.
	 *
	 * @param roads the roads
	 */
	public void doInternalConnections(List<EntityRoad> roads) {
	
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

						// EntityRoad newRoad = new EntityRoad(this, this, r.getRoadType(), false); //
						// False due to it should only be able to spawn on exit from node
						EntityCurvedRoad newRoad = new EntityCurvedRoad(this, this, r.getRoadType(), r.getAngle(),
								otherRoad.getAngle()); // False due to it should only be able to spawn on exit from node

						SIScaling scaler = new SIScaling();

						switch (r.getRoadType()) {
						case BICYCLE:
							newRoad.setSpeedLimit(scaler.kphToPixelsPerStep(20));
							break;
						case CAR:
							if (newRoad.straight)
								newRoad.setSpeedLimit(SharedValues.getInstance().getMaxSpeed("CAR"));
							else {
								newRoad.setSpeedLimit(scaler.kphToPixelsPerStep(MAX_SPEED_TURNS_CARS));

							}
							break;
						default:
							newRoad.setSpeedLimit(scaler.kphToPixelsPerStep(60));
							break;
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

	/**
	 * Handle to create all internal connections.
	 */
	public void doInternalConnections() {
		doInternalConnections(roadsWest);
		doInternalConnections(roadsEast);
		doInternalConnections(roadsNorth);
		doInternalConnections(roadsSouth);
	}
	
	/**
	 * Adds the connection to.
	 *
	 * @param other the other
	 * @param direction the direction
	 * @param roadType the road type
	 */
	/*
	 * Connects the road to a node
	 */
	public void addConnectionTo(EntityNode other, Direction direction, RoadType roadType) {
		EntityRoad road = new EntityRoad(this, other, roadType, spawning);

		addRoad(road, direction);
		other.addRoad(road, getOppositeDirection(direction));

		instanceCreate(road);
	}

	/**
	 * Adds a road to to this node.
	 *
	 * @param road the road
	 * @param direction the direction
	 */
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

	/**
	 * Returns true if the input road ends at this node.
	 *
	 * @param road the road
	 * @return true, if successful
	 */
	public boolean roadIsExit(EntityRoad road) {
		return equals(road.getExitNode());
	}

	/**
	 * Adds a road reservation to this node. The reservations are used to prevent vehicles crossing the crossing when the reservation is occupied.
	 *
	 * @param r the r
	 * @param l the l
	 */
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

	/* 
	 * Returns the node width.
	 */
	@Override
	public int getWidth() {
		return (int) SharedValues.getInstance().getNodeWidth();
	}

	/* 
	 * Returns the node height.
	 */
	@Override
	public int getHeight() {
		return (int) SharedValues.getInstance().getNodeHeight();
	}

}
