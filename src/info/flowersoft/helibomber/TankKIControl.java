package info.flowersoft.helibomber;

public class TankKIControl extends KIControl {

	private Tank tank;
	
	public TankKIControl(GameContext con, Team t, Tank tank) {
		super(con, t);
		this.tank = tank;
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
		Vehicle enemy = getNextEnemy();
		if (enemy != null) {
			tank.setGunTarget(enemy);
		}
	}

	@Override
	public Vehicle getVehicle() {
		return tank;
	}
	
	
}
