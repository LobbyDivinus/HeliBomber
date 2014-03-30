package info.flowersoft.helibomber;

import com.threed.jpct.SimpleVector;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;
import info.flowersoft.gameframe.shape.Shape;
import info.flowersoft.gameframe.shape.ShapeFactory;

public class Game extends AppRenderer {
	
	private GameContext context;
	
	private Shape heliShape;
	private Shape rotorShape;
	
	private AccelerationVector acceleration;
	
	public Game(Bundle savedInstance, Context con) {
		super(savedInstance, con);
		
		acceleration = new AccelerationVector(con);
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
		
		context = new GameContext();
		
		context.xmax = 800;
		context.ymax = context.xmax * h / w;
		
		context.mapWidth = 8000;
		context.segmentWidth = 10;
		
		context.shapeFactory = new ShapeFactory(getWorld(), getBuffer());
		context.shapeFactory.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.blittingEngine = new BlittingEngine(getBuffer());
		context.blittingEngine.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.font = new FontDescription("font", true, 22, 47, ' ', Character.MAX_VALUE);
		
		
		
		GameRessources res = new GameRessources();
		
		res.skyImg = new ImageDescription("sky", false, true);
		
		res.cloudImg = new ImageDescription("clouds", 128, 128, 8, true);
		
		context.res = res;
		
		
		
		context.sky = new Sky(context);
		
		context.terrain = new Terrain(context);
		
		
		heliShape = context.shapeFactory.createImage(
				new ImageDescription("heli", true, true), context.xmax / 2, context.ymax / 2);
		heliShape.midPivot();
		rotorShape = context.shapeFactory.createImage(
				new ImageDescription("rotor", true, true), context.xmax / 2, context.ymax / 2);
		rotorShape.midPivot();
	}

	@Override
	protected void onResolutionChange(int width, int height) {
		context.shapeFactory.setBuffer(getBuffer());
		context.shapeFactory.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.blittingEngine.setBuffer(getBuffer());
		context.blittingEngine.setVirtualResolution(0, 0, context.xmax, context.ymax);
	}

	@Override
	protected void update(double time) {
		long ms = SystemClock.uptimeMillis();
		
		context.sky.update(time);
		context.terrain.update(time);
		
		rotorShape.setScale(Math.abs((float) Math.cos(ms / 20.0)), 1f);
		
		SimpleVector acc = acceleration.get();
		float roll = (float) Math.atan2(acc.x, acc.z);
		float angle = (float) (Math.sin(roll) * Math.atan2(acc.y, acc.x) + Math.cos(roll) * Math.atan2(acc.y, acc.z));
		angle *= -0.5f;
		heliShape.setRotation(angle);
		rotorShape.setRotation(angle);
	}

	@Override
	protected void beforeRendering() {
		getBuffer().clear();
	}

	@Override
	protected void afterRendering() {
		context.blittingEngine.setColor(0, 0, 0);
		context.blittingEngine.blitTextLine(context.font, String.valueOf((int) getFPS()) + " FPS", 0, 0);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
