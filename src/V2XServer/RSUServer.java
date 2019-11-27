package V2XServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import models.SharedValues;
import models.V2XMessage;

public class RSUServer extends Thread {

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
						System.out.println(carInfo);

					} else if (message.equals("Stop")) {// Exit command to kill thread

						// FunctionHandler.getInstance().removeCar(clientSocket);

					}

					if (message.equals("Ping")) {// Echoes a ping from a client

						System.out.println("Ping received " + clientSocket.toString());

					}

					broadcast("Hello", InetAddress.getByName("255.255.255.255"));

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String broadcastMessage, InetAddress address) throws IOException {
		DatagramSocket broadcastSocket = new DatagramSocket();

		broadcastSocket.setBroadcast(true);

		byte[] buffer = broadcastMessage.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address,
				SharedValues.getInstance().getBroadcastPort());
		broadcastSocket.send(packet);
		broadcastSocket.close();
	}
}
