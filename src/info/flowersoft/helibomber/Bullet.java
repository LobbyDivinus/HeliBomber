package info.flowersoft.helibomber;

import java.util.LinkedList;
import java.util.List;

import info.flowersoft.gameframe.shape.ImageShape;

public class Bullet extends GameUpdateable {
	
	private static List<ImageShape> shapePool = new LinkedList<ImageShape>();
	
	private Vehicle source;
	
	private ImageShape shape;
	
	private float x;
	
	private float y;
	
	private float speedX;
	
	private float speedY;
	
	private float angle;
	
	public Bullet(Vehicle source, float x, float y, float speedX, float speedY, float angle, GameContext context) {
		super(context);
		
		this.source = source;
		
		this.x = x;
		this.y = y;
		
		float speed = 300;
		
		this.speedX = speedX + speed * (float) Math.cos(angle);
		this.speedY = speedY + speed * (float) Math.sin(angle);
		
		this.angle = angle;
		
		if (shapePool.isEmpty()) {
			shape = context.shapeFactory.createImage(context.res.bulletImg, x - context.camX, y - context.camY);
			shape.midPivot();
			shape.setRotation(angle);
		} else {
			shape = shapePool.remove(0);
		}
	}

	@Override
	public void update(double time) {
		x += time * speedX;
		y += time * speedY;
		
		speedY += 98.1 * time;
		
		if (x - context.camX >= -4 || x - context.camX <= context.xmax + 4) {
			angle = (float) Math.atan2(speedY, speedX);
			shape.setPosition(x - context.camX, y - context.camY);
			shape.setRotation(angle);
			shape.show();
		} else {
			shape.hide();
		}
		
		if (x < -4 || x > context.mapWidth + 4) {
			dispose();
		} else {
			// Check for collision with vehicles
			Vehicle collisionVehicle = null;
			
			for (Vehicle v:context.vehicleList) {
				float cx = v.getCollisionX();
				float cy = v.getCollisionY();
				float r = v.getCollisionRadius();
				if (v != source && (x - cx) * (x - cx) + (y - cy) * (y - cy) <= r * r) {
					collisionVehicle = v;
				}
			}
			
			if (collisionVehicle != null) {
				// Boom on vehicle
				new Explosion(x, y, context.res.explosionSound, context);
				dispose();
			} else if (y >= context.terrain.getY(x)) {
				// Boom on ground
				new Explosion(x, context.terrain.getY(x) - 5, context.res.explosionEarthSound, context);
				dispose();
			}
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		shape.hide();
		shapePool.add(shape);
	}

}
