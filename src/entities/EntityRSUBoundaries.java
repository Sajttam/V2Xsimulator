package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import models.SharedValues;

public class EntityRSUBoundaries extends Entity {

	private int height = SharedValues.getInstance().getRsuHeight();
	private int width = SharedValues.getInstance().getRsuWidth();
	private int xPos;
	private int yPos;
	int rsuXOffset = (SharedValues.getInstance().getRsuWidth() - SharedValues.getInstance().getNodeWidth()) / 2;
	int rsuYOffset = (SharedValues.getInstance().getRsuHeight() - SharedValues.getInstance().getNodeHeight()) / 2;

	public EntityRSUBoundaries(int xPos, int yPos) {
		this.xPos = xPos - rsuXOffset;
		this.yPos = yPos - rsuYOffset;
		setCollisionBounds(width, height);
		setCollisionBounds(getCollisionBounds(), this.xPos, this.yPos);

	}

	@Override
	public void draw(Graphics g) {

		g.setColor(new Color(255, 255, 255, 100));

		g.fillRect(xPos, yPos, width, height);
	}

	public boolean tryConnect(Entity vehicle) {

		Rectangle vehicleBounds = vehicle.getCollisionBounds();

		if (this.getCollisionBounds().intersects(vehicleBounds)) {
			return true;
		} else {

			return false;

		}

	}

}
