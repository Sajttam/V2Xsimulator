package tests;

import entities.EntityCurvedRoad;
import entities.EntityRoad.RoadType;

public class testCurvedRoads {
	public static void main (String[] args) {
		EntityCurvedRoad r = new EntityCurvedRoad(null, null, RoadType.CAR, 0,Math.PI/2);
		r.setPosition(32, 32, 128, 128);
		
		System.out.println(r.toString());
	}
}
