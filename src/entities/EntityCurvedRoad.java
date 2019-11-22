package entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import entities.EntityNode.Direction;
import javafx.scene.effect.Light.Point;

public class EntityCurvedRoad extends EntityRoad {

	private class RoadPart extends EntityRoad {
		protected EntityCurvedRoad first = null;
		protected EntityRoad last = null;

		public RoadPart(EntityNode enterNode, EntityNode exitNode, RoadType roadType) {
			super(enterNode, exitNode, roadType, false); // False is for roadspawning
			// TODO Auto-generated constructor stub
		}

		public EntityRoad getNextRoad(Boolean turn) {
			return first.getNextRoadPart(this, turn);
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("RoadPart: " + "x1:" + getXPosition() + " y1:" +getYPosition() + " x2:" + x2 + " y2:" +y2);
			return s.toString();
		}

	}

	private Map<EntityRoad, EntityRoad> roadToRoad;
	private EntityRoad first = null;
	private EntityRoad last = null;
	private double startAngle;
	private double exitAngle;
	private double standardRadius;

	public EntityCurvedRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType, double startAngle,
			double exitAngle) {
		super(enterNode, exitNode, roadType, false); // False is for roadspawning
		this.startAngle = startAngle;
		this.exitAngle = exitAngle;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {

		double origoX;
		double origoY;
		int preX2 = x1;
		int preY2 = y1;
		int nextX2;
		int nextY2;

		int numPoints = 10;
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
				nextX2 = (int) (origoX - Math.abs(x1 - x2) * Math.sin(angleDiff * i));
				nextY2 = (int) (origoY - Math.abs(y1 - y2) * Math.cos(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = false;

			} else if (startAngle == 90 && exitAngle == 180) {

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (int) (origoX + Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (int) (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = false;

			} else if (startAngle == 180 && exitAngle == -90) {// done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (int) (origoX - Math.abs(x2 - x1) * Math.sin(-angleDiff * i));
				nextY2 = (int) (origoY + Math.abs(y2 - y1) * Math.cos(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = false;
			} else if (startAngle == -90 && exitAngle == 0) {// done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (int) (origoX - Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (int) (origoY + Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = false;
			} else if (startAngle == 0 && exitAngle == -90) { // done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (int) (origoX - Math.abs(x2 - x1) * Math.sin(angleDiff * i));
				nextY2 = (int) (origoY - (y2 - y1) * Math.cos(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = true;

			} else if (startAngle == -90 && exitAngle == 180) { // done

				angleDiff = Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (int) (origoX + Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (int) (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = true;

			} else if (startAngle == 180 && exitAngle == 90) { // done

				angleDiff = Math.PI / (2 * numPoints);

				origoX = x1;
				origoY = y2;
				nextX2 = (int) (origoX - Math.abs(x2 - x1) * Math.sin(angleDiff * i));
				nextY2 = (int) (origoY - Math.abs(y2 - y1) * Math.cos(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = true;
			} else if (startAngle == 90 && exitAngle == 0) { // done

				angleDiff = -Math.PI / (2 * numPoints);

				origoX = x2;
				origoY = y1;
				nextX2 = (int) (origoX - Math.abs(x2 - x1) * Math.cos(angleDiff * i));
				nextY2 = (int) (origoY - Math.abs(y2 - y1) * Math.sin(angleDiff * i));
				newRoad.straight = false;
				newRoad.leftCurve = true;

			} else if (startAngle == 0) {

				nextX2 = x1 + i * (Math.abs(x2 - x1) / numPoints);
				nextY2 = y2;
				newRoad.straight = true;
				newRoad.leftCurve = false;

			} else {

				nextX2 = x1 - i * (Math.abs(x2 - x1) / numPoints);
				nextY2 = y2;
				newRoad.straight = true;
				newRoad.leftCurve = false;

			}

			newRoad.setPosition(preX2, preY2, nextX2, nextY2);

			newRoad.first = this;

			getEntryNode().instanceCreate(newRoad);

			roadToRoad.put(preRoad, newRoad);

			preX2 = nextX2;
			preY2 = nextY2;
			preRoad = newRoad;
		}

		last = preRoad;
		
		System.out.println(this);
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
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("EntityCurvedRoad: " + "x1:" + getXPosition() + " y1:" +getYPosition() + " x2:" + x2 + " y2:" +y2);
		
		EntityRoad child = getNextRoad(false);
		
		while (child != null) {
			s.append("\n\t" + child.toString() + "");
			child = child.getNextRoad(false);
		}
		
		return s.toString();
	}

}
