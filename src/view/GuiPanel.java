package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;
import controller.StatsController;
import entities.Entity;
import models.SharedValues;

/**
 * The GUI panel keeps track of a list of entities and calls those entities draw
 * methods every time repaint() is called. GuiPanel extends Panel but it also
 * has a JFrame and a JMenu.
 * 
 * @author Mattias Sikvall K�llstr�m
 * @version 2019-03-10
 */
public class GuiPanel extends JPanel {
	public static final String PROJECT_TITLE = "V2X Simulator";

	private CopyOnWriteArrayList<Entity> entityList;
	private BufferedImage background;
	private int width;
	private int height;
	private int levelWidth;
	private int scaling; // TODO: scale depending on screensize
	private JFrame frame;
	private int xOffset;
	private int scoreBoard;
	private int cameraWestSensor = 81; // The largest amount of pixels than can exist between the player and the WEST
										// side of the panel
	private int cameraEastSensor = 150;// The largest amount of pixels than can exist between the player and the EAST
										// side of the panel
	private boolean showCollisionBoxes = false;
	private JPanel performancePanel;

	/**
	 * 
	 * @param width   the width of the game draw are
	 * @param height  the height of the game draw area
	 * @param scaling the scaling of the game draw area
	 * @throws IOException thrown if the game can't load the background image
	 */
	public GuiPanel(int width, int height, int scaling) throws IOException {
		entityList = new CopyOnWriteArrayList<Entity>();
		this.scaling = scaling;
		this.width = width; // 231;
		this.height = height; // 189;
		setLayout(null);
		setBackground(Color.BLACK);
		makeFrame();
		// background = loadBackground();
		repaint();
	}

	/**
	 * Initializes a new game level and resets data from earlier level
	 * 
	 * @param cameraFollow the Entity that the game screen will follow
	 * @param levelWidth   the width of the level in pixels
	 */
	public void initializeNewLevel(int levelWidth) {
		this.levelWidth = levelWidth;
		if (xOffset > 0)
			xOffset = 0;
	}

	/**
	 * Adds a panel on which the game performance can be shown
	 * 
	 * @param performance the performance as a String
	 */
	public void addPerformancePanel(String performance) {
		performancePanel = new JPanel();
		JLabel performanceLabel = new JLabel(performance);
		performancePanel.add(performanceLabel);
		frame.add(performancePanel, BorderLayout.SOUTH);
	}

	/**
	 * Updates the performance panel with a new String
	 * 
	 * @param performance the performance as a String
	 */
	public void updatePerformancePanel(String performance) {
		JLabel performanceLabel = (JLabel) performancePanel.getComponent(0);
		performanceLabel.setText(performance);
	}

	/**
	 * Sets the list of Entities that will be drawn on the screen
	 * 
	 * @param l the list of Entities that will be drawn on the screen
	 */
	public void setDrawInstaces(CopyOnWriteArrayList<Entity> l) {
		entityList = l;
	}

