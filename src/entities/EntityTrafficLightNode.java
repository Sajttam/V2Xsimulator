package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.EntityRoad.RoadType;
import entities.EntityTrafficLight.LightCycle;
import models.SIScaling;

public class EntityTrafficLightNode extends EntityNode {

	private List<EntityTrafficLight> trafficLights;
	private Thread t = null;
	private SIScaling scaler = new SIScaling();

	private List<EntityTrafficLight> setVertical = new ArrayList();
	private List<EntityTrafficLight> setHorizontal = new ArrayList();

	public EntityTrafficLightNode(int x, int y) {
		super(x, y);
		setCollisionBounds(getWidth(), getHeight());
		// TODO Auto-generated constructor stub
	}

	public void generateTrafficLights() {
		trafficLights = new ArrayList<EntityTrafficLight>();

		for (EntityRoad r : getAllRoads()) {

			if (equals(r.getExitNode())) {
				EntityTrafficLight l = new EntityTrafficLight((int) (r.x2 + 8 * Math.cos(r.getAngle() + Math.PI)),
						(int) (r.y2 + 8 * Math.sin(r.getAngle() + Math.PI)));
				if (r.x2 == (int) getXPosition() || r.x2 == (int) getXPosition() + getWidth()) {

					setHorizontal.add(l);
				} else {
					l.setLightCycle(LightCycle.DRIVE);
					setVertical.add(l);
				}
				instanceCreate(l);
				trafficLights.add(l);

				if (r.straight && r.getRoadType().equals(RoadType.CAR)
						&& Arrays.asList(0.0, 180.0).contains(Math.toDegrees(r.getAngle()))) {
					addRoadReservation(r, l);

				}
			}

		}

		// t = new Thread(this);
		// t.start();
	}

	@Override
	public void addRoadReservation(EntityRoad r, EntityTrafficLight l) {

		int resHeight = (int) scaler.getPixelsFromMeter(2.5);
		EntityRoadReservation rRes;

		if (r.getAngle() == 0.0) {
			rRes = new EntityRoadReservation(r.x2 - this.getWidth() * 1.6, r.y2 - resHeight / 3, this.getWidth() * 2,
					resHeight, r.getAngle(), l);
		} else {
			rRes = new EntityRoadReservation(r.x2 - this.getWidth() * 0.4, r.y2 - resHeight / 3, this.getWidth() * 2,
					resHeight, r.getAngle(), l);

		}
		instanceCreate(rRes);

	}

	public void toggleSignal(List<EntityTrafficLight> list) {
		for (EntityTrafficLight tl : list) {
			if (tl.getLightCycle() == LightCycle.STOP) {
				tl.setLightCycle(LightCycle.DRIVE);
			} else {
				tl.setLightCycle(LightCycle.STOP);
			}
		}
	}

	public void toggleSignal(List<EntityTrafficLight> list, LightCycle l) {
		for (EntityTrafficLight tl : list) {
			tl.setLightCycle(l);
		}
	}

	int wait = 0;
	int caseTest = 0;

	@Override
	public void step() {
		if (wait == 0) {
			switch (caseTest) {
			case 0:
				toggleSignal(setVertical, LightCycle.STOP);
				wait = scaler.getStepsPerSecond() * 4;
				caseTest = 1;
				break;
			case 1:
				toggleSignal(setHorizontal, LightCycle.DRIVE);
				wait = scaler.getStepsPerSecond() * 10;
				caseTest = 2;
				break;
			case 2:
				toggleSignal(setHorizontal, LightCycle.STOP);
				wait = scaler.getStepsPerSecond() * 4;
				caseTest = 3;
				break;
			case 3:
				toggleSignal(setVertical, LightCycle.DRIVE);
				wait = scaler.getStepsPerSecond() * 10;
				caseTest = 0;
				break;
			}
		} else
			wait--;
	}

//	@Override
//	public int getWidth() {
//		return (int) (SharedValues.getInstance().getNodeWidth() * 1.5);
//	}

}
