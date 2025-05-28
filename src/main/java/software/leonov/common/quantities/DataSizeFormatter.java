package software.leonov.common.quantities;

import static java.util.Objects.requireNonNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formatter for printing and parsing human-readable strings of bit and byte sizes.
 * 
 * @author Zhenya Leonov
 */
public final class DataSizeFormatter {

    private static final DataSizeFormatter INSTANCE = new DataSizeFormatter(setMaximumFractionDigits(NumberFormat.getInstance(), 3));

    private final NumberFormat nf;

    private DataSizeFormatter(final NumberFormat nf) {
        this.nf = nf;
    }

    /**
     * Returns a {@code SizeFormatter} which uses the default {@code Locale} to format numbers.
     * 
     * @return a {@code SizeFormatter} which uses the default {@code Locale} to format numbers
     */
    public static DataSizeFormatter usingDefaultLocale() {
        return INSTANCE;
    }

    /**
     * Returns a {@code SizeFormatter} which uses the specified {@code Locale} to format numbers.
     * 
     * @param locale the specified locale
     * @return a {@code SizeFormatter} which uses the specified {@code Locale} to format numbers
     */
    public static DataSizeFormatter using(final Locale locale) {
        requireNonNull(locale, "locale == null");
        return new DataSizeFormatter(setMaximumFractionDigits(NumberFormat.getInstance(locale), 3));
    }

    private static NumberFormat setMaximumFractionDigits(final NumberFormat nf, final int x) {
        nf.setMaximumFractionDigits(x);
        return nf;
    }

    /**
     * Returns a {@code DataSize} parsed from the specified string, including the string produced by
     * {@code DataSize.toString()}. The method may not use the entire text of the given string.
     * <p>
     * The string starts with an optional sign, denoted by the ASCII negative or positive symbol. Then a number which will
     * be parsed using this formatter's {@code Locale}, followed by optionally whitespace, then a textual representation of
     * a {@code BitUnit}, {@code BinaryByteUnit}, or {@code DecimalUnit} denoted by either the unit's symbol or name
     * according to the following table:
     * 
     * <pre>
     * <table border cellpadding="3" cellspacing="1">
     *   <tr>
     *     <th>unit</th><th>symbol</th><th>case-insensitive</th><th>name</th><th>case-insensitive</th>
     *   </tr>
     *   <tr>
     *     <td>BitUnit.BITS</td><td>b</td><td>no</td><td>bits</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BitUnit.KILOBITS</td><td>kb</td><td>no</td><td>kilobits or kbits</td><td>yes</td>
     *   </tr>
     *   <tr>
     *   <td>BitUnit.MEGABITS</td><td>Mb</td><td>no</td><td>megabits or mbits</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BitUnit.GIGABITS</td><td>Gb</td><td>no</td><td>gigabits or gbits</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BitUnit.TERABITS</td><td>Tb</td><td>no</td><td>terabits or tbits</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.KIBIBYTES</td><td>KiB</td><td>Yes</td><td>kibibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.MEBIBYTES</td><td>MiB</td><td>Yes</td><td>mebibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.GIBIBYTES</td><td>GiB</td><td>Yes</td><td>gibibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.TEBIBYTES</td><td>TiB</td><td>Yes</td><td>tebibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.PEBIBYTES</td><td>PiB</td><td>Yes</td><td>pebibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.EXBIBYTES</td><td>EiB</td><td>Yes</td><td>exbibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.ZEBIBYTES</td><td>ZiB</td><td>Yes</td><td>zebibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>BinaryByteUnit.YOBIBYTES</td><td>YiB</td><td>Yes</td><td>yobibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.BYTES</td><td>B</td><td>no</td><td>bytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.KILOBYTES</td><td>kB</td><td>no</td><td>kilobytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.MEGABYTES</td><td>MB</td><td>no</td><td>megabytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.GIGABYTES</td><td>GB</td><td>no</td><td>gigabytes</td><td>yes</td>
     *   </tr> 
     *   <tr>
     *     <td>DecimalByteUnit.TERABYTES</td><td>TB</td><td>no</td><td>pebibytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.PETABYTES</td><td>PB</td><td>yes</td><td>petabytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.EXABYTES</td><td>EB</td><td>yes</td><td>exabytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.ZETTABYTES</td><td>ZB</td><td>yes</td><td>zettabytes</td><td>yes</td>
     *   </tr>
     *   <tr>
     *     <td>DecimalByteUnit.YOTTABYTES</td><td>YB</td><td>yes</td><td>yottabytes</td><td>yes</td>
     *  </tr>
     * </table>
     * </pre><b>Warning:</b> This method is free to round or truncate the value represented by the specified string. No
     * guarantee is made as to the final granularity of the returned {@code DataSize}. In general terms: any two
     * {@code DataSize} objects which have identical {@code toString()} representations are not guaranteed to be equivalent.
     * 
     * 
     * @param str the specified string
     * @return the parsed {@code DataSize}
     * @throws ParseException if the string cannot be parsed
     */
    public DataSize parse(final String str) throws ParseException {
        return parse(str, (Enum<?>) null);
    }

