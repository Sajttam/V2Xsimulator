package controller;

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		
		public String getStringName() {
			return nameJLabel.getText();
		}
		
		public String getStringValue() {
			return value+"";
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
	
	private Map<String, StatsHolder> labelsSpawn;
	private Map<String, StatsHolder> labelsCollision;
	
	private static int TEXT_SIZE = 30;
	private static int HEADING_SIZE = 40;
	
	/**
	 * Creates the statics view in the given JFrame
	 * @param jframe
	 */
	private StatsController(JFrame jframe){	
		this.jframe = jframe;
		
		
		labelsSpawn = new TreeMap<String, StatsHolder>();
		labelsCollision = new TreeMap<String, StatsHolder>();
		
		createLabels(); //Creates labels and adds them to labelsWithValues
		
		jpanel = new JPanel();
		
		GridLayout gridLayout = new GridLayout(labelsSpawn.size() + labelsCollision.size() + 2, 2);
		
		jpanel.setLayout(gridLayout);
		
		setFont();
		addToPanel();
		writeStatsToFile("","test");
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
		
		TreeMap<String, StatsHolder> labels = new TreeMap<String, StatsHolder>(labelsSpawn);
		labels.putAll(labelsCollision);
		
		StatsHolder sh = labels.get(eventname);
		JLabel l = sh.getValue();
		sh.incValue();
	}
	
	private void createLabels() {
		for (EventType e : EventType.values()) {
			StatsHolder sh = new StatsHolder(e.getDisplayName(), 0);
			if (e.getHeading().equals("Spawn"))
				labelsSpawn.put(e.getEventType(), sh);
			else
				labelsCollision.put(e.getEventType(), sh);
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
		
		addToPanel(labelsSpawn);
		
		jpanel.add(new JLabel("Collisions"));
		jpanel.add(new JLabel(""));
		
		addToPanel(labelsCollision);
		
				
	}
	
	private void setFont(Map<String, StatsHolder> map) {
		Font font = new Font("Serif", Font.PLAIN, HEADING_SIZE);
		for (StatsHolder sh : map.values()) {
			sh.getName().setFont(font);
			sh.getValue().setFont(font);
		}
	}
	
	private void setFont() {
		setFont(labelsCollision);
		setFont(labelsSpawn);
	}
	
	public void writeStatsToFile(String url, String filename) {
		
		
		StringBuilder sburl = new StringBuilder(url);
		sburl.append(filename);
		sburl.append(".tsv");
		try {
			FileWriter fwriter = new FileWriter(sburl.toString());
			PrintWriter pwriter = new PrintWriter(fwriter);
			pwriter.println("Spawns: \t");
			for(StatsHolder sh : labelsSpawn.values()) {
				pwriter.println(sh.getStringName()+"\t"+sh.getStringValue());
			}
			pwriter.println("Collisions: \t");
			for(StatsHolder sh : labelsCollision.values()) {
				pwriter.println(sh.getStringName()+"\t"+sh.getStringValue());
			}
			
			pwriter.close();
			fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}