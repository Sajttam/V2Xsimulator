package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import entities.EntityNode.Direction;

public class EntityRoad extends Entity{
	public enum RoadType {CAR, BICYCLE;}
	
	private EntityNode enterNode, exitNode;
	int x2, y2;
	private RoadType roadType;
	private double angle = 0;
	private double distX = 0;
	private double distY = 0;
	private Boolean spawning;
	
	public boolean straight = true;
	public boolean leftCurve = false;
	
	public EntityRoad(EntityNode enterNode, EntityNode exitNode, RoadType roadType, Boolean spawning) {
		this.enterNode = enterNode;
		this.exitNode = exitNode;
		this.roadType = roadType;
		this.spawning = spawning;
	}
	
	public void setPosition(int x1, int y1, int x2, int y2) {
		setXPosition(x1);
		setYPosition(y1);
		this.x2 = x2;
		this.y2 = y2;
		
		distX = x2-x1;
		distY = y2-y1;
		
		angle = Math.atan(distY/distX);
		if (distX < 0) angle += Math.PI;
	}
	
	public RoadType getRoadType() {
		return roadType;
	}
	
	public void setPosition(EntityNode node, int x, int y) {
		if (node.equals(enterNode)) {
			setPosition(x,y,x2,y2);
		}
		else {
			setPosition((int)getXPosition(),(int)getYPosition(),x,y);
		}
	}
	

	
	int wait = (int) (50 + (Math.random()*400));;
	@Override
	public void step() {
		if(spawning)
			if (wait <= 0) {
				if (roadType == RoadType.BICYCLE) {
					instanceCreate(new EntityBicycle(this,CollisionObserver.getInstance()));
				}
				else {
					instanceCreate(new EntityCar(this,CollisionObserver.getInstance()));
				}
				wait = (int) (50 + (Math.random()*400));
			}
			wait--;
	}
	
	public double getAngle() {
		return angle;
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
		g.setColor(Color.WHITE);
		//g.drawString((angle*(180.0/Math.PI)) + "°", (int)(getXPosition()+(distX/2)), (int)(getYPosition()+distY/2));
		
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("x1:" + getXPosition() + " y1:" +getYPosition() + " x2:" + x2 + " y2:" +y2);
		return s.toString();
	}
	public double getDistX() {
		return distX;
	}

	public void setDistX(double distX) {
		this.distX = distX;
	}

	public double getDistY() {
		return distY;
	}

	public void setDistY(double distY) {
		this.distY = distY;
	}
	
	public EntityRoad getNextRoad(Boolean turn) {
		return exitNode.getNextRoad(this, turn);
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
}
