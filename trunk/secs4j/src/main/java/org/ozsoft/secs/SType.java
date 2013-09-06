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
 * HSMS SType byte with the message type.
 * 
 * @author Oscar Stigter
 */
public enum SType {

    /** DATA message. */
    DATA(0x00),

    /** SELECT_REQ message. */
    SELECT_REQ(0x01),

    /** SELECT_RESP message. */
    SELECT_RSP(0x02),

    /** DESELECT_REQ message. */
    DESELECT_REQ(0x03),

    /** DESELECT_RSP message. */
    DESELECT_RSP(0x04),

    /** LINKTEST_REQ message. */
    LINKTEST_REQ(0x05),

    /** LINKTEST_RSP message. */
    LINKTEST_RSP(0x06),

    /** REJECT message. */
    REJECT(0x07),

    /** SEPARATE message. */
    SEPARATE(0x09),

    /** Unknown message type. */
    UNKNOWN(0xff),

    ;

    private int value;

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     */
    SType(int value) {
        this.value = value;
    }

    /**
     * Returns the SType value.
     * 
     * @return The SType value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Parse a value as SType.
     * 
     * @param value
     *            The value to parse.
     * 
     * @return The SType.
     */
    public static SType parse(int value) {
        for (SType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return SType.UNKNOWN;
    }

}
