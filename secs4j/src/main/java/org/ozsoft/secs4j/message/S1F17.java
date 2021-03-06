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
 * S1F17 Request ON-LINE (RONL) request message. <br />
 * <br />
 * 
 * This message does not have any data.
 * 
 * @author Oscar Stigter
 */
public class S1F17 extends SecsPrimaryMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 17;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Request ON-LINE (RONL)";

    /** ONLACK: Acknowledge. */
    private static final int ACKNOWLEDGE = 0x00;

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
        if (data != null) {
            throw new SecsParseException("Message must not contain any data");
        }
    }

    @Override
    protected Data<?> getData() {
        // Header-only message, so no data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always acknowledge request.
        getEquipment().setControlState(ControlState.ONLINE_REMOTE);
        S1F18 s1f18 = new S1F18();
        s1f18.setOnlAck(ACKNOWLEDGE);
        return s1f18;
    }
    
}
