package models.stats;

public enum StatsEventType {
	CAR("Car", "Car", "Spawn"),
	BICYCLE("Bicycle", "Bicycle", "Spawn"),
	SMARTCAR("Smartcar", "Smartcar", "Spawn"),
	SMARTCAR2BICYCLE("Smartcar2Bicycle", "Smartcar & Bicycle", "Collision"),
	CAR2BYCYCLE("Car2Bicycle", "Car & Bicycle", "Collision"),
	SMARTCAR2CAR("Smartcar2Car", "Smartcar & Car", "Collision"),
	SMARTCAR2SMARTCAR("Smartcar2Smartcar", "Smartcar & Smartcar", "Collision"),
	CAR2CAR("Car2Car", "Car & Car", "Collision"),
	COLLISION_DATA("CollisionData","Collision","Data"),
	COLLISION_C2B_020("c2b_0120","Speed  1-20 kph","Cars - Bicycles"),
	COLLISION_C2B_2040("c2b_2040","Speed 20-40 kph","Cars - Bicycles"),
	COLLISION_C2B_4060("c2b_4060","Speed 40-60 kph","Cars - Bicycles"),
	COLLISION_SC2B_020("sc2b_0120","Speed  1-20 kph","Smartcars - Bicycles"),
	COLLISION_SC2B_2040("sc2b_2040","Speed 20-40 kph","Smartcars - Bicycles"),
	COLLISION_SC2B_4060("sc2b_4060","Speed 40-60 kph","Smartcars - Bicycles");
	
	private String eventType; // no spaces allowed in this string
	private String displayName;
	private String heading;
	
	StatsEventType(String eventType, String displayName, String heading) {
		this.eventType = eventType;
		this.displayName = displayName;
		this.heading = heading;
	}

	public String getEventType() {
		return eventType;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getHeading() {
		return heading;
	}
}
