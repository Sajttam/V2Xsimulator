package models;

/**
 * Used to convert from SI units to pixels per step
 * 
 * @author
 *
 */
public class SIScaling {

	private int stepsPerSecond = 60;
	private int pixelsPerMeter = 5;

	public SIScaling() {
	}

	public int getStepsPerSecond() {
		return stepsPerSecond;
	}
	public double getStepsPerMillisecond() {
		return 0.06;
	}

	public void setStepsPerSecond(int stepsPerSecond) {
		this.stepsPerSecond = stepsPerSecond;
	}

	public double getPixelsFromMeter(double d) {
		return pixelsPerMeter * d;
	}

	public void setPixelsPerMeter(int pixelsPerMeter) {
		this.pixelsPerMeter = pixelsPerMeter;
	}

	public double kphToPixelsPerStep(double kph) {

//		double kpsecond = kph / 3600;
//		double mpsecond = kpsecond * 1000;
//		double pixelspSecond = mpsecond * 5;
//		double pixelspStep = pixelspSecond / 60;
//
//		return pixelspStep;
		return kph * (pixelsPerMeter * 1000) / (stepsPerSecond * 3600);
	}

	/**
	 * pixelsPerStepToKph: Converts pixels/step to kph
	 * 
	 * @param pps pixels/step to be converted
	 * @return value in kph
	 */
	public double pixelsPerStepToKph(double pps) {
		return (pps * stepsPerSecond * 3600) / (1000 * pixelsPerMeter);
	}

	/**
	 * accelerationPerStep: Take m/s value and convert it to pixels/step
	 * 
	 * @param mps value in meters per second
	 * @return value in pixels per step
	 */
	public double mpsToPixelsPerStep(double mps) {
		return pixelsPerMeter * (mps / (stepsPerSecond));
	}

	public static void main(String[] args) {
		SIScaling s = new SIScaling();
//		System.out.println("1 [meter/sekund^2] = " + s.accelerationPerStep(1) + " [pixlar/step]");

		System.out.println("60 [kph] = " + s.kphToPixelsPerStep(60) + " [pixlar/step]");

	}

}
