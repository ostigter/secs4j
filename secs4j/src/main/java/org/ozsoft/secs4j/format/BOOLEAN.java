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

/**
 * SECS data item BOOLEAN (a single boolean).
 * 
 * @author Oscar Stigter
 */
public class BOOLEAN implements Data<Boolean> {

    /** SECS format code. */
    public static final int FORMAT_CODE = 0x10;

    /** Fixed length of 1 byte. */
    public static final int LENGTH = 1;

    /** Byte value for FALSE. */
    public static final byte FALSE = 0x00;

    /** Byte value for TRUE. */
    public static final byte TRUE = 0x01;

    /** SML value for FALSE. */
    public static final String FALSE_SML = "BOOLEAN {False}";

    /** SML value for TRUE. */
    public static final String TRUE_SML = "BOOLEAN {True}";

    /** The boolean value. */
    private boolean value;

    /**
     * Constructor based on a byte value.
     * 
     * @param b
     *            The byte value.
     */
    public BOOLEAN(byte b) {
        setValue(b == FALSE ? false : true);
    }

    /**
     * Constructor based on a byte value.
     * 
     * @param b
     *            The byte value.
     */
    public BOOLEAN(int b) {
        setValue(b == FALSE ? false : true);
    }

    /**
     * Constructor with an initial value.
     * 
     * @param value
     *            The initial value.
     */
    public BOOLEAN(boolean value) {
        setValue(value);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public int length() {
        return LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[] { FORMAT_CODE, LENGTH, (value) ? TRUE : FALSE };
    }

    @Override
    public String toSml() {
        return value ? TRUE_SML : FALSE_SML;
    }

    @Override
    public String toString() {
        return toSml();
    }

}
