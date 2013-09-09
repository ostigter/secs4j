// This file is part of the secs4j project, an open source SECS/GEM
// library written in Java.
//
// Copyright 2013 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.secs4j.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs4j.util.ConversionUtils;

/**
 * Base class of integer-based SECS data items.
 * 
 * @author Oscar Stigter
 */
public class IntegerBase implements Data<List<Long>> {

    /** The values. */
    private List<Long> values = new ArrayList<Long>();

    /** SECS name. */
    private String name;

    /** SECS format code. */
    private int formatCode;

    /** Whether the integer is signed. */
    private boolean isSigned;

    /** Size in bytes. */
    private int size;

    /** Minimum vlaue. */
    private long minValue;

    /** Maximum value. */
    private long maxValue;

    /**
     * Constructor.
     * 
     * @param name
     *            The SECS name.
     * @param formatCode
     *            The SECS format code.
     * @param isSigned
     *            Whether the integer is signed.
     * @param size
     *            The size in bytes.
     * @param minValue
     *            The minimum value.
     * @param maxValue
     *            The maximum value.
     */
    public IntegerBase(String name, int formatCode, boolean isSigned, int size, long minValue, long maxValue) {
        this.name = name;
        this.formatCode = formatCode;
        this.isSigned = isSigned;
        this.size = size;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Returns the value at a specific index position.
     * 
     * @param index
     *            The index position.
     * 
     * @return The value.
     */
    public long getValue(int index) {
        return values.get(index);
    }

    /**
     * Adds a value to the end of the sequence.
     * 
     * @param value
     *            The value to add.
     */
    public void addValue(long value) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        values.add(value);
    }

    /**
     * Adds a value to the end of the sequence, based on a raw byte buffer.
     * 
     * @param data
     *            The byte buffer containing a single value.
     */
    public void addValue(byte[] data) {
        if (data.length != size) {
            throw new IllegalArgumentException(String.format("Invalid %s length: %d bytes", name, data.length));
        }
        addValue((isSigned) ? ConversionUtils.bytesToSignedInteger(data) : ConversionUtils.bytesToUnsignedInteger(data));
    }

    @Override
    public List<Long> getValue() {
        return values;
    }

    @Override
    public void setValue(List<Long> values) {
        this.values = values;
    }

    @Override
    public int length() {
        return values.size();
    }

    @Override
    public byte[] toByteArray() {
        // Determine length.
        int length = values.size() * size;
        int noOfLengthBytes = 1;
        B lengthBytes = new B();
        lengthBytes.add(length & 0xff);
        if (length > 0xff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 8) & 0xff);
        }
        if (length > 0xffff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 16) & 0xff);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // Write format byte.
            baos.write(formatCode | noOfLengthBytes);

            // Write length bytes.
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }

            // Write values.
            for (long value : values) {
                baos.write(ConversionUtils.integerToBytes(value, size));
            }

            return baos.toByteArray();

        } catch (IOException e) {
            // This should never happen.
            throw new RuntimeException("Could not serialize data item", e);

        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
        int length = length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:%d", name, length));
        if (length > 0) {
            boolean first = true;
            sb.append(" {");
            for (long value : values) {
                if (!first) {
                    sb.append(' ');
                } else {
                    first = false;
                }
                sb.append(value);
            }
            sb.append('}');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerBase) {
            IntegerBase ib = (IntegerBase) obj;
            int length = ib.length();
            if (length == this.size) {
                for (int i = 0; i < length; i++) {
                    if (ib.getValue(i) != values.get(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toSml();
    }

}
