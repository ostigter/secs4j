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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * SECS data item L (list of other data items).
 * 
 * @author Oscar Stigter
 */
public class L implements Data<List<Data<?>>> {
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0x00;
    
    /** The data items. */
    private List<Data<?>> items = new ArrayList<Data<?>>();
    
    /**
     * Constructor with an initial empty list.
     */
    public L() {
        // Empty implementation.
    }

    /**
     * Returns the data item at a specific index position.
     * 
     * @param index
     *            The index position.
     * 
     * @return The data item.
     */
    public Data<?> getItem(int index) {
        return items.get(index);
    }
    
    /**
     * Adds a data item to the end of the list.
     * 
     * @param item
     *            The data item.
     */
    public void addItem(Data<?> item) {
        items.add(item);
    }
    
    @Override
    public List<Data<?>> getValue() {
        return items;
    }

    @Override
    public void setValue(List<Data<?>> value) {
        this.items = value;
    }
    
    @Override
    public int length() {
        return items.size();
    }
    
    @Override
    public byte[] toByteArray() {
        // Construct length bytes.
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
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }
        
            // Write items recursively.
            for (Data<?> item : items) {
                baos.write(item.toByteArray());
            }
            
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Could not serialize L items: " + e.getMessage());
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
        StringBuilder sb = new StringBuilder();
        int length = items.size();
        sb.append(String.format("L:%d {", length));
        for (int i = 0; i < length; i++) {
            sb.append('\n');
            sb.append(items.get(i).toSml());
        }
        sb.append("\n}");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
