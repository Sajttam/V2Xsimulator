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
	COLLISIONDATA("CollisionData","Collision","Data");
	
	
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
