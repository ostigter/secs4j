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

import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.A;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;

/**
 * S99F1 Greeting Request test primary message. <br />
 * <br />
 * 
 * Returns a greeting based on the specified name. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *      NAME     // <A>
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S99F1 extends SecsPrimaryMessage {

    private static final int STREAM = 99;

    private static final int FUNCTION = 1;

    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Greeting Request (GR)";

    private static final String GREETING = "Hello, %s!";
    
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
            throw new SecsParseException("Data not set");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() != 1) {
            throw new SecsParseException("L must have length of exactly 1 item");
        }
        data = l.getItem(0);
        if (!(data instanceof A)) {
            throw new SecsParseException("NAME must be of type A");
        }
        String name = ((A) data).getValue();
        if (name.isEmpty()) {
            throw new SecsParseException("Empty NAME");
        }
        setName(name);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (name == null) {
            throw new SecsParseException("NAME not set");
        }
        if (name.isEmpty()) {
            throw new SecsParseException("Empty NAME");
        }
        
        L l = new L();
        l.addItem(new A(name));
        return l;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always accept any request.
        String greeting = String.format(GREETING, name);
        S99F2 s99f2 = new S99F2();
        s99f2.setGrAck(S99F2.GRACK_ACCEPT);
        s99f2.setGreeting(greeting);
        return s99f2;
    }

}
