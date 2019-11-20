package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EntityCar extends EntityVehicle{

	private boolean smartVehicle = false;
	private Polygon visionArea;
	
	
	public EntityCar(EntityRoad road,PropertyChangeListener listener) {
		super(road,listener);
		setSpeed(1.25);
		setCollisionBounds(16, 16);
		setCollisionBounds(getCollisionBounds(), -8, -8);
		smartVehicle = Math.random() > 0.8 ? true : false;	
		super.castPropertyChange("CAR CREATED");
	}
	
	public Polygon getVisionArea() {
		if (visionArea == null) {
			visionArea = new Polygon();
			
		}
		return visionArea;		
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
			g.setColor(Color.GREEN);
			if (smartVehicle) {
				g.setColor(Color.BLUE);
			}
			g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);
			
			int alpha = 127; // 50% transparent
			Color hitBoxYellow = new Color(235, 229, 52, alpha);
			g.setColor(hitBoxYellow);
			
			//g.drawPolygon(visionArea);
	}
	
}
