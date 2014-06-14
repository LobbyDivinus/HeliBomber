package info.flowersoft.helibomber;

import java.util.ArrayList;
import com.threed.jpct.SimpleVector;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.Button;
import info.flowersoft.gameframe.TouchFrame;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;
import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.shape.ShapeFactory;
import info.flowersoft.gameframe.touch.TouchPoint;

public class Game extends AppRenderer {
	
	private GameContext context;
	
	private GameUI ui;
	
	public class GameUI implements IHumanInterface {
		private AccelerationVector acceleration;
		
		private Button cmdForce;
		
		public TouchFrame frame;

		@Override
		public boolean power() {
			return cmdForce.isPressed();
		}

		@Override
		public boolean fire() {
			return frame.isPressed();
		}

		@Override
		public float getFireX() {
			return frame.getTouchPoint().getX() * context.xmax / getWidth() + context.camX;
		}

		@Override
		public float getFireY() {
			return frame.getTouchPoint().getY() * context.ymax / getHeight() + context.camY;
		}

		@Override
		public SimpleVector getAccelerationVector() {
			return acceleration.get();
		}
	}
	
	public Game(Bundle savedInstance, Context con) {
		super(savedInstance, con);
		
		ui = new GameUI();
		ui.acceleration = new AccelerationVector(con);
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
		loadTexture("heligun", R.raw.gun, true);
		loadTexture("tankgun", R.raw.gun2, true);
		loadTexture("shot", R.raw.shot, true);
		loadTexture("explosion", R.raw.explosion, true);
		loadTexture("smoke", R.raw.smoke, true);
		
		context = new GameContext();
		
		context.xmax = 800;
		context.ymax = context.xmax * h / w;
		
		context.mapWidth = 8000;
		context.segmentWidth = 10;
		
		context.shapeFactory = new ShapeFactory(getWorld(), getBuffer());
		context.shapeFactory.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.blittingEngine = new BlittingEngine(getBuffer());
		context.blittingEngine.setVirtualResolution(0, 0, context.xmax, context.ymax);
		
		context.soundPool = new SoundPool(64, AudioManager.STREAM_MUSIC, 100);
		
		context.updateables = new ArrayList<GameUpdateable>();
		context.vehicleList = new ArrayList<Vehicle>();
		
		
		GameRessources res = new GameRessources();
		
		res.skyImg = new ImageDescription("sky", false, true);
		res.cloudImg = new ImageDescription("clouds", 128, 128, 8, true);
		res.heliImg = new ImageDescription("heli", 128, 32, 2, true);
		res.rotorImg = new ImageDescription("rotor", true, true);
		res.rotor2Img = new ImageDescription("rotor2", true, true);
		res.tankImg = new ImageDescription("tank", 90, 36, 3, true);
		res.treeImg = new ImageDescription("tree", true, true);
		res.font = new FontDescription("font", true, 22, 47, ' ', Character.MAX_VALUE);
		res.buttonImg = new ImageDescription("button", 100, 60, 2, true);
		res.heliGunImg = new ImageDescription("heligun", true, true);
		res.tankGunImg = new ImageDescription("tankgun", true, true);
		res.bulletImg = new ImageDescription("shot", 8, 3, 2, true);
		res.explosionImg = new ImageDescription("explosion", true, true);
		res.smokeImg = new ImageDescription("smoke", true, true);
		
		res.heliSound = context.soundPool.load(getContext(), R.raw.heli_sound, 1);
		res.explosionSound = context.soundPool.load(getContext(), R.raw.explosion_sound, 1);
		res.explosionEarthSound = context.soundPool.load(getContext(), R.raw.earthexplosion_sound, 1);
		
		context.res = res;
		
		
		
		context.sky = new Sky(context);
		
		context.terrain = new Terrain(context);
		
		
		Team teamHuman = new Team("Human");
		Team teamComputer = new Team("Computer");
		
		
		context.player = new Helicopter(600, context);
		context.player.setControl(new HelicopterHumanControl(context, teamHuman, ui, context.player));
		
		Tank t;
		
		t = new Tank(200, context);
		t.setControl(new TankKIControl(context, teamComputer, t));
		
		t = new Tank(300, context);
		t.setControl(new TankKIControl(context, teamComputer, t));
		
		t = new Tank(380, context);
		t.setControl(new TankKIControl(context, teamComputer, t));
		
		for (int i = 0; i < 20; i++) {
			new Tree((float) (context.mapWidth * Math.random()), context);
		}
		
		ui.frame = new TouchFrame(new ScreenRect(0, 0, getWidth(), getHeight()));
		registerTouchable(ui.frame);
		
		ImageShape btn = context.shapeFactory.createImage(res.buttonImg, context.xmax - 100, context.ymax - 60);
		btn.setOrder(-1000);
		
		ui.cmdForce = new Button(btn);
		registerTouchable(ui.cmdForce);
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
		
		for (GameUpdateable upl:new ArrayList<GameUpdateable>(context.updateables)) {
			upl.update(time);
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
