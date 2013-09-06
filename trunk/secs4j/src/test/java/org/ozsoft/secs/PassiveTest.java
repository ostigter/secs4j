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

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test suite to manually verify this SECS/GEM library can communicate with
 * another SECS equipment, with this local entity being the passive endpoint. <br />
 * <br />
 * 
 * While the test is running, the tester should use another SECS/GEM equipment
 * to connect to this test equipment.
 * 
 * @author Oscar Stigter
 */
public class PassiveTest {
    
    /** Port number of the local entity. */
    private static final int PORT = 5555;

    /** SECS session duration. */
    private static final long SESSION_DURATION = 600000L; // 10 min

    /** Interval to poll while waiting for state change. */
    private static final long POLL_INTERVAL = 10L; // 10 ms

    /**
     * Tests the communication with another SECS equipment.
     * 
     * @throws SecsException
     *             If the SECS communication fails.
     */
    @Test
    @Ignore
    public void test() throws SecsException {
        // Configure local entity.
        SecsEquipment equipment = new SecsEquipment();
        equipment.setConnectMode(ConnectMode.PASSIVE);
        equipment.setPort(PORT);

        // Start local entity and wait until communication is established.
        equipment.setEnabled(true);
        while (equipment.getCommunicationState() != CommunicationState.COMMUNICATING) {
            sleep(POLL_INTERVAL);
        }

        // Sleep to allow the tester to send some messages.
        sleep(SESSION_DURATION);

        // Stop the local entity.
        equipment.setEnabled(false);
        while (equipment.getCommunicationState() != CommunicationState.NOT_ENABLED) {
            sleep(POLL_INTERVAL);
        }
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