    /**
     * Obtains a {@code DataSize} from the specified string in the given {@code BitUnit}. The method may not use the entire
     * text of the given string.
     * <p>
     * This class will parse a textual representation of a {@code DataSize}, including the string produced by
     * {@link DataSize#toString()}.
     * <p>
     * <b>Warning:</b> This method is free to round or truncate the value represented by the specified string. No guarantee
     * is made as to the final granularity of the returned {@code DataSize}. In general terms: any two {@code DataSize}
     * objects which have identical {@code toString()} representations are not guaranteed to be equivalent.
     * 
     * @param str  the specified string
     * @param unit the given unit
     * @return the parsed {@code DataSize}
     * @throws ParseException if the string cannot be parsed
     */
    public DataSize parse(final String str, final BitUnit unit) throws ParseException {
        requireNonNull(unit, "unit == null");
        return parse(str, (Enum<?>) unit);
    }

    /**
     * Obtains a {@code DataSize} from the specified string in the given {@code BinaryByteUnit}. The method may not use the
     * entire text of the given string.
     * <p>
     * This class will parse a textual representation of a {@code DataSize}, including the string produced by
     * {@link DataSize#toString()}.
     * <p>
     * <b>Warning:</b> This method is free to round or truncate the value represented by the specified string. No guarantee
     * is made as to the final granularity of the returned {@code DataSize}. In general terms: any two {@code DataSize}
     * objects which have identical {@code toString()} representations are not guaranteed to be equivalent.
     * 
     * @param str  the specified string
     * @param unit the given unit
     * @return the parsed {@code DataSize}
     * @throws ParseException if the string cannot be parsed
     */
    public DataSize parse(String str, final BinaryByteUnit unit) throws ParseException {
        requireNonNull(unit, "unit == null");
        return parse(str, (Enum<?>) unit);
    }

    /**
     * Obtains a {@code DataSize} from the specified string in the given {@code DecimalByteUnit}. The method may not use the
     * entire text of the given string.
     * <p>
     * This will parse a textual representation of a {@code DataSize}, including the string produced by
     * {@link DataSize#toString()}.
     * 
     * @param str  the specified string
     * @param unit the given unit
     * @return the parsed {@code DataSize}
     * @throws ParseException if the string cannot be parsed
     */
    public DataSize parse(String str, final DecimalByteUnit unit) throws ParseException {
        requireNonNull(unit, "unit == null");
        return parse(str, (Enum<?>) unit);
    }

