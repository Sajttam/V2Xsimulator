package V2XServer;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import entities.EntityBikeDetector;
import models.CarData;
import models.V2XCommand;
import models.V2XCommand.Commands;
import models.V2XMessage;

public class FunctionHandler extends Thread {

	public static final int SAVING_STEPS = 200;

	private Map<Integer, CarData> carLogs = new ConcurrentHashMap<Integer, CarData>();
	private RSUServerUDP server;

	/**
	 * Constructor for creating a FunctionHandler
	 */
	public FunctionHandler(RSUServerUDP server) {
		this.server = server;
	}

	/**
	 * Test if all vehicles blindspots
	 * 
	 * @throws IOException
	 */
	private void blindspotChecker() throws Exception {

		for (Entry<Integer, CarData> k : carLogs.entrySet()) {
			
			int port = k.getKey();
			DatagramSocket socket;
			socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("localhost"), port);

			Point newPosition40 = k.getValue().getPositionAfterSteps(40); // position of the car in n number of steps
			Point newPosition20 = k.getValue().getPositionAfterSteps(20); // position of the car in n number of steps
			Set<EntityBikeDetector> bdSet = server.getBikeDetectors();
			for (EntityBikeDetector bikeDetector : bdSet) {
				bikeDetector.addCheckedPoint(newPosition40); // DEBUG
				bikeDetector.addCheckedPoint(newPosition20); // DEBUG
				if (bikeDetector.getCollisionBounds().contains(newPosition40) || bikeDetector.getCollisionBounds().contains(newPosition20) ) {
					if(!bikeDetector.getBicycles().isEmpty()) {
						server.sendCommand(socket, new V2XCommand(Commands.STOP));
					}
				}

			}
		}
	}

	private void eraseOld() {
		for (Entry<Integer, CarData> k : carLogs.entrySet()) {
			V2XMessage message = k.getValue().getNewMessage();
			if (message.getTimeStamp() < Controller.GLOBAL.getTimeStamp() - SAVING_STEPS) {
				carLogs.remove(k.getKey());
			}
		}
	}

	/**
	 * Run all safety functions
	 * 
	 * @throws IOException
	 */
	private void runFunctions() throws Exception {
		eraseOld();
		blindspotChecker();
	}

	/**
	 * 
	 * @param carSocket
	 * @param carInfo
	 */
	public void logCarInfo(int carPort, V2XMessage carInfo) {
		CarData c = carLogs.get(carPort);
		if (c == null) {
			c = new CarData(carInfo);
			carLogs.put(carPort, c);
		} else {
			c.updateMessage(carInfo);
		}
		try {
			runFunctions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
