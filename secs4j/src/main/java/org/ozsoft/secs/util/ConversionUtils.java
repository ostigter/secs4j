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

package org.ozsoft.secs.util;

/**
 * Generic conversion utilities.
 * 
 * @author Oscar Stigter
 */
public abstract class ConversionUtils {

    /**
     * Converts an integer into a byte array.
     * 
     * @param value
     *            The integer (long).
     * @param length
     *            The length of the byte array.
     * 
     * @return The byte array.
     */
    public static byte[] integerToBytes(long value, int length) {
        final byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = (byte) (value >> (length - i - 1) * 8);
        }
        return data;
    }
    
    public static long bytesToSignedInteger(byte[] data) {
        final int length = data.length;
        long value = 0L;
        for (int i = 0; i < length; i++) {
            long b = data[i];
            value |= b << (length - i - 1) * 8;
        }
        return value;
    }

    public static long bytesToUnsignedInteger(byte[] data) {
        final int length = data.length;
        long value = 0L;
        for (int i = 0; i < length; i++) {
            long b = data[i];
            if (b < 0) {
                b += 256;
            }
            value |= b << (length - i - 1) * 8;
        }
        return value;
    }

}
