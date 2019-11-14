package controller;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

import javax.management.*;

/**
 * A class that can return CPU load, memory usage and also measures time elapsed
 * between frames in a game. The aim of the frame measurement is to be able to
 * return how many frames per second that the game produces. To work correctly a
 * start and end point for a frame needs to be defined.
 * 
 * @author Mattias Sikvall Källström
 * @version 2019-03-10
 */
public class PerformanceMonitor {
	private double processCpuLoad;
	private long memoryUsage;
	private int fps;
	private long startTime;
	private long endTime;
	private long[] times;
	private int measurementSize = 15;
	private int steps;
	private boolean newData;

	/**
	 * Creates a new instance of PerformanceMonitor.
	 */
	public PerformanceMonitor() {
		try {
			processCpuLoad = getProcessCpuLoad();
			memoryUsage = getMemoryUsage();
			fps = 0;
			steps = 0;
			times = new long[measurementSize];
			newData = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if new data has been collected since last time this method was
	 * called.
	 * 
	 * @return true if new data has been collected since last time this method was
	 *         called
	 */
	public boolean hasNewData() {
		if (newData) {
			newData = false;
			return true;
		} else
			return false;
	}

	/**
	 * The point in the code where time should start being measured.
	 */
	public void startFrame() {
		startTime = System.nanoTime();
	}

	/**
	 * Saves the difference in time since startFrame() was called. If the desired
	 * measurement size is reached the FPS will be calculated and information abot
	 * CPU and memory will be gathered.
	 */
	public void endFrame() {
		endTime = System.nanoTime();
		times[steps++] = endTime - startTime;
		if (steps >= measurementSize) {
			long averageFrames = 0;
			for (int i = 0; i < measurementSize; i++) {
				averageFrames += times[i];
			}
			fps = (int) ((((long) steps) * 1000000000) / (averageFrames));
			try {
				processCpuLoad = getProcessCpuLoad();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			memoryUsage = getMemoryUsage();
			newData = true;
			steps = 0;
		}
	}

	/**
	 * Returns the current CPU load for the process 
	 * @return the current CPU load for the process 
	 * @throws Exception
	 */
	private double getProcessCpuLoad() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });

		if (list.isEmpty())
			return Double.NaN;

		Attribute att = (Attribute) list.get(0);
		Double value = (Double) att.getValue();

		// usually takes a couple of seconds before we get real values
		if (value == -1.0)
			return Double.NaN;
		// returns a percentage value with 1 decimal point precision
		return ((int) (value * 1000) / 10.0);
	}

	/**
	 * Returns the current memory usage of the process.
	 * @return the current memory usage of the process
	 */
	private long getMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		return (allocatedMemory - freeMemory) / 1024;
	}

	/**
	 * Returns a string with information about the process.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("FPS: " + fps);
		s.append(", CPULoad: " + processCpuLoad + "%");
		s.append(", Memory Usage: " + memoryUsage + " kb");
		return s.toString();
	}
}
