package info.flowersoft.helibomber;

public class GameSound extends GameUpdateable {

	private int soundID;
	
	private int streamID;
	
	private boolean looping;
	
	private float volumeLeft;
	
	private float volumeRight;
	
	private float rate;
	
	private float x;
	
	private float y;
	
	public GameSound(int id, boolean loop, GameContext context) {
		super(context);
		
		looping = loop;
		
		soundID = id;
		
		rate = 1;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRate(float rate) {
		this.rate = rate;
	}

	@Override
	public void update(double time) {
		volumeLeft = 1;
		volumeRight = 1;
		
		if (streamID == 0) {
			int loop = 0;
			if (looping) {
				loop = -1;
			}
			streamID = context.soundPool.play(soundID, volumeLeft, volumeRight, 1, loop, rate);
			if (streamID != 0 && !looping) {
				dispose();
			}
		} else {
			context.soundPool.setVolume(streamID, volumeLeft, volumeRight);
			context.soundPool.setRate(streamID, rate);
		}
	}

}
