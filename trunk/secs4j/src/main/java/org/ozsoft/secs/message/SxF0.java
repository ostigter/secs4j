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
import org.ozsoft.secs.format.Data;

/**
 * Abort Transaction (ABORT) reply message.
 * 
 * @author Oscar Stigter
 */
public class SxF0 extends SecsReplyMessage {
    
    private static final int FUNCTION = 0;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "ABORT";
    
    /**
     * The message stream. 
     */
    private final int stream;
    
    /**
     * Constructor.
     * 
     * @param stream
     *            The message stream.
     */
    public SxF0(int stream) {
        this.stream = stream;
    }

    @Override
    public int getStream() {
        return stream;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

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
            throw new SecsParseException("Unexpected data");
        }
    }

    @Override
    protected Data<?> getData() {
        // No data.
        return null;
    }

    @Override
    protected void handle() {
        // Not implemented.
    }

}
