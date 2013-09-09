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

package org.ozsoft.secs4j.message;

import org.ozsoft.secs4j.ControlState;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;

/**
 * S1F15 Request OFF-LINE (ROFL) request message. <br />
 * <br />
 * 
 * This message does not have any data.
 * 
 * @author Oscar Stigter
 */
public class S1F15 extends SecsPrimaryMessage {

    public static final int OFLA_ACKNOWLEDGE = 0x00;

    private static final int STREAM = 1;

    private static final int FUNCTION = 15;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Request OFF-LINE (ROFL)";

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
    public void parseData(Data<?> data) throws SecsParseException {
        if (data != null) {
            throw new SecsParseException("Message must not contain any data");
        }
    }

    @Override
    public Data<?> getData() {
        // Header-only message, so no data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always accept.
        getEquipment().setControlState(ControlState.HOST_OFFLINE);
        S1F16 s1f16 = new S1F16();
        s1f16.setOflAck(OFLA_ACKNOWLEDGE);
        return s1f16;
    }
    
}
