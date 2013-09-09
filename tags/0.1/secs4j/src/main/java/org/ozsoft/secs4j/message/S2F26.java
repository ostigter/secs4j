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

import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;

/**
 * S2F26 Loopback Diagnostic Acknowledge (LDA) reply message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * TESTDATA     // any data item, or empty
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S2F26 extends SecsReplyMessage {

    private static final int STREAM = 2;

    private static final int FUNCTION = 26;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "Loopback Diagnostic Acknowledge (LDA)";
    
    private Data<?> testData;
    
    public Data<?> getTestData() {
        return testData;
    }
    
    public void setTestData(Data<?> testData) {
        this.testData = testData;
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
    public void parseData(Data<?> data) {
        // Just copy test data as-is.
        setTestData(data);
    }

    @Override
    public Data<?> getData() {
        // Just return test data as-is.
        return getTestData();
    }

    @Override
    protected void handle() {
        // Not implemented.
    }

}
