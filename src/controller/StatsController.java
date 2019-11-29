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
		CAR("Car", "Car"),
		BICYCLE("Bicycle", "Bicycle"),
		SMARTCAR("Smartcar", "Smartcar"),
		SMARTCAR2BICYCLE("Smartcar2Bicycle", "Smartcar & Bicycle"),
		CAR2BYCYCLE("Car2Bicycle", "Car & Bicycle"),
		SMARTCAR2CAR("Smartcar2Car", "Smartcar & Car"),
		SMARTCAR2SMARTCAR("Smartcar2Smartcar", "Smartcar & Smartcar"),
		CAR2CAR("Car2Car", "Car & Car");
		
		private String eventType; // no spaces allowed in this string
		private String displayName;
		
		EventType(String eventType, String displayName) {
			this.eventType = eventType;
			this.displayName = displayName;
		}

		public String getEventType() {
			return eventType;
		}
		
		public String getDisplayName() {
			return displayName;
		}

	}
	
	private JFrame jframe;
	private JPanel jpanel;
	
	private Map<String, StatsHolder> labelsWithValues;
	
	private static int TEXT_SIZE = 30;
	private static int HEADING_SIZE = 40;
	
	/**
	 * Creates the statics view in the given JFrame
	 * @param jframe
	 */
	private StatsController(JFrame jframe){	
		this.jframe = jframe;
		
		labelsWithValues = new TreeMap<String, StatsHolder>();
		createLabels(); //Creates labels and adds them to labelsWithValues
		
		jpanel = new JPanel();
		
		GridLayout gridLayout = new GridLayout(labelsWithValues.size(), 2);
		
		jpanel.setLayout(gridLayout);
		
		setFont();
		addToPanel();
		
		jframe.add(jpanel);
		jframe.setVisible(true);
		
		jframe.pack();
	}
	
	private static StatsController statsController;
	public static void initialize(JFrame jFrame) {
		statsController = new StatsController(jFrame);
	}
	
	public static StatsController getInstance() {
		return statsController;		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {		
		String eventname = event.getPropertyName();
		
		StatsHolder sh = labelsWithValues.get(eventname);
		JLabel l = sh.getValue();
		sh.incValue();
	}
	
	private void createLabels() {
		for (EventType e : EventType.values()) {
			StatsHolder sh = new StatsHolder(e.getDisplayName(), 0);
			labelsWithValues.put(e.getEventType(), sh);
		}
	}
	
	private void addToPanel() {
		for (StatsHolder sh : labelsWithValues.values()) {
			jpanel.add(sh.getName());
			jpanel.add(sh.getValue());
		}
	}
	
	private void setFont() {
		Font font = new Font("Serif", Font.PLAIN, HEADING_SIZE);
		for (StatsHolder sh : labelsWithValues.values()) {
			sh.getName().setFont(font);
			sh.getValue().setFont(font);
		}
	}
}