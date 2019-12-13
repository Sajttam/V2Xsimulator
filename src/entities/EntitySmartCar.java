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
	static int serverInterval = 10;
	int tempWait = serverInterval;
	private ConnectionUDP connectionUDP;
	private byte[] buf = new byte[256];
	private int listenerPort;
	private Thread listenerThread;
	private boolean connected;
	private DatagramPacket receivePacket;
	private Color smartCarColor = Color.BLUE;
	private static final int COLOR_CHANGE_WAIT_TIME = 300;
	private int colorWaitCounter = 0;

	/**
	 * Instantiates a new entity smart car.
	 *
	 * @param road     the road
	 * @param listener the listener
	 */

	public EntitySmartCar(EntityRoad road, PropertyChangeListener listener, String entitytype) {
		super(road, listener, entitytype);
		setVehicleName("Smartcar");
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
					while (true) {
						receivePacket = new DatagramPacket(buf, buf.length);
						// System.out.println("TEST!");
						listenerSocket.receive(receivePacket);
						// System.out.println("Recived");
						V2XCommand command = connectionUDP.receiveCommand(receivePacket);

						switch (command.getCommand()) {
						case DRIVE:
							setRSUStopSignal(false);
							break;
						case STOP:
							setRSUStopSignal(true);
							colorWaitCounter = COLOR_CHANGE_WAIT_TIME;
							break;
						default:
							setRSUStopSignal(false);
							break;

						}
					}

				} catch (IOException | ClassNotFoundException e) {
					if (!(e.getClass().toString().equals("class java.net.SocketException"))) // Hides socket closed
																								// exception,
						e.printStackTrace(); // due to closing a socket that is currently listening results in exception
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
		senderSocket.connect(InetAddress.getByName("localhost"), serverPort); // Connection should
	}

	/*
	 * Overrides the step function to also update the server with the smartCars
	 * information. The additional wait period is there to extend the update
	 * interval.
	 */
	@Override
	public void step() {

		if (tempWait <= 0) {
			try {

				for (RSUServerUDP i : SharedValues.getInstance().getConnectionAreas()) {

					if (i.getRSUBoundaries().tryConnect(this)) {

						V2XMessage message = new V2XMessage(this.hashCode(),
								scaling.pixelsPerStepToKph(this.speed), getAngle(),
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
		super.step();

	}

	private Color getSmartCarColor() {
		if (colorWaitCounter <= 0) {
			return Color.BLUE;
		} else {
			colorWaitCounter--;
			return Color.PINK;
		}
	}

	/*
	 * Sets the smart car to be blue.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		drawVehicleShape(g, getSmartCarColor());

	}

	@Override
	public void collision(Entity other) {
		super.collision(other);
		if (other instanceof EntityVehicle) {
			listenerSocket.close();
			listenerThread.interrupt();
		}
	}
}
