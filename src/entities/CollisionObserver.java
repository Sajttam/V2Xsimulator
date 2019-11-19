package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CollisionObserver extends Entity implements PropertyChangeListener  {
	
	private PropertyChangeSupport propertyChangeSupportCounter;
	private CollisionObserver() {}
	private static final CollisionObserver collisionobserver = new CollisionObserver();
	private int car,bike,collision;
	
	public static CollisionObserver getInstance() {
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
			System.out.println(eventname + " " + car);
			break;
		case "BICYCLE CREATED":
			bike ++;
			castPropertyChange(eventname, bike);
			System.out.println(eventname + " " + bike);
			break;
		case "COLLISION":
			collision ++;
			castPropertyChange(eventname, collision);
			System.out.println(eventname + " " + collision);
			break;
		}
	}
	
}
