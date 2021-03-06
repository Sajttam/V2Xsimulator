package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import V2XServer.RSUServerUDP;
import entities.Entity;
import entities.EntityBicycle;

public class SharedValues {

	private static SharedValues sharedValues = new SharedValues();

	private SIScaling scaler = new SIScaling();
	private int carCounter;// Amount of cars not affected
	private int smartCarCounter; // Amount of smartcars

	private int bicycleCounter; // Amount of bicycles
	private int serverPort; // Portnumber for servercommunication
	private double timeOutValue;

	private volatile long stepsEpic = 0;

	private ArrayList<Integer> serverPortNumbers = new ArrayList<Integer>();
	private double carMaxSpeed = scaler.kphToPixelsPerStep(50); // Maximum velocity a car can travel
	private double bicycleMaxSpeed = scaler.kphToPixelsPerStep(20); // Maximum velocity a bike can travel
	private double nodeHeight = scaler.getPixelsFromMeter(15.5);
	private double nodeWidth = scaler.getPixelsFromMeter(15.5);
	private double rsuHeight = nodeHeight * 2; 	// Grapihcal dimensions of area where RSU is active
	private double rsuWidth = nodeWidth * 2;	// Grapihcal dimensions of area where RSU is active
	private List<RSUServerUDP> availableRSUs = new ArrayList<RSUServerUDP>();
	private boolean showFieldOfView = true;
	private int serverDelayMiliseconds = 0;

	private SharedValues() {

	}

	public static SharedValues getInstance() {
		return sharedValues;
	}

	public int getServerPortNumber() {

		Random rand = new Random();

		int portNo = (rand.nextInt(1000) + 1000); // returns an integer between 1000 and 2000

		if (!serverPortNumbers.contains(portNo)) {

			serverPortNumbers.add(portNo);
			return portNo;

		} else {

			return getServerPortNumber();
		}

	}

	public List<RSUServerUDP> getConnectionAreas() {
		return availableRSUs;
	}

	public void addRSU(RSUServerUDP entity) {
		this.availableRSUs.add(entity);
	}

	public int getCarCounter() {
		return carCounter;
	}

	public void setCarCounter(int carCounter) {
		this.carCounter = carCounter;
	}

	public int getBicycleCounter() {
		return bicycleCounter;
	}

	public void setBicycleCounter(int bicycleCounter) {
		this.bicycleCounter = bicycleCounter;
	}

	public void decrementCarCounter() {
		carCounter--;
	}

	public void decrementSmartCarCounter() {
		smartCarCounter--;
	}

	public void decrementBicycleCounter() {
		bicycleCounter--;
	}

	public void incrementCarCounter() {
		carCounter++;
	}

	public void incrementSmartCarCounter() {
		smartCarCounter++;
	}

	public void incrementBicycleCounter() {
		bicycleCounter++;
	}

	public double getTimeOutValue() {
		return timeOutValue;
	}

	public void setTimeOutValue(double timeOutValue) {
		// if (timeOutValue < 0) throw new Exception ("");
		this.timeOutValue = timeOutValue;
	}

	public int getBroadcastPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTimeStamp() {
		return stepsEpic;
	}

	public void incStepsEpic() {
		this.stepsEpic++;
		if (stepsEpic == Long.MAX_VALUE)
			stepsEpic = 0;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}

	public double getNodeHeight() {
		return nodeHeight;
	}

	public void setNodeHeight(int nodeHeight) {
		this.nodeHeight = nodeHeight;
	}

	public double getNodeWidth() {
		return nodeWidth;
	}

	public void setNodeWidth(int nodeWidth) {
		this.nodeWidth = nodeWidth;
	}

	public double getRsuHeight() {
		return rsuHeight;
	}

	public void setRsuHeight(int rsuHeight) {
		this.rsuHeight = rsuHeight;
	}

	public double getRsuWidth() {
		return rsuWidth;
	}

	public void setRsuWidth(int rsuWidth) {
		this.rsuWidth = rsuWidth;
	}

	public double getMaxSpeed(Entity entity) {

		if (entity instanceof EntityBicycle)
			return bicycleMaxSpeed;
		else {
			return carMaxSpeed;
		}
	}
	/**
	 * Gives the max velocity for a given vehicle type
	 * @param vehicle, Available are BYCYCLE and CAR
	 * @return Returns the maximum velocity
	 */
	public double getMaxSpeed(String vehicle) {

		if (vehicle.equals("BICYCLE"))
			return bicycleMaxSpeed;
		else if ((vehicle.equals("CAR"))) {
			return carMaxSpeed;
		} else {

			return carMaxSpeed;
		}
	}

	public boolean isShowFieldOfView() {
		return showFieldOfView;
	}

	public void setShowFieldOfView(boolean showFieldOfView) {
		this.showFieldOfView = showFieldOfView;
	}

	public int getServerDelayMiliseconds() {
		return serverDelayMiliseconds;
	}

	public void setServerDelayMiliseconds(int serverDelayMiliseconds) {
		this.serverDelayMiliseconds = serverDelayMiliseconds;
	}

	public void setSmartCarCounter(int i) {
		smartCarCounter = i;

	}

	public int getSmartCarCounter() {

		return smartCarCounter;
	}

}
