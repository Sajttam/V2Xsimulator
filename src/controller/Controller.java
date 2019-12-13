package controller;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import entities.Collidable;
import entities.Entity;
import entities.EntityMouseListener;
import entities.EntityRoad;
import models.SharedValues;
import models.map.mapBeta;
import view.GuiPanel;

/**
 * The class Controller handles the game logic. Controller extends Thread and
 * aim to run 60 times every second. The main focus of controller is to handle
 * keyboard inputs, and models called Entities. Every entity that is added to
 * controller will have its step method called during execution of a frame. A
 * controller can load a level from a text file. The controller also updates the
 * GUI with information of what to paint and when to paint it.
 * 
 * @author Mattias Sikvall Källström
 * @version 2019-03-10
 */
public class Controller extends Thread implements ActionListener, PropertyChangeListener {
	private GuiPanel guiPanel;
	private CopyOnWriteArrayList<Entity> instances;
	private List<Entity> createInstances;
	private List<Entity> deleteInstances;
	private PerformanceMonitor performanceMonitor;
	private int score = 0;
	private int width;
	private int height;
	private int gridSize;
	private boolean gameOver;
	private boolean gameWon;
	private String lastUsedFile;
	private KeyEvent keypressed;
	private KeyEvent keyReleased;
	private MouseEvent mouseEvent;
	private int temp = 0;
	public static final SharedValues GLOBAL = SharedValues.getInstance();

	/**
	 * Initializes a new controller with a given GUI.
	 * 
	 * @param guiPanel The GUI that the controller will draw on.
	 */
	public Controller(GuiPanel guiPanel) {
		this.guiPanel = guiPanel;

		GLOBAL.setBicycleCounter(12);
		GLOBAL.setCarCounter(12);
		GLOBAL.setServerPort(1000);
		GLOBAL.setTimeOutValue(16.6667);
		GLOBAL.setSMARTCAR_CHANCE(0.5);

		instances = new CopyOnWriteArrayList<Entity>();
		createInstances = new ArrayList<Entity>();
		deleteInstances = new ArrayList<Entity>();
		guiPanel.getFrame().addKeyListener(new TAdapter());
		guiPanel.addMouseListener(new MAdapater());
		gridSize = 21;
		height = 189;

		initializeGame("resources/map2.txt");
		guiPanel.setDrawInstaces(instances);
		start();
		startGUIThread();

	}

	public void restart() {
		GLOBAL.setBicycleCounter(12);
		GLOBAL.setCarCounter(6);
		GLOBAL.setServerPort(1000);
		GLOBAL.setTimeOutValue(16.6667);
		GLOBAL.setSMARTCAR_CHANCE(0.5);
		instances.clear();
		createInstances.clear();
		deleteInstances.clear();
		guiPanel.setDrawInstaces(instances);
		StatsController.getInstance().clear();
		initializeGame("");
	}

