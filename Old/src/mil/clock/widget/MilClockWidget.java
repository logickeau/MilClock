package mil.clock.widget;

import android.appwidget.AppWidgetProvider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class MilClockWidget extends AppWidgetProvider {

	DateFormat df = new SimpleDateFormat("hh:mm:ss");
	private static int year;
	private static int month;
	private static int day;

	/**
	 * Custom Intent name that is used by the AlarmManager to tell us to update
	 * the clock once per second.
	 */
	public static String CLOCK_WIDGET_UPDATE = "mil.clock.widget.MILCLOCK_WIDGET_UPDATE";
	private static final String URI_SCHEME = "MILCLOCK";

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {

			ComponentName thisAppWidget = new ComponentName(
					context.getPackageName(), getClass().getName());
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
			for (int appWidgetID : ids) {
				updateAppWidget(context, appWidgetManager, appWidgetID);

			}
		}
	}

	private PendingIntent createClockTickIntent(Context context) {
		Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(createClockTickIntent(context));
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 1);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				1000, createClockTickIntent(context));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Tell the AppWidgetManager to perform an update on the current
			// appwidget

			// Update The clock label using a shared method
			int[] initDate = MilClockWidgetConfig.loadTitlePref(context,
					appWidgetId);

			Date initD = new Date(initDate[0], initDate[1], initDate[2]);

			Calendar calFisc = Calendar.getInstance();
			calFisc.setTime(initD);

			year = calFisc.get(Calendar.YEAR) - 1900;
			month = calFisc.get(Calendar.MONTH);
			day = calFisc.get(Calendar.DATE);

			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.main);

		Intent intent = new Intent(context, MilClockWidgetConfig.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		Uri data = Uri.withAppendedPath(
				Uri.parse(URI_SCHEME + "://widget/id/"),
				String.valueOf(appWidgetId));
		intent.setData(data);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		views.setOnClickPendingIntent(R.id.wrapper, pendingIntent);

		int[] initDate = MilClockWidgetConfig.loadTitlePref(context,
				appWidgetId);

		year = initDate[0];
		month = initDate[1];
		day = initDate[2];

		Date initD = new Date(initDate[0], initDate[1], initDate[2]);

		Calendar calFisc = Calendar.getInstance();
		calFisc.setTime(initD);

		year = calFisc.get(Calendar.YEAR) - 1900;
		month = calFisc.get(Calendar.MONTH) + 1;
		day = calFisc.get(Calendar.DATE);

		switch (month) {
		case 1:
			month = Calendar.JANUARY;
			break;
		case 2:
			month = Calendar.FEBRUARY;
			break;
		case 3:
			month = Calendar.MARCH;
			break;
		case 4:
			month = Calendar.APRIL;
		case 5:
			month = Calendar.MAY;
			break;
		case 6:
			month = Calendar.JUNE;
			break;
		case 7:
			month = Calendar.JULY;
			break;
		case 8:
			month = Calendar.AUGUST;
			break;
		case 9:
			month = Calendar.SEPTEMBER;
			break;
		case 10:
			month = Calendar.OCTOBER;
			break;
		case 11:
			month = Calendar.NOVEMBER;
			break;
		case 12:
			month = Calendar.DECEMBER;
			break;
		default:
			month = Calendar.OCTOBER;
		}

		GregorianCalendar fiscalStart = new GregorianCalendar(year, month, day); // 000101

		MilClock fisc = new MilClock(fiscalStart);

		views.setTextViewText(R.id.currentWeek, fisc.getFiscalWeek());

		String[] zulu = fisc.getZulu();
		String ZuluFormatted = zulu[1] + " " + zulu[2] + " " + zulu[0];
		views.setTextViewText(R.id.julianDate, fisc.getJulian());
		views.setTextViewText(R.id.zuluTime, ZuluFormatted);
		// views.setTextViewText(R.id.zuluDayName, zulu[1]);
		// views.setTextViewText(R.id.zuluShortDate, zulu[2]);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		views.setTextViewText(R.id.actualTime, fisc.getDate(dateFormat));

		dateFormat = new SimpleDateFormat("EEE");
		// views.setTextViewText(R.id.actualDayName, fisc.getDate(dateFormat));

		dateFormat = new SimpleDateFormat("MM/dd");
		// views.setTextViewText(R.id.actualShortDate,
		// fisc.getDate(dateFormat));

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

}
