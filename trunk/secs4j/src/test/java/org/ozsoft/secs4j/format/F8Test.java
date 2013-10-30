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
import org.ozsoft.secs4j.format.F8;

public class F8Test {
    
    @Test
    public void test() {
        F8 f8 = new F8();
        Assert.assertEquals(0, f8.length());
        Assert.assertEquals("<F8>", f8.toSml());
        
        f8.addValue(0.0);
        f8.addValue(1.0);
        f8.addValue(-1.0);
        f8.addValue(Double.MAX_VALUE);
        f8.addValue(Double.MIN_VALUE);
        f8.addValue(Double.NaN);
        Assert.assertEquals(6, f8.length());
        Assert.assertEquals("<F8 0.0 1.0 -1.0 1.7976931348623157E308 4.9E-324 NaN>", f8.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0x81, 0x30,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x3f, (byte) 0xf0, 0x00, 0x00,0x00, 0x00, 0x00, 0x00,
                (byte) 0xbf, (byte) 0xf0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x7f, (byte) 0xef, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 
                0x7f, (byte) 0xf8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                }, f8.toByteArray());
    }
    
}
