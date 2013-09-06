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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.util.ConversionUtils;

/**
 * HSMS control message (PType other than DATA). <br />
 * <br />
 * 
 * A control message is always header-only (no data).
 * 
 * @author Oscar Stigter
 */
public class ControlMessage extends Message {
    
    /** Header length bytes for a header-only message. */
    private static final byte[] HEADER_LENGTH_BYTES = new byte[] {0x00, 0x00, 0x00, 0x0a};
    
    /** Header Byte 2 field. */
    private int headerByte2;
    
    /** Header Byte 3 field. */
    private int headerByte3;
    
    /** SType field. */
    private SType sType;
    
    /**
     * Constructor.
     * 
     * @param sessionId
     *            Value of the Session ID field.
     * @param headerByte2
     *            Value of the Header Byte 2 field.
     * @param headerByte3
     *            Value of the Header Byte 3 field.
     * @param sType
     *            Value of the SType field.
     * @param systemBytes
     *            Value of the System Bytes (Transaction ID) field.
     */
    public ControlMessage(int sessionId, int headerByte2, int headerByte3, SType sType, long systemBytes) {
        this.headerByte2 = headerByte2;
        this.headerByte3 = headerByte3;
        this.sType = sType;
        setSessionId(sessionId);
        setTransactionId(systemBytes);
    }
    
    /**
     * Returns the value of the Header Byte 2 field.
     * 
     * @return The value of the Header Byte 2 field.
     */
    public int getHeaderByte2() {
        return headerByte2;
    }
    
    /**
     * Returns the value of the Header Byte 3 field.
     * 
     * @return The value of the Header Byte 3 field.
     */
    public int getHeaderByte3() {
        return headerByte3;
    }
    
    /**
     * Returns the value of the SType field.
     * 
     * @return The value of the SType field.
     */
    public SType getSType() {
        return sType;
    }

    @Override
    public byte[] toByteArray() throws SecsParseException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(HEADER_LENGTH_BYTES);
            baos.write(ConversionUtils.integerToBytes(getSessionId(), U2.SIZE));
            baos.write(headerByte2);
            baos.write(headerByte3);
            baos.write(PType.SECS_II.getValue());
            baos.write(sType.getValue());
            baos.write(ConversionUtils.integerToBytes(getTransactionId(), U4.SIZE));
            return baos.toByteArray();
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new SecsParseException("Could not serialize message: " + e.getMessage());
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
            // Internal error (should never happen).
            throw new RuntimeException(e);
        }
        return String.format("%s {%s}", sType, sb);
    }

}
