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

public class U2Test {
    
    @Test
    public void test() {
        U2 u2 = new U2();
        u2.addValue(0);
        u2.addValue(1);
        u2.addValue(255);
        u2.addValue(256);
        u2.addValue(257);
        u2.addValue(65535);
        u2.addValue(new byte[] {0x11, 0x22});
        u2.addValue(new byte[] {(byte) 0xff, (byte) 0xff});
        Assert.assertEquals(8, u2.length());
        Assert.assertEquals("U2:8 {0 1 255 256 257 65535 4386 65535}", u2.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xa9, 0x10, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xff, 0x01, 0x00,
                0x01, 0x01, (byte) 0xff, (byte) 0xff, 0x11, 0x22, (byte) 0xff, (byte) 0xff}, u2.toByteArray());
    }
    
}
