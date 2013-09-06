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

public class BOOLEANTest {
    
    @Test
    public void test() {
        BOOLEAN b = new BOOLEAN(0x00);
        Assert.assertEquals(1, b.length());
        Assert.assertFalse(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x00}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {False}", b.toSml());
        
        b = new BOOLEAN(0x01);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());

        b = new BOOLEAN(0x02);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());

        b = new BOOLEAN(0xff);
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        TestUtils.assertEquals(new byte[] {0x10, 0x01, 0x01}, b.toByteArray());
        Assert.assertEquals("BOOLEAN {True}", b.toSml());
    }

}
