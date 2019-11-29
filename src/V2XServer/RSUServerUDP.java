package V2XServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import models.V2XCommand;
import models.V2XMessage;

public class RSUServerUDP extends ConnectionUDP implements Runnable {

	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];
	private int port;
	private FunctionHandler functionHandler = null;

	public RSUServerUDP(int port) {
		this.port = port;
		functionHandler = new FunctionHandler(this);
	}

	public void sendCommand(DatagramSocket socket, V2XCommand command) throws IOException {
		DatagramPacket packet = objectToDatagaram(socket, command);

		socket.send(packet);

	}

	/**
	 * Constantly listening on a given port, prints the messages it receives in the
	 * console.
	 */
	@Override
	public void run() {
		try {
			running = true;

			socket = new DatagramSocket(port);

			System.out.println("Server started");
			System.out.println("Waiting for a package ...");

			while (running) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				socket.receive(packet); // Wait for package

				V2XMessage message = recieveMessage(packet);
				System.out.println(message);

				functionHandler.logCarInfo(message.getListenerPort(), message);
			}
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
