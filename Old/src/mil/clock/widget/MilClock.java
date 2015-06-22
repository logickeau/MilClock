package mil.clock.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * MilClockCalendar. Calendar class wrapper with methods to support military
 * fiscal dates
 * 
 * Logickeau
 */
public class MilClock {

	private static GregorianCalendar fiscalStart = new GregorianCalendar(); // 000101
	private GregorianCalendar current = new GregorianCalendar(getCalendar()
			.get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
			getCalendar().get(Calendar.DATE));
	private static GregorianCalendar limitDate; // "MM/dd/yyyy"
	private static Boolean islimitDateActive = Boolean.FALSE;
	public static DateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static final int LENGTH_OF_WEEK = 7 * 24 * 60 * 60 * 1000;
	public static String fiscalWeek;
	public static String julianDate;
	public static String zuluDateTime;

	/**
	 * Constructor for FiscalCalendar.
	 * 
	 * @param yearStart
	 */
	public MilClock(GregorianCalendar yearStart) {
		fiscalStart = yearStart;
	}

	/**
	 * Method getDate. Returns date string specified by the
	 * <code>DateFormat</code> passed to the method. <code>FiscalCalendar</code>
	 * has several premade static <code>DateFormat</code> classes for ease of
	 * use.
	 * 
	 * @param format
	 * @return String
	 */
	public String getDate(DateFormat format) {
		if (islimitDateActive.equals(Boolean.TRUE)) {
			return format.format(limitDate.getTime());
		} else {
			return format.format(new GregorianCalendar().getTime());
		}
	}

	/**
	 * Method getDate. Returns the current date or limited date as a
	 * <code>Date</code> object.
	 * 
	 * @return Date
	 */
	public Date getDate() {
		if (islimitDateActive.equals(Boolean.TRUE)) {
			return limitDate.getTime();
		} else {
			return new GregorianCalendar().getTime();
		}
	}

	/**
	 * Method getCalendar.
	 * 
	 * @return GregorianCalendar
	 */
	public GregorianCalendar getCalendar() {
		if (islimitDateActive.equals(Boolean.TRUE)) {
			return limitDate;
		} else {
			return new GregorianCalendar();
		}
	}

	/**
	 * Method getFiscalYearStart.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param format
	 * @return String
	 */
	public String getFiscalYearStart(int year, int month, int day,
			DateFormat format) {
		GregorianCalendar CalFromDateGiven = new GregorianCalendar(year,
				month - 1, day);
		GregorianCalendar startOfYear = new GregorianCalendar(
				CalFromDateGiven.get(Calendar.YEAR),
				fiscalStart.get(Calendar.MONTH), fiscalStart.get(Calendar.DATE));
		// System.out.println("CalFromDateGiven = " + year + " " + (month-1) +
		// " " +day);
		// System.out.println("CalFromDateGiven = " +
		// CalFromDateGiven.get(Calendar.YEAR) + " " +
		// fiscalStart.get(Calendar.MONTH) + " " +
		// fiscalStart.get(Calendar.DATE));
		if (CalFromDateGiven.before(startOfYear)
				|| CalFromDateGiven.equals(startOfYear)) {
			startOfYear.add(Calendar.YEAR, -1);
		}
		return format.format(startOfYear.getTime());
	}

	/**
	 * Method getFiscalStartYear.
	 * 
	 * @return String
	 */
	public String getFiscalStartYear() {
		GregorianCalendar current = new GregorianCalendar(getCalendar().get(
				Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DATE));
		GregorianCalendar startOfYear = new GregorianCalendar(
				fiscalStart.get(Calendar.YEAR),
				fiscalStart.get(Calendar.MONTH), fiscalStart.get(Calendar.DATE));
		startOfYear.set(Calendar.YEAR, current.get(Calendar.YEAR));

		if (current.before(startOfYear) || current.equals(startOfYear)) {
			startOfYear.add(Calendar.YEAR, -1);
		}
		return Integer.toString(startOfYear.get(Calendar.YEAR));
	}

	/**
	 * Sets the fiscalStart.
	 * 
	 * @param fiscalStart
	 *            The fiscalStart to set
	 */
	public static void setFiscalStart(GregorianCalendar fiscalStart) {
		MilClock.fiscalStart = fiscalStart;

	}

	/**
	 * Method getFiscalWeek.
	 * 
	 * @return String
	 * @throws ParseException
	 */
	public String getFiscalWeek() {

		GregorianCalendar date = fiscalStart;
		date = fiscalStart;
		// fiscal.

		long millisElapsed = current.getTimeInMillis() - date.getTimeInMillis();
		int weeksElapsed = (int) (millisElapsed / LENGTH_OF_WEEK);
		fiscalWeek = Integer.toString(weeksElapsed + 1);
		return fiscalWeek;

	}

	public String getJulian() {
		GregorianCalendar current = new GregorianCalendar(getCalendar().get(
				Calendar.YEAR), getCalendar().get(Calendar.MONTH),
				getCalendar().get(Calendar.DATE));
		julianDate = Integer.toString(current.get(Calendar.DAY_OF_YEAR));
		return julianDate;
	}

	public String[] getZulu() {

		String[] zulu = new String[3];
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		TimeZone zuluTimeZone = TimeZone.getTimeZone("Zulu");
		dateFormat.setTimeZone(zuluTimeZone);
		// get current date time with Date()
		Date date = new Date();

		zulu[0] = dateFormat.format(date);

		dateFormat = new SimpleDateFormat("EEE");
		dateFormat.setTimeZone(zuluTimeZone);
		zulu[1] = dateFormat.format(date);

		dateFormat = new SimpleDateFormat("MM/dd");
		dateFormat.setTimeZone(zuluTimeZone);
		zulu[2] = dateFormat.format(date);

		// zulu[2]= Integer.toString(date.getMonth());
		// zulu[3]= Integer.toString(date.getDate());
		zuluDateTime = zulu[0];
		return zulu;

	}

}
