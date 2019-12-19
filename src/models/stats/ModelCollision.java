package models.stats;

import controller.Controller;

public class ModelCollision {
	private String statsEventType;
	private String vehicleFirstType;
	private double vehicleFirstSpeed;
	private long vehicleFirstSpawnTime;
	
	private String vehicleOtherType;
	private double vehicleOtherSpeed;
	private long vehicleOtherSpawnTime;

	private long timeStamp;

	public ModelCollision(String statsEventType, String vehicleFirstType, double vehicleFirstSpeed,
			String vehicleOtherType, double vehicleOtherSpeed) {
		setStatsEventType(statsEventType);
		setVehicleFirstType(vehicleFirstType);
		setVehicleFirstSpeed(vehicleFirstSpeed);
		setVehicleOtherType(vehicleOtherType);
		setVehicleOtherSpeed(vehicleOtherSpeed);
		setTimeStamp(Controller.GLOBAL.getTimeStamp());
	}

	public ModelCollision() {
		setTimeStamp(Controller.GLOBAL.getTimeStamp());
	}

	public String getStatsEventType() {
		return statsEventType;
	}

	public void setStatsEventType(String statsEventType) {
		this.statsEventType = statsEventType;
	}

	public String getVehicleFirstType() {
		return vehicleFirstType;
	}

	public void setVehicleFirstType(String vehicleFirstType) {
		this.vehicleFirstType = vehicleFirstType;
	}

	public double getVehicleFirstSpeed() {
		return vehicleFirstSpeed;
	}

	public void setVehicleFirstSpeed(double vehicleFirstSpeed) {
		this.vehicleFirstSpeed = vehicleFirstSpeed;
	}

	public String getVehicleOtherType() {
		return vehicleOtherType;
	}

	public void setVehicleOtherType(String vehicleOtherType) {
		this.vehicleOtherType = vehicleOtherType;
	}

	public double getVehicleOtherSpeed() {
		return vehicleOtherSpeed;
	}

	public void setVehicleOtherSpeed(double vehicleOtherSpeed) {
		this.vehicleOtherSpeed = vehicleOtherSpeed;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public static String getTsvHeadings() {
		StringBuilder s = new StringBuilder();
		s.append("First type\t");
		s.append("First speed\t");
		s.append("First spawn time\t");
		s.append("Other type\t");
		s.append("Other speed\t");
		s.append("Other spawn time\t");
		s.append("Time stamp");
		return s.toString();
	}
	
	public String toTsvString() {
		StringBuilder s = new StringBuilder();
		s.append(getVehicleFirstType() + "\t");
		s.append(getVehicleFirstSpeed() + "\t");
		s.append(getVehicleFirstSpawnTime() + "\t");
		s.append(getVehicleOtherType() + "\t");
		s.append(getVehicleOtherSpeed() + "\t");
		s.append(getVehicleOtherSpawnTime() + "\t");
		s.append(getTimeStamp());
		return s.toString();
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("_FIRST_ type:" + getVehicleFirstType());
		s.append(" speed:" + getVehicleFirstSpeed());
		
		s.append("_SECOND_ type:" + getVehicleOtherType());
		s.append(" speed:" + getVehicleOtherSpeed());
		
		s.append(" timeStamp:" +getTimeStamp());
		
		return s.toString();
	}

	public long getVehicleFirstSpawnTime() {
		return vehicleFirstSpawnTime;
	}

	public void setVehicleFirstSpawnTime(long vehicleFirstSpawnTime) {
		this.vehicleFirstSpawnTime = vehicleFirstSpawnTime;
	}

	public long getVehicleOtherSpawnTime() {
		return vehicleOtherSpawnTime;
	}

	public void setVehicleOtherSpawnTime(long vehicleOtherSpawnTime) {
		this.vehicleOtherSpawnTime = vehicleOtherSpawnTime;
	}
}
