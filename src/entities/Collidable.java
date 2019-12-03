package entities;
/**
 * This interface is used in order for Controller to check only if a Entity is suppose to interact with other object in the game.  
 *
 * @author 
 * @version 2019-12-03
 */
public interface Collidable {

    public void collision(Entity other);

}
