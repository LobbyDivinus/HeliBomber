package info.flowersoft.helibomber;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.shape.Shape;

public class Terrain {

	private GameContext context;
	
	private Shape shape;
	
	private int width;
	
	private float[] height;
	
	public Terrain(GameContext context) {
		this.context = context;
		
		width = context.mapWidth / context.segmentWidth;
		
		height = new float[width + 1];
		
		float[][] xy = new float[width][8];
		float[][] uv = new float[width][8];
		
		for (int x = 0; x <= width; x++) {
			height[x] = context.ymax - 50 + (int) (25 - 50 * Math.random());
		}
		for (int i = 0; i < 30; i++) {
			for (int x = 0; x < width; x++) {
				height[x] = (height[x] + height[x + 1]) / 2;
			}
		}
		for (int x = 0; x <= width; x++) {
			height[x] += (int) (2 - 4 * Math.random());
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
}
