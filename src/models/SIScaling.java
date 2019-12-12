package models;
/**
 * Used to convert from SI units to pixels per step
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
		return pixelsPerMeter * ((kph * 1000) / (stepsPerSecond * 3600));
	}
	/**
	 * pixelsPerStepToKph: Converts pixels/step to kph
	 * @param pps pixels/step to be converted
	 * @return value in kph
	 */
	public double pixelsPerStepToKph(double pps) {
		return (pps *stepsPerSecond * 3600) / (1000 *pixelsPerMeter);
	}
	
	/**
	 * accelerationPerStep: Take m/s value and convert it to pixels/step
	 * @param mps value in meters per second
	 * @return value in pixels per step
	 */
	public double accelerationPerStep(double mps) {
		return pixelsPerMeter * (mps/stepsPerSecond);
	}

}
