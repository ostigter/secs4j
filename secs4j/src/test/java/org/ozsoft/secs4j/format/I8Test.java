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
import org.ozsoft.secs4j.format.I8;

public class I8Test {
    
    @Test
    public void test() {
        I8 i8 = new I8();
        Assert.assertEquals(0, i8.length());
        Assert.assertEquals("<I8>", i8.toSml());

        i8.addValue(0);
        i8.addValue(1);
        i8.addValue(-1);
        i8.addValue(Long.MAX_VALUE);
        i8.addValue(Long.MIN_VALUE);
        Assert.assertEquals(5, i8.length());
        Assert.assertEquals("<I8 0 1 -1 9223372036854775807 -9223372036854775808>", i8.toSml());
        TestUtils.assertEquals(new byte[] {0x61, 0x28, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, i8.toByteArray());
    }
    
}
