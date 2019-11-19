package mapModels;

import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import entities.Entity;
import entities.EntityNode;
import entities.EntityRoad.RoadType;

public class mapAlpha implements SimulationMap {

	@Override
	public List<Entity> getMap(Controller controller) {
		List<Entity> list = new ArrayList<Entity>();

		// Create node objects
		EntityNode nodeWest = new EntityNode(64, 472 - 32);
		EntityNode nodeEast = new EntityNode(748, 472 - 32);
		EntityNode nodeTop = new EntityNode(406, 128);
		EntityNode nodeCenter = new EntityNode(406, 472);

		nodeWest.setSpawning(true);
		nodeEast.setSpawning(true);
		nodeTop.setSpawning(true);
		// nodeCenter.setSpawning(true);

		// Create simulation instances
		controller.createInstance(nodeWest);
		controller.createInstance(nodeEast);
		controller.createInstance(nodeTop);
		controller.createInstance(nodeCenter);

		// WEST - CENTER
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST, RoadType.BICYCLE);
		nodeCenter.addConnectionTo(nodeWest, EntityNode.Direction.WEST, RoadType.CAR);
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST, RoadType.CAR);

		// EAST - CENTER
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST, RoadType.BICYCLE);
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST, RoadType.CAR);

		// TOP - CENTER
		nodeTop.addConnectionTo(nodeCenter, EntityNode.Direction.SOUTH, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeTop, EntityNode.Direction.NORTH, RoadType.CAR);

		nodeCenter.doInternalConnections();

		return list;
	}

}
