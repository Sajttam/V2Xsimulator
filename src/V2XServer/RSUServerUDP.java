package V2XServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import models.SharedValues;
import models.V2XCommand.Commands;
import models.V2XMessage;

public class RSUServerUDP extends ConnectionUDP implements Runnable {
	
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	private int port;
	private FunctionHandler functionHandler = null;

	public RSUServerUDP(int port) {
		this.port = port;
		functionHandler = new FunctionHandler();
	}
	
	public void sendCommand(DatagramSocket socket, Commands command) throws IOException {
		DatagramPacket packet = objectToDatagaram(socket, command);		
		socket.send(packet);
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
				
				functionHandler.logCarInfo(packet.getPort(), message);
			}
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}

