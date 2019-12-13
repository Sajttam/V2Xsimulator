package controller;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.SIScaling;
import models.SharedValues;
import models.stats.ModelCollision;
import models.stats.ModelStatsHolder;
import models.stats.StatsEventType;
import view.StatsPanel;

public class StatsController implements PropertyChangeListener {

	private SIScaling scaling = new SIScaling();
	private JFrame jframe;
	private JPanel jpanel;
    private List<ModelCollision> modelCollisions;
	private Map<String, Map<String, ModelStatsHolder>> headings;
	private String[] hStr = { "Spawn", "Collision", "Data", "Cars - Bicycles", "Smartcars - Bicycles"};

	/*
	 * private Map<String, ModelStatsHolder> labelsSpawn; private Map<String,
	 * ModelStatsHolder> labelsCollision; private Map<Long, CollisionData>
	 * collisionDataMap;
	 */

	/**
	 * Creates the statics view in the given JFrame
	 * 
	 * @param jframe
	 */
	private StatsController(JFrame jframe) {
		this.jframe = jframe;
		jpanel = new JPanel();
		//clear();
		
		modelCollisions = new ArrayList<ModelCollision>();

		headings = new TreeMap<String, Map<String, ModelStatsHolder>>();
		for (String s : hStr)
			headings.put(s, new TreeMap<String, ModelStatsHolder>());

		createLabels(); // Creates labels and adds them to labelsWithValues

		jpanel = new StatsPanel(headings);
		
		jframe.add(jpanel);
		jframe.setJMenuBar(makeMenu());
		jframe.setVisible(true);
		jframe.pack();

	}

	/**
	 * Creates a menubar for statistics window
	 * 
	 * @param jframe the frame that the menubar will be added to
	 */
	public JMenuBar makeMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		JMenuItem itemSave = new JMenuItem("Save stats");
		itemSave.addActionListener(e -> saveToFile());

		menuBar.add(menuFile);
		menuFile.add(itemSave);
		
		return menuBar;
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
		if (eventname.equals(StatsEventType.COLLISION_DATA.getEventType())) { // for handling a received CollisionData
			if (event.getNewValue() instanceof ModelCollision) {
				ModelCollision mc = (ModelCollision) event.getNewValue();
				modelCollisions.add(mc);
				
				if (mc.getVehicleFirstSpeed() < 20) {
					if (mc.getVehicleFirstType().equals("Car")) 
						headings.get(hStr[3]).get("c2b_020").incValue();
					else
						headings.get(hStr[4]).get("sc2b_020").incValue();
				}
				else if (mc.getVehicleFirstSpeed() < 40) {
					if (mc.getVehicleFirstType().equals("Car")) 
						headings.get(hStr[3]).get("c2b_2040").incValue();
					else
						headings.get(hStr[4]).get("sc2b_2040").incValue();
				}
				else {
					if (mc.getVehicleFirstType().equals("Car")) 
						headings.get(hStr[3]).get("c2b_4060").incValue();
					else
						headings.get(hStr[4]).get("sc2b_4060").incValue();
				}
				
			}
		} else {
			TreeMap<String, ModelStatsHolder> labels = new TreeMap<String, ModelStatsHolder>((headings.get(hStr[0])));
			labels.putAll((headings.get(hStr[1])));
			ModelStatsHolder sh = labels.get(eventname);
			JLabel l = sh.getValue();
			sh.incValue();
		}
	}

	public void clear() {
		// Creates labels and adds them to labelsWithValues
		/*jpanel.removeAll();
		headings = new TreeMap<String, Map<String, ModelStatsHolder>>();
		headings.put(hStr[0], new TreeMap<String, ModelStatsHolder>());
		headings.put(hStr[1], new TreeMap<String, ModelStatsHolder>());
		createLabels();
		setFont();
		addToPanel();
		jframe.pack();*/
	}

	private void createLabels() {
		for (StatsEventType e : StatsEventType.values()) {
			ModelStatsHolder sh = new ModelStatsHolder(e.getDisplayName(), 0);
			Map<String, ModelStatsHolder> head = headings.get(e.getHeading());
			head.put(e.getEventType(), sh);
		}
	}

	/**
	 * saveToFile: Opens a InputDialog that register filename
	 */
	public void saveToFile() {
		String fileName = JOptionPane.showInputDialog(jframe, "Specifiy name for savefile");
		writeStatsToFile("", fileName);
	}

	/**
	 * writeStatsToFile: Saves statistics to a .tsv file
	 * 
	 * @param url      path to were the file is supposed to be saved
	 * @param filename name of file to be saved
	 */
	public void writeStatsToFile(String url, String filename) {
		StringBuilder sburl = new StringBuilder(url);
		sburl.append(filename);
		sburl.append(".tsv");
		try {
			FileWriter fwriter = new FileWriter(sburl.toString());
			PrintWriter pwriter = new PrintWriter(fwriter);
			pwriter.println("Spawns: \t");
			for (ModelStatsHolder sh : headings.get(hStr[0]).values()) {
				pwriter.println(sh.getStringName() + "\t" + sh.getStringValue());
			}
			pwriter.println("Collisions: \t");
			for (ModelStatsHolder sh : headings.get(hStr[1]).values()) {
				pwriter.println(sh.getStringName() + "\t" + sh.getStringValue());
			}
			pwriter.println("Cars - Bicycles: \t");
			for (ModelStatsHolder sh : headings.get(hStr[3]).values()) {
				pwriter.println(sh.getStringName() + "\t" + sh.getStringValue());
			}
			pwriter.println("Smartcars - Bicycles: \t");
			for (ModelStatsHolder sh : headings.get(hStr[4]).values()) {
				pwriter.println(sh.getStringName() + "\t" + sh.getStringValue());
			}

			pwriter.close();
			fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}