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
 * Indicates an unsupported SECS message.
 * 
 * @author Oscar Stigter
 */
public class UnsupportedMessageException extends SecsException {

    private static final long serialVersionUID = -1434846673624462650L;
    
    private final int stream;
    
    private final int function;
    
    private final long transactionId;

    /**
     * Constructor.
     * 
     * @param stream
     *            The message's stream.
     * @param function
     *            The message's function.
     * @param transactionId
     *            The message's transaction ID.
     */
    public UnsupportedMessageException(int stream, int function, long transactionId) {
        super(String.format("Unsupported SECS message: S%dF%d", stream, function));
        this.stream = stream;
        this.function = function;
        this.transactionId = transactionId;
    }
    
    /**
     * Returns the stream of the message.
     * 
     * @return The stream.
     */
    public int getStream() {
        return stream;
    }
    
    /**
     * Returns the function of the message.
     * 
     * @return The function.
     */
    public int getFunction() {
        return function;
    }

    /**
     * Returns the transaction ID of the message.
     * 
     * @return The transaction ID.
     */
    public long getTransactionId() {
        return transactionId;
    }

}
