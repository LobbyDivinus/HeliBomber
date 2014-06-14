package info.flowersoft.helibomber;

public class Team {

	private String name;
	
	public Team(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAgainst(Team t) {
		return this != t;
	}
	
}