    private DataSize parse(final String str, Enum<?> unit) throws ParseException {
        requireNonNull(str, "str == null");

        final ParsePosition position = new ParsePosition(0);
        final Number number = nf.parse(str, position);

        if (number == null)
            throw new ParseException("cannot parse: '" + str + "'", position.getErrorIndex());

        if (unit == null)
            unit = parseUnit(str.substring(position.getIndex()));

        if (number instanceof Long)
            return new DataSize(number.longValue(), unit);
        else {
            double d = number.doubleValue();
            if (unit.ordinal() > 0) {
                d = d * (unit instanceof DecimalByteUnit || unit instanceof BitUnit ? 1000 : 1024);
                if (!Double.isFinite(d))
                    throw new ArithmeticException("result is infinite");
                unit = prev(unit);
            }

            // what happens when number is too big
            return new DataSize((long) (d > 0 ? Math.ceil(d) : Math.floor(d)), unit);
        }
    }

    private static final Pattern FIRST_WORD = Pattern.compile("\\w(.+)\\w");

    private static Enum<?> parseUnit(final String symbol) throws ParseException {
        final Matcher matcher = FIRST_WORD.matcher(symbol);
        if (matcher.find()) {
            final String text = matcher.group();
            if (text.startsWith("Tb") || text.equalsIgnoreCase("TERABITS") || text.equalsIgnoreCase("TBITS"))
                return BitUnit.TERABITS;
            else if (text.startsWith("Gb") || text.equalsIgnoreCase("GIGABITS") || text.equalsIgnoreCase("GBITS"))
                return BitUnit.GIGABITS;
            else if (text.startsWith("Mb") || text.equalsIgnoreCase("MEGABITS") || text.equalsIgnoreCase("MBITS"))
                return BitUnit.MEGABITS;
            else if (text.startsWith("kb") || text.equalsIgnoreCase("KILOBITS") || text.equalsIgnoreCase("KBITS"))
                return BitUnit.KILOBITS;
            else if (text.startsWith("b") || text.equalsIgnoreCase("BITS"))
                return BitUnit.BITS;
            else if (text.equalsIgnoreCase("YiB") || text.equalsIgnoreCase("YOBIBYTES"))
                return BinaryByteUnit.YOBIBYTES;
            else if (text.equalsIgnoreCase("ZiB") || text.equalsIgnoreCase("ZEBIBYTES"))
                return BinaryByteUnit.ZEBIBYTES;
            else if (text.equalsIgnoreCase("EiB") || text.equalsIgnoreCase("EXBIBYTES"))
                return BinaryByteUnit.EXBIBYTES;
            else if (text.equalsIgnoreCase("PiB") || text.equalsIgnoreCase("PEBIBYTES"))
                return BinaryByteUnit.PEBIBYTES;
            else if (text.equalsIgnoreCase("TiB") || text.equalsIgnoreCase("TEBIBYTES"))
                return BinaryByteUnit.TEBIBYTES;
            else if (text.equalsIgnoreCase("GiB") || text.equalsIgnoreCase("GIBIBYTES"))
                return BinaryByteUnit.GIBIBYTES;
            else if (text.equalsIgnoreCase("MiB") || text.equalsIgnoreCase("MEBIBYTES"))
                return BinaryByteUnit.MEBIBYTES;
            else if (text.equalsIgnoreCase("KiB") || text.equalsIgnoreCase("KIBIBYTES"))
                return BinaryByteUnit.KIBIBYTES;
            else if (text.equalsIgnoreCase("YB") || text.equalsIgnoreCase("YOTTABYTES"))
                return DecimalByteUnit.YOTTABYTES;
            else if (text.equalsIgnoreCase("ZB") || text.equalsIgnoreCase("ZETTABYTES"))
                return DecimalByteUnit.ZETTABYTES;
            else if (text.equalsIgnoreCase("EB") || text.equalsIgnoreCase("EXABYTES"))
                return DecimalByteUnit.EXABYTES;
            else if (text.equalsIgnoreCase("PB") || text.equalsIgnoreCase("PETABYTES"))
                return DecimalByteUnit.PETABYTES;
            else if (text.startsWith("TB") || text.equalsIgnoreCase("TERABYTES"))
                return DecimalByteUnit.TERABYTES;
            else if (text.startsWith("GB") || text.equalsIgnoreCase("GIGABYTES"))
                return DecimalByteUnit.GIGABYTES;
            else if (text.startsWith("MB") || text.equalsIgnoreCase("MEGABYTES"))
                return DecimalByteUnit.MEGABYTES;
            else if (text.startsWith("kB") || text.equalsIgnoreCase("KILOBYTES"))
                return DecimalByteUnit.KILOBYTES;
            else if (text.startsWith("B") || text.equalsIgnoreCase("BYTES"))
                return DecimalByteUnit.BYTES;
        }

        throw new ParseException("cannot parse '" + symbol + "' as DecimalByteUnit/BinaryByteUnit/BitUnit'", -1);
    }

