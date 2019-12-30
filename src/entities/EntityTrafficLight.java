package entities;

import java.awt.Color;
import java.awt.Graphics;

public class EntityTrafficLight extends Entity{
	
	enum LightCycle {STOP, DRIVE, SLOW_DOWN}
	private LightCycle lightCycle = LightCycle.STOP;
	

	private int offsetX = -4;
	private int offsetY = -4;
	
	public EntityTrafficLight (int x, int y) {
		super(1,1);
		setXPosition(x);
		setYPosition(y);
		setCollisionBounds(1, 1, offsetX, offsetY);
		setLightCycle(LightCycle.STOP);
	}
	/**
	 * Returns the status of the trafficlight.
	 * @return Returns the status : STOP, DRIVE or SLOW_DOWN
	 */
	public LightCycle getLightCycle() {
		return lightCycle;
	}
	/**
	 * Makes the trafficlight a collidable object when its
	 * status is set as STOP.
	 * this to make vehicle able to recognise them and
	 * stop when neccesary
	 * @param lightCycle
	 */
	public void setLightCycle(LightCycle lightCycle) {
		this.lightCycle = lightCycle;
		
		switch (lightCycle) {
		case STOP:
	
				setCollisionBounds(1, 1, offsetX, offsetY);
	
			
			break;
		case DRIVE:
			setCollisionBounds(0, 0, 0, 0);
			break;
		case SLOW_DOWN:
		default:
			break;
		}
	}
	
	public void draw(Graphics g) {
		switch (lightCycle) {
		case SLOW_DOWN:
			g.setColor(Color.YELLOW);
			break;
		case STOP:
			g.setColor(Color.RED);
			break;
		case DRIVE:
			g.setColor(Color.GREEN);
			break;
		default:
			g.setColor(Color.WHITE);
			break;
		}
		g.fillOval((int)getXPosition()+offsetX, (int)getYPosition()+offsetY, 8, 8);
	}
}
