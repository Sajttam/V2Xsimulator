package models;

import java.util.ArrayList;
import java.util.Random;

public class SharedValues {

	private static SharedValues sharedValues = new SharedValues();

	private int carCounter;
	private int bicycleCounter;
	private int serverPort;
	private double timeOutValue;
	private double SMARTCAR_CHANCE;
	private volatile long stepsEpic = 0;
	private ArrayList<Integer> carPortNumbers = new ArrayList<Integer>();


	private SharedValues() {
	}

	public static SharedValues getInstance() {

		return sharedValues;

	}

	public int getPortNumber() {

		Random rand = new Random();

		int portNo = (rand.nextInt(64534) + 1001); // returns an integer between 1001 and 65535

		if (!carPortNumbers.contains(portNo)) {

			carPortNumbers.add(portNo);
			return portNo;

		} else {

			return getPortNumber();

		}

	}

	public void removePortNumber(int portNo) {

		carPortNumbers.remove(carPortNumbers.indexOf(portNo));

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
		// if (timeOutValue < 0) throw new Exception("");
		this.timeOutValue = timeOutValue;
	}
	
	public double getSMARTCAR_CHANCE() {
		return SMARTCAR_CHANCE;
	}

	public void setSMARTCAR_CHANCE(double sMARTCAR_CHANCE) {
		SMARTCAR_CHANCE = sMARTCAR_CHANCE;
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
}
