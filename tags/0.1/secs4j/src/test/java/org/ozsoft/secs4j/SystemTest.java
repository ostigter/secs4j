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

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.secs4j.CommunicationState;
import org.ozsoft.secs4j.ConnectMode;
import org.ozsoft.secs4j.ConnectionState;
import org.ozsoft.secs4j.ControlState;
import org.ozsoft.secs4j.SecsEquipment;
import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.message.S99F1;
import org.ozsoft.secs4j.message.S99F2;

/**
 * Test suite verifying the communication between two local SECS equipment.
 * 
 * @author Oscar Stigter
 */
public class SystemTest {

    /** Timeout before communication is considered to have failed. */
    private static final long CONNECTION_TIMEOUT = 500L;

    /**
     * Tests the communication between two local SECS equipment.
     * 
     * @throws SecsException
     *             If the SECS communications fails.
     */
    @Test
    public void test() throws SecsException {
        // Create passive entity listening on default port.
        SecsEquipment passiveEntity = new SecsEquipment();
        passiveEntity.setConnectMode(ConnectMode.PASSIVE);
        Assert.assertFalse(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Register test primary message on passive entity.
        passiveEntity.addMessageType(S99F1.class);

        // Start passive entity.
        passiveEntity.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Create active entity connecting to default host and port.
        SecsEquipment activeEntity = new SecsEquipment();
        activeEntity.setConnectMode(ConnectMode.ACTIVE);
        Assert.assertFalse(activeEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());

        // Register test reply message on active entity.
        activeEntity.addMessageType(S99F2.class);

        // Enable active entity, connecting to passive entity.
        activeEntity.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(passiveEntity.isEnabled());
        Assert.assertTrue(activeEntity.isEnabled());
        Assert.assertEquals(ConnectionState.SELECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(ConnectionState.SELECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, activeEntity.getCommunicationState());

        // Send S99F1 message from active to passive entity.
        S99F1 s99f1 = new S99F1();
        s99f1.setName("Mr. Smith");
        SecsReplyMessage replyMessage = activeEntity.sendMessageAndWait(s99f1);
        Assert.assertEquals("Incorrect stream", 99, replyMessage.getStream());
        Assert.assertEquals("Incorrect function", 2, replyMessage.getFunction());
        Assert.assertTrue("Reply message not S99F2", replyMessage instanceof S99F2);
        S99F2 s99f2 = (S99F2) replyMessage;
        Assert.assertEquals("Incorrect GRACK value", S99F2.GRACK_ACCEPT, s99f2.getGrAck());
        Assert.assertEquals("Incorrect GREETING value", "Hello, Mr. Smith!", s99f2.getGreeting());

        // Disable active entity.
        activeEntity.setEnabled(false);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertFalse(activeEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Disable passive entity.
        passiveEntity.setEnabled(false);
        Assert.assertFalse(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, passiveEntity.getCommunicationState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());
    }

    /**
     * Suspends the current thread for a specific duration.
     * 
     * @param duration
     *            The duration in miliseconds.
     */
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
