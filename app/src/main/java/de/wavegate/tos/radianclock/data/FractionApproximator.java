package de.wavegate.tos.radianclock.data;

import android.util.Log;

import java.util.HashMap;
import java.util.Vector;

import static de.wavegate.tos.radianclock.RadianClockActivity.LOGTAG;

/**
 * Created by Nils on 09.10.2016.
 */

public class FractionApproximator {

	private static FractionApproximator approximator;

	private HashMap<Double, Vector<Integer>> lkp;

	private FractionApproximator() {
		lkp = new HashMap<>();

		Log.i(LOGTAG, "A new FractionApproximator was setup right now!");
	}

	public synchronized static FractionApproximator getApproximator() {
		if (approximator == null) {
			approximator = new FractionApproximator();
		}

		return approximator;
	}

	public synchronized Vector<Integer> approximate(double value, int max) {
		if (lkp.containsKey(value)) {
			return lkp.get(value);
		}

		double numerator = 1;
		double denominator = 1;

		double bestnumerator = 1;
		double bestdenominator = 1;
		double bestdiff = Double.MAX_VALUE;

		while (numerator < max && denominator < max) {
			double approx = numerator / denominator * Math.PI;

			double diff = value - approx;
			if (diff == 0) {
				bestnumerator = numerator;
				bestdenominator = denominator;
				break;
			}

			double absdiff = Math.abs(diff);
			if (absdiff < bestdiff) {
				bestdiff = absdiff;
				bestnumerator = numerator;
				bestdenominator = denominator;
			}

			if (diff > 0) {
				numerator++;
			} else {
				denominator++;
			}
		}

		//Log.i(LOGTAG, "Best approx for " + value + " is: " + bestnumerator + "/" + bestdenominator);

		Vector<Integer> v = new Vector<>();
		v.add((int) bestnumerator);
		v.add((int) bestdenominator);

		lkp.put(value, v);

		return v;
	}

}
