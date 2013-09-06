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

package org.ozsoft.secs.format;

import org.junit.Assert;
import org.junit.Test;

public class U4Test {
    
    @Test
    public void test() {
        U4 u4 = new U4();
        u4.addValue(0);
        u4.addValue(1);
        u4.addValue(255);
        u4.addValue(256);
        u4.addValue(257);
        u4.addValue(65535);
        u4.addValue(0xffffffffL);
        u4.addValue(new byte[] {0x11, 0x22, 0x33, 0x44});
        u4.addValue(new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, });
        Assert.assertEquals(9, u4.length());
        Assert.assertEquals("U4:9 {0 1 255 256 257 65535 4294967295 287454020 4294967295}", u4.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xb1, 0x24, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
                (byte) 0xff, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0xff, (byte) 0xff,(byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, 0x11, 0x22, 0x33, 0x44, (byte) 0xff,(byte) 0xff, (byte) 0xff, (byte) 0xff}, u4.toByteArray());
    }
    
}
