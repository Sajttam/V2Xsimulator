package controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import entities.*;
import entities.EntityRoad.RoadType;
import view.*;

/**
 * The class Controller handles the game logic. Controller extends Thread and
 * aim to run 60 times every second. The main focus of controller is to handle
 * keyboard inputs, and models called Entities. Every entity that is added to
 * controller will have its step method called during execution of a frame. A
 * controller can load a level from a text file. The controller also updates the
 * GUI with information of what to paint and when to paint it.
 * 
 * @author Mattias Sikvall K�llstr�m
 * @version 2019-03-10
 */
public class Controller extends Thread implements ActionListener, PropertyChangeListener {
	private GuiPanel guiPanel;
	private List<Entity> instances;
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

	/**
	 * Initializes a new controller with a given GUI.
	 * 
	 * @param guiPanel The GUI that the controller will draw on.
	 */
	public Controller(GuiPanel guiPanel) {
		this.guiPanel = guiPanel;
		instances = new ArrayList<Entity>();
		createInstances = new ArrayList<Entity>();
		deleteInstances = new ArrayList<Entity>();
		guiPanel.getFrame().addKeyListener(new TAdapter());
		guiPanel.addMouseListener(new MAdapater());
		gridSize = 21;
		height = 189;

		initializeGame("resources/map2.txt");
		guiPanel.setDrawInstaces(instances);
		start();
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
				int timeOutValue = ((int) (16.6667 - (endTime - startTime) * 0.000001));
				if (timeOutValue >= 0)
					sleep(timeOutValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		//Create node objects
		EntityNode nodeWest = new EntityNode(64, 472-32);
		EntityNode nodeEast = new EntityNode(748, 472-32);
		EntityNode nodeTop = new EntityNode(406, 128);
		EntityNode nodeCenter = new EntityNode(406, 472);
		
		nodeWest.setSpawning(true);
		nodeEast.setSpawning(true);
		nodeTop.setSpawning(true);
		//nodeCenter.setSpawning(true);
		
		//Create simulation instances
		createInstance(nodeWest);
		createInstance(nodeEast);
		createInstance(nodeTop);
		createInstance(nodeCenter);
		
		//WEST - CENTER
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST, RoadType.BICYCLE);
		nodeCenter.addConnectionTo(nodeWest, EntityNode.Direction.WEST, RoadType.CAR);
		nodeWest.addConnectionTo(nodeCenter, EntityNode.Direction.EAST, RoadType.CAR);
		
		//EAST - CENTER
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST, RoadType.BICYCLE);
		nodeEast.addConnectionTo(nodeCenter, EntityNode.Direction.WEST, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeEast, EntityNode.Direction.EAST, RoadType.CAR);
		
		//TOP - CENTER
		nodeTop.addConnectionTo(nodeCenter, EntityNode.Direction.SOUTH, RoadType.CAR);
		nodeCenter.addConnectionTo(nodeTop, EntityNode.Direction.NORTH, RoadType.CAR);
		
		nodeCenter.doInternalConnections();
	}

	/**
	 * Adds an entity to the creatInstances list and makes this Controller as that
	 * entities observer. The entity will fully be added to the game during the
	 * ActionPerformed method.
	 * 
	 * @param entity the Entity which is to be added to the game
	 */
	public void createInstance(Entity entity) {
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
	public void collisionChecking(Entity entity) {
		for (Entity other : instances) {
			if (entity != other) {
				Rectangle entityBounds = entity.getCollisionBounds();
				Rectangle otherBounds = other.getCollisionBounds();
				if (entityBounds.intersects(otherBounds)) {
					if (entity instanceof Collidable) {
						((Collidable) entity).collision(other);
					}
				}
			}
		}
	}
	
	public Entity getEntityAtPosition(int x, int y) {
		for (Entity entity : instances) {
			Rectangle entityBounds = entity.getCollisionBounds();
			if (entityBounds.contains(x, y)) return entity;
		}
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof Entity) {
			if (evt.getOldValue() == null && evt.getNewValue() instanceof Entity) {
				createInstance((Entity) evt.getNewValue());
			}
			else if (evt.getOldValue().equals(evt.getSource()) && evt.getNewValue() == null) {
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
		
		// Update GUI
		guiPanel.repaint();

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
	 * @author Mattias Sikvall K�llstr�m
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
		public void mouseClicked(MouseEvent e) {
			mouseEvent = e;
		}
	}
}