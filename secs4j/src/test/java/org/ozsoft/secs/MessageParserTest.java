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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.BOOLEAN;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.message.S1F13;
import org.ozsoft.secs.message.S2F25;

/**
 * Test suite for the <code>MessageParser<code>.
 * 
 * @author Oscar Stigter
 */
public class MessageParserTest {
    
    private static Map<Integer, Class<? extends SecsMessage>> messageTypes;
    
    @BeforeClass
    public static void beforeClass() {
        messageTypes = new HashMap<Integer, Class<? extends SecsMessage>>();
        addMessageType(S1F13.class);
        addMessageType(S2F25.class);
    }
    
    private static void addMessageType(Class<? extends SecsMessage> messageType) {
        try {
            SecsMessage message = messageType.newInstance();
            int messageId = message.getStream() * 256 + message.getFunction();
            messageTypes.put(messageId, messageType);
        } catch (Exception e) {
            // Internal error (should never happen).
            Assert.fail("Could not instantiate message type: " + messageType);
        }
    }
    
    /**
     * Tests the parsing of empty data items.
     */
    @Test
    public void emptyData() {
        try {
            MessageParser.parseData(null);
            Assert.fail("Missed exception");
        } catch (SecsParseException e) {
            Assert.assertEquals("Empty data item", e.getMessage());
        }
        
        try {
            MessageParser.parseData("");
            Assert.fail("Missed exception");
        } catch (SecsParseException e) {
            Assert.assertEquals("Empty data item", e.getMessage());
        }
    }

    /**
     * Tests the parsing of B data items.
     */
    @Test
    public void dataB() throws SecsException {
        Data<?> data = MessageParser.parseData("B {01 02 03}");
        Assert.assertTrue(data instanceof B);
        B b = (B) data;
        Assert.assertEquals(3, b.length());
        Assert.assertEquals(0x01, b.get(0));
        Assert.assertEquals(0x02, b.get(1));
        Assert.assertEquals(0x03, b.get(2));
    }

    /**
     * Tests the parsing of BOOLEAN data items.
     */
    @Test
    public void dataBOOLEAN() throws SecsException {
        Data<?> data = MessageParser.parseData("BOOLEAN {True}");
        Assert.assertTrue(data instanceof BOOLEAN);
        BOOLEAN b = (BOOLEAN) data;
        Assert.assertTrue(b.getValue());

        data = MessageParser.parseData("BOOLEAN {False}");
        Assert.assertTrue(data instanceof BOOLEAN);
        b = (BOOLEAN) data;
        Assert.assertFalse(b.getValue());
    }

    /**
     * Tests the parsing of A data items.
     */
    @Test
    public void dataA() throws SecsException {
        Data<?> data = MessageParser.parseData("A {Test}");
        Assert.assertTrue(data instanceof A);
        A a = (A) data;
        Assert.assertEquals(4, a.length());
        Assert.assertEquals("Test", a.getValue());
    }

    /**
     * Tests the parsing of U2 data items.
     */
    @Test
    public void dataU2() throws SecsException {
        Data<?> data = MessageParser.parseData("U2 {65535}");
        Assert.assertTrue(data instanceof U2);
        U2 u2 = (U2) data;
        Assert.assertEquals(0xffff, u2.getValue(0));
    }

    /**
     * Tests the parsing of U4 data items.
     */
    @Test
    public void dataU4() throws SecsException {
        Data<?> data = MessageParser.parseData("U4 {4294967295}");
        Assert.assertTrue(data instanceof U4);
        U4 u4 = (U4) data;
        Assert.assertEquals(0xffffffffL, u4.getValue(0));
    }

    /**
     * Tests the parsing of simple L data items.
     */
    @Test
    public void dataLSimple() throws SecsException {
        Data<?> data = MessageParser.parseData("L {}");
        Assert.assertTrue(data instanceof L);
        L l = (L) data;
        Assert.assertEquals(0, l.length());

        data = MessageParser.parseData("L {A {V1} A {V2}}");
        Assert.assertTrue(data instanceof L);
        l = (L) data;
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("V1", l.getItem(0).getValue());
        Assert.assertEquals("V2", l.getItem(1).getValue());
    }

