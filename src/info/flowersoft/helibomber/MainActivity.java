package info.flowersoft.helibomber;

import info.flowersoft.gameframe.AppActivity;
import info.flowersoft.gameframe.AppRenderer;
import android.os.Bundle;

public class MainActivity extends AppActivity {

	@Override
	public AppRenderer getRenderer(Bundle savedInstanceState) {
		return new Game(savedInstanceState, this);
	}

	

}
