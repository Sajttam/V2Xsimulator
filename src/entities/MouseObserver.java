package entities;

import java.awt.event.MouseEvent;

public class MouseObserver extends Entity implements EntityMouseListener{
	/**
	 * makes it possible to register mouse clicks on the map.
	 * used for diagnostics
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse_x: " + e.getX() + " Mouse_y: " + e.getY());
	}
}
