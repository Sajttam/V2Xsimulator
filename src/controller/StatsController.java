package controller;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entities.CollisionObserver;

public class StatsController implements PropertyChangeListener {
	private CollisionObserver collisionobserver;
	private JFrame jframe;
	private int car = 0;
	private int bicycle = 0;
	private int collision = 0;
	private JPanel jpanel;
	private JLabel labelStat;
	private JLabel labelCar;
	private JLabel labelBicycle;
	private JLabel labelCollision;
	
	public StatsController(JFrame jframe){
	
		this.jframe = jframe;
		labelStat = new JLabel("Statistics:");
		labelCar = new JLabel("Car: " + car);
		labelBicycle = new JLabel("Bicycle: " + bicycle);
		labelCollision = new JLabel("Collisions: " + collision);
		labelStat.setBounds(50, 50, 100, 30);
		labelCar.setBounds(50, 50, 100, 30);
		labelBicycle.setBounds(50, 50, 100, 30);
		labelCollision.setBounds(50, 50, 100, 30);
		jpanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(jpanel, BoxLayout.Y_AXIS);
		jpanel.setLayout(boxlayout);
		setFont();
		jpanel.add(labelStat); 
		jpanel.add(labelCar); 
		jpanel.add(labelBicycle); 
		jpanel.add(labelCollision);
		jframe.add(jpanel);
		jframe.setVisible(true);
		collisionobserver = CollisionObserver.getInstance();
		collisionobserver.addObserver(this);
		jframe.pack();
	}
	
	private void setFont() {
		labelStat.setFont(new Font("Serif", Font.PLAIN, 50));
		labelCar.setFont(new Font("Serif", Font.PLAIN, 50));
		labelBicycle.setFont(new Font("Serif", Font.PLAIN, 50));
		labelCollision.setFont(new Font("Serif", Font.PLAIN, 50));
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		String eventname = event.getPropertyName();
		switch(eventname) {
		case "CAR CREATED":
			car = (int) event.getNewValue();
			labelCar.setText("Car: " + car);
			break;
			
		case "BICYCLE CREATED":
			bicycle = (int) event.getNewValue();
			labelBicycle.setText("Bicycle: " + bicycle);
			break;
			
		case "COLLISION":
			collision = (int) event.getNewValue();
			labelCollision.setText("Collision: " + collision/2);
			break;
		}
	}
	
	
}
