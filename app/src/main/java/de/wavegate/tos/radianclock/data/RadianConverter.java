package de.wavegate.tos.radianclock.data;

import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import static de.wavegate.tos.radianclock.RadianClockActivity.LOGTAG;

/**
 * Created by Nils on 09.10.2016.
 */

public class RadianConverter {

	public static final char half = '½';
	public static final char third = '⅓';
	public static final char fourth = '¼';
	public static final char fifth = '⅛';
	public static final char sixth = '⅙';
	public static final char seventh = '⅐';
	public static final char eightth = '⅛';
	public static final char nineth = '⅑';
	public static final char tenth = '⅒';

	private Date date;
	private double hourRadian, minuteRadian, secondRadian;

	public RadianConverter(Date date) {
		this.date = date;

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);

		double hourPercentage = (double) hour / 12.0;
		double minutePercentage = (double) min / 60.0;
		double secondPercentage = (double) sec / 60.0;

		Log.i(LOGTAG, "RadianConverter: h: " + hour + " [" + hourPercentage + "] m: " + min + " [" + minutePercentage + "] s: " + sec + " [" + secondPercentage + "]");

		hourRadian = toRadian(hourPercentage);
		minuteRadian = toRadian(minutePercentage);
		secondRadian = toRadian(secondPercentage);

		Log.i(LOGTAG, "half " + Character.getNumericValue(third));
	}

	public static String radianToString(double radian) {
		String s = "";

		int whole = (int) (radian / Math.PI);
		double rest = radian % Math.PI;

		Vector<Integer> fractal = FractionApproximator.getApproximator().approximate(rest, 50);
		Log.i(LOGTAG, radian + " = " + whole + " * PI + " + rest + " -> " + fractal.get(0) + " / " + fractal.get(1));

		if (whole == 0 && rest == 0) {
			return "0";
		}

		if (whole > 0) {
			s = whole + " ";
		}
		if (rest != 0) {
			s = s + "(" + fractal.get(0) + "/" + fractal.get(1) + ")";
		}
		s = s + "π";

		http://stackoverflow.com/questions/1373035/how-do-i-scale-one-rectangle-to-the-maximum-size-possible-within-another-rectang

		return s;
	}

	public double getHourRadian() {
		return hourRadian;
	}

	public double getMinuteRadian() {
		return minuteRadian;
	}

	public double getSecondRadian() {
		return secondRadian;
	}

	private double toRadian(double percentage) {
		double degree = percentage * 360;
		return degree * (2 * Math.PI / 360);
	}

	public Date getDate() {
		return date;
	}

	public String getDateString() {
		return getFormat().format(date);
	}

	private DateFormat getFormat() {
		return DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());
	}
}
