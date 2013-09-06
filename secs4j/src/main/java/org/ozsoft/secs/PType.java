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

package org.ozsoft.secs;

/**
 * HSMS PType byte with the message protocol. <br />
 * <br />
 * 
 * Only the SECS_II value is supported.
 * 
 * @author Oscar Stigter
 */
public enum PType {

    /** SECS-II protocol. */
    SECS_II(0x00),

    /** Unknown protocol. */
    UNKNOWN(0xff),

    ;

    private int value;

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     */
    PType(int value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * 
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Parses a value as PType.
     * 
     * @param value
     *            The value to parse.
     * @return The PType.
     */
    public static PType parse(int value) {
        for (PType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return PType.UNKNOWN;
    }

}
