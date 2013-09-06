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

package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;

/**
 * S1F18 ON-LINE Acknowledge (ONLA) reply message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * ONLACK       // <B:1> (0x00 = Acknowledge, 0x01 = Error)
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S1F18 extends SecsReplyMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 18;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "ON-LINE Acknowledge (ONLA)";
    
    /** ONLACK: Acknowledge. */
    private static final int ONLA_ACKNOWLEDGE = 0x00;

    private Integer onlAck;
    
    public int getOnlAck() {
        return onlAck;
    }
    
    public void setOnlAck(int onlAck) {
        this.onlAck = onlAck;
    }

    @Override
    public int getStream() {
        return STREAM;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

    @Override
    public boolean withReply() {
        return WITH_REPLY;
    }

    @Override
    public String getDescripton() {
        return DESCRIPTION;
    }

    @Override
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof B)) {
            throw new SecsParseException("ONLACK must be of type B");
        }
        B b = (B) data;
        if (b.length() != 1) {
            throw new SecsParseException("ONLACK must have a length of exactly 1 byte");
        }
        int onlAck = b.get(0);
        if (onlAck != ONLA_ACKNOWLEDGE) {
            throw new SecsParseException("Invalid ONLACK value: " + onlAck);
        }
        setOnlAck(onlAck);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (onlAck == null) {
            throw new SecsParseException("ONLACK not set");
        }
        
        return new B(onlAck);
    }

    @Override
    protected void handle() {
        // Not implemented.
    }

}
