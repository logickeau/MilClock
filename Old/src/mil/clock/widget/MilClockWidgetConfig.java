/*
Long initDate = MilClockWidgetConfig.loadTitlePref(context, appWidgetId);
	        
	        Date initD = new Date(initDate);
	        
	        //year = initD.getYear();
	        Calendar calFisc= Calendar.getInstance();
	        calFisc.setTime(initD);
			
			year = calFisc.get(Calendar.YEAR) - 1900;
			month = calFisc.get(Calendar.MONTH);
			day = calFisc.get(Calendar.DATE); * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mil.clock.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import java.util.ArrayList;

/**
 * The configuration screen for the ExampleAppWidgetProvider widget sample.
 */
public class MilClockWidgetConfig extends Activity {
	static final String TAG = "MilClockWidgetConfig";

	private static final String PREFS_NAME = "com.example.android.apis.appwidget.MilClockWidget";
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	DatePicker mAppWidgetPrefix;

	public MilClockWidgetConfig() {
		super();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Set the result to CANCELED. This will cause the widget host to cancel
		// out of the widget placement if they press the back button.
		setResult(RESULT_CANCELED);

		// Set the view layout resource to use.
		setContentView(R.layout.config);

		// Find the EditText
		mAppWidgetPrefix = (DatePicker) findViewById(R.id.datePicker);
		// mAppWidgetPrefix.init(2011,9, 01, null);
		// Bind the action for the save button.
		findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		final Context context = MilClockWidgetConfig.this;
		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
		int[] initDate = MilClockWidgetConfig.loadTitlePref(context,
				mAppWidgetId);

		mAppWidgetPrefix.init(initDate[0], initDate[1], initDate[2], null);

	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final Context context = MilClockWidgetConfig.this;

			// When the button is clicked, save the string in our prefs and
			// return that they
			// clicked OK.
			// Date fiscal = new
			// Date(mAppWidgetPrefix.getYear(),mAppWidgetPrefix.getMonth(),
			// mAppWidgetPrefix.getDayOfMonth());
			// Long titlePrefix = fiscal.getTime();
			saveTitlePref(context, mAppWidgetId, mAppWidgetPrefix.getYear(),
					mAppWidgetPrefix.getMonth(),
					mAppWidgetPrefix.getDayOfMonth());

			// Push widget update to surface with newly set prefix
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			MilClockWidget.updateAppWidget(context, appWidgetManager,
					mAppWidgetId);

			// Make sure we pass back the original appWidgetId
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	};

	// Write the prefix to the SharedPreferences object for this widget
	static void saveTitlePref(Context context, int appWidgetId, int year,
			int month, int day) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		// prefs.putLong(PREF_PREFIX_KEY + appWidgetId, fiscal);

		prefs.putInt("year" + appWidgetId, year);
		prefs.putInt("month" + appWidgetId, month);
		prefs.putInt("day" + appWidgetId, day);
		prefs.commit();
	}

	// Read the prefix from the SharedPreferences object for this widget.
	// If there is no preference saved, get the default from a resource
	static int[] loadTitlePref(Context context, int appWidgetId) {
		int date[] = new int[3];
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		date[0] = prefs.getInt("year" + appWidgetId, 2011);
		date[1] = prefs.getInt("month" + appWidgetId, 9);
		date[2] = prefs.getInt("day" + appWidgetId, 01);
		{
			return date;
		}
	}

	static void deleteTitlePref(Context context, int appWidgetId) {

	}

	static void loadAllTitlePrefs(Context context,
			ArrayList<Integer> appWidgetIds, ArrayList<Long> texts) {
	}
}