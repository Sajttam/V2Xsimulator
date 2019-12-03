package controller;

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatsController implements PropertyChangeListener {
	
	private class StatsHolder {
		private JLabel nameJLabel = null;
		private JLabel valueJLabel = null;
		private int value = 0;
		
		public StatsHolder (String name, int value) {
			setName(new JLabel(name));
			valueJLabel = new JLabel("\t"+value);
		}
		
		public JLabel getName() {
			return nameJLabel;
		}
		public void setName(JLabel name) {
			this.nameJLabel = name;
		}
		
		public JLabel getValue() {
			return valueJLabel;
		}
		
		public void incValue() {
			value++;
			valueJLabel.setText("\t"+value);
		}
	}
	
	public enum EventType {
		CAR("Car", "Car", "Spawn"),
		BICYCLE("Bicycle", "Bicycle", "Spawn"),
		SMARTCAR("Smartcar", "Smartcar", "Spawn"),
		SMARTCAR2BICYCLE("Smartcar2Bicycle", "Smartcar & Bicycle", "Collision"),
		CAR2BYCYCLE("Car2Bicycle", "Car & Bicycle", "Collision"),
		SMARTCAR2CAR("Smartcar2Car", "Smartcar & Car", "Collision"),
		SMARTCAR2SMARTCAR("Smartcar2Smartcar", "Smartcar & Smartcar", "Collision"),
		CAR2CAR("Car2Car", "Car & Car", "Collision");
		
		private String eventType; // no spaces allowed in this string
		private String displayName;
		private String heading;
		
		EventType(String eventType, String displayName, String heading) {
			this.eventType = eventType;
			this.displayName = displayName;
			this.heading = heading;
		}

		public String getEventType() {
			return eventType;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public String getHeading() {
			return heading;
		}
	}
	
	private JFrame jframe;
	private JPanel jpanel;
	
	private Map<String, StatsHolder> labelsCollsions;
	private Map<String, StatsHolder> labelsSpawns;
	
	private static int TEXT_SIZE = 30;
	private static int HEADING_SIZE = 40;
	
	/**
	 * Creates the statics view in the given JFrame
	 * @param jframe
	 */
	private StatsController(JFrame jframe){	
		this.jframe = jframe;
		
		labelsCollsions = new TreeMap<String, StatsHolder>();
		labelsSpawns = new TreeMap<String, StatsHolder>();
		
		createLabels(); //Creates labels and adds them to labelsWithValues
		
		jpanel = new JPanel();
		
		GridLayout gridLayout = new GridLayout(labelsCollsions.size() + labelsSpawns.size() + 2, 2);
		
		jpanel.setLayout(gridLayout);
		
		setFont();
		addToPanel();
		
		jframe.add(jpanel);
		jframe.setVisible(true);
		
		jframe.pack();
	}
	
	private static StatsController statsController;
	public static void initialize(JFrame jFrame) {
		if (statsController == null)
			statsController = new StatsController(jFrame);
		else {			
			jFrame.add(getInstance().jpanel);
			jFrame.setVisible(true);			
			jFrame.pack();
		}
			
	}
	
	public static StatsController getInstance() {
		return statsController;		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {		
		String eventname = event.getPropertyName();
		
		TreeMap<String, StatsHolder> labels = new TreeMap<String, StatsHolder>(labelsCollsions);
		labels.putAll(labelsSpawns);
		
		StatsHolder sh = labels.get(eventname);
		JLabel l = sh.getValue();
		sh.incValue();
	}
	
	private void createLabels() {
		for (EventType e : EventType.values()) {
			StatsHolder sh = new StatsHolder(e.getDisplayName(), 0);
			if (e.getHeading().equals("Spawn"))
				labelsCollsions.put(e.getEventType(), sh);
			else
				labelsSpawns.put(e.getEventType(), sh);
		}
	}
	
	private void addToPanel(Map<String, StatsHolder> map) {
		for (StatsHolder sh : map.values()) {
			jpanel.add(sh.getName());
			jpanel.add(sh.getValue());
		}
	}
	
	private void addToPanel() {	
		
		
		jpanel.add(new JLabel("Spawns"));
		jpanel.add(new JLabel(""));
		
		addToPanel(labelsCollsions);
		
		jpanel.add(new JLabel("Collisions"));
		jpanel.add(new JLabel(""));
		
		addToPanel(labelsSpawns);
		
				
	}
	
	private void setFont(Map<String, StatsHolder> map) {
		Font font = new Font("Serif", Font.PLAIN, HEADING_SIZE);
		for (StatsHolder sh : map.values()) {
			sh.getName().setFont(font);
			sh.getValue().setFont(font);
		}
	}
	
	private void setFont() {
		setFont(labelsSpawns);
		setFont(labelsCollsions);
	}
}