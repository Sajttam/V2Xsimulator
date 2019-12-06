package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import models.SharedValues;

public class EntityRSUBoundaries extends Entity {

	private double height = SharedValues.getInstance().getRsuHeight();
	private double width = SharedValues.getInstance().getRsuWidth();
	private double xPos;
	private double yPos;
	double rsuXOffset = (SharedValues.getInstance().getRsuWidth() - SharedValues.getInstance().getNodeWidth()) / 2;
	double rsuYOffset = (SharedValues.getInstance().getRsuHeight() - SharedValues.getInstance().getNodeHeight()) / 2;

	public EntityRSUBoundaries(int xPos, int yPos) {
		this.xPos = xPos - rsuXOffset;
		this.yPos = yPos - rsuYOffset;

		setCollisionBounds((int) width, (int) height);
		setCollisionBounds(((Rectangle) getCollisionBounds()), (int) this.xPos, (int) this.yPos);


	}

	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(255, 255, 255, 50));
		g.fillRect((int)xPos,(int) yPos,(int) width,(int) height);
	}

	public boolean tryConnect(Entity vehicle) {
		Rectangle vehicleBounds = (Rectangle) vehicle.getCollisionBounds();
		return this.getCollisionBounds().intersects(vehicleBounds);
	}

}
