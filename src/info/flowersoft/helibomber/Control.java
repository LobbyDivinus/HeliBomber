package info.flowersoft.helibomber;

import java.util.LinkedList;
import java.util.List;

public abstract class Control {

	protected GameContext context;
	
	protected Team team;
	
	public Control(GameContext con, Team t) {
		context = con;
		team = t;
	}
	
	public Team getTeam() {
		return team;
	}
	
	protected List<Vehicle> getEnemies() {
		List<Vehicle> list = new LinkedList<Vehicle>();
		
		for (Vehicle v : context.vehicleList) {
			if (v.isAlive() && team.isAgainst(v.getControl().getTeam())) {
				list.add(v);
			}
		}
		
		return list;
	}
	
	protected Vehicle getNextEnemy() {
		double dist = Double.MAX_VALUE;
		Vehicle enemy = null;
		
		for (Vehicle v : context.vehicleList) {
			if (v.isAlive() && team.isAgainst(v.getControl().getTeam())) {
				if (getVehicle().distanceTo(v) < dist) {
					dist = getVehicle().distanceTo(v);
					enemy = v;
				}
			}
		}
		
		return enemy;
	}
	
	abstract public void onSpawn();
	
	abstract public void onDead();
	
	abstract public void think();
	
	abstract public Vehicle getVehicle();
	
}
