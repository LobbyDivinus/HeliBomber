package info.flowersoft.helibomber;

import com.threed.jpct.SimpleVector;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.shape.Shape;

public class Terrain extends GameUpdateable {

	private Shape shape;
	
	private int width;
	
	private float[] height;
	
	public Terrain(GameContext context) {
		super(context);
		
		width = context.mapWidth / context.segmentWidth;
		
		height = new float[width + 1];
		
		float[][] xy = new float[width][8];
		float[][] uv = new float[width][8];
		
		for (int x = 0; x <= width; x++) {
			height[x] = (float) (context.ymax - 50 + 25 - 50 * Math.random());
		}
		for (int i = 0; i < 30; i++) {
			for (int x = 0; x < width; x++) {
				height[x] = (height[x] + height[x + 1]) / 2;
			}
		}
		for (int x = 0; x <= width; x++) {
			height[x] += 1 - 2 * Math.random();
		}
		
		for (int x = 0; x < width; x++) {
			xy[x][0] = context.segmentWidth * x;
			xy[x][1] = height[x];
			xy[x][2] = context.segmentWidth * x;
			xy[x][3] = context.ymax;
			xy[x][4] = context.segmentWidth * (x + 1);
			xy[x][5] = context.ymax;
			xy[x][6] = context.segmentWidth * (x + 1);
			xy[x][7] = height[x + 1];
			for (int j = 0; j < 8; j++) {
				uv[x][j] = xy[x][j] / 50;
			}
			uv[x][1] = 0;
			uv[x][3] = 1;
			uv[x][5] = 1;
			uv[x][7] = 0;
		}
		
		shape = context.shapeFactory.createPolygons(xy, uv);
		shape.setBrush(new Brush("grass"));
	}
	
	public void update(double time) {
		shape.setPosition(-context.camX, -context.camY);
	}
	
	public float getY(float x) {
		if (x < 0) {
			return height[0];
		} else if (x >= context.mapWidth) {
			return height[height.length - 1];
		} else {
			int startIdx = (int) (x / context.segmentWidth);
			if (x == startIdx * context.segmentWidth) {
				return height[startIdx];
			} else {
				int endIdx = startIdx + 1;
				float p = (x - startIdx * context.segmentWidth) / context.segmentWidth;
				float y = height[startIdx] + p * (height[endIdx] - height[startIdx]);
				return y;
			}
		}
	}
	
	public SimpleVector getNormal(float x, float width) {
		float leftY = getY(x - width / 2);
		float rightY = getY(x + width / 2);
		
		float incline = (rightY - leftY) / width;
		
		SimpleVector result = new SimpleVector(-incline, 1, 0);
		
		return result.normalize();
	}
	
	public float getAngle(float x, float width) {
		float leftY = getY(x - width / 2);
		float rightY = getY(x + width / 2);
		
		float incline = (rightY - leftY) / width;
		
		return (float) Math.atan2(-incline, 1);
	}
	
}
