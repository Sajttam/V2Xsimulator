package models;

public class SharedValues {
	
	private static SharedValues sharedValues = new SharedValues();
	
	private int carCounter;
	private int bicycleCounter;
	
	
	
	private SharedValues() {
		
		
	
	}
	
	public static SharedValues getInstance(){
		
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
	
	
	
	

}
