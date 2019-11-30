package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StatisticsObserver extends Entity implements PropertyChangeListener {

	private PropertyChangeSupport propertyChangeSupportCounter;

	private StatisticsObserver() {
	}

	private static final StatisticsObserver collisionObserver = new StatisticsObserver();
	private int car, smartCar, bike, car2Bicycle, smartcar2Bicycle, smartcar2Car, smartcar2Smartcar, car2Car;

	public static StatisticsObserver getInstance() {
		return collisionObserver;
	}

	@Override
	public void addObserver(PropertyChangeListener listener) {
		if (propertyChangeSupportCounter == null)
			propertyChangeSupportCounter = new PropertyChangeSupport(this);
		propertyChangeSupportCounter.addPropertyChangeListener(listener);
	}

	public void castPropertyChange(String eventname, int change) {
		propertyChangeSupportCounter.firePropertyChange(eventname, null, change);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String eventName = event.getPropertyName();
		switch (eventName) {
		case "Car":
			car++;
			castPropertyChange(eventName, car);
			break;
		case "Smartcar":
			smartCar++;
			castPropertyChange(eventName, smartCar);
			break;
		case "Bicycle":
			bike++;
			castPropertyChange(eventName, bike);
			break;
		case "Smartcar2Bicycle":
			smartcar2Bicycle++;
			castPropertyChange(eventName, smartcar2Bicycle);
			break;
		case "Car2Bicycle":
			car2Bicycle++;
			castPropertyChange(eventName, car2Bicycle);
			break;
		case "Smartcar2Car":
			smartcar2Car++;
			castPropertyChange(eventName, smartcar2Car);
			break;
		case "Smartcar2Smartcar":
			smartcar2Smartcar++;
			castPropertyChange(eventName, smartcar2Smartcar);
			break;
		case "Car2Car":
			car2Car++;
			castPropertyChange(eventName, car2Car);
			break;
		}
	}

}
