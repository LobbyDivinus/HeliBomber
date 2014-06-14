package info.flowersoft.helibomber;

import com.threed.jpct.SimpleVector;

public interface IHumanInterface {

	boolean power();
	
	boolean fire();
	
	float getFireX();
	
	float getFireY();
	
	SimpleVector getAccelerationVector();
	
}
