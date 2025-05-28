
package software.leonov.common.quantities;
/*
 * Copyright (C) 2019 Zhenya Leonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static java.lang.Math.multiplyExact;
import static software.leonov.common.quantities.MoreMath.divideExact;

import java.math.RoundingMode;
import java.util.Objects;

/**
 * Defines the commonly used <a href="https://physics.nist.gov/cuu/Units/binary.html" target="_blank">binary
 * prefixes</a> that represent the <i>power-of-2</i> byte size of the file system and memory. This {@code Enum} is a
 * poor attempt to deal with the surprisingly <a target="_blank" href=
 * "https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Quantities_of_bytes_and_bits" >difficult
 * issue</a> of representing the size of digital quantities.
 * <p>
 * A {@code BinaryByteUnit} does not hold byte size information, but only helps organize and use byte size
 * representations that may be maintained separately across various contexts.
 * <p>
 * The <i>convert</i> methods in this {@code Enum} are directional, meaning they accept negative arguments. Conversions
 * from finer to coarser granularities round towards the <i>nearest neighbor</i> using {@link RoundingMode#HALF_UP}. For
 * example converting to {@code KIBIBYTES.from(511, BYTES) == 0} while converting to
 * {@code KIBIBYTES.from(512, BYTES) == 1}. Conversions from coarser to finer granularities with arguments that would
 * overflow a {@code long} will result in an {@code ArithmeticException}.
 * 
 * @author Zhenya Leonov
 */
public enum BinaryByteUnit {

    /**
     * A single byte consists of 8 bits.
     */
    BYTES(1, "B"),

    /**
     * A kibibyte (KiB) consists of 1024 bytes.
     */
    KIBIBYTES(1024, "KiB"),

    /**
     * A mebibyte (MiB) consists of 1024 kibibytes.
     */
    MEBIBYTES(KIBIBYTES.base * 1024, "MiB"),

    /**
     * A gibibyte (GiB) consists of 1024 mebibytes.
     */
    GIBIBYTES(MEBIBYTES.base * 1024, "GiB"),

    /**
     * A tebibyte (TiB) consists of 1024 gibibytes.
     */
    TEBIBYTES(GIBIBYTES.base * 1024, "TiB"),

    /**
     * A pebibyte (PiB) consists of 1024 tebibytes.
     */
    PEBIBYTES(TEBIBYTES.base * 1024, "PiB"),

    /**
     * An exabyte (EiB) consists of 1024 pebibytes.
     */
    EXBIBYTES(PEBIBYTES.base * 1024, "EiB"),

    /**
     * A zebibyte (ZiB) consists of 1024 exbibyte.
     */
    ZEBIBYTES(EXBIBYTES.base * 1024, "ZiB"),

    /**
     * A yobibyte (YiB) consists of 1024 zebibytes.
     */
    YOBIBYTES(ZEBIBYTES.base * 1024, "YiB");

    final long base;
    private final String symbol;

    private BinaryByteUnit(final long base, final String symbol) {
        this.base = base;
        this.symbol = symbol;
    }

//    /**
//     * Converts the given size from this {@code BinaryByteUnit} to the specified {@code BinaryByteUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code BinaryByteUnit}
//     * @return the given size converted from this {@code BinaryByteUnit} to the specified {@code BinaryByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BinaryByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        if (this == unit)
//            return size;
//
//        return divide(multiply(size, this.base), unit.base);
//    }
//
//    /**
//     * Converts the given size from this {@code BinaryByteUnit} to the specified {@code DecimalByteUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code DecimalByteUnit}
//     * @return the given size converted from this {@code BinaryByteUnit} to the specified {@code DecimalByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final DecimalByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return divide(multiply(size, this.base), unit.base);
//    }
//
//    /**
//     * Converts the given size from this {@code BinaryByteUnit} to the specified {@code BitUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code BitUnit}
//     * @return the given size converted from this {@code BinaryByteUnit} to the specified {@code BitUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BitUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return multiply(divide(multiply(size, this.base), unit.base), 8);
//    }

