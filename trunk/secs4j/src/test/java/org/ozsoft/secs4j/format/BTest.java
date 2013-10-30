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
import org.ozsoft.secs4j.format.B;

public class BTest {
    
    @Test
    public void test() {
        B b = new B();
        Assert.assertEquals(0, b.length());
        TestUtils.assertEquals(new byte[] {0x21, 0x00}, b.toByteArray());
        
        Assert.assertEquals("<B>", b.toSml());
        b.add(0x01);
        b.add(0x02);
        b.add(0x7f);
        b.add(0x80);
        b.add(0xff);
        Assert.assertEquals(5, b.length());
        Assert.assertEquals(1, b.get(0));
        Assert.assertEquals(255, b.get(4));
        TestUtils.assertEquals(new byte[] {0x21, 0x05, 0x01, 0x02, 0x7f, (byte) 0x80, (byte) 0xff}, b.toByteArray());
        Assert.assertEquals("<B 0x01 0x02 0x7f 0x80 0xff>", b.toSml());
        
        b.add(new byte[] {100, 101, 102});
        Assert.assertEquals(8, b.length());
        TestUtils.assertEquals(new byte[] {0x21, 0x08, 0x01, 0x02, 0x7f, (byte) 0x80, (byte) 0xff, 100, 101, 102}, b.toByteArray());
        Assert.assertEquals("<B 0x01 0x02 0x7f 0x80 0xff 0x64 0x65 0x66>", b.toSml());
    }
    
}
