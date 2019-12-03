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
import V2XServer.RSUServerUDP;
import models.SharedValues;
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
	private byte[] buf = new byte[256];
	private int listenerPort;
	private Thread listenerThread;
	private boolean connected;
	private DatagramPacket receivePacket;

	/**
	 * Instantiates a new entity smart car.
	 *
	 * @param road     the road
	 * @param listener the listener
	 */

	public EntitySmartCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {

		super(road, listener, entitytype);
		startListener();

	}

	private void startListener() {

		try {
			listenerSocket = new DatagramSocket();
			listenerPort = listenerSocket.getLocalPort();
		} catch (SocketException e1) {

			e1.printStackTrace();
		}

		listenerThread = new Thread() {
			@Override
			public void run() {

				try {

					receivePacket = new DatagramPacket(buf, buf.length);

					listenerSocket.receive(receivePacket);

					V2XCommand command = connectionUDP.receiveCommand(receivePacket);

					if (command.getCommand().equals(V2XCommand.Commands.STOP)) {

						stop = true;
					}

				} catch (IOException | ClassNotFoundException e) {

					if (!(e.getClass().toString().equals("class java.net.SocketException")))
						e.printStackTrace();
				} // Wait for package

			}

		};

		listenerThread.start();

	}

	/**
	 * Connects this car to the RSU.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private void connectToRSU(int serverPort) throws UnknownHostException, SocketException {
		connectionUDP = new ConnectionUDP();
		senderSocket = new DatagramSocket();
		senderSocket.connect(InetAddress.getByName("localhost"), serverPort); // Connection
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

				for (RSUServerUDP i : SharedValues.getInstance().getConnectionAreas()) {

					if (i.getRSUBoundaries().tryConnect(this)) {

						V2XMessage message = new V2XMessage(this.hashCode(), this.speed, getAngle(),
								new Point2D.Double(getXPosition(), getYPosition()), listenerPort);

						connectToRSU(i.getServerPort());
						connectionUDP.sendMessage(senderSocket, message);

					}
				}
				tempWait = serverInterval;

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			tempWait--;
		}

		if (stop) {

			setSpeed(0);

		}

	}

	/*
	 * Sets the smart car to be blue.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawCar(Color.blue, g);
//		g.fillOval((int) getXPosition() - 8, (int) getYPosition() - 8, 16, 16);

	}

	@Override
	public void collision(Entity other) {

		listenerSocket.close();
		listenerThread.interrupt();
		super.collision(other);

	}

}
