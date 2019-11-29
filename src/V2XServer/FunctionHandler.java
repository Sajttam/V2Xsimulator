package V2XServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import models.V2XCommand;
import models.V2XMessage;

public class FunctionHandler extends Thread {

	ConcurrentHashMap<Integer, V2XMessage> carLogs = new ConcurrentHashMap<Integer, V2XMessage>();
	RSUServerUDP server;

	/**
	 * Constructor for creating a FunctionHandler
	 */
	public FunctionHandler(RSUServerUDP server) {
		this.server = server;
	}

	/**
	 * Test if all vehicles blindspots
	 */
	private void blindspotChecker() {

		for (int i : carLogs.keySet()) {
			int port = i;
			DatagramSocket socket;
			try {
				socket = new DatagramSocket();
				socket.connect(InetAddress.getByName("localhost"), port);
				server.sendCommand(socket, new V2XCommand());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

	}

	private void eraseOld() {
		for (Entry<Integer, V2XMessage> k : carLogs.entrySet()) {
			if (k.getValue().getTimeStamp() < Controller.GLOBAL.getTimeStamp() - 1200) {
				carLogs.remove(k.getKey());
			}
		}
	}

	/**
	 * Run all safety functions
	 */
	private void runFunctions() {
		eraseOld();
		// blindspotChecker();
	}

	/**
	 * 
	 * @param carSocket
	 * @param carInfo
	 */
	public void logCarInfo(int carPort, V2XMessage carInfo) {
		carLogs.put(carPort, carInfo);
		runFunctions();
	}

}
