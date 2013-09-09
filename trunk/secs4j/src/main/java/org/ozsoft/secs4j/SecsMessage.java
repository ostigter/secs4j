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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.U2;
import org.ozsoft.secs4j.format.U4;
import org.ozsoft.secs4j.util.ConversionUtils;

/**
 * SECS data message. <br />
 * <br />
 * 
 * Used for standard SECS/GEM messages or custom, application-specifc messages.
 * 
 * @author Oscar Stigter
 */
public abstract class SecsMessage extends Message {

    private static final int WITH_REPLY_MASK = 0x80;

    /**
     * Returns the stream.
     * 
     * @return The stream.
     */
    public abstract int getStream();

    /**
     * Returns the function.
     * 
     * @return The function.
     */
    public abstract int getFunction();

    /**
     * Indicates whether this primary message requires a reply message.
     * 
     * @return True if a reply message is required, otherwise false.
     */
    //TODO: Refactor withReply() away.
    public abstract boolean withReply();

    /**
     * Returns the message type's description (e.g. "Communication Request (CR)").
     * 
     * @return The description.
     */
    public abstract String getDescripton();

    /**
     * Returns the message type based on the stream and function (e.g. "S1F13").
     * 
     * @return The message type.
     */
    public final String getType() {
        return String.format("S%dF%d", getStream(), getFunction());
    }

    /**
     * Parses the data of an incoming message. <br />
     * <br />
     * 
     * Typically used to set any message specific values (e.g. COMMACK).
     * 
     * @param data
     *            The message data.
     * 
     * @throws SecsParseException
     *             If the data could not be parsed due to an invalid message.
     */
    protected abstract void parseData(Data<?> data) throws SecsParseException;

    /**
     * Returns the message data. <br />
     * <br />
     * 
     * The data is typically based on message specific values (e.g. COMMACK).
     * 
     * @return The message data.
     * 
     * @throws SecsParseException
     *             If any of the message specific values are not set or invalid.
     */
    protected abstract Data<?> getData() throws SecsParseException;

    @Override
    /* package */final byte[] toByteArray() throws SecsParseException {
        int length = SecsConstants.HEADER_LENGTH;
        Data<?> data = getData();
        byte[] dataBytes = null;
        if (data != null) {
            dataBytes = data.toByteArray();
            length += dataBytes.length;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(ConversionUtils.integerToBytes(length, U4.SIZE));
            baos.write(ConversionUtils.integerToBytes(getSessionId(), U2.SIZE));
            baos.write((withReply()) ? getStream() | WITH_REPLY_MASK : getStream()); // HeaderByte2
            baos.write(getFunction()); // HeaderByte3
            baos.write(PType.SECS_II.getValue());
            baos.write(SType.DATA.getValue());
            baos.write(ConversionUtils.integerToBytes(getTransactionId(), U4.SIZE));
            if (data != null) {
                baos.write(dataBytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new SecsParseException("Could not serialize data message", e);
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            for (byte b : toByteArray()) {
                sb.append(String.format("%02x ", b));
            }
        } catch (SecsParseException e) {
            throw new RuntimeException("Could not serialize message", e);
        }
        return String.format("%s - %s {%s}", getType(), getDescripton(), sb);
    }

}
