package info.flowersoft.helibomber;

abstract public class Vehicle {

	protected GameContext context;
	
	public Vehicle(GameContext context) {
		this.context = context;
		
		context.vehicleList.add(this);
	}
	
	abstract public void update(double time);
	
}
