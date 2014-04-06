package info.flowersoft.helibomber;

import java.util.List;

import android.media.SoundPool;
import info.flowersoft.gameframe.BlittingEngine;
import info.flowersoft.gameframe.shape.ShapeFactory;

public class GameContext {

	public GameRessources res;
	
	public int xmax;
	
	public int ymax;
	
	public int mapWidth;
	
	public int segmentWidth;
	
	public ShapeFactory shapeFactory;
	
	public BlittingEngine blittingEngine;
	
	public SoundPool soundPool;
	
	public float camX;
	
	public float camY;
	
	public Sky sky;
	
	public Terrain terrain;
	
	public Helicopter player;
	
	public List<GameUpdateable> updateables;
	
	public List<Vehicle> vehicleList;
	
	public int objectOrder;
	
}
