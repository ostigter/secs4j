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

/**
 * SECS data item.
 * 
 * @param <T>
 *            Underlying value class.
 * 
 * @author Oscar Stigter
 */
public interface Data<T> {

    /**
     * Returns the value.
     * 
     * @return The value.
     */
    T getValue();

    /**
     * Sets the value.
     * 
     * @param value
     *            The value.
     */
    void setValue(T value);

    /**
     * Returns the length. <br />
     * <br />
     * 
     * For items that can contain multiple items, this method returns the number
     * of items it currently contains.
     * 
     * @return The length.
     */
    int length();

    /**
     * Returns a byte array with the data item serialized as bytes according to
     * the SEMI E05 SECS-II standard.
     * 
     * @return The byte array.
     */
    byte[] toByteArray();

    /**
     * Returns the SML text representing this data item.
     * 
     * @return The SML text.
     */
    String toSml();

}
