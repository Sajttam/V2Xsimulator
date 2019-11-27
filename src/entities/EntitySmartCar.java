package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import V2XServer.ConnectionUDP;
import models.V2XMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class EntitySmartCar.
 */
public class EntitySmartCar extends EntityCar {

	DatagramSocket socket;
	static int serverInterval = 60;
	int tempWait = serverInterval;
	private ConnectionUDP connectionUDP;

	/**
	 * Instantiates a new entity smart car.
	 *
	 * @param road     the road
	 * @param listener the listener
	 */
	public EntitySmartCar(EntityRoad road, PropertyChangeListener listener) {
		super(road, listener);
		try {
			connectToRSU();
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Connects this car to the RSU.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private void connectToRSU() throws UnknownHostException, SocketException {
		connectionUDP = new ConnectionUDP();
		socket = new DatagramSocket();
		socket.connect(InetAddress.getByName("localhost"), 1000); // Connection should probably come from RSU somehow
		System.out.println(socket);
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
						new Point2D.Double(getXPosition(), getYPosition()));
				connectionUDP.sendMessage(socket, message);
				tempWait = serverInterval;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			tempWait--;
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

}
