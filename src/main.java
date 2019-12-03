import java.io.IOException;

import javax.swing.JFrame;

import controller.Controller;
import controller.StatsController;
import view.GuiPanel;

/**
 * This class contains the main method for the program.
 * 
 * @author Mattias Sikvall K�llstr�m
 *
 */
public class main {
	private static Thread thread;

	/**
	 * Loads the controller, guiPanel and local HighscoreServer.
	 * 
	 * @param args are not used
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		GuiPanel gui;
		JFrame stats;

		// try {
		// TileSet.loadTileSet();
		int scaling = 1;
		gui = new GuiPanel(300 * scaling, 200 * scaling, scaling);
		stats = new JFrame("Statistics");
		// StatsController statscontroller = new StatsController(stats);
		StatsController.initialize(stats);
		stats.setSize(700, 500);
		Controller controller = new Controller(gui);
		gui.makeMenu(controller);
		/*
		 * } catch (IOException e) { JOptionPane.showMessageDialog(null,
		 * e.getStackTrace(), "Failed to load game", JOptionPane.ERROR_MESSAGE);
		 * System.exit(0); } catch (Exception e) { JOptionPane.showMessageDialog(null,
		 * e.getStackTrace(), "Failed to load game", JOptionPane.ERROR_MESSAGE);
		 * System.out.println(e); System.exit(0); }
		 */

	}
}
