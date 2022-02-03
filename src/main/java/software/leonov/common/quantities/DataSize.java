
package software.leonov.common.quantities;

import static java.lang.Math.addExact;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.subtractExact;
import static software.leonov.common.quantities.BitUnit.BITS;
import static software.leonov.common.quantities.MoreMath.divideExact;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * This class models a quantity or size in amounts of bytes and is immutable and thread safe. A {@code DataSize} is
 * directional, meaning it can be negative.
 * <p>
 * This class is a poor attempt to deal with the surprisingly <a target="_blank" href=
 * "https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Quantities_of_bytes_and_bits" >difficult
 * issue</a> of representing the size of digital quantities.
 * <p>
 * The <i>convert</i> methods in this class are directional, meaning they accept negative arguments. Conversions from
 * finer to coarser granularities round towards the <i>nearest neighbor</i> using {@link RoundingMode#HALF_UP}. For
 * example converting to {@code DataSize.of(511, BYTES).toKibibytes() == 0} while converting to
 * {@code DataSize.of(512, BYTES).toKibibytes() == 1}. Conversions from coarser to finer granularities with arguments
 * that would overflow a {@code long} will result in an {@code ArithmeticException}.
 * 
 * @author Zhenya Leonov
 */
public final class DataSize implements Comparable<DataSize>, Externalizable {

    private static final long serialVersionUID = 1L;

    private long size;
    private Enum<?> unit;
    private long sizeInBytes;
    private String fstr;

    DataSize(final long size, final Enum<?> unit) {
        Objects.requireNonNull(unit, "unit == null");

        this.size = size;
        this.unit = unit;
        this.sizeInBytes = toBytes(size, unit);
        this.fstr = format(size, unit);
    }

    /**
     * Returns a {@code DataSize} representing the specified {@code BinaryByteUnit}.
     * 
     * @param size the specified amount
     * @param unit the unit the amount is measured in
     * @return a {@code DataSize} representing the specified amount
     */
    public static DataSize of(final long size, final BinaryByteUnit unit) {
        return new DataSize(size, unit);
    }

    /**
     * Returns a {@code DataSize} representing the specified {@code BitUnit}.
     * 
     * @param size the specified amount
     * @param unit the unit the amount is measured in
     * @return a {@code DataSize} representing the specified amount
     */
    public static DataSize of(final long size, final BitUnit unit) {
        return new DataSize(size, unit);
    }

    /**
     * Returns a {@code DataSize} representing the specified {@code DecimalByteUnit}.
     * 
     * @param size the specified amount
     * @param unit the unit the amount is measured in
     * @return a {@code DataSize} representing the specified amount
     */
    public static DataSize of(final long size, final DecimalByteUnit unit) {
        return new DataSize(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with a positive size.
     * <p>
     * This instance is immutable and unaffected by this method call.
     * 
     * @return a copy of this {@code DataSize} with a positive size
     */
    public DataSize abs() {
        if (isNegative())
            return new DataSize(Math.abs(size), unit);
        return this;
    }

    /**
     * Returns a copy of this {@code DataSize} divided by the specified value.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor the value to divide this {@code DataSize} by
     * @return a {@code DataSize} based on this {@code DataSize} divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero or if numeric overflow occurs
     */
    public DataSize dividedBy(long divisor) {
        if (divisor == 0)
            throw new ArithmeticException("division by zero");

        if (divisor == 1)
            return this;

        return new DataSize(divideExact(size, divisor, RoundingMode.HALF_UP), unit);
    }

    /**
     * Checks if this {@code DataSize} is negative, excluding zero.
     * 
     * @return {@code true} if this {@code DataSize} is negative, {@code false} otherwise
     */
    public boolean isNegative() {
        return sizeInBytes < 0;
    }

    /**
     * Checks if this {@code DataSize} is zero.
     * 
     * @return {@code true} if this {@code DataSize} is zero, {@code false} otherwise
     */
    public boolean isZero() {
        return sizeInBytes == 0;
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified {@code DataSize} subtracted.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @return a copy of this {@code DataSize} with the specified {@code DataSize} subtracted
     */
    public DataSize minus(final DataSize size) {
        Objects.requireNonNull(size, "size == null");
        return subtract(size.size, size.unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount subtracted.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount subtracted
     */
    public DataSize minus(final long size, final BinaryByteUnit unit) {
        return subtract(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount subtracted.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount subtracted
     */
    public DataSize minus(final long size, final BitUnit unit) {
        return subtract(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount subtracted.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount subtracted
     */
    public DataSize minus(final long size, final DecimalByteUnit unit) {
        return subtract(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} multiplied by the specified value.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param multiplicand the value to multiply this {@code DataSize} by
     * @return a {@code DataSize} based on this {@code DataSize} multiplied by the specified multiplicand
     * @throws ArithmeticException if a numeric overflow occurs
     */
    public DataSize multipliedBy(long multiplicand) {
        if (multiplicand == 0)
            throw new ArithmeticException("division by zero");

        if (multiplicand == 1)
            return this;

        return new DataSize(multiplyExact(size, multiplicand), unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the size negated.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @return a copy of this {@code DataSize} with the size negated Duration
     */
    public DataSize negated() {
        return new DataSize(-size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified {@code DataSize} added.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @return a copy of this {@code DataSize} with the specified {@code DataSize} added
     */
    public DataSize plus(final DataSize size) {
        Objects.requireNonNull(size, "size == null");
        return add(size.size, size.unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount added.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount added
     */
    public DataSize plus(final long size, final BinaryByteUnit unit) {
        return add(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount added.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount added
     */
    public DataSize plus(final long size, final BitUnit unit) {
        return add(size, unit);
    }

    /**
     * Returns a copy of this {@code DataSize} with the specified amount added.
     * 
     * This instance is immutable and unaffected by this method call.
     * 
     * @param size the amount to subtract
     * @param unit the unit the amount is measured in
     * @return a copy of this {@code DataSize} with the specified amount added
     */
    public DataSize plus(final long size, final DecimalByteUnit unit) {
        return add(size, unit);
    }

    /**
     * Returns a representation of this {@code DataSize} in the specified unit.
     * 
     * @param unit the specified unit
     * @return a representation of this {@code DataSize} in the specified unit
     */
    public long to(final BinaryByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return unit.from(this.sizeInBytes, DecimalByteUnit.BYTES);
    }

    /**
     * Returns a representation of this {@code DataSize} in the specified unit.
     * 
     * @param unit the specified unit
     * @return a representation of this {@code DataSize} in the specified unit
     */
    public long to(final BitUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return unit.from(this.sizeInBytes, DecimalByteUnit.BYTES);
    }

    /**
     * Returns a representation of this {@code DataSize} in the specified unit.
     * 
     * @param unit the specified unit
     * @return a representation of this {@code DataSize} in the specified unit
     */
    public long to(final DecimalByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return unit.from(this.sizeInBytes, DecimalByteUnit.BYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.BYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#BYTES}
     */
    public long toBytes() {
        return to(DecimalByteUnit.BYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.KILOBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#KILOBYTES}
     */
    public long toKilobytes() {
        return to(DecimalByteUnit.KILOBYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.MEGABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#MEGABYTES}
     */
    public long toMegabytes() {
        return to(DecimalByteUnit.MEGABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.GIGABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#GIGABYTES}
     */
    public long toGigabytes() {
        return to(DecimalByteUnit.GIGABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.TERABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#TERABYTES}
     */
    public long toTerabytes() {
        return to(DecimalByteUnit.TERABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.PETABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#PETABYTES}
     */
    public long toPetabytes() {
        return to(DecimalByteUnit.PETABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.EXABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#EXABYTES}
     */
    public long toExabytes() {
        return to(DecimalByteUnit.EXABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.ZETTABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#ZETTABYTES}
     */
    public long toZettabytes() {
        return to(DecimalByteUnit.ZETTABYTES);
    }

    /**
     * Shorthand for {@link #to(DecimalByteUnit) to(DecimalByteUnit.YOTTABYTES)}.
     *
     * @return this {@code DataSize} converted to {@link DecimalByteUnit#YOTTABYTES}
     */
    public long toYottabytes() {
        return to(DecimalByteUnit.YOTTABYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.KIBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#KIBIBYTES}
     */
    public long toKibibytes() {
        return to(BinaryByteUnit.KIBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.MEBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#MEBIBYTES}
     */
    public long toMebibytes() {
        return to(BinaryByteUnit.MEBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.GIBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#GIBIBYTES}
     */
    public long toGibibytes() {
        return to(BinaryByteUnit.GIBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.TEBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#TEBIBYTES}
     */
    public long toTebibytes() {
        return to(BinaryByteUnit.TEBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.PEBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#PEBIBYTES}
     */
    public long toPebibytes() {
        return to(BinaryByteUnit.PEBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.EXBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#EXBIBYTES}
     */
    public long toExbibytes() {
        return to(BinaryByteUnit.EXBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.ZEBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#ZEBIBYTES}
     */
    public long toZebibytes() {
        return to(BinaryByteUnit.ZEBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BinaryByteUnit) to(BinaryByteUnit.YOBIBYTES)}.
     *
     * @return this {@code DataSize} converted to {@link BinaryByteUnit#YOBIBYTES}
     */
    public long toYobibytes() {
        return to(BinaryByteUnit.YOBIBYTES);
    }

    /**
     * Shorthand for {@link #to(BitUnit) to(BitUnit.BITS)}.
     * 
     * @return this {@code DataSize} converted to {@link BitUnit#BITS}
     */
    public long toBits() {
        return to(BitUnit.BITS);
    }

    /**
     * Shorthand for {@link #to(BitUnit) to(BitUnit.KILOBITS)}.
     * 
     * @return this {@code DataSize} converted to {@link BitUnit#KILOBITS}
     */
    public long toKilobits() {
        return to(BitUnit.KILOBITS);
    }

    /**
     * Shorthand for {@link #to(BitUnit) to(BitUnit.MEGABITS)}.
     * 
     * @return this {@code DataSize} converted to {@link BitUnit#MEGABITS}
     */
    public long toMegabits() {
        return to(BitUnit.MEGABITS);
    }

    /**
     * Shorthand for {@link #to(BitUnit) to(BitUnit.GIGABITS)}.
     *
     * @return this {@code DataSize} converted to {@link BitUnit#GIGABITS}
     */
    public long toGigabits() {
        return to(BitUnit.GIGABITS);
    }

    /**
     * Shorthand for {@link #to(BitUnit) to(BitUnit.TERABITS)}.
     * 
     * @return this {@code DataSize} converted to {@link BitUnit#TERABITS}
     */
    public long toTerabits() {
        return to(BitUnit.TERABITS);
    }

    /**
     * Formats this {@code DataSize} a human-readable string.
     * 
     * @return a human-readable string representing this {@code DataSize}
     */
    @Override
    public String toString() {
        return fstr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeInBytes);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || this.getClass() != other.getClass())
            return false;

        final DataSize that = (DataSize) other;

        return this.sizeInBytes == that.sizeInBytes;
    }

    @Override
    public int compareTo(final DataSize other) {
        return Long.compare(sizeInBytes, other.sizeInBytes);
    }

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeLong(size);
        oos.writeObject(unit);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeLong(size);
        out.writeObject(unit);
    }

    private void readObject(ObjectInputStream s) throws InvalidObjectException {
        throw new InvalidObjectException("use readExternal"); // Defend against malicious callers
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        size = in.readLong();
        unit = (Enum<?>) in.readObject();
        fstr = format(size, unit);
    }

    private DataSize add(final long size, final Enum<?> unit) {
        Objects.requireNonNull(unit, "unit == null");
        final long result = addExact(this.sizeInBytes, toBytes(size, unit));
        return this.unit instanceof BitUnit ? new DataSize(bytesToBits(result), BITS) : new DataSize(result, getByteUnit(this.unit));
    }

    private DataSize subtract(final long size, final Enum<?> unit) {
        Objects.requireNonNull(unit, "unit == null");
        final long result = subtractExact(this.sizeInBytes, toBytes(size, unit));
        return this.unit instanceof BitUnit ? new DataSize(bytesToBits(result), BITS) : new DataSize(result, getByteUnit(this.unit));
    }

    private static long bytesToBits(final long size) {
        return BitUnit.BITS.from(size, DecimalByteUnit.BYTES);
    }

    private static String format(final long size, final Enum<?> unit) {
        // @formatter:off
        return unit instanceof BitUnit ?
                    DataSizeFormatter.usingDefaultLocale().format(size, (BitUnit) unit) :
                    unit instanceof BinaryByteUnit ?
                         DataSizeFormatter.usingDefaultLocale().format(size, (BinaryByteUnit) unit) :
                         DataSizeFormatter.usingDefaultLocale().format(size, (DecimalByteUnit) unit);
        // @formatter:on        

    }

    private static Enum<?> getByteUnit(final Enum<?> unit) {
        return unit instanceof BinaryByteUnit ? BinaryByteUnit.BYTES : DecimalByteUnit.BYTES;
    }

    private static long toBytes(final long size, final Enum<?> unit) {
        return unit instanceof BitUnit ? ((BitUnit) unit).toBytes(size) : unit instanceof BinaryByteUnit ? ((BinaryByteUnit) unit).toBytes(size) : ((DecimalByteUnit) unit).toBytes(size);
    }

}
