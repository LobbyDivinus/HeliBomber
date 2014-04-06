package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.ImageShape;

import java.util.LinkedList;
import java.util.List;

public class Smoke extends GameUpdateable {

	private static List<ImageShape> shapePool = new LinkedList<ImageShape>();
	
	private ImageShape shape;
	
	private float x;
	
	private float y;
	
	private float speedX;
	
	private float speedY;
	
	private float size;
	
	private float angle;
	
	private float angleSpeed;
	
	public Smoke(float x, float y, GameContext context) {
		super(context);
		
		this.x = x;
		this.y = y;
		
		if (shapePool.isEmpty()) {
			shape = context.shapeFactory.createImage(context.res.smokeImg, 0, 0);
			shape.midPivot();
			shape.setOrder(-300);
			shape.hide();
		} else {
			shape = shapePool.remove(0);
		}
		
		angle = (float) (2 * Math.PI * Math.random());
		angleSpeed = 0.5f * (float) (0.5 - Math.random());
		
		speedX = 10 * (float) Math.random();
		speedY = -20 * (float) Math.random() - 30;
	}

	@Override
	public void update(double time) {
		size += time;
		angle += time * angleSpeed;
		
		speedY -= time * 0.981;
		
		x += time * speedX;
		y += time * speedY;
		
		if (x - context.camX >= -64 && x - context.camX <= context.xmax + 64) {
			shape.getObject().setTransparency((int) (16 / (1 + size)));
			
			shape.setScale(1 + size, 1 + size);
			shape.setPosition(x - context.camX, y - context.camY);
			shape.setRotation(angle);
			shape.show();
		} else {
			shape.hide();
		}
		
		if (y < -64) {
			dispose();
		}
	}

	public void dispose() {
		super.dispose();
		
		shape.hide();
		shapePool.add(shape);
	}
	
}
