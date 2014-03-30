package info.flowersoft.helibomber;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.threed.jpct.SimpleVector;

public class AccelerationVector {

	private SimpleVector vector;
	
	private float[] values;
	
	public SensorEventListener listener;
	
	public AccelerationVector(Context context) {
		vector = new SimpleVector();
		
		values = new float[3];
		
		listener = new SensorEventListener() {

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized (values) {
					for (int i = 0; i < 3; i++) {
						values[i] = event.values[i];
					}
				}
			}
			
		};
		
		
		
		SensorManager mgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
		Sensor sensor = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		mgr.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public SimpleVector get() {
		synchronized (values) {
			vector.x = values[0];
			vector.y = values[1];
			vector.z = values[2];
			return vector;
		}
	}
}
