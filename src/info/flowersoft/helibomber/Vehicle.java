package info.flowersoft.helibomber;

abstract public class Vehicle extends GameUpdateable {
	
	protected float life;
	
	public Vehicle(GameContext context) {
		super(context);
		
		context.vehicleList.add(this);
		
		life = getMaxLife();
	}
	
	public void dispose() {
		super.dispose();
		
		context.vehicleList.remove(this);
	}

	abstract public void update(double time);
	
	abstract protected float getMaxLife();
	
	abstract protected float getCollisionX();
	
	abstract protected float getCollisionY();
	
	abstract protected float getCollisionRadius();
	
}
