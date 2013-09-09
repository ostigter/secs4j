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

package org.ozsoft.secs4j;

/**
 * SECS reply message as response to a primary message.
 * 
 * @author Oscar Stigter
 */
public abstract class SecsReplyMessage extends SecsMessage {

    /**
     * Handles the reply message. <br />
     * <br />
     * 
     * Optionally, this method can be implemented to do any processing upon receiving this reply message.
     * 
     * @throws SecsException
     *             If the message could not be processed.
     */
    protected abstract void handle() throws SecsException;

}
