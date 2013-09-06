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

public class I2Test {
    
    @Test
    public void test() {
        I2 i2 = new I2();
        i2.addValue(0);
        i2.addValue(1);
        i2.addValue(-1);
        i2.addValue(32767);
        i2.addValue(-32768);
        Assert.assertEquals(5, i2.length());
        Assert.assertEquals("I2:5 {0 1 -1 32767 -32768}", i2.toSml());
        TestUtils.assertEquals(new byte[] {0x69, 0x0a, 0x00, 0x00, 0x00, 0x01,
                (byte) 0xff, (byte) 0xff, (byte) 0x7f, (byte) 0xff, (byte) 0x80, 0x00}, i2.toByteArray());
    }
    
}
