package entities;

import java.awt.Color;
import java.awt.Graphics;

import models.SharedValues;

public class EntityRSUBoundaries extends Entity {

	private int height = SharedValues.getInstance().getRsuHeight();
	private int width = SharedValues.getInstance().getRsuWidth();
	private int xPos;
	private int yPos;

	public EntityRSUBoundaries(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		setCollisionBounds(height, width);
		setCollisionBounds(getCollisionBounds(), xPos, yPos);

	}

	@Override
	public void draw(Graphics g) {

		g.setColor(new Color(255, 255, 255, 100));

		g.fillRect(xPos, yPos, height, width);
	}

}
