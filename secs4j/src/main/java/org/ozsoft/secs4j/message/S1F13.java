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

import org.ozsoft.secs4j.CommunicationState;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.A;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;

/**
 * S1F13 Establish Communication Request (CR) primary message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *      MDLN            // A:20
 *      SOFTREV         // A:20
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S1F13 extends SecsPrimaryMessage {
    
    private static final int STREAM = 1;

    private static final int FUNCTION = 13;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Establish Communication Request (CR)";

    private static final int COMMACK_ACCEPTED = 0x00;

    private String modelName;
    
    private String softRev;
    
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
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
            // No MDLN and SOFTREV specified; use empty values.
            setModelName("");
            setSoftRev("");
        } else if (l.length() == 2) {
            Data<?> dataItem = l.getItem(0);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("MDLN must be of type A");
            }
            String modelName = ((A) dataItem).getValue();
            dataItem = l.getItem(1);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("SOFTREV must be of type A");
            }
            String softRev = ((A) dataItem).getValue();
            setModelName(modelName);
            setSoftRev(softRev);
        } else {
            throw new SecsParseException("L must contain exactly 2 items");
        }
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (modelName == null) {
            throw new SecsParseException("MDLN not set");
        }
        if (softRev == null) {
            throw new SecsParseException("SOFTREV not set");
        }
        
        L l = new L();
        l.addItem(new A(modelName));
        l.addItem(new A(softRev));
        return l;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always accept.
        getEquipment().setCommunicationState(CommunicationState.COMMUNICATING);
        S1F14 s1f14 = new S1F14();
        s1f14.setCommAck(COMMACK_ACCEPTED);
        s1f14.setModelName(getEquipment().getModelName());
        s1f14.setSoftRev(getEquipment().getSoftRev());
        return s1f14;
    }

}
