package info.flowersoft.helibomber;

public abstract class HumanControl extends Control {

	protected IHumanInterface humanInterface;
	public HumanControl(GameContext con, Team t, IHumanInterface hInterface) {
		super(con, t);
		humanInterface = hInterface;
	}

}
