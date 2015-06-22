package mil.clock.widget;

import mil.clock.widget.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MilClockActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitymain);
		TextView t = (TextView) findViewById(R.id.textView1);
		t.setText(MilClock.fiscalWeek + " " + MilClock.julianDate + " "
				+ MilClock.zuluDateTime);

	}
}
