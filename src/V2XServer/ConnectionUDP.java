package V2XServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import models.V2XMessage;

public class ConnectionUDP {
	private byte[] buf = new byte[256]; //Contains the data that is put and received by a message
										// this class expect this value to be convertible to a V2XMessage
	
	/**
	 * Takes a V2X and sends sends it by the information given in the DatagramSocket
	 * @param socket
	 * @param msg
	 * @throws IOException
	 */
	public void sendMessage(DatagramSocket socket, V2XMessage msg) throws IOException {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream);
		oo.writeObject(msg);
		oo.close();
		buf = bStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, socket.getInetAddress(), socket.getPort());
		socket.send(packet);
	}
	
	/**
	 * Takes a datagram packet and converts it to a V2X message, throws an exceptions if it fails
	 * @param p
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public V2XMessage reciveMessage(DatagramPacket p) throws IOException, ClassNotFoundException {
		buf = p.getData();
		ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
		V2XMessage message = (V2XMessage) iStream.readObject();
		iStream.close();
		return message;
	}
}
