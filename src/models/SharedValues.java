package models;

public class SharedValues {

	public int getPort() {
		return serverPort;
	}

	public void setPort(int port) {
		this.serverPort = port;
	}

	private static SharedValues sharedValues = new SharedValues();

	private int carCounter;
	private int bicycleCounter;
	private int serverPort;
	private int broadcastPort;

	private SharedValues() {
	}

	public static SharedValues getInstance() {

		return sharedValues;

	}

	public int getBroadcastPort() {
		return broadcastPort;
	}

	public void setBroadcastPort(int broadcastPort) {
		this.broadcastPort = broadcastPort;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
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

	public void decrementBicycleCounter() {

		bicycleCounter--;

	}

	public void incrementCarCounter() {

		carCounter++;

	}

	public void incrementBicycleCounter() {

		bicycleCounter++;

	}

}
