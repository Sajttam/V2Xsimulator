package entities;

import java.util.ArrayList;
import java.util.List;

import entities.EntityTrafficLight.LightCycle;

public class EntityTrafficLightNode extends EntityNode implements Runnable {

	private List<EntityTrafficLight> trafficLights;
	private Thread t = null;

	public EntityTrafficLightNode(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public void generateTrafficLights() {
		trafficLights = new ArrayList<EntityTrafficLight>();

		for (EntityRoad r : getAllRoads()) {

			if (equals(r.getExitNode())) {
				EntityTrafficLight l = new EntityTrafficLight(r.x2, r.y2);
				if (r.x2 == (int) getXPosition() || r.x2 == (int) getXPosition() + getWidth())
					l.setLightCycle(LightCycle.DRIVE);
				instanceCreate(l);
				trafficLights.add(l);
			}
		}

		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (EntityTrafficLight tl : trafficLights) {
					if (tl.getLightCycle() == LightCycle.STOP)
						tl.setLightCycle(LightCycle.DRIVE);
					else
						tl.setLightCycle(LightCycle.STOP);
				}
				t.sleep(6000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
