package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StatisticsObserver extends Entity implements PropertyChangeListener  {
	
	private PropertyChangeSupport propertyChangeSupportCounter;
	private StatisticsObserver() {}
	private static final StatisticsObserver collisionobserver = new StatisticsObserver();
	private int car,smartcar,bike,car2Bicycle,smartcar2Bicycle,smartcar2Car,smartcar2Smartcar,car2Car;
	
	public static StatisticsObserver getInstance() {
		return collisionobserver;
	}
	
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
		String eventname = event.getPropertyName();
		switch(eventname) {
		case "Car":
			car ++;
			castPropertyChange(eventname, car);
			break;
		case "Smartcar":
			smartcar ++;
			castPropertyChange(eventname, smartcar);
			break;
		case "Bicycle":
			bike ++;
			castPropertyChange(eventname, bike);
			break;
		case "Smartcar2Bicycle":
			smartcar2Bicycle ++;
			castPropertyChange(eventname, smartcar2Bicycle);
			break;
		case "Car2Bicycle":
			car2Bicycle++;
			castPropertyChange(eventname, car2Bicycle);
			break;
		case "Smartcar2Car":
			smartcar2Car ++;
			castPropertyChange(eventname, smartcar2Car);
			break;
		case "Smartcar2Smartcar":
			smartcar2Smartcar ++;
			castPropertyChange(eventname, smartcar2Smartcar);
			break;
		case "Car2Car":
			car2Car ++;
			castPropertyChange(eventname, car2Car);
			break;
		}
	}
	
}
