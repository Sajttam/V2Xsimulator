package models.map;

import java.util.List;

import controller.Controller;
import entities.Entity;

public interface SimulationMap{
	public List<Entity> getMap(Controller controller);
}
