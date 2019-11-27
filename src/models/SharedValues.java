package models;

public class SharedValues {

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private static SharedValues sharedValues = new SharedValues();

	private int carCounter;
	private int bicycleCounter;
	private int port;
	private double timeOutValue;
	private volatile long stepsEpic = 0;

	private SharedValues() {
	}

	public static SharedValues getInstance() {

		return sharedValues;

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

	public double getTimeOutValue() {
		return timeOutValue;
	}

	public void setTimeOutValue(double timeOutValue) {
		//if (timeOutValue < 0) throw new Exception("");
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
}
