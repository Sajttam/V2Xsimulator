package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CollisionObserver extends Entity implements PropertyChangeListener  {
	
	private CollisionObserver() {}
	private static final CollisionObserver collisionobserver = new CollisionObserver();
	private int car,bike,collision;
	
	public static CollisionObserver getInstance() {
		return collisionobserver;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String eventname = event.getPropertyName();
		switch(eventname) {
		case "CAR CREATED":
			car ++;
			System.out.println(eventname + " " + car);
			break;
		case "BICYCLE CREATED":
			bike ++;
			System.out.println(eventname + " " + bike);
			break;
		case "COLLISION":
			collision ++;
			System.out.println(eventname + " " + collision);
			break;
		}
	}
	
}
