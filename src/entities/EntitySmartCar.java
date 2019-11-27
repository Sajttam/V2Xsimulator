package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import models.SharedValues;
import models.V2XMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class EntitySmartCar.
 */
public class EntitySmartCar extends EntityCar {

	Socket socket;
	ObjectOutputStream outToServer;
	ObjectInputStream inFromServer;
	static int serverInterval = 60;
	int tempWait = serverInterval;

	/**
	 * Instantiates a new entity smart car.
	 *
	 * @param road     the road
	 * @param listener the listener
	 */
	public EntitySmartCar(EntityRoad road, PropertyChangeListener listener) {
		super(road, listener);

	}

	/**
	 * Connects this car to the RSU.
	 */
	private void connectToRSU() {

		try {
			socket = new Socket("127.0.0.1", SharedValues.getInstance().getPort());
			System.out.println("Connected");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Retrying...");
			connectToRSU();

		}

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

				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("localhost");
				byte[] sendData = new byte[1024];
				V2XMessage message = new V2XMessage(getSpeed(), getAngle(),
						new Point2D.Double(getXPosition(), getYPosition()));

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(outputStream);
				os.writeObject(message);
				byte[] data = outputStream.toByteArray();
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress,
						SharedValues.getInstance().getPort());
				clientSocket.send(sendPacket);
				clientSocket.close();

				tempWait = serverInterval;

			} catch (IOException e) {
				e.printStackTrace();

			}

		} else {

			tempWait--;

		}
	}

	@Override
	public void collision(Entity other) {

		super.collision(other);
//
//		try {
//			outToServer.writeObject("Stop");
//			outToServer.flush();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}

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
