package info.flowersoft.helibomber;

abstract public class Vehicle extends GameUpdateable {
	
	public Vehicle(GameContext context) {
		super(context);
	}

	abstract public void update(double time);
	
}
