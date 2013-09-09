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

package org.ozsoft.secs4j.util;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs4j.format.TestUtils;
import org.ozsoft.secs4j.util.ConversionUtils;

/**
 * Test suite for the {@link ConversionUtils} class.
 * 
 * @author Oscar Stigter
 */
public class ConversionUtilsTest {
    
    /**
     * Tests converting an integer to a byte arrays.
     */
    @Test
    public void integerToBytes() {
        // 1-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00}, ConversionUtils.integerToBytes(0, 1));
        TestUtils.assertEquals(new byte[] {0x01}, ConversionUtils.integerToBytes(1, 1));
        TestUtils.assertEquals(new byte[] {(byte) 0xff}, ConversionUtils.integerToBytes(-1, 1));
        TestUtils.assertEquals(new byte[] {0x7f}, ConversionUtils.integerToBytes(127, 1));
        TestUtils.assertEquals(new byte[] {(byte) 0x81}, ConversionUtils.integerToBytes(-127, 1));
        TestUtils.assertEquals(new byte[] {(byte) 0x80}, ConversionUtils.integerToBytes(-128, 1));
        
        // 2-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00, 0x00}, ConversionUtils.integerToBytes(0, 2));
        TestUtils.assertEquals(new byte[] {0x00, 0x01}, ConversionUtils.integerToBytes(1, 2));
        TestUtils.assertEquals(new byte[] {0x00, 0x7f}, ConversionUtils.integerToBytes(127, 2));
        TestUtils.assertEquals(new byte[] {0x00, (byte) 0x80}, ConversionUtils.integerToBytes(128, 2));
        TestUtils.assertEquals(new byte[] {0x00, (byte) 0xff}, ConversionUtils.integerToBytes(255, 2));
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, ConversionUtils.integerToBytes(256, 2));
        TestUtils.assertEquals(new byte[] {0x01, 0x01}, ConversionUtils.integerToBytes(257, 2));
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff}, ConversionUtils.integerToBytes(-1, 2));
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0x01}, ConversionUtils.integerToBytes(-255, 2));
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0x00}, ConversionUtils.integerToBytes(-256, 2));
        TestUtils.assertEquals(new byte[] {(byte) 0xfe, (byte) 0xff}, ConversionUtils.integerToBytes(-257, 2));
        
        // 4-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x00, 0x00, 0x00, 0x01}, ConversionUtils.integerToBytes(0x00000001, 4));
        TestUtils.assertEquals(new byte[] {0x11, 0x22, 0x33, 0x44}, ConversionUtils.integerToBytes(0x11223344, 4));
        TestUtils.assertEquals(new byte[] {0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff}, ConversionUtils.integerToBytes(Integer.MAX_VALUE, 4));
        TestUtils.assertEquals(new byte[] {(byte) 0x80, 0x00, 0x00, 0x00}, ConversionUtils.integerToBytes(Integer.MIN_VALUE, 4));
        TestUtils.assertEquals(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, ConversionUtils.integerToBytes(0xffffffffL, 4));

        // 8-byte signed integer.
        TestUtils.assertEquals(new byte[] {0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88}, ConversionUtils.integerToBytes(0x1122334455667788L, 8));
    }
    
    /**
     * Tests converting a byte array to a signed integer.
     */
    @Test
    public void bytesToSignedInteger() {
        // 1-byte signed integer.
        Assert.assertEquals(0, ConversionUtils.bytesToSignedInteger(new byte[] {0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToSignedInteger(new byte[] {0x01}));
        Assert.assertEquals(127, ConversionUtils.bytesToSignedInteger(new byte[] {0x7f}));
        Assert.assertEquals(-128, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0x80}));
        Assert.assertEquals(0, ConversionUtils.bytesToI1(new byte[] {0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToI1(new byte[] {0x01}));
        Assert.assertEquals(127, ConversionUtils.bytesToI1(new byte[] {0x7f}));
        Assert.assertEquals(-128, ConversionUtils.bytesToI1(new byte[] {(byte) 0x80}));
        
        // 2-byte signed integer.
        Assert.assertEquals(0, ConversionUtils.bytesToSignedInteger(new byte[] {0x00, 0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToSignedInteger(new byte[] {0x00, 0x01}));
        Assert.assertEquals(-1, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0xff}));
        Assert.assertEquals(127, ConversionUtils.bytesToSignedInteger(new byte[] {0x00, 0x7f}));
        Assert.assertEquals(-128, ConversionUtils.bytesToSignedInteger(new byte[] {0x00, (byte) 0x80}));
        Assert.assertEquals(258, ConversionUtils.bytesToSignedInteger(new byte[] {0x01, 0x02}));
        Assert.assertEquals(0, ConversionUtils.bytesToI2(new byte[] {0x00, 0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToI2(new byte[] {0x00, 0x01}));
        Assert.assertEquals(-1, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0xff}));
        Assert.assertEquals(127, ConversionUtils.bytesToI2(new byte[] {0x00, 0x7f}));
        Assert.assertEquals(128, ConversionUtils.bytesToI2(new byte[] {0x00, (byte) 0x80}));
        Assert.assertEquals(129, ConversionUtils.bytesToI2(new byte[] {0x00, (byte) 0x81}));
        Assert.assertEquals(255, ConversionUtils.bytesToI2(new byte[] {0x00, (byte) 0xff}));
        Assert.assertEquals(258, ConversionUtils.bytesToI2(new byte[] {0x01, 0x02}));
        Assert.assertEquals(-129, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0x7f}));
        Assert.assertEquals(-128, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0x80}));
        Assert.assertEquals(-127, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0x81}));
        
        Assert.assertEquals(-127, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0x81}));
        Assert.assertEquals(-128, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0x80}));
        Assert.assertEquals(-129, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0x7f}));
        Assert.assertEquals(-255, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0x01}));
        Assert.assertEquals(-256, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xff, (byte) 0x00}));