    /**
     * Tests the parsing of nested L data items.
     */
    @Test
    public void dataLNested() throws SecsException {
        Data<?> data = MessageParser.parseData("L {}");
        Assert.assertTrue(data instanceof L);
        L l = (L) data;
        Assert.assertEquals(0, l.length());

        data = MessageParser.parseData("L {A {V1} A {V2}}");
        Assert.assertTrue(data instanceof L);
        l = (L) data;
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("V1", l.getItem(0).getValue());
        Assert.assertEquals("V2", l.getItem(1).getValue());

        data = MessageParser.parseData("L {L {A {V1} A {V2}} L {A {V3} A {V4}}}");
        Assert.assertTrue(data instanceof L);
        l = (L) data;
        Assert.assertEquals(2, l.length());
        L l1 = (L) l.getItem(0);
        Assert.assertEquals("V1", l1.getItem(0).getValue());
        Assert.assertEquals("V2", l1.getItem(1).getValue());
        L l2 = (L) l.getItem(1);
        Assert.assertEquals(2, l2.length());
        Assert.assertEquals("V3", l2.getItem(0).getValue());
        Assert.assertEquals("V4", l2.getItem(1).getValue());
    }

    /**
     * Tests the parsing of incomplete messages.
     */
    @Test
    public void incompleteMessage() {
        byte[] data = new byte[] {};
        try {
            MessageParser.parseMessage(data, data.length, messageTypes);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 0)", e.getMessage());
        }

