package controller;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entities.StatisticsObserver;

public class StatsController implements PropertyChangeListener {
	private StatisticsObserver collisionobserver;
	private JFrame jframe;
	private int car = 0;
	private int smartcar = 0;
	private int bicycle = 0;
	private int smartcar2Bicycle = 0;
	private int car2Bicycle = 0;
	private int smartcar2Car = 0;
	private int smartcar2Smartcar = 0;
	private int car2Car = 0;
	private JPanel jpanel;
	private JLabel labelStat;
	private JLabel labelCar;
	private JLabel labelSmartCar;
	private JLabel labelBicycle;
	private JLabel labelcollisions;
	private JLabel labelsmartcar2Bicycle;
	private JLabel labelcar2Bicycle;
	private JLabel labelsmartcar2Car;
	private JLabel labelsmartcar2Smartcar;
	private JLabel labelcar2Car;
	private int textsize = 30;
	
	public StatsController(JFrame jframe){
	
		this.jframe = jframe;
		createLabels();
		jpanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(jpanel, BoxLayout.Y_AXIS);
		jpanel.setLayout(boxlayout);
		setBounds();
		setFont();
		addToPanel();
		jframe.add(jpanel);
		jframe.setVisible(true);
		collisionobserver = StatisticsObserver.getInstance();
		collisionobserver.addObserver(this);
		jframe.pack();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		String eventname = event.getPropertyName();
		switch(eventname) {
		case "Car":
			car = (int) event.getNewValue();
			labelCar.setText("Car: " + car);
			break;		
		case "Smartcar":
			smartcar = (int) event.getNewValue();
			labelSmartCar.setText("Smart car: " + smartcar);
			break;	
		case "Bicycle":
			bicycle = (int) event.getNewValue();
			labelBicycle.setText("Bicycle: " + bicycle);
			break;
		case "Smartcar2Bicycle":
			smartcar2Bicycle = (int) event.getNewValue();
			labelsmartcar2Bicycle.setText("Smartcar & Bicycle: " + smartcar2Bicycle);;
			break;
		case "Car2Bicycle":
			car2Bicycle = (int) event.getNewValue();
			labelcar2Bicycle.setText("Car & Bicycle: " + car2Bicycle);
			break;
		case "Smartcar2Car":
			smartcar2Car = (int) event.getNewValue();
			labelsmartcar2Car.setText("Smartcar & Car: " + smartcar2Car);
			break;
		case "Smartcar2Smartcar":
			smartcar2Smartcar = (int) event.getNewValue();
			labelsmartcar2Smartcar.setText("Smartcar & Smartcar: " + smartcar2Smartcar/2);
			break;
		case "Car2Car":
			car2Car = (int) event.getNewValue();
			labelcar2Car.setText("Car & Car: " + car2Car/2);
			break;
		}
	}
	
	private void createLabels() {
		labelStat = new JLabel("Statistics:");
		labelCar = new JLabel("Car: " + car);
		labelSmartCar = new JLabel("Smart car: " + smartcar);
		labelBicycle = new JLabel("Bicycle: " + bicycle);
		labelcollisions = new JLabel("Collisions:");
		labelsmartcar2Bicycle = new JLabel("Smartcar & Bicycle: " + smartcar2Bicycle);
		labelcar2Bicycle = new JLabel("Car & Bicycle: " + car2Bicycle);
		labelsmartcar2Car = new JLabel("Smartcar & Car: " + smartcar2Car);
		labelsmartcar2Smartcar = new JLabel("Smartcar & Smartcar: " + smartcar2Car);
		labelcar2Car = new JLabel("Car & Car: " + smartcar2Car);
	}
	private void setBounds() {
		labelStat.setBounds(50, 50, 100, 30);
		labelCar.setBounds(50, 50, 100, 30);
		labelSmartCar.setBounds(50, 50, 100, 30);
		labelBicycle.setBounds(50, 50, 100, 30);
		labelcollisions.setBounds(50, 50, 100, 30);
		labelsmartcar2Bicycle.setBounds(50, 50, 100, 30);
		labelcar2Bicycle.setBounds(50, 50, 100, 30);
		labelsmartcar2Car.setBounds(50, 50, 100, 30);
		labelsmartcar2Smartcar.setBounds(50, 50, 100, 30);
		labelcar2Car.setBounds(50, 50, 100, 30);
	}
	private void addToPanel() {
		jpanel.add(labelStat); 
		jpanel.add(labelCar); 
		jpanel.add(labelSmartCar); 
		jpanel.add(labelBicycle); 
		jpanel.add(labelcollisions);
		jpanel.add(labelsmartcar2Bicycle);
		jpanel.add(labelcar2Bicycle);
		jpanel.add(labelsmartcar2Car);
		jpanel.add(labelsmartcar2Smartcar);
		jpanel.add(labelcar2Car);
	}
	private void setFont() {
		labelStat.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelCar.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelSmartCar.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelBicycle.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelcollisions.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelsmartcar2Bicycle.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelcar2Bicycle.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelsmartcar2Car.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelsmartcar2Smartcar.setFont(new Font("Serif", Font.PLAIN, textsize));
		labelcar2Car.setFont(new Font("Serif", Font.PLAIN, textsize));
	}

	
	
}
