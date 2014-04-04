package info.flowersoft.helibomber;

import java.util.ArrayList;
import java.util.List;

import info.flowersoft.gameframe.shape.Shape;

public class Sky extends GameUpdateable {

	private Shape background;
	
	private List<Cloud> cloudList;
	
	public Sky(GameContext context) {
		super(context);
		
		background = context.shapeFactory.createImage(context.res.skyImg, 0, 0, context.xmax, context.ymax, 0);
		background.setOrder(1000);
		
		cloudList = new ArrayList<Cloud>();
		
		for (int i = 0; i < 20; i++) {
			cloudList.add(new Cloud(context));
		}
	}
	
	public void update(double time) {
		
	}
}
