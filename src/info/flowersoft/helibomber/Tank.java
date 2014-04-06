package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.shape.Shape;

public class Tank extends Vehicle {

	private ImageShape shape;
	
	private Shape gun;
	
	private float x;
	
	private float y;
	
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
	
	public void setGunTarget(float x, float y) {
		gunAngle = (float) (Math.atan2(y - gunY, x - gunX));
	}
	
	@Override
	public void update(double time) {
		super.update(time);
		
		x += 20 * time;
		
		angle = context.terrain.getAngle(x, 20);
		y = context.terrain.getY(x) + 3;
		
		frame += 10 * time;
		
		shape.setRotation(angle);
		shape.setPosition(x - context.camX, y - context.camY);
		shape.setFrame(((int) frame) % 2);
		
		gunX = x + (float) (18 * Math.sin(angle));
		gunY = y - (float) (18 * Math.cos(angle));
		gun.setPosition(gunX - context.camX, gunY - context.camY);
		gun.setRotation(gunAngle);
		
		fireCount += time;
		if (fireCount > 0.75f) {
			fire();
			fireCount = 0;
		}
	}

	@Override
	protected float getMaxLife() {
		return 100;
	}

	@Override
	protected float getCollisionX() {
		return x;
	}

	@Override
	protected float getCollisionY() {
		return y;
	}

	@Override
	protected float getCollisionRadius() {
		return 20;
	}

}
