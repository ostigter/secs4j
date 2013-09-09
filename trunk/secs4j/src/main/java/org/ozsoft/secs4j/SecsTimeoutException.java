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

package org.ozsoft.secs4j;

/**
 * SECS timeout exception.
 * 
 * @author Oscar Stigter
 */
public class SecsTimeoutException extends SecsException {

    private static final long serialVersionUID = 8246754378510512955L;

    /**
     * Constructor.
     * 
     * @param message
     *            Message describing the timeout.
     */
    public SecsTimeoutException(String message) {
        super(message);
    }

}
