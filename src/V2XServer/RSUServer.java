package V2XServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import models.SharedValues;
import models.V2XMessage;

public class RSUServer extends Thread {

//	private Socket socket = null;
//	private ServerSocket server = null;
//	private DataInputStream in = null;
//	private int port;

	DatagramSocket serverSocket;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	ByteArrayInputStream in;
	ObjectInputStream is;
	V2XMessage carInfo;
	Socket clientSocket;
	DatagramPacket receivePacket;

	public RSUServer(int port) {

		try {
			serverSocket = new DatagramSocket(SharedValues.getInstance().getPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {

			System.out.println("Server started");
			System.out.println("Waiting for a client...");

			while (true) {
				// socket = server.accept();

				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				byte[] data = receivePacket.getData();
				in = new ByteArrayInputStream(data);
				is = new ObjectInputStream(in);

				try {
					Object message = is.readObject();

					if (message instanceof V2XMessage) {// information message from car

						carInfo = (V2XMessage) message;
						// FunctionHandler.getInstance().logCarInfo(clientSocket, (V2XMessage) message);
						System.out.println(receivePacket.getPort() + " " + carInfo);

					} else if (message.equals("Stop")) {// Exit command to kill thread

						// FunctionHandler.getInstance().removeCar(clientSocket);

					}

					if (message.equals("Ping")) {// Echoes a ping from a client

						System.out.println("Ping received " + clientSocket.toString());

					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
