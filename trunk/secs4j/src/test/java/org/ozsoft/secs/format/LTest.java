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

public class LTest {
    
    @Test
    public void empty() {
        L l = new L();
        Assert.assertEquals(0, l.length());
        Assert.assertEquals("L:0 {\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x00}, l.toByteArray());
    }
    
    @Test
    public void variousItemTypes() {
        L l = new L();
        l.addItem(new B(new byte[] {0x01, 0x02, 0x03}));
        Assert.assertEquals(1, l.length());
        Assert.assertEquals("L:1 {\nB:3 {01 02 03}\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x01, 0x21, 0x03, 0x01, 0x02, 0x03}, l.toByteArray());
        
        l.addItem(new A("Test"));
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("L:2 {\nB:3 {01 02 03}\nA:4 {Test}\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x02, 0x21, 0x03, 0x01, 0x02, 0x03, 0x41, 0x04, 'T', 'e', 's', 't'}, l.toByteArray());
        
        l.addItem(new U2(511));
        Assert.assertEquals(3, l.length());
        Assert.assertEquals("L:3 {\nB:3 {01 02 03}\nA:4 {Test}\nU2:1 {511}\n}", l.toSml());
        TestUtils.assertEquals(new byte[] {0x01, 0x03, 0x21, 0x03, 0x01, 0x02, 0x03, 0x41, 0x04, 'T', 'e', 's', 't', (byte) 0xa9, 0x02, 0x01, (byte) 0xff}, l.toByteArray());
    }

}
