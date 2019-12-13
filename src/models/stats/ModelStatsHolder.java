package models.stats;

import javax.swing.JLabel;

public class ModelStatsHolder {
	private JLabel nameJLabel = null;
	private JLabel valueJLabel = null;
	private int value = 0;
	
	
	public ModelStatsHolder (String name, int value) {
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

	public void reset() {
		value = -1;
		incValue();
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