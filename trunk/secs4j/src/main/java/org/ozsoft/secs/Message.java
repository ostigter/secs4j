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
 * Root class of all SECS messages.
 * 
 * @author Oscar Stigter
 */
public abstract class Message {

    /** The SECS equipment. */
    private SecsEquipment equipment;

    /** Session ID. */
    private int sessionId;

    /** Transaction ID. */
    private long transactionId;

    /**
     * Returns the Session ID (Device ID).
     * 
     * @return The Session ID (Device ID).
     */
    public final int getSessionId() {
        return sessionId;
    }

    /**
     * Returns the Transaction ID (System Bytes).
     * 
     * @return The Transaction ID (System Bytes).
     */
    public final long getTransactionId() {
        return transactionId;
    }

    /**
     * Returns the SECS equipment.
     * 
     * @return The SECS equipment.
     */
    protected SecsEquipment getEquipment() {
        return equipment;
    }

    /**
     * Sets the SECS equipment.
     * 
     * @param equipment
     *            The SECS equipment.
     */
    /* package */void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Sets the Session ID (Device ID).
     * 
     * @param sessionId
     *            The Session ID (Device ID).
     */
    /* package */void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Sets the Transaction ID (System Bytes).
     * 
     * @param transactionId
     *            The Transaction ID (System Bytes).
     */
    /* package */void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Serializes the message to a byte array.
     * 
     * @throws SecsParseException
     *             If the messag could not be serialized.
     */
    /* package */abstract byte[] toByteArray() throws SecsParseException;

}
