package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class EntityRoad extends Entity{
	EntityNode enterNode, exitNode;
	int x2, y2;
	
	public EntityRoad(EntityNode enterNode, EntityNode exitNode) {
		this.enterNode = enterNode;
		this.exitNode = exitNode;
	}
	
	public void setPosition(int x1, int y1, int x2, int y2) {
		setXPosition(x1);
		setYPosition(y1);
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setPosition(EntityNode node, int x, int y) {
		if (node.equals(enterNode)) {			
			setXPosition(x);
			setYPosition(y);
		}
		else {
			x2 = x;
			y2 = y;
		}
	}
	
	public EntityNode getEntryNode() {
		return enterNode;
	}
	
	public EntityNode getExitNode() {
		return exitNode;
	}
	
	public EntityNode getOtherNode(EntityNode node) {
		if (node.equals(enterNode)) return exitNode;
		else return enterNode;
	}
	
	int wait = (int) (50 + (Math.random()*500));;
	@Override
	public void step() {
		if (wait <= 0) {
			instanceCreate(new EntityCar(this));
			wait = (int) (50 + (Math.random()*500));
		}
		wait--;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawLine(getXPosition(), getYPosition(), x2, y2);
		g.fillRect(x2,y2-4,8,8);
	}
}
