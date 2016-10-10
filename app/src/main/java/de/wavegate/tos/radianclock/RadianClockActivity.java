package de.wavegate.tos.radianclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.wavegate.tos.radianclock.data.FractionApproximator;
import de.wavegate.tos.radianclock.data.RadianConverter;

public class RadianClockActivity extends AppCompatActivity {

	public static final String LOGTAG = "RadianClock";
	private static final int CLOCK_UPDATE_TIME = 1000;

	private ExecutorService clockService;
	private TextView traditionalTimeLB, radianTimeLB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radian_clock);

		traditionalTimeLB = (TextView) findViewById(R.id.traditional_time_lb);
		radianTimeLB = (TextView) findViewById(R.id.radian_time_lb);

		requestTime(new Date());
		setupExectutors();
	}

	@Override
	protected void onResume() {
		super.onResume();

		setupExectutors();
	}

	private void setupExectutors() {
		if (clockService != null) {
			clockService.shutdownNow();
		}

		clockService = Executors.newSingleThreadExecutor();
		clockService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (!clockService.isShutdown()) {
						requestTime(new Date());

						Thread.sleep(CLOCK_UPDATE_TIME);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.e(LOGTAG, "Loop interrupted!", e);
				}
			}
		});
	}

	private synchronized void requestTime(Date time) {
		//SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//String lod = preferences.getString("prefs_date_detail", getString(R.string.error_unknown));

		final RadianConverter converter = new RadianConverter(time);

		traditionalTimeLB.post(new Runnable() {
			@Override
			public void run() {
				traditionalTimeLB.setText(converter.getDateString());
			}
		});

		radianTimeLB.post(new Runnable() {
			@Override
			public void run() {
				String h = RadianConverter.radianToString(converter.getHourRadian());
				String m = RadianConverter.radianToString(converter.getMinuteRadian());
				String s = RadianConverter.radianToString(converter.getSecondRadian());

				radianTimeLB.setText(h + " : " + m + " : " + s);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (clockService != null) {
			clockService.shutdownNow();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(LOGTAG, "Method call: onRestart()");
	}
}
