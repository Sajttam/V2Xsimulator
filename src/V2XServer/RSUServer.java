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

			System.out.println("Waiting for a client ...");

			while (true) {
				socket = server.accept();
				System.out.println("Client accepted");

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
	boolean running = true;

	ObjectInputStream oIn;
	ObjectOutputStream oOut;
	OutputStream out;

	public Messaging(Socket socket) {

		clientSocket = socket;
		this.start();

	}

	@Override
	public void run() {

		System.out.println("Car connected " + clientSocket.toString());

		InputStream in;

		try {
			in = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();

			oIn = new ObjectInputStream(in);
			oOut = new ObjectOutputStream(out);

			while (clientSocket.isConnected()) {
				Object message = oIn.readObject();

				if (message instanceof V2XMessage) {

					System.out.println("Client " + clientSocket.getPort() + " " + message.toString());

				} else if (message.equals("Over")) {

					in.close();
					out.close();
					oOut.close();
					oIn.close();
					clientSocket.close();

				}
				if (message.equals("Ping")) {

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

}
