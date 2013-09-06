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
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S1F1 Are You There (R) primary message. <br />
 * <br />
 * 
 * This message does not use any data.
 *  
 * @author Oscar Stigter
 */
public class S1F1 extends SecsPrimaryMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 1;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Are You There (R)";
    
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
            throw new SecsParseException("No data expected");
        }
    }

    @Override
    protected Data<?> getData() {
        // No data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() {
        S1F2 s1f2 = new S1F2();
        s1f2.setModelName(getEquipment().getModelName());
        s1f2.setSoftRev(getEquipment().getSoftRev());
        return s1f2;
    }

}
