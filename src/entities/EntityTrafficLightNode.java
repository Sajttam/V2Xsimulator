package entities;

import java.util.ArrayList;
import java.util.List;

import entities.EntityTrafficLight.LightCycle;

public class EntityTrafficLightNode extends EntityNode implements Runnable {

	private List<EntityTrafficLight> trafficLights;
	private Thread t = null;
	
	private List<EntityTrafficLight> setVertical = new ArrayList();
	private List<EntityTrafficLight> setHorizontal = new ArrayList();

	public EntityTrafficLightNode(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public void generateTrafficLights() {
		trafficLights = new ArrayList<EntityTrafficLight>();

		for (EntityRoad r : getAllRoads()) {

			if (equals(r.getExitNode())) {
				EntityTrafficLight l = new EntityTrafficLight((int)(r.x2 + 8 * Math.cos(r.getAngle()+Math.PI)), (int)(r.y2 + 8 * Math.sin(r.getAngle()+Math.PI)));
				if (r.x2 == (int) getXPosition() || r.x2 == (int) getXPosition() + getWidth()) {
					
					setHorizontal.add(l);
				}
				else {
					l.setLightCycle(LightCycle.DRIVE);
					setVertical.add(l);
				}
				instanceCreate(l);
				trafficLights.add(l);
			}
		}

		t = new Thread(this);
		t.start();
	}
	
	public void toggleSignal(List<EntityTrafficLight> list) {
		for (EntityTrafficLight tl : list) {
			if (tl.getLightCycle() == LightCycle.STOP) {
				tl.setLightCycle(LightCycle.DRIVE);
			}
			else {
				tl.setLightCycle(LightCycle.STOP);
			}
		}
	}
	
	public void toggleSignal(List<EntityTrafficLight> list, LightCycle l) {
		for (EntityTrafficLight tl : list) {
			tl.setLightCycle(l);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				t.sleep(5000);				
				
				toggleSignal(setVertical, LightCycle.STOP);	
				
				t.sleep(2000);
				
				toggleSignal(setHorizontal, LightCycle.DRIVE);
				
				t.sleep(5000);
				
				toggleSignal(setHorizontal, LightCycle.STOP);
				
				t.sleep(2000);
				
				toggleSignal(setVertical, LightCycle.DRIVE);
				
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
