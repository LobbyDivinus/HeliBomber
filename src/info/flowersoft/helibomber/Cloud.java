package info.flowersoft.helibomber;

import info.flowersoft.gameframe.shape.Shape;

public class Cloud {

	GameContext context;
	
	Shape shape;
	
	float x;
	
	float y;
	
	float xSpeed;
	
	public Cloud(GameContext context) {
		this.context = context;
		
		int size = (int) (100 * Math.random()) + 300;
		int frame = (int) (8 * Math.random());
		int trans = (int) (16 * Math.random());
		
		x = (int) ((context.mapWidth + 400) * Math.random()) - 400;
		y = (int) ((context.ymax - 100) * Math.random()) - 200;
		
		shape = context.shapeFactory.createImage(context.res.cloudImg, x, y, size, size, frame);
		shape.setOrder(999);
		shape.getBrush().setTransparency(trans);
		shape.applyBrush();
		
		xSpeed = 40 * (float) Math.random() * (context.ymax - 300 - y) / context.ymax;
	}
	
	public void update(double time) {
		x += time * xSpeed;
		if (x > context.mapWidth) {
			x = -300;
		}
		
		shape.setPosition(x - context.camX, y - context.camY);
	}
	
}
