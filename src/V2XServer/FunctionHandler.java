package V2XServer;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import models.V2XMessage;

public class FunctionHandler extends Thread {

	LinkedList<Socket> activeCars = new LinkedList<Socket>();
	ConcurrentHashMap<Socket, V2XMessage> carLogs = new ConcurrentHashMap<Socket, V2XMessage>();
	private static FunctionHandler functionHandler = new FunctionHandler();

	private FunctionHandler() {

	}

	private void blindspotChecker() {

	}

	private void runFunctions() {

		blindspotChecker();
	}

	public static FunctionHandler getInstance() {

		return functionHandler;

	}

	public void addCar(Socket carSocket) {

		activeCars.add(carSocket);

	}

	public void removeCar(Socket carSocket) {

		activeCars.remove(carSocket);

	}

	public void logCarInfo(Socket carSocket, V2XMessage carInfo) {

		carLogs.put(carSocket, carInfo);
		runFunctions();

	}

}
