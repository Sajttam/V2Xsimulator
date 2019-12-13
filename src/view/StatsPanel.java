package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.StatsController;
import models.stats.ModelStatsHolder;
import models.stats.StatsEventType;

public class StatsPanel extends JPanel {
	
	private static int TEXT_SIZE = 20;
	private static int HEADING_SIZE = 24;
	
	private String[] hStr = { "Spawn", "Collision",  "Data", "Cars - Bicycles", "Smartcars - Bicycles"};
	private Map<String, Map<String, ModelStatsHolder>> headings;
	
	public StatsPanel(Map<String, Map<String, ModelStatsHolder>> headings) {
		this.headings = headings;
		
		GridLayout gridLayout = new GridLayout(headings.get(hStr[0]).size() + headings.get(hStr[1]).size() + 2 + 8, 2);
		setLayout(gridLayout);
		
		setFont();
		addToPanel();
	}
	
	private void addToPanel(Map<String, ModelStatsHolder> map) {
		for (ModelStatsHolder sh : map.values()) {
			add(sh.getName());
			add(sh.getValue());
		}
	}
	
	public void addHeading(String s) {
		JLabel heading = new JLabel(s);
		heading.setFont(new Font("Serif", Font.BOLD, HEADING_SIZE));
		add(heading);
		add(new JLabel(""));
	}

	private void addToPanel() {
		addHeading("Spawns");
		addToPanel(headings.get(hStr[0]));
		
		addHeading("Collisions");		
		addToPanel(headings.get(hStr[1]));		
		
		addHeading("Cars - Bicycles");
		addToPanel(headings.get(hStr[3]));
		
		addHeading("Smartcars - Bicycles");
		addToPanel(headings.get(hStr[4]));
	}

	private void setFont(Map<String, ModelStatsHolder> map) {
		Font font = new Font("Serif", Font.PLAIN, TEXT_SIZE);
		for (ModelStatsHolder sh : map.values()) {
			sh.getName().setFont(font);
			sh.getValue().setFont(font);
		}
	}

	private void setFont() {
		setFont(headings.get(hStr[0]));
		setFont(headings.get(hStr[1]));
		setFont(headings.get(hStr[3]));
		setFont(headings.get(hStr[4]));
	}
}
