package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.shape.Shape;

public class Tank extends Vehicle {

	private ImageShape shape;
	
	private Shape gun;
	
	private float angle;
	
	private float frame;
	
	private float gunX;
	
	private float gunY;
	
	private float gunAngle;
	
	private float fireCount;
	
	public Tank(float x, GameContext context) {
		super(context);
		
		this.x = x;
		
		shape = context.shapeFactory.createImage(context.res.tankImg, 0, 0);
		shape.setPivot(shape.getWidth() / 2, shape.getHeight());
		shape.setOrder(context.objectOrder--);
		
		gun = context.shapeFactory.createImage(context.res.tankGunImg, x, 0);
		gun.midPivot();
		gun.setOrder(context.objectOrder--);
	}

	private void fire() {
		new Bullet(this, gunX, gunY, 0, 0, gunAngle, context);
	}
	
	public void setGunTarget(Vehicle v) {
		setGunTarget(v.x, v.y);
	}
	
	public void setGunTarget(float x, float y) {
		gunAngle = (float) (Math.atan2(y - gunY, x - gunX));
	}
	
	@Override
	public void show() {
		shape.show();
		if (isAlive()) {
			gun.show();
		}
	}
	
	@Override
	public void hide() {
		shape.hide();
		gun.hide();
	}
	
	@Override
	public void drawAliveVehicle() {
		shape.setRotation(angle);
		shape.setPosition(x - context.camX, y - context.camY);
		shape.setFrame(((int) frame) % 2);
		
		gun.setPosition(gunX - context.camX, gunY - context.camY);
		gun.setRotation(gunAngle);
	}
	
	@Override
	public void drawDeadVehicle() {
		shape.setRotation(angle);
		shape.setPosition(x - context.camX, y - context.camY + 5);
	}
	
	@Override
	public void updateAliveVehicle(double time) {
		x += 20 * time;
		
		angle = context.terrain.getAngle(x, 20);
		y = context.terrain.getY(x) + 3;
		
		frame += 10 * time;
		
		fireCount += time;
		if (fireCount > 0.75f) {
			fire();
			fireCount = 0;
		}
		
		gunX = x + (float) (20 * Math.sin(angle));
		gunY = y - (float) (20 * Math.cos(angle));
	}
	
	@Override
	protected void updateDeadVehicle(double time) {
	}

	@Override
	public float getMaxLife() {
		return 100;
	}

	@Override
	public float getCollisionX() {
		return x;
	}

	@Override
	public float getCollisionY() {
		return y;
	}

	@Override
	public float getCollisionRadius() {
		return 20;
	}

	@Override
	protected void onSpawn() {
		
	}

	@Override
	protected void onDead() {
		shape.setFrame(2);
		
		gun.hide();
	}

	@Override
	public float getWidth() {
		return 64;
	}
	
}
