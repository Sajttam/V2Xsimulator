package V2XServer;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import controller.Controller;
import models.V2XMessage;

public class FunctionHandler extends Thread {

	ConcurrentHashMap<Integer, V2XMessage> carLogs = new ConcurrentHashMap<Integer, V2XMessage>();
	private static FunctionHandler functionHandler = new FunctionHandler();
	
	/**
	 * Constructor for creating a FunctionHandler
	 */
	public FunctionHandler() {

	}

	/**
	 * Test if all vehicles blindspots
	 */
	private void blindspotChecker() {
		
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
		blindspotChecker();
	}
	
	/**
	 * 
	 * @param carSocket
	 * @param carInfo
	 */
	public void logCarInfo(int carPort, V2XMessage carInfo) {
		carLogs.put(carPort, carInfo);
		runFunctions();
		System.out.println(carLogs.toString());
	}

}
