package V2XServer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import models.V2XMessage;

public class RSUServer extends Thread {

	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private int port;

	public RSUServer(int port) {

		this.port = port;

	}

	@Override
	public void run() {
		try {

			server = new ServerSocket(port);

			System.out.println("Server started");
			System.out.println("Waiting for a client...");

			while (true) {
				socket = server.accept();
				FunctionHandler.getInstance().addCar(socket);
				// System.out.println("Client accepted");

				// Starts a new thread in which the server and client can communicate
				new Messaging(socket);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Messaging extends Thread {

	Socket clientSocket;
	FileOutputStream f;
	V2XMessage carInfo;
	boolean running = true;

	ObjectOutputStream oOut;
	ObjectInputStream oIn;
	InputStream in;
	OutputStream out;

	public Messaging(Socket socket) {

		clientSocket = socket;
		this.start();

	}

	@Override
	public void run() {

		// System.out.println("Car connected " + clientSocket.toString());

		try {

			out = clientSocket.getOutputStream();
			in = clientSocket.getInputStream();

			oOut = new ObjectOutputStream(out);
			oIn = new ObjectInputStream(in);

			while (!clientSocket.isClosed()) {// loop to keep checking for new messages
				Object message = oIn.readObject();

				if (message instanceof V2XMessage) {// information message from car

					carInfo = (V2XMessage) message;
					FunctionHandler.getInstance().logCarInfo(clientSocket, (V2XMessage) message);

					// System.out.println("Client " + clientSocket.getPort() + " " +
					// message.toString());
//					oOut.writeObject("Recieved");
//					oOut.flush();

				} else if (message.equals("Stop")) {// Exit command to kill thread

					FunctionHandler.getInstance().removeCar(clientSocket);

					in.close();
					out.close();
					oOut.close();
					oIn.close();
					clientSocket.close();
					this.interrupt();

				}

				if (message.equals("Ping")) {// Echoes a ping from a client

					System.out.println("Ping received " + clientSocket.toString());

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public V2XMessage getCarInfo() {

		return carInfo;

	}

}
