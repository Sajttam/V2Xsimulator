package V2XServer;

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import models.SharedValues;
import models.V2XCommand;
import models.V2XMessage;

public class ConnectionUDP {
	private byte[] buf = new byte[256]; // Contains the data that is put and received by a message
										// this class expect this value to be convertible to a V2XMessage

	/**
	 * Takes a V2X and sends sends it by the information given in the DatagramSocket
	 * 
	 * @param socket
	 * @param msg
	 * @throws IOException
	 */
	public void sendMessage(DatagramSocket socket, V2XMessage msg) throws IOException {
		socket.send(objectToDatagaram(socket, msg));
	}

	public DatagramPacket objectToDatagaram(DatagramSocket socket, Object object) throws IOException {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream);
		oo.writeObject(object);
		oo.close();
		buf = bStream.toByteArray();
		return new DatagramPacket(buf, buf.length, socket.getInetAddress(), socket.getPort());
	}

	/**
	 * Takes a datagram packet and converts it to a V2X message, throws an
	 * exceptions if it fails
	 * 
	 * @param p
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public V2XMessage receiveMessage(DatagramPacket p) throws IOException, ClassNotFoundException {
		buf = p.getData();
		ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
		V2XMessage message = (V2XMessage) iStream.readObject();
		iStream.close();
		return message;
	}

	public V2XCommand receiveCommand(DatagramPacket p) throws IOException, ClassNotFoundException {
		buf = p.getData();
		ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
		V2XCommand message = (V2XCommand) iStream.readObject();
		iStream.close();
		return message;
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

	public class BroadcastReceiver extends Thread {
		byte[] receiveData = new byte[1024];
		int port;
		// private PropertyChangeSupport propertyChangeSupport = new
		// PropertyChangeSupport();

		public BroadcastReceiver(PropertyChangeListener ps) {
			port = SharedValues.getInstance().getBroadcastPort();
			// this.ps = ps;
			this.start();
		}

		@Override
		public void run() {
			try {
				DatagramSocket serverSocket = new DatagramSocket(port);
				byte[] receiveData = new byte[8];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				while (true) {
					serverSocket.receive(receivePacket);
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			// should close serverSocket in finally block
		}
	}
}
