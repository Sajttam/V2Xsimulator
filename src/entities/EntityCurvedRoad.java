package entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import entities.EntityNode.Direction;

public class EntityCurvedRoad extends EntityRoad {
	
	private class RoadPart extends EntityRoad {
		protected EntityCurvedRoad first = null;
		protected EntityRoad last = null;
		
		public RoadPart(EntityNode enterNode, EntityNode exitNode, RoadType roadType) {
			super(enterNode, exitNode, roadType,false); //False is for roadspawning
			// TODO Auto-generated constructor stub
		}
		
		public EntityRoad getNextRoad(Boolean turn) {		
			return first.getNextRoadPart(this, turn);
		}
		
	}
	
	private Map<EntityRoad, EntityRoad> roadToRoad;
	private EntityRoad first = null;
	private EntityRoad last = null;
	
	public EntityCurvedRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType) {
		super(enterNode, exitNode, roadType,false); //False is for roadspawning
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {		
		double origoX = x2;
		double origoY = y1;		
		
		
		int numPoints = 10;
		double angleDiff = -1*Math.PI/(2.0 * numPoints);
		roadToRoad = new LinkedHashMap<EntityRoad, EntityRoad>();
		
		
		//super.setPosition(x1,y1,(int) (origoX + (x2-x1) * Math.cos(angleDiff*1)), (int) (origoY + (y2-y1) * Math.sin(angleDiff*1)));
		first = this;
		
		int preX2 = x2;
		int preY2 = y2;
		EntityRoad preRoad = this;
		
		
		for (int i = 1; i <= numPoints; i++) {
			RoadPart newRoad = new RoadPart(null, null, getRoadType());
			
			
			int nextX2 = (int) (origoX + (x2-x1) * Math.cos(angleDiff * i));
			int nextY2 = (int) (origoY + (y2-y1) * Math.sin(angleDiff * i));
			
			////////////////////////////HÄR FEL
			
			newRoad.setPosition(preX2, preY2, nextX2, nextY2);
			newRoad.first = this;
			
			
			getEntryNode().instanceCreate(newRoad);


			//////////////////////
		
			roadToRoad.put(preRoad, newRoad);
			
			
			
			preX2 = nextX2;
			preY2 = nextY2;
			preRoad = newRoad;
		}
		
//		HashMap <EntityRoad, EntityRoad>tempMap = new HashMap<EntityRoad, EntityRoad>();
//		ArrayList<EntityRoad> tempList = new ArrayList<EntityRoad>();
//		tempList.addAll(roadToRoad.keySet());
//		
//		for(int i=tempList.size()-1; i>-1; i-- ){
//			
//			tempMap.put(tempList.get(i), roadToRoad.get(tempList.get(i)));
//			
//			System.out.println("tempmap " + tempMap.get(tempList.get(i)));
//			
//			
//		}
//		roadToRoad.clear();
//		
//		roadToRoad = tempMap;
 
	
		last = preRoad;
	}
	
	@Override
	public EntityRoad getNextRoad(Boolean turn) {		
		return roadToRoad.get(this);
	}
	
	public EntityRoad getNextRoadPart(EntityRoad road, Boolean turn) {
		if (road.equals(last))
			return getExitNode().getNextRoad(this, turn);
		else
			return roadToRoad.get(road);
	}
}
