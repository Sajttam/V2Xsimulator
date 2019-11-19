package entities;

import java.awt.Color;
import java.awt.Graphics;

public class EntityCar extends EntityVehicle{

	private boolean smartVehicle = false;
	
	public EntityCar(EntityRoad road) {
		super(road);
		setSpeed(1.25);
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		smartVehicle = Math.random() > 0.8 ? true : false;	
	}
	
	
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
			g.setColor(Color.GREEN);
			if (smartVehicle) {
				g.setColor(Color.BLUE);
			}
			g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);
	}
	
}