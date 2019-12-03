package entities;

import java.awt.event.MouseEvent;

public class MouseObserver extends Entity implements EntityMouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse_x: " + e.getX() + " Mouse_y: " + e.getY());
	}

}
