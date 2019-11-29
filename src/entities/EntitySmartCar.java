package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import V2XServer.ConnectionUDP;
import controller.Controller;
import models.V2XCommand;
import models.V2XMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class EntitySmartCar.
 */
public class EntitySmartCar extends EntityCar {

	DatagramSocket senderSocket;
	DatagramSocket listenerSocket;
	static int serverInterval = 60;
	int tempWait = serverInterval;
	private ConnectionUDP connectionUDP;
	private boolean stop;
	byte[] receiveData = new byte[1024];
	private int listenerPort;

	/**
	 * Instantiates a new entity smart car.
	 *
	 * @param road     the road
	 * @param listener the listener
	 */

	public EntitySmartCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener, entitytype);


		startListener();

		try {
			connectToRSU();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startListener() {

		listenerPort = Controller.GLOBAL.getPortNumber();
		try {
			listenerSocket = new DatagramSocket(listenerPort);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		(new Thread() {
			@Override
			public void run() {
				DatagramPacket recievePacket = new DatagramPacket(receiveData, receiveData.length);

				try {

					listenerSocket.receive(recievePacket);

					V2XCommand command = connectionUDP.recieveCommand(recievePacket);

					if (command.getCommand().equals(V2XCommand.Commands.STOP)) {

						stop = true;
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				} // Wait for package

			}
		}).start();

	}

	
	
	
	/**
	 * Connects this car to the RSU.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private void connectToRSU() throws UnknownHostException, SocketException {
		connectionUDP = new ConnectionUDP();
		senderSocket = new DatagramSocket();
		senderSocket.connect(InetAddress.getByName("localhost"), Controller.GLOBAL.getServerPort()); // Connection
																										// should

	}

	/*
	 * Overrides the step function to also update the server with the smartCars
	 * information. The additional wait period is there to extend the update
	 * interval.
	 */
	@Override
	public void step() {
		super.step();
		if (tempWait <= 0) {
			try {
				V2XMessage message = new V2XMessage(this.hashCode(), this.getSpeed(), getAngle(),
						new Point2D.Double(getXPosition(), getYPosition()), listenerPort);
				connectionUDP.sendMessage(senderSocket, message);
				tempWait = serverInterval;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			tempWait--;
		}

		if (stop) {

			this.setSpeed(0);

		}
	}

	/*
	 * Sets the smart car to be blue.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.BLUE);
		g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);

	}

	@Override
	public void collision(Entity other) {

		Controller.GLOBAL.removePortNumber(listenerPort);
		listenerSocket.close();
		super.collision(other);

	}

}
