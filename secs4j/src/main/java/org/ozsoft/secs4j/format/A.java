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

import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * SECS data item A (single 7-bit ASCII text).
 * 
 * @author Oscar Stigter
 */
public class A implements Data<String> {
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0x40;
    
    /** The value. */
    private String value;
    
    /**
     * Constructor with an empty value.
     */
    public A() {
        this("");
    }
    
    /**
     * Constructor with an initial value.
     * 
     * @param value
     *            The value.
     */
    public A(String value) {
        setValue(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public int length() {
        return value.length();
    }

    @Override
    public byte[] toByteArray() {
        // Determine length.
        int length = length();
        int noOfLengthBytes = 1;
        B lengthBytes = new B();
        lengthBytes.add(length & 0xff);
        if (length > 0xff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 8) & 0xff);
        }
        if (length > 0xffff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 16) & 0xff);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // Write format byte.
            baos.write(FORMAT_CODE | noOfLengthBytes);
            
            // Write length bytes.
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }
            
            // Write character bytes.
            for (int b : value.getBytes()) {
                baos.write(b);
            }
            
            return baos.toByteArray();
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
    	if (length() == 0) {
    		return "<A>";
    	} else {
    		return String.format("<A \"%s\">", value);
    	}
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof A) {
            return ((A) obj).value.equals(value);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
