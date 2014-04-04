package info.flowersoft.helibomber;

public abstract class GameUpdateable {

	protected GameContext context;
	
	public GameUpdateable(GameContext context) {
		this.context = context;
		
		context.updateables.add(this);
	}
	
	public void dispose() {
		context.updateables.remove(this);
	}
	
	abstract public void update(double time);
	
}
