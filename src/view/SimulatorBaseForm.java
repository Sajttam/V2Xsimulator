package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimulatorBaseForm extends JPanel {
	public SimulatorBaseForm() {
		String[] labels = {"Max cars: ", "Max bicycles: ", "Smart car %: "};
		int numPairs = labels.length;

		for (int i = 0; i < numPairs; i++) {
		    JLabel l = new JLabel(labels[i], JLabel.TRAILING);
		    add(l);
		    JTextField textField = new JTextField(10);
		    l.setLabelFor(textField);
		    add(textField);
		}
		
		JButton button = new JButton("Start");
		add(button);
	}
}
