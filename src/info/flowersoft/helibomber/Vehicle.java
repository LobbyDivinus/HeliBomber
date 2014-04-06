package info.flowersoft.helibomber;

abstract public class Vehicle extends GameUpdateable {
	
	protected boolean alive;
	
	protected float life;
	
	protected float smokeCount;
	
	public Vehicle(GameContext context) {
		super(context);
		
		context.vehicleList.add(this);
		
		alive = true;
		life = getMaxLife();
	}
	
	public void dispose() {
		super.dispose();
		
		context.vehicleList.remove(this);
	}
	
	public void hit() {
		life -= 10;
	}

	public void update(double time) {
		if (alive) {
			if (life < getMaxLife() / 2) {
				smokeCount += time;
				if (smokeCount >= 1) {
					new Smoke(getCollisionX(), getCollisionY(), context);
					smokeCount = 0;
				}
			}
		}
		
		if (alive && life <= 0) {
			alive = false;
			// onDead();
			float x = getCollisionX();
			float y = getCollisionY();
			float r = getCollisionRadius();
			for (int i = 0; i < 8; i++) {
				float angle = (float) (2 * Math.PI * Math.random());
				float px = x + (float) (Math.random() * r * Math.cos(angle));
				float py = y + (float) (Math.random() * r * Math.sin(angle));
				new Explosion(px, py, context.res.explosionSound, context);
			}
		}
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	abstract protected float getMaxLife();
	
	abstract protected float getCollisionX();
	
	abstract protected float getCollisionY();
	
	abstract protected float getCollisionRadius();
	
}