    /**
     * Formats the specified size into a human-readable string.
     * 
     * @param size the specified size
     * @param unit the unit of the size
     * @return a human-readable string representing the specified size in the given unit
     * @throws ArithmeticException if a <i>double overflow</i> (the result is not finite) occurs
     */
    public String format(final long size, final DecimalByteUnit unit) {
        return format(size, 1000, unit, nf);
    }

    /**
     * Formats the specified size into a human-readable string.
     * 
     * @param size the specified size
     * @param unit the unit of the size
     * @return a human-readable string representing the specified size in the given unit
     * @throws ArithmeticException if a <i>double overflow</i> (the result is not finite) occurs
     */
    public String format(final long size, final BinaryByteUnit unit) {
        return format(size, 1024, unit, nf);
    }

    /**
     * Formats the specified size into a human-readable string.
     * 
     * @param size the specified size
     * @param unit the unit of the size
     * @return a human-readable string representing the specified size in the given unit
     * @throws ArithmeticException if a <i>double overflow</i> (the result is not finite) occurs
     */
    public String format(final long size, final BitUnit unit) {
        return format(size, 1000, unit, nf);
    }

    private static String format(long size, final long base, Enum<?> unit, final NumberFormat nf) {

        final Enum<?> last = last(unit);
        double d = size;
        if (size >= 0)
            for (; d >= base && unit != last; unit = next(unit))
                d /= base;
        else
            for (; d <= -base && unit != last; unit = next(unit))
                d /= base;

        // We can't have fractional BITS nor can we have fractional BYTES which are not in multiples of 0.125
        if (unit == BitUnit.BITS)
            d = d > 0 ? Math.ceil(d) : Math.floor(d);
        else if (unit == DecimalByteUnit.BYTES || unit == BinaryByteUnit.BYTES)
            d = d > 0 ? MoreMath.ceil(d, 0.125) : MoreMath.floor(d, 0.125);

        /*
         * Is there a better way to handle double overflow? Returning Double.MAX_VALUE doesn't make any sense
         */
        if (!Double.isFinite(d))
            throw new ArithmeticException("result is infinite");

        final String fsize = nf.format(d);

        if (unit != last(unit) && parse(nf, fsize) == (size < 0 ? -base : base))
            return nf.format(size < 0 ? -1 : 1) + next(unit);

        return fsize + unit;
    }

    private static Enum<?> last(final Enum<?> type) {
        return type instanceof DecimalByteUnit ? DecimalByteUnit.values()[DecimalByteUnit.values().length
                - 1] : type instanceof BinaryByteUnit ? BinaryByteUnit.values()[BinaryByteUnit.values().length - 1] : BitUnit.values()[BitUnit.values().length - 1];
    }

    private static Enum<?> next(final Enum<?> type) {
        final int index = type.ordinal() + 1;
        return type instanceof DecimalByteUnit ? DecimalByteUnit.values()[index] : type instanceof BinaryByteUnit ? BinaryByteUnit.values()[index] : BitUnit.values()[index];
    }

    private static Enum<?> prev(final Enum<?> type) {
        final int index = type.ordinal() - 1;
        return type instanceof DecimalByteUnit ? DecimalByteUnit.values()[index] : type instanceof BinaryByteUnit ? BinaryByteUnit.values()[index] : BitUnit.values()[index];
    }

    private static int parse(final NumberFormat format, final String size) {
        try {
            return format.parse(size).intValue();
        } catch (final ParseException e) {
            throw new AssertionError(); // cannot happen
        }
    }

}