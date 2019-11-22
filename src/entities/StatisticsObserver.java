package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StatisticsObserver extends Entity implements PropertyChangeListener  {
	
	private PropertyChangeSupport propertyChangeSupportCounter;
	private StatisticsObserver() {}
	private static final StatisticsObserver collisionobserver = new StatisticsObserver();
	private int car,bike,collision;
	
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
		case "CAR CREATED":
			car ++;
			castPropertyChange(eventname, car);
			break;
		case "BICYCLE CREATED":
			bike ++;
			castPropertyChange(eventname, bike);
			break;
		case "COLLISION":
			collision ++;
			castPropertyChange(eventname, collision);
			break;
		}
	}
	
}
