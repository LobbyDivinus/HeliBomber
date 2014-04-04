package info.flowersoft.helibomber;

import java.util.LinkedList;
import java.util.List;

import com.threed.jpct.Object3D;

import info.flowersoft.gameframe.shape.ImageShape;

public class Explosion extends GameUpdateable {

	private static List<ImageShape> shapePool = new LinkedList<ImageShape>();
	
	private ImageShape shape;
	
	private float x;
	
	private float y;
	
	private float p;
	
	private float smokeTimer;
	
	public Explosion(float x, float y, GameContext context) {
		super(context);
		
		this.x = x;
		this.y = y;
		
		if (shapePool.isEmpty()) {
			shape = context.shapeFactory.createImage(context.res.explosionImg, 0, 0);
			shape.midPivot();
			shape.getObject().setTransparencyMode(Object3D.TRANSPARENCY_MODE_ADD);
			shape.setOrder(-400);
			shape.hide();
		} else {
			shape = shapePool.remove(0);
		}
		
		shape.setRotation((float) (2 * Math.PI * Math.random()));
		
		smokeTimer = Float.MAX_VALUE;
	}

	@Override
	public void update(double time) {
		p += time;
		smokeTimer += time;
		
		if (smokeTimer > 2f) {
			new Smoke(x, y, context);
			smokeTimer = 0;
		}
		
		if (x - context.camX >= -64 && x - context.camX <= context.xmax + 64) {
			shape.setScale(0.5f * p, 0.5f * p);
			shape.getObject().setTransparency((int) (15 - 3 * p));
			
			shape.setPosition(x - context.camX, y - context.camY);
			shape.show();
		} else {
			shape.hide();
		}
		
		if (p > 5) {
			dispose();
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		shape.hide();
		shapePool.add(shape);
	}

}
