package de.bushnaq.abdalla.pluvia.util;

class RcTimeStruct {
	String	character;
	String	seperator;
	int		width;

	RcTimeStruct(final String aSeperator, final String aCharacter, final int aWidth) {
		seperator = aSeperator;
		character = aCharacter;
		width = aWidth;
	}
}

/**
 * @author kuterbunt
 */
public class TimeUnit {
	private static final int	DAY_INDEX			= 3;
	public static final long	DAYS_PER_YEAR		= 100L;
	private static final int	HOUR_INDEX			= 2;

	private static final int	MINUTE_INDEX		= 1;

	private static final int	SECONDS_INDEX		= 0;

//	public static String toString(final long time) {
//		return toString(time, TimeAccuracy.HOUR_ACCURACY);
//	}
//
//	public static String toString(final long time, final TimeAccuracy dayAccuracy) {
//		final long	h	= time - (time / TimeUnit.TICKS_PER_DAY) * TimeUnit.TICKS_PER_DAY;
//		final long	d	= (time / TimeUnit.TICKS_PER_DAY) - ((time / (TimeUnit.TICKS_PER_DAY * TimeUnit.DAYS_PER_YEAR)) * TimeUnit.DAYS_PER_YEAR);
//		final long	y	= time / (TimeUnit.TICKS_PER_DAY * TimeUnit.DAYS_PER_YEAR);
//		if (h != 0 && dayAccuracy == TimeAccuracy.HOUR_ACCURACY) {
//			return String.format("%d.%02d.%d", y, d, h);
//		} else if (d == 0 && dayAccuracy == TimeAccuracy.YEAR_ACCURACY) {
//			return String.format("%d.", y);
//		} else {
//			return String.format("%d.%02d", y, d);
//		}
//	}

	private static final long	serialVersionUID	= 2573182506689782820L;

	public static final long	TICKS_PER_DAY		= 100L;
	public static final long	TICKS_PER_YEAR		= DAYS_PER_YEAR * TICKS_PER_DAY;

	/**
	 * If aUseCharacters is true, seconds will be followed with s, hours with h... Result will be xd xh:xm:xs or x x:x:x
	 *
	 * @param aTime
	 * @param aUseSeconds
	 * @param aUseCharacters
	 * @param aPrintLeadingZeros
	 * @return String
	 */
	public static String createDurationString(long aTime, final boolean aUseSeconds, final boolean aUseCharacters, final boolean aPrintLeadingZeros) {
		String					_result		= "";
		final long[]			_timePieces	= { 0, 0, 0, 0 };
		final RcTimeStruct[]	_time		= { new RcTimeStruct(":", "s", 2), new RcTimeStruct(":", "m", 2), new RcTimeStruct(":", "h", 2), new RcTimeStruct(" ", "d", 2) };
		_timePieces[DAY_INDEX] = aTime / 86400000;
		aTime -= _timePieces[DAY_INDEX] * 86400000;
		_timePieces[HOUR_INDEX] = aTime / 3600000;
		aTime -= _timePieces[HOUR_INDEX] * 3600000;
		_timePieces[MINUTE_INDEX] = aTime / 60000;
		aTime -= _timePieces[MINUTE_INDEX] * 60000;
		_timePieces[SECONDS_INDEX] = aTime / 1000;
		boolean	_weFoundTheFirstNonezeroValue	= aPrintLeadingZeros;
		int		_indexEnd						= 0;
		if (!aUseSeconds) {
			_indexEnd = 1;
		}
		for (int _index = DAY_INDEX; _index >= _indexEnd; _index--) {
			if ((_timePieces[_index] != 0) || _weFoundTheFirstNonezeroValue) {
				if (aUseCharacters) {
					_result += longToString(_timePieces[_index], _weFoundTheFirstNonezeroValue);
					_result += _time[_index].character;
					if (_index != _indexEnd) {
						_result += _time[_index].seperator;
					} else {
						// ---Do not add a seperator at the end
					}
				} else {
					_result += longToString(_timePieces[_index], _weFoundTheFirstNonezeroValue);
					if (_index != _indexEnd) {
						_result += _time[_index].seperator;
					} else {
						// ---Do not add a seperator at the end
					}
				}
				_weFoundTheFirstNonezeroValue = true;
			} else {
				// ---Ignore all leading zero values
			}
		}
		// ---In case the result is empty
		if (_result.length() == 0) {
			if (aUseCharacters) {
				if (aUseSeconds) {
					_result = "0s";
				} else {
					_result = "0m";
				}
			} else {
				_result = "0";
			}
		} else {
			// ---The result is not ampty
		}
		return _result;
	}

	public static long days(final long time) {
		return time / TimeUnit.TICKS_PER_DAY;
	}

	public static boolean isInt(final long currentTime) {
		return ((currentTime / 100) * 100 - currentTime) == 0L;
	}

	private static String longToString(final long aValue, final boolean aCreateLeadingZero) {
		if (!aCreateLeadingZero || (aValue > 9)) {
			return Long.toString(aValue);
		} else {
			return "0" + aValue;
		}
	}

	public static long years(final long time) {
		return time / (TimeUnit.DAYS_PER_YEAR * TimeUnit.TICKS_PER_DAY);
	}

}
