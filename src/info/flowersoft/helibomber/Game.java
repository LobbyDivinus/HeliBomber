package info.flowersoft.helibomber;

import java.util.ArrayList;

import com.threed.jpct.SimpleVector;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.Button;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;
import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.shape.Shape;
import info.flowersoft.gameframe.shape.ShapeFactory;

public class Game extends AppRenderer {
	
	private GameContext context;
	
	private Shape heliShape;
	private Shape rotorShape;
	
	private AccelerationVector acceleration;
	
	private Button cmdForce;
	
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
		loadTexture("tank", R.raw.tank, true);
		loadTexture("tree", R.raw.tree, true);
		loadTexture("font", R.raw.font, true);
		loadTexture("button", R.raw.button, true);
		
		
		context = new GameContext();
		
		context.xmax = 800;
		context.ymax = context.xmax * h / w;
		
		context.mapWidth = 8000;
		context.segmentWidth = 10;
		
		context.shapeFactory = new ShapeFactory(getWorld(), getBuffer());
		context.shapeFactory.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.blittingEngine = new BlittingEngine(getBuffer());
		context.blittingEngine.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.updateables = new ArrayList<GameUpdateable>();
		
		
		GameRessources res = new GameRessources();
		
		res.skyImg = new ImageDescription("sky", false, true);
		res.cloudImg = new ImageDescription("clouds", 128, 128, 8, true);
		res.heliImg = new ImageDescription("heli", true, true);
		res.rotorImg = new ImageDescription("rotor", true, true);
		res.rotor2Img = new ImageDescription("rotor2", true, true);
		res.tankImg = new ImageDescription("tank", 90, 36, 3, true);
		res.treeImg = new ImageDescription("tree", true, true);
		res.font = new FontDescription("font", true, 22, 47, ' ', Character.MAX_VALUE);
		res.buttonImg = new ImageDescription("button", 100, 60, 2, true);
		
		context.res = res;
		
		
		
		context.sky = new Sky(context);
		
		context.terrain = new Terrain(context);
		
		
		context.player = new Helicopter(400, 300, context);
		
		new Tank(300, context);
		
		for (int i = 0; i < 100; i++) {
			new Tree((float) (context.mapWidth * Math.random()), context);
		}
		
		
		ImageShape btn = context.shapeFactory.createImage(res.buttonImg, context.xmax - 100, context.ymax - 60);
		btn.setOrder(-1000);
		
		cmdForce = new Button(btn);
		registerTouchable(cmdForce);
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
		
		for (GameUpdateable upl:context.updateables) {
			upl.update(time);
		}
		
		SimpleVector acc = acceleration.get();
		float roll = (float) Math.atan2(acc.x, acc.z);
		float angle = (float) (Math.sin(roll) * Math.atan2(acc.y, acc.x) + Math.cos(roll) * Math.atan2(acc.y, acc.z));
		angle *= -1f;
		context.player.tilt(angle);
		
		if (cmdForce.isPressed()) {
			context.player.control(18);
		} else {
			context.player.control(0);
		}
		
		context.camX += 2 * time * (
				Math.max(Math.min(context.player.getX() - context.xmax / 2, context.mapWidth - context.xmax), 0)
				- context.camX);
	}

	@Override
	protected void beforeRendering() {
		getBuffer().clear();
	}

	@Override
	protected void afterRendering() {
		context.blittingEngine.setColor(0, 0, 0);
		context.blittingEngine.blitTextLine(context.res.font, String.valueOf((int) getFPS()) + " FPS", 0, 0);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
