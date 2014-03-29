package info.flowersoft.helibomber;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.RGBColor;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;
import info.flowersoft.gameframe.shape.Shape;
import info.flowersoft.gameframe.shape.ShapeFactory;

public class Game extends AppRenderer {

	private int segmentWidth = 10;
	
	private int mapWidth = 8000;
	
	private int[] mapHeight;
	
	private int xmax;
	
	private int ymax;
	
	private ShapeFactory shapeFactory;
	
	private BlittingEngine blittingEngine;
	
	private FontDescription font;
	
	private Shape skyShape;
	
	private List<Cloud> cloudList;
	
	private Shape heliShape;
	private Shape rotorShape;
	
	public Game(Bundle savedInstance, Context con) {
		super(savedInstance, con);
	}

	@Override
	protected void onCreate(int w, int h) {
		loadTexture("grass", R.raw.grass);
		loadTexture("sky", R.raw.sky);
		loadTexture("clouds", R.raw.clouds, true);
		loadTexture("heli", R.raw.heli, true);
		loadTexture("rotor", R.raw.rotor, true);
		loadTexture("rotor2", R.raw.rotor2, true);
		loadTexture("font", R.raw.font, true);
		
		xmax = 800;
		ymax = xmax * h / w;
		
		shapeFactory = new ShapeFactory(getWorld(), getBuffer());
		shapeFactory.setVirtualResolution(0, 0, xmax, ymax);
		
		blittingEngine = new BlittingEngine(getBuffer());
		blittingEngine.setVirtualResolution(0, 0, xmax, ymax);
		
		createTerrain();
		
		skyShape = shapeFactory.createImage(new ImageDescription("sky", true, true), 0, 0, xmax, ymax, 0);
		skyShape.setOrder(1000);
		
		cloudList = new ArrayList<Cloud>();
		
		ImageDescription cloudImg = new ImageDescription("clouds", 128, 128, 8, true);
		for (int i = 0; i < 100; i++) {
			addCloud(cloudImg);
		}
		
		heliShape = shapeFactory.createImage(new ImageDescription("heli", true, true), xmax / 2, ymax / 2);
		heliShape.midPivot();
		rotorShape = shapeFactory.createImage(new ImageDescription("rotor", true, true), xmax / 2, ymax / 2);
		rotorShape.midPivot();
		
		font = new FontDescription("font", true, 22, 47, ' ', Character.MAX_VALUE);
	}
	
	private void addCloud(ImageDescription img) {
		Cloud cloud = new Cloud();
		int size = (int) (100 * Math.random()) + 300;
		cloud.cloud = shapeFactory.createImage(img, 0, 0, size, size, (int) (8 * Math.random()));
		cloud.x = (int) ((mapWidth + 400) * Math.random()) - 400;
		cloud.y = (int) ((ymax - 100) * Math.random()) - 200;
		cloud.xSpeed = 40 * (float) Math.random() * (ymax - 300 - cloud.y) / ymax;
		cloud.cloud.setOrder(999);
		
		cloud.cloud.getBrush().setTransparency((int) (16 * Math.random()));
		cloud.cloud.applyBrush();
		
		cloudList.add(cloud);
	}
	
	private void createTerrain() {
		Brush grassBrush = new Brush("grass");
		
		int count = mapWidth / segmentWidth;
		float[][] xy = new float[count][8];
		float[][] uv = new float[count][8];
		int[] height = new int[count + 1];
		
		for (int x = 0; x <= count; x++) {
			height[x] = ymax - 50 + (int) (25 - 50 * Math.random());
		}
		for (int i = 0; i < 30; i++) {
			for (int x = 0; x < count; x++) {
				height[x] = (height[x] + height[x + 1]) / 2;
			}
		}
		for (int x = 0; x <= count; x++) {
			height[x] += (int) (2 - 4 * Math.random());
		}
		
		for (int x = 0; x < count; x++) {
			xy[x][0] = segmentWidth * x;
			xy[x][1] = height[x];
			xy[x][2] = segmentWidth * x;
			xy[x][3] = ymax;
			xy[x][4] = segmentWidth * (x + 1);
			xy[x][5] = ymax;
			xy[x][6] = segmentWidth * (x + 1);
			xy[x][7] = height[x + 1];
			for (int j = 0; j < 8; j++) {
				uv[x][j] = xy[x][j] / 50;
			}
			uv[x][1] = 0;
			uv[x][3] = 1;
			uv[x][5] = 1;
			uv[x][7] = 0;
		}
		Shape grassShape = shapeFactory.createPolygons(xy, uv);
		grassShape.setBrush(grassBrush);
		
		mapHeight = height;
	}

	@Override
	protected void onResolutionChange(int width, int height) {
		shapeFactory.setBuffer(getBuffer());
		shapeFactory.setVirtualResolution(0, 0, xmax, ymax);
		
		blittingEngine.setBuffer(getBuffer());
		blittingEngine.setVirtualResolution(0, 0, xmax, ymax);
	}

	@Override
	protected void update(double time) {
		long ms = SystemClock.uptimeMillis();
		
		for (Cloud c:cloudList) {
			c.x += time * c.xSpeed;
			c.cloud.setPosition(c.x, c.y);
			if (c.x > mapWidth) {
				c.x = -400;
			}
		}
		
		rotorShape.setScale(Math.abs((float) Math.cos(ms / 20.0)), 1f);
	}

	@Override
	protected void beforeRendering() {
		getBuffer().clear();
	}

	@Override
	protected void afterRendering() {
		blittingEngine.setColor(0, 0, 0);
		blittingEngine.blitTextLine(font, String.valueOf((int) getFPS()) + " FPS", 0, 0);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
