package org.mockbukkit.metaminer.util;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils
{
	private static final int MAXIMUM_DECIMAL_PLACES = 6;

	/**
	 * Output the Big Decimal with a maximum of 6-digit decimal places.
	 *
	 * @param bigDecimal The value to be formatted.
	 *
	 * @return The formatted value.
	 */
	public static double toDouble(@Nonnull BigDecimal bigDecimal)
	{
		Preconditions.checkNotNull(bigDecimal, "The big decimal cannot be null!");
		return bigDecimal.setScale(MAXIMUM_DECIMAL_PLACES, RoundingMode.HALF_UP).doubleValue();
	}

	private NumberUtils()
	{
		// Hide the public constructor
	}

}
