package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import entities.EntityNode.Direction;

public class EntityRoad extends Entity{
	public enum RoadType {CAR, BICYCLE;}
	
	private EntityNode enterNode, exitNode;
	int x2, y2;
	private Direction direction;
	private RoadType roadType;

	public EntityRoad(EntityNode enterNode, EntityNode exitNode, Direction direction, RoadType roadType) {
		this.enterNode = enterNode;
		this.exitNode = exitNode;
		this.direction = direction;
		this.roadType = roadType;
	}
	
	public void setPosition(int x1, int y1, int x2, int y2) {
		setXPosition(x1);
		setYPosition(y1);
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public RoadType getRoadType() {
		return roadType;
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
		/*if (wait <= 0) {
			instanceCreate(new EntityVehicles(this));
			wait = (int) (50 + (Math.random()*500));
		}
		wait--;*/
	}
	
	@Override
	public void draw(Graphics g) {
		switch (roadType) {
		case CAR:
			g.setColor(Color.GREEN);
			break;
		case BICYCLE:
			g.setColor(Color.ORANGE);
			break;
		}
		
		g.drawLine((int)getXPosition(), (int)getYPosition(), x2, y2);
		//g.fillRect(x2,y2-4,8,8);
	}
}
