package entities;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class EntityCurvedRoad.
 */
public class EntityCurvedRoad extends EntityRoad {

	/**
	 * The Class RoadPart which defines a singular fraction of a road.
	 */
	private class RoadPart extends EntityRoad {
		
	
		protected EntityCurvedRoad first = null;
		protected EntityRoad last = null;

		/**
		 * Instantiates a new road part.
		 *
		 * @param enterNode the enter node
		 * @param exitNode the exit node
		 * @param roadType the road type
		 */
		public RoadPart(EntityNode enterNode, EntityNode exitNode, RoadType roadType) {
			super(enterNode, exitNode, roadType, false); // False is for roadspawning
			
		}

		/* 
		 * @see entities.EntityRoad#getNextRoad(java.lang.Boolean)
		 */
		@Override
		public EntityRoad getNextRoad(Boolean turn) {
			return first.getNextRoadPart(this, turn);
		}

		/* 
		 * @see entities.EntityRoad#toString()
		 */
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("RoadPart: " + "x1:" + getXPosition() + " y1:" + getYPosition() + " x2:" + x2 + " y2:" + y2);
			return s.toString();
		}

	}

	
	private Map<EntityRoad, EntityRoad> roadToRoad;
	private EntityRoad first = null;
	private EntityRoad last = null;
	private double startAngle;
	private double exitAngle;
	private double standardRadius;

	/**
	 * Instantiates a new entity curved road.
	 *
	 * @param enterNode the enter node
	 * @param exitNode the exit node
	 * @param roadType the road type
	 * @param startAngle the start angle
	 * @param exitAngle the exit angle
	 */
	public EntityCurvedRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType, double startAngle,
			double exitAngle) {
		super(enterNode, exitNode, roadType, false); // False is for roadspawning
		this.startAngle = startAngle;
		this.exitAngle = exitAngle;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see entities.EntityRoad#setPosition(double, double, double, double)
	 */
	@Override
	public void setPosition(double x1, double y1, double x2, double y2) {

		double origoX;
		double origoY;
		double preX2 = x1;
		double preY2 = y1;
		double nextX2;
		double nextY2;

		int numPoints = 20;
		double angleDiff;
		roadToRoad = new LinkedHashMap<EntityRoad, EntityRoad>();

		// super.setPosition(x1,y1,(int) (origoX + (x2-x1) * Math.cos(angleDiff*1)),
		// (int) (origoY + (y2-y1) * Math.sin(angleDiff*1)));
		first = this;

		setXPosition(x1);
		setYPosition(y1);
		this.x2 = x1;
		this.y2 = y1;

		EntityRoad preRoad = this;

		angle = startAngle;
		startAngle = startAngle * 180 / Math.PI;
		exitAngle = exitAngle * 180 / Math.PI;

		for (int i = 1; i <= numPoints; i++) {
			RoadPart newRoad = new RoadPart(null, null, getRoadType());

			if (startAngle == 0 && exitAngle == 90) { // done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (origoX - Math.abs(x1 - x2) * Math.sin(angleDiff * i));
				nextY2 = (origoY - Math.abs(y1 - y2) * Math.cos(angleDiff * i));
				straight = false;
			} else if (startAngle == 90 && exitAngle == 180) {

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (origoX + Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				straight = false;

			} else if (startAngle == 180 && exitAngle == -90) {// done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (origoX - Math.abs(x2 - x1) * Math.sin(-angleDiff * i));
				nextY2 = (origoY + Math.abs(y2 - y1) * Math.cos(angleDiff * i));
				straight = false;
			} else if (startAngle == -90 && exitAngle == 0) {// done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (origoX - Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (origoY + Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				straight = false;

			} else if (startAngle == 0 && exitAngle == -90) { // done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (origoX - Math.abs(x2 - x1) * Math.sin(angleDiff * i));
				nextY2 = (origoY - (y2 - y1) * Math.cos(angleDiff * i));
				straight = false;
				leftCurve = true;

			} else if (startAngle == -90 && exitAngle == 180) { // done

				angleDiff = Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (origoX + Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				straight = false;
				leftCurve = true;

			} else if (startAngle == 180 && exitAngle == 90) { // done

				angleDiff = Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (origoX - Math.abs(x2 - x1) * Math.sin(angleDiff * i));
				nextY2 = (origoY - Math.abs(y2 - y1) * Math.cos(angleDiff * i));
				straight = false;
				leftCurve = true;

			} else if (startAngle == 90 && exitAngle == 0) { // done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (origoX - Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				straight = false;
				leftCurve = true;

			} else if (startAngle == 0) {

				nextX2 = x1 + i * (Math.abs(x2 - x1) / numPoints);
				nextY2 = y2;

			} else {

				nextX2 = x1 - i * (Math.abs(x2 - x1) / numPoints);
				nextY2 = y2;

			}

//			if(Math.abs(x1 -nextX2) > Math.abs(x1-x2)) {
//
//				
//				nextX2 = x2;
//				
//			}
//	
//			
//			if(Math.abs(y1 -nextY2) > Math.abs(y1-y2)) {
//				
//	
//				nextY2 = y2;
//				
//			}

			newRoad.setPosition(preX2, preY2, nextX2, nextY2);
			newRoad.straight = this.straight;
			newRoad.leftCurve = this.leftCurve;
			newRoad.first = this;
			newRoad.setSpeedLimit(getSpeedLimit());

			getEntryNode().instanceCreate(newRoad);

			roadToRoad.put(preRoad, newRoad);

			preX2 = nextX2;
			preY2 = nextY2;
			preRoad = newRoad;
		}

		last = preRoad;

	}

	/* (non-Javadoc)
	 * @see entities.EntityRoad#getNextRoad(java.lang.Boolean)
	 */
	@Override
	public EntityRoad getNextRoad(Boolean turn) {
		return roadToRoad.get(this);
	}

	/**
	 * Gets the next road part.
	 *
	 * @param road the road
	 * @param turn the turn
	 * @return the next road part
	 */
	public EntityRoad getNextRoadPart(EntityRoad road, Boolean turn) {
		if (road.equals(last))
			return getExitNode().getNextRoad(this, turn);
		else
			return roadToRoad.get(road);
	}

	/* (non-Javadoc)
	 * @see entities.EntityRoad#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("EntityCurvedRoad: " + "x1:" + getXPosition() + " y1:" + getYPosition() + " x2:" + x2 + " y2:" + y2);

		EntityRoad child = getNextRoad(false);

		while (child != null) {
			s.append("\n\t" + child.toString() + "");
			child = child.getNextRoad(false);
		}

		return s.toString();
	}

}
