package info.flowersoft.helibomber;

public class HelicopterHumanControl extends HumanControl {

	private Helicopter heli;
	
	public HelicopterHumanControl(GameContext con, Team t, Helicopter helicopter) {
		super(con, t);
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
		
	}

	@Override
	public Vehicle getVehicle() {
		return heli;
	}

}