    /**
     * Shorthand for {@code BinaryByteUnit.BYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#BYTES}
     */
    public long toBytes(final long size) {
        return BinaryByteUnit.BYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.KIBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#KIBIBYTES}
     */
    public long toKibibytes(final long size) {
        return BinaryByteUnit.KIBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.MEBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#MEBIBYTES}
     */
    public long toMebibytes(final long size) {
        return BinaryByteUnit.MEBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.GIBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#GIBIBYTES}
     */
    public long toGibibytes(final long size) {
        return BinaryByteUnit.GIBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.TEBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#TEBIBYTES}
     */
    public long toTebibytes(final long size) {
        return BinaryByteUnit.TEBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.PEBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#PEBIBYTES}
     */
    public long toPebibytes(final long size) {
        return BinaryByteUnit.PEBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.EXBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#EXBIBYTES}
     */
    public long toExbibytes(final long size) {
        return BinaryByteUnit.EXBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.ZEBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#ZEBIBYTES}
     */
    public long toZebibytes(final long size) {
        return BinaryByteUnit.ZEBIBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BinaryByteUnit.YOBIBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#YOBIBYTES}
     */
    public long toYobibytes(final long size) {
        return BinaryByteUnit.YOBIBYTES.from(size, this);
    }

    /**
     * Converts the given size from the specified {@code BinaryByteUnit} to this {@code BinaryByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code BinaryByteUnit} to this {@code BinaryByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BinaryByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");

        if (this == unit)
            return size;

        return divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP);
    }

    /**
     * Converts the given size from the specified {@code DecimalByteUnit} to this {@code BinaryByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code DecimalByteUnit} to this {@code BinaryByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final DecimalByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP);
    }

    /**
     * Converts the given size from the specified {@code BitUnit} to this {@code BinaryByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code BitUnit} to this {@code BinaryByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BitUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return multiplyExact(divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP), 8);
    }

//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.BYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#BYTES}
//     */
//    public long fromBytes(final long size) {
//        return from(size, BinaryByteUnit.BYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.KIBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#KIBIBYTES}
//     */
//    public long fromKibibytes(final long size) {
//        return from(size, BinaryByteUnit.KIBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.MEBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#MEBIBYTES}
//     */
//    public long fromMebibytes(final long size) {
//        return from(size, BinaryByteUnit.MEBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.GIBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#GIBIBYTES}
//     */
//    public long fromGibibytes(final long size) {
//        return from(size, BinaryByteUnit.GIBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.TEBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#TEBIBYTES}
//     */
//    public long fromTebibytes(final long size) {
//        return from(size, BinaryByteUnit.TEBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.PEBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#PEBIBYTES}
//     */
//    public long fromPebibytes(final long size) {
//        return from(size, BinaryByteUnit.PEBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.EXBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#EXBIBYTES}
//     */
//    public long fromExbibytes(final long size) {
//        return from(size, BinaryByteUnit.EXBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.ZEBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#ZEBIBYTES}
//     */
//    public long fromZebibytes(final long size) {
//        return from(size, BinaryByteUnit.ZEBIBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BinaryByteUnit) from(size, BinaryByteUnit.YOBIBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code BinaryByteUnit} to {@link BinaryByteUnit#YOBIBYTES}
//     */
//    public long fromYobibytes(final long size) {
//        return from(size, BinaryByteUnit.YOBIBYTES);
//    }

    /**
     * Returns the <a href="https://physics.nist.gov/cuu/Units/binary.html" target="_blank">binary</a> symbol of this
     * {@code BinaryByteUnit}.
     * 
     * @return the <a href="https://physics.nist.gov/cuu/Units/binary.html" target="_blank">binary</a> symbol of this
     *         {@code BinaryByteUnit}
     */
    @Override
    public String toString() {
        return symbol;
    }

}