//        Assert.assertEquals(-257, ConversionUtils.bytesToSignedInteger(new byte[] {(byte) 0xfe, (byte) 0xff}));
        
        Assert.assertEquals(-255, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0x01}));
        Assert.assertEquals(-256, ConversionUtils.bytesToI2(new byte[] {(byte) 0xff, (byte) 0x00}));
        Assert.assertEquals(-257, ConversionUtils.bytesToI2(new byte[] {(byte) 0xfe, (byte) 0xff}));
        Assert.assertEquals(-511, ConversionUtils.bytesToI2(new byte[] {(byte) 0xfe, (byte) 0x01}));
        Assert.assertEquals(-512, ConversionUtils.bytesToI2(new byte[] {(byte) 0xfe, (byte) 0x00}));
        Assert.assertEquals(-513, ConversionUtils.bytesToI2(new byte[] {(byte) 0xfd, (byte) 0xff}));
        Assert.assertEquals(-32767, ConversionUtils.bytesToI2(new byte[] {(byte) 0x80, (byte) 0x01}));
        Assert.assertEquals(-32768, ConversionUtils.bytesToI2(new byte[] {(byte) 0x80, (byte) 0x00}));

        // 4-byte signed integer.
        Assert.assertEquals(-255, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01}));
        Assert.assertEquals(-256, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00}));
        Assert.assertEquals(-257, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0xff}));
        Assert.assertEquals(-511, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0x01}));
        Assert.assertEquals(-512, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0x00}));
        Assert.assertEquals(-513, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xfd, (byte) 0xff}));
        Assert.assertEquals(-65535, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x01}));
        Assert.assertEquals(-65536, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00}));
        Assert.assertEquals(-65537, ConversionUtils.bytesToI4(new byte[] {(byte) 0xff, (byte) 0xfe, (byte) 0xff, (byte) 0xff}));

        // 8-byte signed integer.
        Assert.assertEquals(-255, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01}));
        Assert.assertEquals(-256, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00}));
        Assert.assertEquals(-257, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0xff}));
        Assert.assertEquals(-511, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0x01}));
        Assert.assertEquals(-512, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0x00}));
        Assert.assertEquals(-513, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfd, (byte) 0xff}));
        Assert.assertEquals(-65535, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x01}));
        Assert.assertEquals(-65536, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00}));
        Assert.assertEquals(-65537, ConversionUtils.bytesToI8(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe, (byte) 0xff, (byte) 0xff}));
    }

    /**
     * Tests converting a byte array to an unsigned integer.
     */
    @Test
    public void bytesToUnsignedInteger() {
        // 1-byte unsigned integer.
        Assert.assertEquals(0, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00}));
        Assert.assertEquals(1, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01}));
        Assert.assertEquals(127, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x7f}));
        Assert.assertEquals(128, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0x80}));
        Assert.assertEquals(255, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff}));

        // 2-byte unsigned integer.
        Assert.assertEquals(0x0000, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x00}));
        Assert.assertEquals(0x0001, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x01}));
        Assert.assertEquals(0x007f, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, 0x7f}));
        Assert.assertEquals(0x0080, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x00, (byte) 0x80}));
        Assert.assertEquals(0x0100, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01, 0x00}));
        Assert.assertEquals(0x0101, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x01, 0x01}));
        Assert.assertEquals(0x7fff, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x7f, (byte) 0xff}));
        Assert.assertEquals(0xffff, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff, (byte) 0xff}));

        // 4-byte unsigned integer.
        Assert.assertEquals(0x11223344, ConversionUtils.bytesToUnsignedInteger(new byte[] {0x11, 0x22, 0x33, 0x44}));

        // 8-byte unsigned integer.
        Assert.assertEquals(0xffffffffL, ConversionUtils.bytesToUnsignedInteger(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}));
    }

}
