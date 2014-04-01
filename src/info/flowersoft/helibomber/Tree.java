package info.flowersoft.helibomber;

import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import info.flowersoft.gameframe.shape.ImageShape;

public class Tree extends GameUpdateable {

	ImageShape shape;
	
	float x;
	
	float y;
	
	float angle;
	
	public Tree(float x, GameContext context) {
		super(context);
		
		this.x = x;
		y = context.terrain.getY(x);
		angle = context.terrain.getAngle(x, 5);
		
		shape = context.shapeFactory.createImage(context.res.treeImg, x, y);
		shape.midPivot();
		
		if (Math.random() < 0.5) {
			shape.setOrder(500);
		} else {
			shape.setOrder(-500);
		}
	}

	@Override
	public void update(double time) {
		if (x - context.camX >= -100 && x - context.camX <= context.xmax + 100) {
			shape.setPosition(x - context.camX, y - context.camY);
			shape.show();
		} else {
			shape.hide();
		}
	}

}