        data = new byte[] { 0x00, 0x00, 0x00, 0x0a };
        try {
            MessageParser.parseMessage(data, data.length, messageTypes);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 4)", e.getMessage());
        }

        data = new byte[] { 0x00, 0x00, 0x00, 0x0a, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        try {
            MessageParser.parseMessage(data, data.length, messageTypes);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (declared length: 14; actual length: 13)", e.getMessage());
        }
    }

    /**
     * Tests the parsing of SELECT_REQ messages.
     */
    @Test
    public void selectReq() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x11, 0x12, 0x13, 0x14};
        ControlMessage message = (ControlMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Assert.assertEquals(0xffff, message.getSessionId());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(SType.SELECT_REQ, message.getSType());
        Assert.assertEquals(0x11121314L, message.getTransactionId());
    }

    /**
     * Tests the parsing of DESELECT_REQ messages.
     */
    @Test
    public void deselectReq() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x02, 0x11, 0x12, 0x13, 0x14 };
        ControlMessage message = (ControlMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Assert.assertEquals(0xffff, message.getSessionId());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(SType.SELECT_RSP, message.getSType());
        Assert.assertEquals(0x11121314L, message.getTransactionId());
    }
    
    /**
     * Tests the parsing of empty data messages (header only).
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageEmpty() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14};
        SecsMessage message = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Assert.assertEquals(0x0001, message.getSessionId());
        Assert.assertEquals(2, message.getStream());
        Assert.assertEquals(25, message.getFunction());
        Assert.assertTrue(message.withReply());
        Assert.assertEquals(0x11121314L, message.getTransactionId());
        Assert.assertEquals("S2F25", message.getType());
        Assert.assertNull(message.getData());
    }

    /**
     * Tests the parsing of data messages with B items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   B {21 22 23}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageB() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0f, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x21, 0x03, 0x21, 0x22, 0x23};
        SecsMessage dataMessage = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Data<?> text = dataMessage.getData();
        Assert.assertTrue(text instanceof B);
        B b = (B) text;
        Assert.assertEquals(3, b.length());
        Assert.assertEquals(0x21, b.get(0));
        Assert.assertEquals(0x22, b.get(1));
        Assert.assertEquals(0x23, b.get(2));
        Assert.assertEquals("B:3 {21 22 23}", text.toSml());
    }

    /**
     * Tests the parsing of data messages with BOOLEAN items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   BOOLEAN {True}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageBOOLEAN() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0d, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x11, 0x01, 0x01};
        SecsMessage dataMessage = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Data<?> text = dataMessage.getData();
        Assert.assertTrue(text instanceof BOOLEAN);
        BOOLEAN b = (BOOLEAN) text;
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
        Assert.assertEquals("BOOLEAN {True}", text.toSml());
    }

    /**
     * Tests the parsing of data messages with A items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   A {'Test'}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageA() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x10, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x41, 0x04, 'T', 'e', 's', 't' };
        Message message = MessageParser.parseMessage(data, data.length, messageTypes);
        SecsMessage dataMessage = (SecsMessage) message;
        Data<?> text = dataMessage.getData();
        Assert.assertTrue(text instanceof A);
        A a = (A) text;
        Assert.assertEquals(4, a.length());
        Assert.assertEquals("Test", a.getValue());
        Assert.assertEquals("A:4 {Test}", text.toSml());
    }

    /**
     * Tests the parsing of data messages with U2 items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   U2:6 {0 1 255 256 257 65535}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageU2Multiple() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x18, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14,
                (byte) 0xa9, 0x0c, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xff, 0x01, 0x00, 0x01, 0x01, (byte) 0xff, (byte) 0xff};
        Message message = MessageParser.parseMessage(data, data.length, messageTypes);
        SecsMessage dataMessage = (SecsMessage) message;
        Data<?> text = dataMessage.getData();
        Assert.assertTrue(text instanceof U2);
        U2 u2 = (U2) text;
        Assert.assertEquals(6, u2.length());
        Assert.assertEquals(0x0000, u2.getValue(0));
        Assert.assertEquals(0x0001, u2.getValue(1));
        Assert.assertEquals(0x00ff, u2.getValue(2));
        Assert.assertEquals(0x0100, u2.getValue(3));
        Assert.assertEquals(0x0101, u2.getValue(4));
        Assert.assertEquals(0xffff, u2.getValue(5));
        Assert.assertEquals("U2:6 {0 1 255 256 257 65535}", text.toSml());
    }

    /**
     * Tests single-level lists. <br />
     * <br />
     * 
     * Message:
     * <pre>
     * L {
     *   A {'V1'}
     *   A {'V2'}
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLSimple() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x14, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02,
                'V', '1', 0x41, 0x02, 'V', '2' };
        SecsMessage message = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Assert.assertEquals(0x0001, message.getSessionId());
        Assert.assertEquals(2, message.getStream());
        Assert.assertEquals(25, message.getFunction());
        Assert.assertEquals("S2F25", message.getType());
        Assert.assertEquals(0x11121314L, message.getTransactionId());
        Data<?> text = message.getData();
        Assert.assertTrue(text instanceof L);
        L l = (L) text;
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("V1", l.getItem(0).getValue());
        Assert.assertEquals("V2", l.getItem(1).getValue());
        Assert.assertEquals("L:2 {\nA:2 {V1}\nA:2 {V2}\n}", text.toSml());
    }

    /**
     * Tests single-level lists. <br />
     * <br />
     * 
     * Message:
     * <pre>
     * L {
     *   A {'V1'}
     *   L {
     *     A {'V2'}
     *     A {'V3'}
     *   }
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLMixed() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x1a, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02,
                'V', '1', 0x01, 0x02, 0x41, 0x02, 'V', '2', 0x41, 0x02, 'V', '3' };
        SecsMessage message = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Data<?> text = message.getData();
        Assert.assertTrue(text instanceof L);
        L l1 = (L) text;
        Assert.assertEquals(2, l1.length());
        Assert.assertEquals("V1", l1.getItem(0).getValue());
        L l2 = (L) l1.getItem(1);
        Assert.assertEquals(2, l2.length());
        Assert.assertEquals("V2", l2.getItem(0).getValue());
        Assert.assertEquals("V3", l2.getItem(1).getValue());
        Assert.assertEquals("L:2 {\nA:2 {V1}\nL:2 {\nA:2 {V2}\nA:2 {V3}\n}\n}", text.toSml());
    }

    /**
     * Tests nested lists. <br />
     * <br />
     * 
     * Message:
     * <pre>
     * L {
     *   L {
     *     B {11 12 13}
     *     B {21 22 23}
     *   }
     *   L {
     *     B {31 32 33}
     *     B {41 42 43}
     *   }
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLNested() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x24, 0x00, 0x01, (byte) 0x82, 0x19, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x01, 0x02,
                0x21, 0x03, 0x11, 0x12, 0x13, 0x21, 0x03, 0x21, 0x22, 0x23, 0x01, 0x02, 0x21, 0x03, 0x31, 0x32, 0x33, 0x21, 0x03, 0x41, 0x42, 0x43 };
        SecsMessage message = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Data<?> text = message.getData();
        Assert.assertTrue(text instanceof L);
        L l1 = (L) text;
        Assert.assertEquals(2, l1.length());
        L l2 = (L) l1.getItem(0);
        Assert.assertEquals(2, l2.length());
        B b1 = (B) l2.getItem(0);
        Assert.assertEquals(0x11, b1.get(0));
        Assert.assertEquals(0x12, b1.get(1));
        Assert.assertEquals(0x13, b1.get(2));
        B b2 = (B) l2.getItem(1);
        Assert.assertEquals(0x21, b2.get(0));
        Assert.assertEquals(0x22, b2.get(1));
        Assert.assertEquals(0x23, b2.get(2));
        L l3 = (L) l1.getItem(1);
        Assert.assertEquals(2, l3.length());
        B b3 = (B) l3.getItem(0);
        Assert.assertEquals(0x31, b3.get(0));
        Assert.assertEquals(0x32, b3.get(1));
        Assert.assertEquals(0x33, b3.get(2));
        B b4 = (B) l3.getItem(1);
        Assert.assertEquals(0x41, b4.get(0));
        Assert.assertEquals(0x42, b4.get(1));
        Assert.assertEquals(0x43, b4.get(2));
        Assert.assertEquals("L:2 {\nL:2 {\nB:3 {11 12 13}\nB:3 {21 22 23}\n}\nL:2 {\nB:3 {31 32 33}\nB:3 {41 42 43}\n}\n}", text.toSml());
    }

    /**
     * Tests the parsing of a an S1F13 primary data message. <br />
     * <br />
     * 
     * Message:
     * <pre>
     * L {
     *   A {SECS Equipment}
     *   A {1.0}
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void s1f13() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x21, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x01, 0x02, 0x41, 0x0e, 0x53, 0x45, 0x43, 0x53, 0x20, 0x45, 0x71, 0x75, 0x69, 0x70, 0x6d, 0x65, 0x6e, 0x74, 0x41, 0x03, 0x31, 0x2e, 0x30};
        SecsMessage dataMessage = (SecsMessage) MessageParser.parseMessage(data, data.length, messageTypes);
        Assert.assertEquals(1, dataMessage.getSessionId());
        Assert.assertEquals(2L, dataMessage.getTransactionId());
        Assert.assertEquals(1, dataMessage.getStream());
        Assert.assertEquals(13, dataMessage.getFunction());
        Assert.assertTrue(dataMessage instanceof S1F13);
        S1F13 s1f13 = (S1F13) dataMessage;
        Assert.assertEquals("SECS Equipment", s1f13.getModelName());
        Assert.assertEquals("1.0", s1f13.getSoftRev());
        Data<?> text = dataMessage.getData();
        Assert.assertEquals("L:2 {\nA:14 {SECS Equipment}\nA:3 {1.0}\n}", text.toSml());
    }

}