	/**
	 * Creates the frame for the game and also adds the game panel to it.
	 */
	public void makeFrame() {
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle(PROJECT_TITLE);
		frame.setIconImage(TileSet.getTile(19, 3));

		setPreferredSize(new Dimension(width * scaling * 4, height * scaling * 4));
		setDoubleBuffered(true);
		Container contentPane = frame.getContentPane();

		contentPane.add(this, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	public void changeSpeed() {
		String txtValue = JOptionPane.showInputDialog(frame, "Set simulation speed");
		double value = Double.parseDouble(txtValue);
		SharedValues.getInstance().setTimeOutValue(value);
	}

	/**
	 * Adds a menu to the game
	 * 
	 * @param controller the controller for this GUI
	 */
	public void makeMenu(Controller controller) {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		// JMenuItem itemSave = new JMenuItem("Save");
		JMenuItem itemLoad = new JMenuItem("Load");
		// itemLoad.addActionListener(e -> controller.loadLevel());
		JMenuItem itemExit = new JMenuItem("Exit");
		itemExit.addActionListener(e -> System.exit(0));

		JMenu menuOptions = new JMenu("Options");
		JMenuItem itemFullscreen = new JMenuItem("Fullscreen");
		JMenuItem itemSpeed = new JMenuItem("Simulation Speed");
		itemSpeed.addActionListener(e -> changeSpeed());

		JMenu menuDebug = new JMenu("Debug");
		JMenuItem itemServer = new JMenuItem("Connect to server");
		JMenuItem itemGetFromServer = new JMenuItem("GetFromServer");
		JMenuItem itemCollisionBoxes = new JMenuItem("Collision Bounds");
		JMenuItem itemPerformance = new JMenuItem("Performance");
		itemCollisionBoxes.addActionListener(e -> showCollisionBoxes());
		itemPerformance.addActionListener(e -> controller.addPerformanceMonitor());

		JMenu menuView = new JMenu("View");
		JMenuItem itemStatisticsView = new JMenuItem("Statistics");
		itemStatisticsView.addActionListener(e -> StatsController.initialize(new JFrame()));
		itemPerformance.addActionListener(e -> controller.addPerformanceMonitor());

		JMenu menuHelp = new JMenu("Help");
		JMenuItem itemObjectives = new JMenuItem("Objectives");
		JMenuItem itemAbout = new JMenuItem("About");
		itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(frame, "HELLO"));

		menuBar.add(menuFile);
		// menuFile.add(itemSave);
		// menuFile.add(itemLoad);
		menuFile.add(itemExit);

		menuBar.add(menuOptions);
		menuOptions.add(itemSpeed);
		// menuOptions.add(itemHighscore);

		menuBar.add(menuDebug);
		// menuDebug.add(itemServer);
		// menuDebug.add(itemGetFromServer);
		menuDebug.add(itemCollisionBoxes);
		menuDebug.add(itemPerformance);

		menuBar.add(menuView);
		menuView.add(itemStatisticsView);

		menuBar.add(menuHelp);
		// menuHelp.add(itemObjectives);
		menuHelp.add(itemAbout);

		frame.add(menuBar, BorderLayout.NORTH);
		frame.pack();
	}

	/**
	 * Loads the background image for the game.
	 * 
	 * @return the background image for the game
	 * @throws IOException exception thrown if the file could't be read.
	 */
	public BufferedImage loadBackground() throws IOException {
		InputStream in = getClass().getResourceAsStream("/graphics/backgrounds.png");
		return ImageIO.read(in);
	}

	/**
	 * Returns the frame for the game
	 * 
	 * @return the frame for the game
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * When this method is called it updates the game panel with a new frame. Scales
	 * the screen and paints it with the current xOffset. Draws bars for health and
	 * armor. Cycles through all given Entities and executes their draw methods.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int paintXOffset = 0;
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(scaling, scaling);
		g2d.translate(paintXOffset, 0);
		super.paintComponent(g);
		for (int i = 0; i < 20; i++)
			g2d.drawImage(background, null, (int) (231 * i + paintXOffset * 0.1), 0);

		// System.out.println(getComponent(0).toString());
		// ((Entity) getComponent(0)).paint(g);
		// paintAll(g);
		for (Entity e : entityList) {
			// if (e.getXPosition() > (-21 - paintXOffset) && e.getXPosition() < (231 -
			// paintXOffset)) {
			e.draw(g2d);
			if (showCollisionBoxes)
				e.drawCollisionBounds(g2d);
			// }
		}

		g2d.dispose();
		g.dispose();
	}

	/**
	 * Toggles whether to draw entities collision boxes or not.
	 */
	public void showCollisionBoxes() {
		if (!showCollisionBoxes)
			showCollisionBoxes = true;
		else
			showCollisionBoxes = false;
	}

	/**
	 * Updates the score that is drawn on the screen
	 * 
	 * @param score the score that is to be drawn on the screen
	 */
	public void updateScoreBoard(int score) {
		scoreBoard = score;
	}

}
