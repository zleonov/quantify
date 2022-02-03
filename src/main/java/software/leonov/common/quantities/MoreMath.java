package software.leonov.common.quantities;

import static java.lang.Math.abs;
import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.math.RoundingMode;

class MoreMath {

    private MoreMath() {
    }

    /**
     * Returns the result of {@code x / y}, rounding the result using the specified {@code RoundingMode}, and throwing an
     * {@code ArithmeticException} if the result overflows a {@code long}. Such overflow occurs in this method if {@code x}
     * is {@link Long#MIN_VALUE} and {@code y} is {@code -1}.
     * 
     * @param x    the dividend
     * @param y    the divisor
     * @param mode the {@link RoundingMode} to use
     * @return the result of {@code x / y}
     * @throws ArithmeticException if {@code y} is zero or the quotient
     */
    // code adapted from https://github.com/google/guava/blob/master/guava/src/com/google/common/math/LongMath.java
    public static long divideExact(final long x, final long y, final RoundingMode mode) {
        if (x == Long.MIN_VALUE && y == -1)
            throw new ArithmeticException("long overflow");

        long div = x / y; // throws if q == 0
        long rem = x - y * div; // equals p % q

        if (rem == 0) {
            return div;
        }

        int signum = 1 | (int) ((x ^ y) >> (Long.SIZE - 1));
        boolean increment;
        switch (mode) {
        case UNNECESSARY:
            if (rem != 0)
                throw new ArithmeticException("rounding necessary");
        case DOWN:
            increment = false;
            break;
        case UP:
            increment = true;
            break;
        case CEILING:
            increment = signum > 0;
            break;
        case FLOOR:
            increment = signum < 0;
            break;
        case HALF_EVEN:
        case HALF_DOWN:
        case HALF_UP:
            long absRem = abs(rem);
            long cmpRemToHalfDivisor = absRem - (abs(y) - absRem);
            // subtracting two nonnegative longs can't overflow
            // cmpRemToHalfDivisor has the same sign as compare(abs(rem), abs(q) / 2).
            if (cmpRemToHalfDivisor == 0) { // exactly on the half mark
                increment = (mode == HALF_UP || (mode == HALF_EVEN && (div & 1) != 0));
            } else {
                increment = cmpRemToHalfDivisor > 0; // closer to the UP value
            }
            break;
        default:
            throw new AssertionError();
        }
        return increment ? div + signum : div;
    }

    /**
     * Rounds the given {@code double} to the nearest specified multiple that is greater or equal to the argument.
     * 
     * @param x    the double to round
     * @param mult the specified multiple
     * @return the rounded double
     */
    public static double ceil(final double x, final double mult) {
        return mult == 0 ? x : Math.ceil(x / mult) * mult;
    }

    /**
     * Rounds the given {@code double} to the nearest specified multiple that is less than or equal to the argument.
     * 
     * @param x    the double to round
     * @param mult the specified multiple
     * @return the rounded double
     */
    public static double floor(final double x, final double mult) {
        return mult == 0 ? x : Math.floor(x / mult) * mult;
    }

    /**
     * Rounds a double using the specified {@code RoundingMode}.
     * 
     * @param x     the double to round
     * @param scale the number of digits to the right of the decimal point.
     * @param mode  the {@code RoundingMode}
     * @return the rounded double
     */
    public static double round(final double x, final int scale, final RoundingMode mode) {
        return new BigDecimal(x).setScale(scale, mode).doubleValue();
    }

}
