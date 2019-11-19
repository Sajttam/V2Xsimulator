package entities;

import java.awt.Color;
import java.awt.Graphics;

public class EntityTrafficLight extends Entity{
	
	enum LightCycle {STOP, DRIVE, SLOW_DOWN}
	private LightCycle lightCycle = LightCycle.STOP;
	

	private int offsetX = -4;
	private int offsetY = -4;
	
	public EntityTrafficLight (int x, int y) {
		super(8,8);
		setXPosition(x);
		setYPosition(y);
		setCollisionBounds(8, 8, offsetX, offsetY);
		setLightCycle(LightCycle.STOP);
	}
	
	public LightCycle getLightCycle() {
		return lightCycle;
	}

	public void setLightCycle(LightCycle lightCycle) {
		this.lightCycle = lightCycle;
		switch (lightCycle) {
		case STOP:
			setCollisionBounds(8, 8, offsetX, offsetY);
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
