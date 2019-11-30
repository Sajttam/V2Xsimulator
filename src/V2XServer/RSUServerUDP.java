package V2XServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import entities.EntityRSUBoundaries;
import models.V2XCommand;
import models.V2XMessage;

public class RSUServerUDP extends ConnectionUDP implements Runnable {

	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	private int serverPort;
	private FunctionHandler functionHandler = null;
	private EntityRSUBoundaries rsuBoundaries;

	public RSUServerUDP(int serverPort, int xPos, int yPos) {
		this.serverPort = serverPort;
		functionHandler = new FunctionHandler(this);
		rsuBoundaries = new EntityRSUBoundaries(xPos, yPos);
	}

	public void sendCommand(DatagramSocket socket, V2XCommand command) throws IOException {
		DatagramPacket packet = objectToDatagaram(socket, command);

		socket.send(packet);

	}

	public int getServerPort() {

		return serverPort;

	}

	public EntityRSUBoundaries getRSUBoundaries() {

		return rsuBoundaries;
	}

	/**
	 * Constantly listening on a given port, prints the messages it receives in the
	 * console.
	 */
	@Override
	public void run() {
		try {
			running = true;

			socket = new DatagramSocket(serverPort);

			System.out.println("Server started");
			System.out.println("Waiting for a package ...");

			while (running) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				socket.receive(packet); // Wait for package

				V2XMessage message = receiveMessage(packet);
				System.out.println("Server: " + serverPort + " " + message);

				functionHandler.logCarInfo(message.getListenerPort(), message);
			}
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
