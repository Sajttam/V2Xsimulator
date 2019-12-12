package models;

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

	public double pixelsPerStepToKph(double pixelsPerStep) {

		return 3.6 * ((pixelsPerStep / pixelsPerMeter) * stepsPerSecond);

	}

}
