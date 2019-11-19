import java.io.IOException;
import javax.swing.JOptionPane;

import controller.Controller;
import view.*;
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
		//try {
			//TileSet.loadTileSet();
			gui = new GuiPanel(231, 189, 4);
			Controller controller = new Controller(gui);
			gui.makeMenu(controller);
		/*} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace(), "Failed to load game", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace(), "Failed to load game", JOptionPane.ERROR_MESSAGE);
			System.out.println(e);
			System.exit(0);
		}*/
	}
}