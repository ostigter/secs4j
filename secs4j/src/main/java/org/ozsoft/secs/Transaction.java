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

package org.ozsoft.secs;

/**
 * SECS data message transaction consisting of a pair of a primary message and corresponding reply message.
 * 
 * @author Oscar Stigter
 */
public class Transaction {

    /** The timestamp of the start of the transaction. */
    private final long timestamp;

    /** The primary message. */
    private final Message primaryMessage;

    /** The reply message. */
    private Message replyMessage;

    /**
     * Constructor.
     * 
     * @param primaryMessage
     *            The primary message.
     */
    public Transaction(Message primaryMessage) {
        timestamp = System.currentTimeMillis();
        this.primaryMessage = primaryMessage;
    }

    /**
     * Returns the timestamp of the start of the transaction.
     * 
     * @return The timestamp of the start of the transaction.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the primary message.
     * 
     * @return The primary message.
     */
    public Message getPrimaryMessage() {
        return primaryMessage;
    }

    /**
     * Returns the reply message.
     * 
     * @return The reply message.
     */
    public Message getReplyMessage() {
        return replyMessage;
    }

    /**
     * Sets the reply message.
     * 
     * @param replyMessage
     *            The reply message.
     */
    /* package */void setReplyMessage(Message replyMessage) {
        if (replyMessage.getTransactionId() != primaryMessage.getTransactionId()) {
            throw new IllegalArgumentException("Reply message does not match request message");
        }
        this.replyMessage = replyMessage;
    }

}
