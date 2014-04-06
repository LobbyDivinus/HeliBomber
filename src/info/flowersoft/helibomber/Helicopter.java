package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.Shape;

public class Helicopter extends Vehicle {

	private final static float MIN_SPEED = 0;
	
	private final static float MAX_SPEED = 18;
	
	private Shape shape;
	
	private Shape rotor;
	
	private Shape rotor2;
	
	private Shape gun;
	
	private float x;
	
	private float y;
	
	private float gunX;
	
	private float gunY;
	
	private float gunAngle;
	
	private float angle;
	
	private float targetAngle;
	
	private float xSpeed;
	
	private float ySpeed;
	
	private float speed;
	
	private float targetSpeed;
	
	private float sum;
	
	private boolean fireMode;
	
	private float fireCount;
	
	private GameSound rotorSound;
	
	public Helicopter(float x, GameContext context) {
		super(context);
		
		shape = context.shapeFactory.createImage(context.res.heliImg, x, y);
		shape.midPivot();
		shape.setOrder(context.objectOrder--);
		
		rotor = context.shapeFactory.createImage(context.res.rotorImg, x, y);
		rotor.midPivot();
		rotor.setOrder(context.objectOrder);
		
		rotor2 = context.shapeFactory.createImage(context.res.rotor2Img, x, y);
		rotor2.midPivot();
		rotor2.setOrder(context.objectOrder);
		
		gun = context.shapeFactory.createImage(context.res.heliGunImg, x, y);
		gun.midPivot();
		gun.setOrder(context.objectOrder--);
		
		this.x = x;
		this.y = context.terrain.getY(x) - 15;
		
		speed = MIN_SPEED;
		targetSpeed = MIN_SPEED;
		
		rotorSound = new GameSound(context.res.heliSound, true, context);
	}
	
	public void tilt(float targetAngle) {
		this.targetAngle = targetAngle;
	}
	
	public void control(float targetSpeed) {
		this.targetSpeed = targetSpeed;
	}
	
	public void controlUp() {
		control(18);
	}
	
	public void controlDown() {
		control(0);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setGunTarget(float x, float y) {
		gunAngle = (float) Math.atan2(y - gunY, x - gunX);
	}
	
	public void setFireMode(boolean state) {
		fireMode = state;
	}
	
	private void fire() {
		new Bullet(
				this,
				gunX,
				gunY,
				xSpeed,
				ySpeed,
				gunAngle,
				context);
	}

	@Override
	public void update(double time) {
		super.update(time);
		
		sum += 2 * time * (speed + 8);
		angle += 4 * time * (targetAngle - angle);
		
		speed += 4 * time * (targetSpeed - speed);
		
		rotorSound.setRate(0.5f + 1.5f * (speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED));
		
		float force = 10 * speed;
		float forceX = (float) Math.sin(angle) * force;
		float forceY = -(float) Math.cos(angle) * force + 98.1f;
		
		if (y < 60) {
			forceY += 10 * (60 - y);
		}
		
		if (x < 200) {
			forceX += context.xmax / 2 - x;
		}
		
		if (x > context.mapWidth - 200) {
			forceX += context.mapWidth - context.xmax / 2 - x;
		}
		
		xSpeed *= 0.99;
		ySpeed *= 0.99;
		xSpeed += time * forceX;
		ySpeed += time * forceY;
		
		x += time * xSpeed;
		y += time * ySpeed;
		
		float height = context.terrain.getY(x, 10) - y;
		if (height < 4) {
			if (ySpeed > 80) {
				life -= ySpeed - 80;
			}
			
			angle += 16 * time * (context.terrain.getAngle(x, 10) - angle);
			y = context.terrain.getY(x, 10) - 4;
			xSpeed *= 0.9f;
			ySpeed = 0;
		}
		
		if (x - context.camX >= -80 && x - context.camX <= context.xmax + 80) {
			shape.setPosition(x - context.camX, y - context.camY);
			shape.setRotation(angle);
			
			rotor.setPosition(x - context.camX, y - context.camY);
			rotor.setRotation(angle);
			rotor.setScale((float) Math.cos(sum), 1);
			
			rotor2.setPosition(
					x - context.camX + (float) (35 * Math.cos(angle) + 5 * Math.sin(angle)),
					y - context.camY + (float) (35 * Math.sin(angle) - 5 * Math.cos(angle)));
			rotor2.setRotation(sum);
			
			gunX = x + (float) (5 * Math.cos(angle - Math.PI * 3 / 4));
			gunY = y + (float) (5 * Math.sin(angle - Math.PI * 3 / 4));
			gun.setPosition(gunX - context.camX, gunY - context.camY);
			gun.setRotation(gunAngle);
			
			shape.show();
			rotor.show();
			rotor2.show();
			gun.show();
		} else {
			shape.hide();
			rotor.hide();
			rotor2.hide();
			gun.hide();
		}
		
		fireCount += time;
		if (fireMode) {
			if (fireCount > 0.75f) {
				fire();
				fireCount = 0;
			}
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
