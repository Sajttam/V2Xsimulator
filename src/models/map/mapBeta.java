package models.map;

import java.util.ArrayList;
import java.util.List;

import V2XServer.RSUServerUDP;
import controller.Controller;
import entities.Entity;
import entities.EntityBikeDetector;
import entities.EntityNode;
import entities.EntityRoad.RoadType;
import entities.EntityTrafficLightNode;
import entities.MouseObserver;
import models.SharedValues;

public class mapBeta implements SimulationMap {

	@Override
	public List<Entity> getMap(Controller controller) {
		List<Entity> list = new ArrayList<Entity>();

		controller.createInstance(new MouseObserver());

		EntityNode nodeWest = new EntityNode(64, 600);
		EntityNode nodeEast = new EntityNode(748, 600);
		EntityNode nodeNorthWest = new EntityNode(64, 128);
		EntityNode nodeNorthEast = new EntityNode(748, 128);
		EntityTrafficLightNode nodeNorth = new EntityTrafficLightNode(406, 128);
		EntityTrafficLightNode nodeCenter = new EntityTrafficLightNode(406, 600);

		RSUServerUDP serverNorth = new RSUServerUDP(SharedValues.getInstance().getServerPortNumber(),
				(int) nodeNorth.getXPosition(), (int) nodeNorth.getYPosition());
		(new Thread(serverNorth)).start();

		RSUServerUDP serverCenter = new RSUServerUDP(SharedValues.getInstance().getServerPortNumber(),
				(int) nodeCenter.getXPosition(), (int) nodeCenter.getYPosition());
		(new Thread(serverCenter)).start();

		EntityBikeDetector bikeDetectorCenterTopRight = new EntityBikeDetector(460, 600, 75, 30, 0);
		controller.createInstance(bikeDetectorCenterTopRight);
		EntityBikeDetector bikeDetectorNorthBottomLeft = new EntityBikeDetector(370, 180, 75, 30, 180);
		controller.createInstance(bikeDetectorNorthBottomLeft);

		serverNorth.addBikeDetector(bikeDetectorNorthBottomLeft);
		serverCenter.addBikeDetector(bikeDetectorCenterTopRight);

		// Create simulation instances
		controller.createInstance(nodeWest);
		controller.createInstance(nodeEast);
		controller.createInstance(nodeNorth);
		controller.createInstance(nodeCenter);
		controller.createInstance(nodeNorthWest);
		controller.createInstance(nodeNorthEast);

		controller.createInstance(serverNorth.getRSUBoundaries());
		controller.createInstance(serverCenter.getRSUBoundaries());

		SharedValues.getInstance().addRSU(serverNorth);
		SharedValues.getInstance().addRSU(serverCenter);

		nodeWest.setSpawning(true);

		// WEST - CENTER, W - NE

		nodeCenter.addConnectionTo(nodeWest, EntityNode.Direction.WEST, RoadType.BICYCLE);

		nodeCenter.addConnectionTo(nodeWest, EntityNode.Direction.WEST, RoadType.CAR);
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST, RoadType.CAR);

		nodeNorthWest.addConnectionTo(nodeWest, EntityNode.Direction.SOUTH, RoadType.CAR);
		nodeWest.addConnectionTo(nodeNorthWest, EntityNode.Direction.NORTH, RoadType.CAR);
		nodeWest.addConnectionTo(nodeNorthWest, EntityNode.Direction.NORTH, RoadType.BICYCLE);

		// EAST - CENTER, E-NE
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST, RoadType.BICYCLE);
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST, RoadType.CAR);

		nodeNorthEast.addConnectionTo(nodeEast, EntityNode.Direction.SOUTH, RoadType.BICYCLE);
		nodeNorthEast.addConnectionTo(nodeEast, EntityNode.Direction.SOUTH, RoadType.CAR);
		nodeEast.addConnectionTo(nodeNorthEast, EntityNode.Direction.NORTH, RoadType.CAR);

		// TOP - CENTER, T - NW, T-NE

		// nodeCenter.addConnectionTo(nodeTop,
		// EntityNode.Direction.NORTH,RoadType.BICYCLE);
		nodeNorth.addConnectionTo(nodeCenter, EntityNode.Direction.SOUTH, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeNorth, EntityNode.Direction.NORTH, RoadType.CAR);

		nodeNorthEast.addConnectionTo(nodeNorth, EntityNode.Direction.WEST, RoadType.CAR);
		nodeNorth.addConnectionTo(nodeNorthEast, EntityNode.Direction.EAST, RoadType.CAR);

		nodeNorth.addConnectionTo(nodeNorthEast, EntityNode.Direction.EAST, RoadType.BICYCLE);

		nodeNorth.addConnectionTo(nodeNorthWest, EntityNode.Direction.WEST, RoadType.CAR);
		nodeNorthWest.addConnectionTo(nodeNorth, EntityNode.Direction.EAST, RoadType.CAR);
		nodeNorthWest.addConnectionTo(nodeNorth, EntityNode.Direction.EAST, RoadType.BICYCLE);

		nodeWest.doInternalConnections();
		nodeEast.doInternalConnections();
		nodeCenter.doInternalConnections();
		nodeNorth.doInternalConnections();
		nodeNorthEast.doInternalConnections();
		nodeNorthWest.doInternalConnections();

		nodeCenter.generateTrafficLights();
		nodeNorth.generateTrafficLights();

		nodeWest.setSpawning(true);

		return list;
	}

}
