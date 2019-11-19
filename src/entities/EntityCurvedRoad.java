package entities;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import entities.EntityNode.Direction;

public class EntityCurvedRoad extends EntityRoad {
	
	private class RoadPart extends EntityRoad {
		protected EntityCurvedRoad first = null;
		protected EntityRoad last = null;
		
		public RoadPart(EntityNode enterNode, EntityNode exitNode, RoadType roadType) {
			super(enterNode, exitNode, roadType);
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
		super(enterNode, exitNode, roadType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {		
		double origoX = x1;
		double origoY = y2;		
		int numPoints = 10;
		double angleDiff = -1*Math.PI/(4.0 * numPoints);
		roadToRoad = new HashMap<EntityRoad, EntityRoad>();
		
		super.setPosition(x1,y1,(int) (origoX + (x2-x1) * Math.cos(angleDiff*1)), (int) (origoY + (y2-y1) * Math.sin(angleDiff*1)));
		first = this;
		
		int preX2 = this.x2;
		int preY2 = this.y2;
		EntityRoad preRoad = this;
		/*for (int i = 2; i <= numPoints; i++) {
			RoadPart newRoad = new RoadPart(null, null, getRoadType());
			
			int nextX2 = (int) (origoX + (x2-x1) * Math.cos(angleDiff * i));
			int nextY2 = (int) (origoY + (y2-y1) * Math.sin(angleDiff * i));;
			newRoad.setPosition(preX2, preY2, nextX2, nextY2);
			newRoad.first = this;
			roadToRoad.put(preRoad, newRoad);
			//getEntryNode().instanceCreate(newRoad);
			preX2 = nextX2;
			preY2 = nextY2;
			preRoad = newRoad;
		}*/
		
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
