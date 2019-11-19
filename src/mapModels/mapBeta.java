package mapModels;

import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import entities.Entity;
import entities.EntityNode;
import entities.EntityRoad.RoadType;

public class mapBeta implements SimulationMap {

	@Override
	public List<Entity> getMap(Controller controller) {
		List<Entity> list = new ArrayList<Entity>();

		EntityNode nodeWest = new EntityNode(64, 472);
		EntityNode nodeEast = new EntityNode(748, 472);
		EntityNode nodeTop = new EntityNode(406, 128);
		EntityNode nodeCenter = new EntityNode(406, 472);
		EntityNode nodeNorthWest = new EntityNode(64, 128);
		EntityNode nodeNorthEast = new EntityNode(748, 128);
		
		//Create simulation instances
		controller.createInstance(nodeWest);
		controller.createInstance(nodeEast);
		controller.createInstance(nodeTop);
		controller.createInstance(nodeCenter);
		controller.createInstance(nodeNorthWest);
		controller.createInstance(nodeNorthEast);

		
		//WEST - CENTER, W - NE
		
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST,RoadType.BICYCLE);

		nodeCenter.addConnectionTo(nodeWest, EntityNode.Direction.WEST,RoadType.CAR);
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST,RoadType.CAR);

		
		nodeNorthWest.addConnectionTo(nodeWest, EntityNode.Direction.SOUTH,RoadType.CAR);
		nodeWest.addConnectionTo(nodeNorthWest, EntityNode.Direction.NORTH,RoadType.CAR);
		nodeNorthWest.addConnectionTo(nodeWest, EntityNode.Direction.SOUTH,RoadType.BICYCLE);


		
		//EAST - CENTER, E-NW
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST,RoadType.BICYCLE);
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST,RoadType.CAR);
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST,RoadType.CAR);

		
		nodeNorthEast.addConnectionTo(nodeEast, EntityNode.Direction.SOUTH,RoadType.BICYCLE);
		nodeNorthEast.addConnectionTo(nodeEast, EntityNode.Direction.SOUTH,RoadType.CAR);
		nodeEast.addConnectionTo(nodeNorthEast, EntityNode.Direction.NORTH,RoadType.CAR);
		
		//TOP - CENTER, T - NW, T-NE
		nodeCenter.addConnectionTo(nodeTop, EntityNode.Direction.NORTH,RoadType.BICYCLE);
		nodeTop.addConnectionTo(nodeCenter, EntityNode.Direction.SOUTH,RoadType.CAR);
		nodeCenter.addConnectionTo(nodeTop, EntityNode.Direction.NORTH,RoadType.CAR);
		
		nodeNorthEast.addConnectionTo(nodeTop, EntityNode.Direction.WEST,RoadType.CAR);
		nodeTop.addConnectionTo(nodeNorthEast, EntityNode.Direction.EAST,RoadType.CAR);

		nodeTop.addConnectionTo(nodeNorthEast, EntityNode.Direction.EAST,RoadType.BICYCLE);

		
		nodeTop.addConnectionTo(nodeNorthWest, EntityNode.Direction.WEST,RoadType.CAR);
		nodeNorthWest.addConnectionTo(nodeTop, EntityNode.Direction.EAST,RoadType.CAR);
		nodeTop.addConnectionTo(nodeNorthWest, EntityNode.Direction.WEST,RoadType.BICYCLE);

		
		
		nodeWest.doInternalConnections();
		nodeEast.doInternalConnections();
		nodeCenter.doInternalConnections();
		nodeTop.doInternalConnections();
		nodeNorthEast.doInternalConnections();
		nodeNorthWest.doInternalConnections();
		
		
		nodeWest.setSpawning(true);


		

		return list;
	}

}
