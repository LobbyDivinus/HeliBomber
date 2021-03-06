package info.flowersoft.helibomber;

abstract public class Vehicle extends GameUpdateable {
	
	protected float x;
	
	protected float y;
	
	protected boolean alive;
	
	protected float life;
	
	protected float smokeCount;
	
	protected Control control;
	
	public Vehicle(GameContext context) {
		super(context);
		
		context.vehicleList.add(this);
		
		alive = true;
		life = getMaxLife();
	}
	
	public void setControl(Control c) {
		control = c;
	}
	
	public Control getControl() {
		return control;
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
			control.think();
			updateAliveVehicle(time);
			
			if (life < getMaxLife() / 2) {
				smokeCount += time;
				if (smokeCount >= 1) {
					new Smoke(x, y - 10, context);
					smokeCount = 0;
				}
			}
		} else {
			updateDeadVehicle(time);
		}
		
		if (x - context.camX >= - getWidth() && x - context.camX <= context.xmax + getWidth()) {
			if (alive) {
				drawAliveVehicle();
			} else {
				drawDeadVehicle();
			}
			
			show();
		} else {
			hide();
		}
		
		if (alive && life <= 0) {
			alive = false;
			onDead();
			control.onDead();
			float r = getCollisionRadius();
			for (int i = 0; i < 4; i++) {
				float angle = (float) (2 * Math.PI * Math.random());
				float px = x + (float) (Math.random() * r * Math.cos(angle));
				float py = y + (float) (Math.random() * r * Math.sin(angle)) - 10;
				new Explosion(px, py, context.res.explosionSound, context);
			}
		}
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public double getLife() {
		return life;
	}
	
	public double distanceTo(Vehicle v) {
		double dx = x - v.x;
		double dy = y - v.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	abstract protected void show();
	
	abstract protected void hide();
	
	abstract protected void drawAliveVehicle();
	
	abstract protected void drawDeadVehicle();
	
	abstract protected void updateAliveVehicle(double time);
	
	abstract protected void updateDeadVehicle(double time);
	
	abstract protected void onSpawn();
	
	abstract protected void onDead();
	
	abstract public float getMaxLife();
	
	abstract public float getCollisionX();
	
	abstract public float getCollisionY();
	
	abstract public float getCollisionRadius();
	
	abstract public float getWidth();
	
}