	/**
	 * {@inheritDoc} Executes the method actionPerformed() sixty times every
	 * seconds.
	 */
	@Override
	public void run() {
		while (true) {

			long startTime = System.nanoTime();
			actionPerformed(null);
			long endTime = System.nanoTime();
			try {
				int timeOutValue = ((int) (GLOBAL.getTimeOutValue() - (endTime - startTime) * 0.000001));
				if (timeOutValue >= 0)
					sleep(timeOutValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GLOBAL.incStepsEpic();
		}
	}

	/**
	 * Initializes the game from a text file that follows the correct syntax. This
	 * method will replace the current game state with a fresh one.
	 * 
	 * @param fileName the filename and path of .txt-file that can be loaded as a
	 *                 level.
	 */
	public void initializeGame(String fileName) {
		// (new mapAlpha()).getMap(this);
		(new mapBeta()).getMap(this);
	}

	private void startGUIThread() {

		(new Thread() {
			@Override
			public void run() {

				while (true) {
					try {
						sleep(16);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					guiPanel.repaint();
				}
			}
		}).start();

	}

	/**
	 * Adds an entity to the creatInstances list and makes this Controller as that
	 * entities observer. The entity will fully be added to the game during the
	 * ActionPerformed method.
	 * 
	 * @param entity the Entity which is to be added to the game
	 */
	public synchronized void createInstance(Entity entity) {

		createInstances.add(entity);
		entity.setController(this);
		entity.addObserver(this);

	}

	/**
	 * Tests if one entities collision bound collied with any of the other Entities
	 * collision bounds in the game. If a collision is detected the given entities
	 * collision method is performed. An entity needs to implement the Collidable
	 * interface in order to be able to use this method properly.
	 * 
	 * @param entity The Entity that collisions are tested for.
	 */
	public synchronized void collisionChecking(Entity entity) {
		for (Entity other : instances) {
			if (entity != other && entity instanceof Collidable) {
				Rectangle entityBounds = entity.getCollisionBounds();
				Rectangle otherBounds = other.getCollisionBounds();
				if (entityBounds.intersects(otherBounds)) {
					if (other instanceof Collidable) {
						((Collidable) entity).collision(other);
					}
				}
			}
		}
	}

	public Entity getEntityAtPosition(int x, int y) {
		for (Entity entity : instances) {
			Rectangle2D entityBounds = entity.getCollisionBounds();
			if (entityBounds.contains(x, y))
				return entity;
		}
		return null;
	}

	public List<Entity> getEntitiesInsideArea(Polygon area) {
		List<Entity> entities = new ArrayList<Entity>();

		for (Entity entity : instances) {
			Rectangle2D entityBounds = entity.getCollisionBounds();

			if (!(entity instanceof EntityRoad)) {
				if (area.getBounds().intersects(entityBounds) && area.intersects(entityBounds)) {

					entities.add(entity);
				}
			}
		}

		return entities;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof Entity) {
			if (evt.getOldValue() == null && evt.getNewValue() instanceof Entity) {
				createInstance((Entity) evt.getNewValue());
			} else if (evt.getOldValue().equals(evt.getSource()) && evt.getNewValue() == null) {
				deleteInstances.add((Entity) evt.getOldValue());
			}
		}
	}

	/**
	 * This method is performed every game step. It first checks if the game is over
	 * or won. Then it creates instances that are queued for creation and deletes
	 * instances queued for deletion. Then it updates playerOne on keyboard actions
	 * and performs all the instanced Entites step methods. Collision checking for
	 * Collidable instances is performed. Finally it tells the GUI to repaint the
	 * screen If there is a performance monitor it will also be handled.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (gameOver)
			gameOver(); // Restart the game if gameOver is true or the player is outside of the map
		if (gameWon)
			gameWon(); // Restart the game if gameWon is true

		// Create instances that has been queued for creation
		for (Entity entity : createInstances) {
			instances.add(entity);
		}
		createInstances.clear();

		// Delete instances that has been queued for termination
		for (Entity entity : deleteInstances) {
			instances.remove(entity);
		}
		deleteInstances.clear();

		// Call for keyboard events for player one
		if (keypressed != null) {
			// playerOne.keyPressed(keypressed);
			keypressed = null;
		}

		if (keyReleased != null) {
			// playerOne.keyReleased(keyReleased);
			keyReleased = null;
		}

		if (mouseEvent != null) {
			for (Entity entity : instances) {
				if (entity instanceof EntityMouseListener) {
					((EntityMouseListener) entity).mouseClicked(mouseEvent);
				}
			}
			mouseEvent = null;
		}

		// Do step event and collision events for entities
		for (Entity entity : instances) {
			entity.step();

		}

		for (Entity entity : instances) {
			if (entity instanceof Collidable) {
				collisionChecking(entity);
			}
		}

		if (performanceMonitor != null) {
			performanceMonitor.endFrame();
			if (performanceMonitor.hasNewData()) {
				guiPanel.updatePerformancePanel(performanceMonitor.toString());
			}
			performanceMonitor.startFrame();
		}
	}

	/**
	 * Adds a performance monitor to the game which will keep track of FPS, CPU load
	 * and memory usage.
	 */
	public void addPerformanceMonitor() {
		performanceMonitor = new PerformanceMonitor();
		performanceMonitor.startFrame();
		guiPanel.addPerformancePanel(performanceMonitor.toString());
	}

	/**
	 * Increases the game score with a given value.
	 * 
	 * @param value The value that the score is to be increased by.
	 */
	public void increaseScore(int value) {
		score = score + value;
		guiPanel.updateScoreBoard(score);
	}

	/**
	 * A JOptionPan congratulates you on winning the game and asks for your name.
	 * Your name and score will then be uploaded to the local high score server. The
	 * level is then restarted.
	 */
	private void gameWon() {
		String playerName = JOptionPane.showInputDialog(guiPanel,
				"Congratulation you completed the level.\n\n\tSCORE:" + score + "\n\nPlease enter your name: ",
				"Level completed", JOptionPane.INFORMATION_MESSAGE);
		initializeGame(lastUsedFile);
	}

	/**
	 * A JOptionPane tells you that it's game over and displays you final score. The
	 * game is then restarted.
	 */
	private void gameOver() {
		JOptionPane.showMessageDialog(null, "YOU DIED\n\n\tSCORE:" + score, "Game Over",
				JOptionPane.INFORMATION_MESSAGE);
		initializeGame(lastUsedFile);
	}

	/**
	 * An adapter that logs keyboard inputs and executes them at the correct
	 * situation in the game state.
	 * 
	 * @author Mattias Sikvall Källström
	 *
	 */
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			keypressed = e;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keyReleased = e;
		}
	}

	private class MAdapater extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			mouseEvent = e;
		}
	}
}
