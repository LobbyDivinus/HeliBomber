package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.Shape;

public class Helicopter extends Vehicle {

	Shape shape;
	
	Shape rotor;
	
	Shape rotor2;
	
	float x;
	
	float y;
	
	float angle;
	
	float targetAngle;
	
	float xSpeed;
	
	float ySpeed;
	
	float speed;
	
	float targetSpeed;
	
	float sum;
	
	public Helicopter(float x, float y, GameContext context) {
		super(context);
		
		shape = context.shapeFactory.createImage(context.res.heliImg, x, y);
		shape.midPivot();
		
		rotor = context.shapeFactory.createImage(context.res.rotorImg, x, y);
		rotor.midPivot();
		
		rotor2 = context.shapeFactory.createImage(context.res.rotor2Img, x, y);
		rotor2.midPivot();
		
		this.x = x;
		this.y = y;
		
		speed = 10;
	}
	
	public void tilt(float targetAngle) {
		this.targetAngle = targetAngle;
	}
	
	public void control(float targetSpeed) {
		this.targetSpeed = targetSpeed;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	@Override
	public void update(double time) {
		sum += 2 * time * (speed + 5);
		angle += 4 * time * (targetAngle - angle);
		
		speed += 4 * time * (targetSpeed - speed);
		
		float force = 10 * speed;
		float forceX = -(float) Math.sin(angle) * force;
		float forceY = -(float) Math.cos(angle) * force + 98.1f;
		
		xSpeed *= 0.99;
		ySpeed *= 0.99;
		xSpeed += time * forceX;
		ySpeed += time * forceY;
		
		x += time * xSpeed;
		y += time * ySpeed;
		
		float height = context.terrain.getY(x) - y;
		if (height < 5) {
			angle += 16 * time * (context.terrain.getAngle(x, 5) - angle);
			y = context.terrain.getY(x) - 5;
			xSpeed *= 0.9f;
			ySpeed = 0;
		}
		
		shape.setPosition(x - context.camX, y - context.camY);
		shape.setRotation(angle);
		
		rotor.setPosition(x - context.camX, y - context.camY);
		rotor.setRotation(angle);
		rotor.setScale((float) Math.abs(Math.cos(sum)), 1);
		
		rotor2.setRotation(sum);
	}

}
