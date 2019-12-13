package models.stats;

import controller.Controller;

public class ModelCollision {
	private String statsEventType;
	private String vehicleFirstType;
	private double vehicleFirstSpeed;

	private String vehicleOtherType;
	private double vehicleOtherSpeed;

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
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("_FIRST_ type:" + getVehicleFirstType());
		s.append(" speed:" + getVehicleFirstSpeed());
		
		s.append("_SECOND_ type:" + getVehicleOtherType());
		s.append(" speed:" + getVehicleOtherSpeed());
		
		s.append(" timeStamp:" +getTimeStamp());
		
		return s.toString();
	}
}
