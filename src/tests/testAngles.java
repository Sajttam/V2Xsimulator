package tests;

public class testAngles {
	
	public static int radToDeg (double rad) {
		return (int) (rad * (180/Math.PI));
	}
	
	public static String getAngle(int x, int y) {
		return "x:" + x + " y:" + y + " \tAngle: " + radToDeg(Math.atan2(y, x));
	}
	
	public static String getAngleBetweenPoints(double x1, double y1, double x2, double y2) {
		double delta_x = x2 - x1;
		double delta_y = y2 - y1;
		double theta_radians = Math.atan2(delta_y, delta_x);
		return "BAngle: " + radToDeg(theta_radians);
	}
	
	public static void main(String[] args) {
		System.out.println(getAngle(0,0));
		System.out.println(getAngle(0,1));
		System.out.println(getAngle(1,0));
		System.out.println(getAngle(1,1));
		System.out.println(getAngle(-0,-0));
		System.out.println(getAngle(0,-1));
		System.out.println(getAngle(-1,0));
		System.out.println(getAngle(-1,-1));
		System.out.println(getAngle(214,523));
		System.out.println(getAngleBetweenPoints(214, 523, 215, 524));
	}

}
