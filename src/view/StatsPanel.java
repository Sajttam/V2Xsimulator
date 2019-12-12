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
	
	private static int TEXT_SIZE = 30;
	private static int HEADING_SIZE = 40;
	
	private String[] hStr = { "Spawn", "Collision", "Data"};
	private Map<String, Map<String, ModelStatsHolder>> headings;
	
	public StatsPanel(Map<String, Map<String, ModelStatsHolder>> headings) {
		this.headings = headings;
		
		GridLayout gridLayout = new GridLayout(headings.get(hStr[0]).size() + headings.get(hStr[1]).size() + 2, 2);
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

	private void addToPanel() {
		add(new JLabel("Spawns"));
		add(new JLabel(""));

		addToPanel(headings.get(hStr[0]));

		add(new JLabel("Collisions"));
		add(new JLabel(""));

		addToPanel(headings.get(hStr[1]));
	}

	private void setFont(Map<String, ModelStatsHolder> map) {
		Font font = new Font("Serif", Font.PLAIN, HEADING_SIZE);
		for (ModelStatsHolder sh : map.values()) {
			sh.getName().setFont(font);
			sh.getValue().setFont(font);
		}
	}

	private void setFont() {
		setFont(headings.get(hStr[0]));
		setFont(headings.get(hStr[1]));
	}
}
