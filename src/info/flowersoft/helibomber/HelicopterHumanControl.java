package info.flowersoft.helibomber;

import info.flowersoft.gameframe.touch.TouchPoint;

import com.threed.jpct.SimpleVector;

public class HelicopterHumanControl extends HumanControl {

	private Helicopter heli;
	
	public HelicopterHumanControl(GameContext con, Team t, IHumanInterface hI, Helicopter helicopter) {
		super(con, t, hI);
		heli = helicopter;
	}

	@Override
	public void onSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void think() {
		SimpleVector acc = humanInterface.getAccelerationVector();
		float roll = (float) Math.atan2(acc.x, acc.z);
		float angle = (float) (Math.sin(roll) * Math.atan2(acc.y, acc.x) + Math.cos(roll) * Math.atan2(acc.y, acc.z));
		context.player.tilt(angle);
		
		if (humanInterface.power()) {
			context.player.controlUp();
		} else {
			context.player.controlDown();
		}
		
		if (humanInterface.fire()) {
			context.player.setGunTarget(humanInterface.getFireX(), humanInterface.getFireY());
			
			context.player.setFireMode(true);
		} else {
			context.player.setFireMode(false);
		}
	}

	@Override
	public Vehicle getVehicle() {
		return heli;
	}

}
