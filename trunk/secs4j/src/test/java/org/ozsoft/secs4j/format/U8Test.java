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

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs4j.format.U8;

public class U8Test {
    
    @Test
    public void test() {
        U8 u8 = new U8();
        u8.addValue(0L);
        u8.addValue(0x1122334455667788L);
        u8.addValue(0x7fffffffffffffffL);
        Assert.assertEquals(3, u8.length());
        Assert.assertEquals("U8:3 {0 1234605616436508552 9223372036854775807}", u8.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xa1, 0x18,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88,
                (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, 
                u8.toByteArray());
    }
    
}
