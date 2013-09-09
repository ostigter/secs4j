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

import org.apache.log4j.Logger;
import org.ozsoft.secs4j.CommunicationState;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.A;
import org.ozsoft.secs4j.format.B;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;

/**
 * S1F14 - Establish Communication Request Acknowledge (CRA) reply message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *   COMMACK            // B:01 (0x00 = Accepted, 0x01 = Denied, Try Again)
 *   <L
 *     MDLN             // A:20
 *     SOFTREV          // A:20
 *   >
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S1F14 extends SecsReplyMessage {
    
    private static final Logger LOG = Logger.getLogger(S1F14.class);

    public static final int COMMACK_ACCEPTED = 0x00;

    public static final int COMMACK_DENIED_TRY_AGAIN = 0x01;

    private static final int STREAM = 1;

    private static final int FUNCTION = 14;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "Establish Communication Request Acknowledge (CRA)";
    
    private Integer commAck;
    
    private String modelName;
    
    private String softRev;
    
    public int getCommAck() {
        return commAck;
    }
    
    public void setCommAck(int commAck) {
        this.commAck = commAck;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public String getSoftRev() {
        return softRev;
    }
    
    public void setSoftRev(String softRev) {
        this.softRev = softRev;
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
        if (!(data instanceof L)) {
            throw new SecsParseException("Top-level data item must be of type L");
        }
        L l = (L) data;
        if (l.length() != 2) {
            throw new SecsParseException("Top-level L must contain exactly 2 items");
        }
        data = l.getItem(0);
        if (!(data instanceof B)) {
            throw new SecsParseException("COMMACK must be of type B");
        }
        B b = (B) data;
        if (b.length() != 1) {
            throw new SecsParseException("COMMACK must have a length of exactly 1 byte");
        }
        int commAck = b.get(0);
        if (commAck != COMMACK_ACCEPTED && commAck != COMMACK_DENIED_TRY_AGAIN) {
            throw new SecsParseException("Invalid COMMACK value: " + commAck);
        }
        setCommAck(commAck);
        data = l.getItem(1);
        if (!(data instanceof L)) {
            throw new SecsParseException("MDLN and SOFTREV must be in a L");
        }
        l = (L) data;
        if (l.length() == 0) {
            // MDLN and SOFTREV not specified; set empty.
            setModelName("");
            setSoftRev("");
        } else if (l.length() == 2) {
            data = l.getItem(0);
            if (!(data instanceof A)) {
                throw new SecsParseException("MDLN must be an A");
            }
            String modelName = ((A) data).getValue();
            data = l.getItem(1);
            if (!(data instanceof A)) {
                throw new SecsParseException("SOFTREV must be an A");
            }
            String softRev = ((A) data).getValue();
            setModelName(modelName);
            setSoftRev(softRev);
        } else {
            throw new SecsParseException("Nested L must contain exactly 0 or 2 items");
        }
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (commAck == null) {
            throw new SecsParseException("COMMACK not set");
        }
        if (modelName == null) {
            throw new SecsParseException("MDLN not set");
        }
        if (softRev == null) {
            throw new SecsParseException("SOFTREV not set");
        }

        L data = new L();
        data.addItem(new B(commAck));
        L l = new L();
        l.addItem(new A(modelName));
        l.addItem(new A(softRev));
        data.addItem(l);
        return data;
    }

    @Override
    protected void handle() {
        if (commAck == COMMACK_ACCEPTED) {
            LOG.debug("Received S1F14 with COMMACK: Accepted");
            getEquipment().setCommunicationState(CommunicationState.COMMUNICATING);
        } else if (commAck == COMMACK_DENIED_TRY_AGAIN) {
            LOG.warn("Received S1F14 with COMMACK: Denied, Try Again");
        } else {
            LOG.warn("Received S1F14 with COMMACK value " + commAck);
        }
    }
    
}
