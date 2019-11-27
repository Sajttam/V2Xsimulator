package V2XServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import models.V2XMessage;

public class RSUServerUDP extends ConnectionUDP implements Runnable {
	
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	private int port;

	public RSUServerUDP(int port) {
		this.port = port;
	}
	
	/**
	 * Constantly listening on a given port, prints the messages it receives in the console.
	 */
	@Override
	public void run() {
		try {
			running = true;

			socket = new DatagramSocket(port);

			System.out.println("Server started");

			while (running) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				System.out.println("Waiting for a package ...");
				
				socket.receive(packet); // Wait for package

				V2XMessage message = reciveMessage(packet);
				
				System.out.println("Client " + packet.getPort() + " " + message.toString());
			}
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}