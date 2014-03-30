package info.flowersoft.helibomber;

import java.util.ArrayList;
import java.util.List;

import info.flowersoft.gameframe.shape.Shape;

public class Sky {

	private Shape background;
	
	private List<Cloud> cloudList;
	
	public Sky(GameContext context) {
		background = context.shapeFactory.createImage(context.res.skyImg, 0, 0, context.xmax, context.ymax, 0);
		background.setOrder(1000);
		
		cloudList = new ArrayList<Cloud>();
		
		for (int i = 0; i < 50; i++) {
			cloudList.add(new Cloud(context));
		}
	}
	
	public void update(double time) {
		for (Cloud cloud:cloudList) {
			cloud.update(time);
		}
	}
}
