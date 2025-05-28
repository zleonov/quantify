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
 * that represent the <i>power-of-10</i> bit size of the file system and memory. This {@code Enum} is a poor attempt to
 * deal with the surprisingly <a target="_blank" href=
 * "https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Quantities_of_bytes_and_bits" >difficult
 * issue</a> of representing the size of digital quantities.
 * <p>
 * A {@code BitUnit} does not hold the size information, but only helps organize and use bit size representations that
 * may be maintained separately across various contexts.
 * <p>
 * The <i>convert</i> methods in this {@code Enum} are directional, meaning they accept negative arguments. Conversions
 * from finer to coarser granularities round towards the <i>nearest neighbor</i> using {@link RoundingMode#HALF_UP}. For
 * example converting to {@code KILOBITS.from(499, BITS) == 0} while converting to
 * {@code KILOBITS.from(500, BITS) == 1}. Conversions from coarser to finer granularities with arguments that would
 * overflow a {@code long} will result in an {@code ArithmeticException}.
 * 
 * @author Zhenya Leonov
 */
public enum BitUnit {

    /**
     * The base unit of information.
     */
    BITS(1, "b"),

    /**
     * A kilobit (kb) consists of 1000 bits.
     */
    KILOBITS(1000, "kb"),

    /**
     * A megabit (Mb) consists of 1000 bits.
     */
    MEGABITS(KILOBITS.base * 1000, "Mb"),

    /**
     * A gigabit (Gb) consists of 1000 megabits.
     */
    GIGABITS(MEGABITS.base * 1000, "Gb"),

    /**
     * A terabit (Tb) consists of 1000 gigabits.
     */
    TERABITS(GIGABITS.base * 1000, "Tb");

    final long base;
    private final String symbol;

    private BitUnit(final long base, final String symbol) {
        this.base = base;
        this.symbol = symbol;
    }

//    /**
//     * Converts the given size from this {@code BitUnit} to the specified {@code BinaryByteUnit}.
//     * 
//     * @param size the size to to
//     * @param unit the specified {@code BinaryByteUnit}
//     * @return the given size converted from this {@code BitUnit} to the specified {@code BinaryByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BinaryByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return divide(divide(multiply(size, this.base), unit.base), 8);
//    }
//
//    /**
//     * Converts the given size from this {@code BitUnit} to the specified {@code DecimalByteUnit}.
//     * 
//     * @param size the size to to
//     * @param unit the specified {@code DecimalByteUnit}
//     * @return the given size converted from this {@code BitUnit} to the specified {@code DecimalByteUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final DecimalByteUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        return divide(divide(multiply(size, this.base), unit.base), 8);
//    }
//
//    /**
//     * Converts the given size from this {@code BitUnit} to the specified {@code BitUnit}.
//     * 
//     * @param size the size to to
//     * @param unit the specified {@code BitUnit}
//     * @return the given size converted from this {@code BitUnit} to the specified {@code BitUnit}
//     * @throws ArithmeticException if the result overflows a {@code long}
//     */
//    public long to(final long size, final BitUnit unit) {
//        Objects.requireNonNull(unit, "unit == null");
//
//        if (this == unit)
//            return size;
//
//        return divide(multiply(size, this.base), unit.base);
//    }

    /**
     * Shorthand for {@code BitUnit.BITS.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#BITS}
     */
    public long toBits(final long size) {
        return BitUnit.BITS.from(size, this);
    }

    /**
     * Shorthand for {@code BYTES.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link DecimalByteUnit#BYTES}
     */
    public long toBytes(final long size) {
        return DecimalByteUnit.BYTES.from(size, this);
    }

    /**
     * Shorthand for {@code BitUnit.KILOBITS.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#KILOBITS}
     */
    public long toKilobits(final long size) {
        return BitUnit.KILOBITS.from(size, this);
    }

    /**
     * Shorthand for {@code BitUnit.MEGABITS.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#MEGABITS}
     */
    public long toMegabits(final long size) {
        return BitUnit.MEGABITS.from(size, this);
    }

    /**
     * Shorthand for {@code BitUnit.GIGABITS.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#GIGABITS}
     */
    public long toGigabits(final long size) {
        return BitUnit.GIGABITS.from(size, this);
    }

    /**
     * Shorthand for {@code BitUnit.TERABITS.from(size, this)}.
     * 
     * @param size the size to to
     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#TERABITS}
     */
    public long toTerabits(final long size) {
        return BitUnit.TERABITS.from(size, this);
    }

    /**
     * Converts the given size from the specified {@code BinaryByteUnit} to this {@code BitUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code BinaryByteUnit} to this {@code BitUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BinaryByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");

        return multiplyExact(divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP), 8);
    }

    /**
     * Converts the given size from the specified {@code DecimalByteUnit} to this {@code BitUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code DecimalByteUnit} to this {@code BitUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final DecimalByteUnit unit) {
        Objects.requireNonNull(unit, "unit == null");

        return multiplyExact(divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP), 8);
    }

    /**
     * Converts the given size from the specified {@code BitUnit} to this {@code BitUnit}.
     * 
     * @param size the size to convert
     * @param unit the specified {@code BitUnit}
     * @return the given size from the specified {@code BitUnit} to this {@code BitUnit}
     * @throws ArithmeticException if the result overflows a {@code long}
     */
    public long from(final long size, final BitUnit unit) {
        Objects.requireNonNull(unit, "unit == null");

        if (this == unit)
            return size;

        return divideExact(multiplyExact(size, unit.base), this.base, RoundingMode.HALF_UP);
    }

//    /**
//     * Shorthand for {@link #from(long, BitUnit) from(size, BitUnit.BITS)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#BITS}
//     */
//    public long fromBits(final long size) {
//        return from(size, BitUnit.BITS);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, DecimalByteUnit) from(size, BYTES)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link DecimalByteUnit#BYTES}
//     */
//    public long fromBytes(final long size) {
//        return from(size, DecimalByteUnit.BYTES);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BitUnit) from(size, BitUnit.KILOBITS)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#KILOBITS}
//     */
//    public long fromKilobits(final long size) {
//        return from(size, BitUnit.KILOBITS);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BitUnit) from(size, BitUnit.MEGABITS)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#MEGABITS}
//     */
//    public long fromMegabits(final long size) {
//        return from(size, BitUnit.MEGABITS);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BitUnit) from(size, BitUnit.GIGABITS)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#GIGABITS}
//     */
//    public long fromGigabits(final long size) {
//        return from(size, BitUnit.GIGABITS);
//    }
//
//    /**
//     * Shorthand for {@link #from(long, BitUnit) from(size, BitUnit.TERABITS)}.
//     * 
//     * @param size the size to to
//     * @return the given size converted from this {@code BitUnit} to {@link BitUnit#TERABITS}
//     */
//    public long fromTerabits(final long size) {
//        return from(size, BitUnit.TERABITS);
//    }

    /**
     * Returns the <a target="_blank" href="https://physics.nist.gov/cuu/Units/prefixes.html">SI</a> symbol of this
     * {@code BitUnit}.
     * 
     * @return the <a target="_blank" href="https://physics.nist.gov/cuu/Units/prefixes.html">SI</a> symbol of this
     *         {@code BitUnit}
     */
    @Override
    public String toString() {
        return symbol;
    }

}