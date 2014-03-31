package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.ImageShape;

public class Tank extends Vehicle {

	private ImageShape shape;
	
	private float x;
	
	private float y;
	
	private float frame;
	
	public Tank(float x, GameContext context) {
		super(context);
		
		this.x = x;
		
		shape = context.shapeFactory.createImage(context.res.tankImg, 0, 0);
		shape.setPivot(shape.getWidth() / 2, shape.getHeight());
	}

	@Override
	public void update(double time) {
		x += 20 * time;
		
		float angle = context.terrain.getAngle(x, 20);
		y = context.terrain.getY(x) + 3;
		
		frame += 10 * time;
		
		shape.setRotation(angle);
		shape.setPosition(x - context.camX, y - context.camY);
		shape.setFrame(((int) frame) % 2);
	}

}
