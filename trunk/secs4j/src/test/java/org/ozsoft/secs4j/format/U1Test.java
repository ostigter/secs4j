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
import org.ozsoft.secs4j.format.U1;

public class U1Test {
    
    @Test
    public void test() {
        U1 u1 = new U1();
        Assert.assertEquals(0, u1.length());
        Assert.assertEquals("<U1>", u1.toSml());
        
        u1.addValue(0);
        u1.addValue(1);
        u1.addValue(255);
        Assert.assertEquals(3, u1.length());
        Assert.assertEquals("<U1 0 1 255>", u1.toSml());
        TestUtils.assertEquals(new byte[] {(byte) 0xa5, 0x03, 0x00, 0x01, (byte) 0xff}, u1.toByteArray());
    }
    
}
