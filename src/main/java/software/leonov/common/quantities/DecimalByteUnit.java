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
 * Defines the commonly used <a href="https://physics.nist.gov/cuu/Units/prefixes.html" target="_blank">SI prefixes</a>
 * that represent the <i>power-of-10</i> byte size of the file system and memory. This {@code Enum} is a poor attempt to
 * deal with the surprisingly <a target="_blank" href=
 * "https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Quantities_of_bytes_and_bits" >difficult
 * issue</a> of representing the size of digital quantities.
 * <p>
 * A {@code DecimalByteUnit} does not hold the size information, but only helps organize and use byte size
 * representations that may be maintained separately across various contexts.
 * <p>
 * The <i>convert</i> methods in this {@code Enum} are directional, meaning they accept negative arguments. Conversions
 * from finer to coarser granularities round towards the <i>nearest neighbor</i> using {@link RoundingMode#HALF_UP}. For
 * example converting to {@code KILOBYTES.from(499, BYTES) == 0} while converting to
 * {@code KILOBYTES.from(500, BYTES) == 1}. Conversions from coarser to finer granularities with arguments that would
 * overflow a {@code long} will result in an {@code ArithmeticException}.
 * 
 * @author Zhenya Leonov
 */
public enum DecimalByteUnit {

    /**
     * A single byte consists of 8 bits.
     */
    BYTES(1, "B"),

    /**
     * A kilobyte (kB) consists of 1000 bytes.
     */
    KILOBYTES(1000, "kB"),

    /**
     * A megabyte (MB) consists of 1000 kilobytes.
     */
    MEGABYTES(KILOBYTES.base * 1000, "MB"),

    /**
     * A gigabyte (GB) consists of 1000 megabytes.
     */
    GIGABYTES(MEGABYTES.base * 1000, "GB"),

    /**
     * A terabyte (TB) consists of 1000 gigabytes.
     */
    TERABYTES(GIGABYTES.base * 1000, "TB"),

    /**
     * A petabyte (PB) consists of 1000 terabytes.
     */
    PETABYTES(TERABYTES.base * 1000, "PB"),

    /**
     * An exabyte (EB) consists of 1000 petabytes.
     */
    EXABYTES(PETABYTES.base * 1000, "EB"),

    /**
     * A zettabyte (ZB) consists of 1000 exabytes.
     */
    ZETTABYTES(EXABYTES.base * 1000, "ZB"),

    /**
     * A yottabyte (YB) consists of 1000 zettabytes.
     */
    YOTTABYTES(ZETTABYTES.base * 1000, "YB");

    final long base;
    private final String symbol;

    private DecimalByteUnit(final long base, final String symbol) {
        this.base = base;
        this.symbol = symbol;
    }

//    /**
//     * Converts the given size from this {@code DecimalByteUnit} to the specified {@code BinaryByteUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code BinaryByteUnit}
//     * @return the given size converted from this {@code DecimalByteUnit} to the specified {@code BinaryByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BinaryByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return divide(multiply(size, this.base), unit.base);
//    }
//
//    /**
//     * Converts the given size from this {@code DecimalByteUnit} to the specified {@code DecimalByteUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code DecimalByteUnit}
//     * @return the given size converted from this {@code DecimalByteUnit} to the specified {@code DecimalByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final DecimalByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        if (this == unit)
//            return size;
//
//        return divide(multiply(size, this.base), unit.base);
//    }
//
//    /**
//     * Converts the given size from this {@code DecimalByteUnit} to the specified {@code BitUnit}.
//     * 
//     * @param size the size to convert
//     * @param unit the specified {@code BitUnit}
//     * @return the given size converted from this {@code DecimalByteUnit} to the specified {@code BitUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BitUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return multiply(divide(multiply(size, this.base), unit.base), 8);
//    }

    /**
     * Shorthand for {@code DecimalByteUnit.BYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#BYTES}
     */
    public long toBytes(final long size) {
        return DecimalByteUnit.BYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.KILOBYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#KILOBYTES}
     */
    public long toKilobytes(final long size) {
        return DecimalByteUnit.KILOBYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.MEGABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#MEGABYTES}
     */
    public long toMegabytes(final long size) {
        return DecimalByteUnit.MEGABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.GIGABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#GIGABYTES}
     */
    public long toGigabytes(final long size) {
        return DecimalByteUnit.GIGABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.TERABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#TERABYTES}
     */
    public long toTerabytes(final long size) {
        return DecimalByteUnit.TERABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.PETABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#PETABYTES}
     */
    public long toPetabytes(final long size) {
        return DecimalByteUnit.PETABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.EXABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#EXABYTES}
     */
    public long toExabytes(final long size) {
        return DecimalByteUnit.EXABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.ZETTABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#ZETTABYTES}
     */
    public long toZettabytes(final long size) {
        return DecimalByteUnit.ZETTABYTES.from(size, this);
    }

    /**
     * Shorthand for {@code DecimalByteUnit.YOTTABYTES.from(size, this)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#YOTTABYTES}
     */
    public long toYottabytes(final long size) {
        return DecimalByteUnit.YOTTABYTES.from(size, this);
    }

    /**
     * Converts the given size from the specified {@code BinaryByteUnit} to this {@code DecimalByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size converted from the specified {@code BinaryByteUnit} to this {@code DecimalByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BinaryByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP);
    }

    /**
     * Converts the given size from the specified {@code DecimalByteUnit} to this {@code DecimalByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size converted from the specified {@code DecimalByteUnit} to this {@code DecimalByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final DecimalByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");

        if (this == unit)
            return size;

        return divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP);
    }

    /**
     * Converts the given size from the specified {@code BitUnit} to this {@code DecimalByteUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size converted from the specified {@code BitUnit} to this {@code DecimalByteUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BitUnit unit) {
        Objects.requireNonNull(unit, "unit == null");
        return multiplyExact(divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP), 8);
    }

//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.BYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#BYTES}
//     */
//    public long fromBytes(final long size) {
//        return from(size, DecimalByteUnit.BYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.KILOBYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#KILOBYTES}
//     */
//    public long fromKilobytes(final long size) {
//        return from(size, DecimalByteUnit.KILOBYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.MEGABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#MEGABYTES}
//     */
//    public long fromMegabytes(final long size) {
//        return from(size, DecimalByteUnit.MEGABYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.GIGABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#GIGABYTES}
//     */
//    public long fromGigabytes(final long size) {
//        return from(size, DecimalByteUnit.GIGABYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.TERABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#TERABYTES}
//     */
//    public long fromTerabytes(final long size) {
//        return from(size, DecimalByteUnit.TERABYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.PETABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#PETABYTES}
//     */
//    public long fromPetabytes(final long size) {
//        return from(size, DecimalByteUnit.PETABYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.EXABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#EXABYTES}
//     */
//    public long fromExabytes(final long size) {
//        return from(size, DecimalByteUnit.EXABYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.ZETTABYTES)}.
//     * 
//     * @param size the size to convert
//     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#ZETTABYTES}
//     */
//    public long fromZettabytes(final long size) {
//        return from(size, DecimalByteUnit.ZETTABYTES);
//    }

    /**
     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, DecimalByteUnit.YOTTABYTES)}.
     * 
     * @param size the size to convert
     * @return the given size converted from this {@code DecimalByteUnit} to {@link DecimalByteUnit#YOTTABYTES}
     */
    public long fromYottabytes(final long size) {
        return from(size, DecimalByteUnit.YOTTABYTES);
    }

    /**
     * Returns the <a href="https://physics.nist.gov/cuu/Units/prefixes.html" target="_blank">SI</a> symbol of this
     * {@code DecimalByteUnit}.
     * 
     * @return the <a href="https://physics.nist.gov/cuu/Units/prefixes.html" target="_blank">SI</a> symbol of this
     *         {@code DecimalByteUnit}
     */
    @Override
    public String toString() {
        return symbol;
    }

}