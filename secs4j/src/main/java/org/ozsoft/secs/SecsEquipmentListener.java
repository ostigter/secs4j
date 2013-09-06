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

package org.ozsoft.secs;

/**
 * Listener for SECS equipment events like state changes and messages sent and
 * received.
 * 
 * @author Oscar Stigter
 */
public interface SecsEquipmentListener {

    /**
     * Notification that the HSMS Connection State has changed.
     * 
     * @param connectionState
     *            The new Connection State.
     */
    void connectionStateChanged(ConnectionState connectionState);

    /**
     * Notification that the SECS Communication State has changed.
     * 
     * @param communicationState
     *            The new Communication State.
     */
    void communicationStateChanged(CommunicationState communicationState);

    /**
     * Notification that the SECS Control State has changed.
     * 
     * @param controlState
     *            The new Control State.
     */
    void controlStateChanged(ControlState controlState);

    /**
     * Notification of an incoming SECS message.
     * 
     * @param message
     *            The message that has been received.
     */
    void messageReceived(Message message);

    /**
     * Notification of an outgoing SECS message.
     * 
     * @param message
     *            The message that has been sent.
     */
    void messageSent(Message message);

}
