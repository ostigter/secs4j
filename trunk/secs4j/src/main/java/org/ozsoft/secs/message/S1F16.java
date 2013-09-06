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
 * S1F16 OFF-LINE Acknowledge (OFLA) reply message.
 * <br />
 * 
 * Format:
 * <pre>
 * OFLACK       // B:01 (0x00 = Acknowledge, 0x00 = Error)
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S1F16 extends SecsReplyMessage {

    public static final int OFLACK_ACKNOWLEDGE = 0x00;

    private static final int STREAM = 1;

    private static final int FUNCTION = 16;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "OFF-LINE Acknowledge (OFLA)";
    
    private Integer oflAck;
    
    public int getOflAck() {
        return oflAck;
    }
    
    public void setOflAck(int oflAck) {
        this.oflAck = oflAck;
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
            throw new SecsParseException("OFLACK must be of type B");
        }
        B b = (B) data;
        if (b.length() != 1) {
            throw new SecsParseException("OFLACK must have a length of exactly 1 byte");
        }
        int ofla = b.get(0);
        if (ofla != OFLACK_ACKNOWLEDGE) {
            throw new SecsParseException("Invalid OFLACK value: " + ofla);
        }
        setOflAck(ofla);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (oflAck == null) {
            throw new SecsParseException("OFLACK not set");
        }
        
        return new B(oflAck);
    }

    @Override
    protected void handle() {
        // Not implemented.
    }

}
