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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ozsoft.secs4j.message.S1F1;
import org.ozsoft.secs4j.message.S1F13;
import org.ozsoft.secs4j.message.S1F14;
import org.ozsoft.secs4j.message.S1F15;
import org.ozsoft.secs4j.message.S1F16;
import org.ozsoft.secs4j.message.S1F17;
import org.ozsoft.secs4j.message.S1F18;
import org.ozsoft.secs4j.message.S1F2;
import org.ozsoft.secs4j.message.S2F25;
import org.ozsoft.secs4j.message.S2F26;
import org.ozsoft.secs4j.message.SxF0;

/**
 * SECS equipment implementing the following SEMI standards:
 * <ul>
 *   <li>E05 SECS-II</li>
 *   <li>E30 GEM</li>
 *   <li>E37 HSMS</li>
 * </ul>
 * 
 * This is the core class of the SECS/GEM library.
 * 
 * @author Oscar Stigter
 */
public class SecsEquipment {
    
    private static final int S9 = 9;

    private static final int MIN_DEVICE_ID = 0;

    private static final int MAX_DEVICE_ID = 32767;

    private static final int MIN_PORT = 1025;

    private static final int MAX_PORT = 65535;

    private static final int MIN_T3 = 1;

    private static final int MAX_T3 = 120;

    private static final int MIN_T5 = 1;

    private static final int MAX_T5 = 240;

    private static final int MIN_T6 = 1;

    private static final int MAX_T6 = 240;

    private static final int MIN_T7 = 1;

    private static final int MAX_T7 = 240;

    private static final long POLL_INTERVAL = 10L;

    private static final int BUFFER_SIZE = 8192;

    private static final Logger LOG = Logger.getLogger(SecsEquipment.class);

    private final Map<Integer, Class<? extends SecsMessage>> messageTypes;

    private final Set<SecsEquipmentListener> listeners;
    
    private final Map<Long, Transaction> transactions;
    
    private int deviceId = SecsConstants.DEFAULT_DEVICE_ID;

    private String modelName = SecsConstants.DEFAULT_MDLN;
    
    private String softRev = SecsConstants.DEFAULT_SOFTREV;

    private ConnectMode connectMode = SecsConstants.DEFAULT_CONNECT_MODE;

    private String host = SecsConstants.DEFAULT_HOST;

    private int port = SecsConstants.DEFAULT_PORT;
    
    private int t3 = SecsConstants.DEFAULT_T3;

    private int t5 = SecsConstants.DEFAULT_T5;

    private int t6 = SecsConstants.DEFAULT_T6;

    private int t7 = SecsConstants.DEFAULT_T7;

    private boolean isEnabled;

    private ConnectionState connectionState;

    private CommunicationState communicationState;

    private ControlState controlState;

    private Thread connectionThread;
    
    private Socket socket;
    
    private long nextTransactionId = 1L;
    
    public SecsEquipment() {
        listeners  = new HashSet<SecsEquipmentListener>();
        messageTypes = new HashMap<Integer, Class<? extends SecsMessage>>();
        transactions = new HashMap<Long, Transaction>();
        
        addDefaultMessageTypes();
        
        isEnabled = false;
        setConnectionState(ConnectionState.NOT_CONNECTED);
        setCommunicationState(CommunicationState.NOT_ENABLED);
        setControlState(ControlState.EQUIPMENT_OFFLINE);
    }
    
    private void addDefaultMessageTypes() {
        LOG.debug("Add default message types");
        addMessageType(S1F1.class); // Are You There (R)
        addMessageType(S1F2.class); // On Line Data (D)
        addMessageType(S1F13.class); // Establish Communication Request (CR)
        addMessageType(S1F14.class); // Establish Communication Request Acknowledge (CRA)
        addMessageType(S1F15.class); // Request OFF-LINE (ROFL)
        addMessageType(S1F16.class); // OFF-LINE Acknowledge (OFLA)
        addMessageType(S1F17.class); // Request ON-LINE (RONL)
        addMessageType(S1F18.class); // ON-LINE Acknowledge (ONLA)
        addMessageType(S2F25.class); // Request Loopback Diagnostic Request
                                     // (LDR)
        addMessageType(S2F26.class); // Loopback Diagnostic Acknowledge (LDA)
    }

    public int getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(int deviceId) throws SecsConfigurationException {
        if (deviceId < MIN_DEVICE_ID || deviceId > MAX_DEVICE_ID) {
            throw new SecsConfigurationException("Invalid device ID: " + deviceId);
        }
        this.deviceId = deviceId;
        LOG.info("Device ID set to " + deviceId);
    }

    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
        LOG.info("Model Name set to " + modelName);
    }

    public String getSoftRev() {
        return softRev;
    }
    
    public void setSoftRev(String softRev) {
        this.softRev = softRev;
        LOG.info("SoftRev set to " + softRev);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) throws SecsConfigurationException {
        if (host == null || host.isEmpty()) {
            throw new SecsConfigurationException(String.format("Invalid host: '%s'", host));
        }
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) throws SecsConfigurationException {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new SecsConfigurationException("Invalid port number: " + port);
        }
        this.port = port;
    }
    
    public ConnectMode getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(ConnectMode connectMode) {
        this.connectMode = connectMode;
        LOG.info("Connect Mode set to " + connectMode);
    }

    public int getT3Timeout() {
        return t3;
    }
    
    public void setT3Timeout(int t3) throws SecsConfigurationException {
        if (t3 < MIN_T3 || t3 > MAX_T3) {
            throw new SecsConfigurationException("Invalid value for T3: " + t3);
        }
        this.t3 = t3;
        LOG.info(String.format("T3 set to %d seconds", t3));
    }
    
    public int getT5Timeout() {
        return t5;
    }
    
    public void setT5Timeout(int t5) throws SecsConfigurationException {
        if (t5 < MIN_T5 || t5 > MAX_T5) {
            throw new SecsConfigurationException("Invalid value for T5: " + t5);
        }
        this.t5 = t5;
        LOG.info(String.format("T5 set to %d seconds", t5));
    }
    
    public int getT6Timeout() {
        return t6;
    }
    
    public void setT6Timeout(int t6) throws SecsConfigurationException {
        if (t6 < MIN_T6 || t6 > MAX_T6) {
            throw new SecsConfigurationException("Invalid value for T6: " + t6);
        }
        this.t6 = t6;
        LOG.info(String.format("T6 set to %d seconds", t6));
    }
    
    public int getT7Timeout() {
        return t7;
    }
    
    public void setT7Timeout(int t7) throws SecsConfigurationException {
        if (t7 < MIN_T7 || t7 > MAX_T7) {
            throw new SecsConfigurationException("Invalid value for T7: " + t7);
        }
        this.t7 = t7;
        LOG.info(String.format("T5 set to %d seconds", t7));
    }
    
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        if (this.connectionState != connectionState) {
            this.connectionState = connectionState;
            for (SecsEquipmentListener listener : listeners) {
                listener.connectionStateChanged(connectionState);
            }
            LOG.info("Connection State set to " + connectionState);
        }
    }

    public CommunicationState getCommunicationState() {
        return communicationState;
    }

    public void setCommunicationState(CommunicationState communicationState) {
        if (this.communicationState != communicationState) {
            this.communicationState = communicationState;
            LOG.info("Communication State set to " + communicationState);
            for (SecsEquipmentListener listener : listeners) {
                listener.communicationStateChanged(communicationState);
            }
        }
    }

    public ControlState getControlState() {
        return controlState;
    }

    public void setControlState(ControlState controlState) {
        if (this.controlState != controlState) {
            this.controlState = controlState;
            LOG.info("Control State set to " + controlState);
            for (SecsEquipmentListener listener : listeners) {
                listener.controlStateChanged(controlState);
            }
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) throws SecsException {
        if (isEnabled) {
            // Enable equipment.
            if (!isEnabled()) {
                enable();
            } else {
                throw new SecsException("Already enabled");
            }
        } else {
            // Disable equipment.
            if (isEnabled()) {
                disable();
            } else {
                throw new SecsException("Already disabled");
            }
        }
    }

    public void addMessageType(Class<? extends SecsMessage> messageType) {
        SecsMessage message = null;
        try {
            message = messageType.newInstance();
            int messageId = message.getStream() * 256 + message.getFunction();
            messageTypes.put(messageId, messageType);
            LOG.debug("Added message type " + message.getDescripton());
        } catch (Exception e) {
            LOG.error("Could not instantiate message type: " + messageType, e);
        }
    }

    public void removeMessageType(Class<? extends SecsMessage> messageType) {
        SecsMessage message = null;
        try {
            message = messageType.newInstance();
            int messageId = message.getStream() * 256 + message.getFunction();
            if (messageTypes.containsKey(messageId)) {
                messageTypes.remove(messageId);
            }
            LOG.info("Removed message type " + message.getDescripton());
        } catch (Exception e) {
            LOG.error("Could not instantiate message type: " + messageType, e);
        }
    }
    
    public void addListener(SecsEquipmentListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(SecsEquipmentListener listener) {
        listeners.remove(listener);
    }
    
    public void sendMessage(SecsPrimaryMessage primaryMessage) throws SecsException {
        sendMessage(primaryMessage, true);
    }
        
    private void sendMessage(Message message, boolean checkCommunicationState) throws SecsException {
        if (checkCommunicationState && communicationState != CommunicationState.COMMUNICATING) {
            throw new SecsException("Communication State not COMMUNICATING");
        }
        
        message.setEquipment(this);
        message.setSessionId(deviceId);
        
        if (message.getTransactionId() == 0L) {
            message.setTransactionId(getNextTransactionId());
        }
        
        LOG.trace(String.format("Send message %s", message));
        
        try {
            OutputStream os = socket.getOutputStream();
            os.write(message.toByteArray());
            os.flush();
            
            for (SecsEquipmentListener listener : listeners) {
                listener.messageSent(message);
            }
            
        } catch (IOException e) {
            String msg = "Internal error while sending message"; 
            LOG.error(msg, e);
            throw new SecsException(msg, e);
        }
    }
    
    public SecsReplyMessage sendMessageAndWait(SecsPrimaryMessage primaryMessage) throws SecsException {
        if (communicationState != CommunicationState.COMMUNICATING) {
            throw new SecsException("Communication State not COMMUNICATING");
        }
        
        primaryMessage.setEquipment(this);
        primaryMessage.setSessionId(deviceId);
        long transactionId = getNextTransactionId();
        primaryMessage.setTransactionId(transactionId);
        startTransaction(primaryMessage);
        
        sendMessage(primaryMessage, true);
        
        long startTime = System.currentTimeMillis();
        SecsReplyMessage replyMessage = null;
        while (replyMessage == null) {
            sleep(POLL_INTERVAL);
            synchronized (transactions) {
                Transaction transaction = transactions.get(transactionId);
                if (transaction != null) {
                    Message message = transaction.getReplyMessage();
                    if (message != null) {
                        if (message instanceof SecsReplyMessage) {
                            replyMessage = (SecsReplyMessage) message;
                        } else {
                            String msg = "Unexpected reply message type: " + message;
                            LOG.warn(msg);
                            throw new SecsException(msg);
                        }
                    }
                }
            }
            if (replyMessage == null && (System.currentTimeMillis() - startTime) > (t3 * 1000L)) {
                // T3 transaction timeout.
                String msg = String.format("T3 timeout for request message %s with transaction ID %d", primaryMessage.getType(), transactionId); 
                LOG.warn(msg);
                throw new SecsTimeoutException(msg);
            }
        }
        return replyMessage;
    }
    
    private void enable() {
        isEnabled = true;
        LOG.info("Enabled State set to ENABLED");
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        if (connectMode == ConnectMode.ACTIVE) {
            // Active mode; establish HSMS connection (client).
            connectionThread = new ActiveConnectionThread();
        } else {
            // Passive mode; accept incoming HSMS connection (server).
            connectionThread = new PassiveConnectionThread();
        }
        connectionThread.start();
    }

    private void disable() {
        if (connectionState != ConnectionState.NOT_CONNECTED) {
            ControlMessage message = new ControlMessage(deviceId, 0x00, 0x00, SType.SEPARATE, getNextTransactionId());
            try {
                OutputStream os = socket.getOutputStream();
                os.write(message.toByteArray());
                os.flush();
            } catch (Exception e) {
                LOG.error("Internal error while sending SEPARATE message", e);
            }
        }

        isEnabled = false;
        LOG.info("Enabled State set to DISABLED");
        while (communicationState != CommunicationState.NOT_COMMUNICATING) {
            sleep(POLL_INTERVAL);
        }
        
        setCommunicationState(CommunicationState.NOT_ENABLED);
    }

    private void handleConnection() {
        String clientHost = socket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        setConnectionState(ConnectionState.NOT_SELECTED);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (isEnabled && getConnectionState() != ConnectionState.NOT_CONNECTED) {
                if (connectMode == ConnectMode.ACTIVE && connectionState == ConnectionState.NOT_SELECTED) {
                    // Not selected yet; send SELECT_REQ.
                    Message message = new ControlMessage(deviceId, 0x00, 0x00, SType.SELECT_REQ, getNextTransactionId());
                    sendMessage(message, false);
                    startTransaction(message);
                    //FIXME: Get rid of sleep() statements.
                    sleep(100L);
                } else if (connectMode == ConnectMode.ACTIVE && connectionState == ConnectionState.SELECTED && communicationState == CommunicationState.NOT_COMMUNICATING) {
                    // Not communicating yet; send S1F13 (Communication Request).
                    S1F13 s1f13 = new S1F13();
                    s1f13.setModelName(modelName);
                    s1f13.setSoftRev(softRev);
                    sendMessage(s1f13, false);
                    //FIXME: Get rid of sleep() statements.
                    sleep(100L);
                }
                
                if (is.available() > 0) {
                    int length = is.read(buf);
                    try {
                        Message requestMessage = MessageParser.parseMessage(buf, length, messageTypes);
                        LOG.trace(String.format("Received message: %s", requestMessage));
                        Message replyMessage = handleMessage(requestMessage);
                        if (replyMessage != null) {
                            sendMessage(replyMessage, false);
                        }
                        
                    } catch (UnsupportedMessageException e) {
                        // Unsupported message type -- ABORT.
                        LOG.warn(e.getMessage());
                        SecsMessage sxf0 = new SxF0(e.getStream());
                        sxf0.setTransactionId(e.getTransactionId());
                        sendMessage(sxf0, false);
                        
                    } catch (SecsParseException e) {
                        // Protocol fault by remote equipment.
                        LOG.warn("Received invalid SECS message: " + e.getMessage());
                        
                    } catch (SecsException e) {
                        LOG.error("Internal SECS error while handling message", e);
                    }
                } else {
                    sleep(POLL_INTERVAL);
                }
            }
        } catch (Exception e) {
            // Internal error (should never happen).
            LOG.error("Internal error while handling connection", e);
        }

        // Disconnect.
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(socket);
        disconnect();
    }

    private Message handleMessage(Message message) throws SecsException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        updateTransactionId(transactionId);

        Message replyMessage = null;

        if (message instanceof ControlMessage) {
            // HSMS control message.
            ControlMessage controlMessage = (ControlMessage) message;
            SType sType = controlMessage.getSType();
            int headerByte3 = controlMessage.getHeaderByte3();
            switch (sType) {
                case SELECT_REQ:
                    // Always accept SELECT_REQ.
                    if (getConnectionState() == ConnectionState.NOT_SELECTED) {
                        headerByte3 = 0x00; // SelectStatus: Communication Established
                        setConnectionState(ConnectionState.SELECTED);
                    } else {
                        headerByte3 = 0x01; // SelectStatus: Communication Already Active
                    }
                    
                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, SType.SELECT_RSP, transactionId);
                    break;

                case SELECT_RSP:
                    if (endTransaction(transactionId)) {
                        int selectStatus = controlMessage.getHeaderByte3();
                        if (selectStatus == 0x00) { // SelectStatus: Communication Established
                            LOG.debug("Received SELECT_RSP message with SelectStatus: Communication Established");
                            setConnectionState(ConnectionState.SELECTED);
                        } else if (selectStatus == 0x01) { // SelectStatus: Communication Already Active
                            LOG.debug("Received SELECT_RSP message with SelectStatus: Communication Already Active");
                            setConnectionState(ConnectionState.SELECTED);
                        } else if (selectStatus == 0x02) { // SelectStatus: Connection Not Ready
                            LOG.warn("Received SELECT_RSP message with SelectStatus: Connection Not Ready -- Communication failed");
                            setConnectionState(ConnectionState.NOT_SELECTED);
                        } else if (selectStatus == 0x03) { // SelectStatus: Connect Exhaust
                            LOG.warn("Received SELECT_RSP message with SelectStatus: Connect Exhaust -- Communication failed");
                            setConnectionState(ConnectionState.NOT_SELECTED);
                        } else {
                            LOG.warn("Received SELECT_RSP message with invalid SelectStatus: " + selectStatus);
                        }
                    } else {
                        LOG.warn("Unexpected SELECT_RSP received -- ignored");
                    }
                    break;

                case DESELECT_REQ:
                    // Acknowledge DESELECT_REQ if selected, otherwise fail.
                    if (getConnectionState() == ConnectionState.SELECTED) {
                        headerByte3 = 0x00; // DeselectStatus: Success
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else {
                        headerByte3 = 0x01; // DeselectStatus: Failed
                    }
                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, SType.DESELECT_RSP, transactionId);
                    break;

                case DESELECT_RSP:
                    if (endTransaction(transactionId)) {
                        int deselectStatus = controlMessage.getHeaderByte3();
                        if (deselectStatus == 0x00) { // Accept
                            LOG.debug("Received DESELECT_RSP message with DeselectStatus: Success");
                            setConnectionState(ConnectionState.NOT_SELECTED);
                        } else {
                            LOG.debug("Received DESELECT_RSP message with DeselectStatus: Failed");
                        }
                    } else {
                        LOG.warn("Unexpected DESELECT_RSP received -- ignored");
                    }
                    break;

                case SEPARATE:
                    // Immediately disconnect on SEPARATE message.
                    LOG.debug("Received SEPARATE message");
                    disconnect();
                    break;

                case LINKTEST_REQ:
                    // Send LINKTEST_RSP message.
                    replyMessage = new ControlMessage(sessionId, 0x00, 0x00, SType.LINKTEST_RSP, transactionId);
                    break;

                case LINKTEST_RSP:
                    if (!endTransaction(transactionId)) {
                        LOG.warn("Unexpected LINKTEST_RSP received -- ignored");
                    }
                    break;

                case REJECT:
                    // Always accept REJECT; no action required.
                    LOG.warn("Received REJECT message");
                    break;

                default:
                    LOG.warn(String.format("Unsupported control message (SType = %s) -- ignored", sType));
            }
        } else {
            // Data message (standard GEM or custom).
            SecsMessage dataMessage = (SecsMessage) message;
            int stream = dataMessage.getStream();
            int function = dataMessage.getFunction();

            if (function == 0) {
                // Received SxF0 (ABORT) message; nothing to do.
                LOG.warn(String.format("Received ABORT for transaction %d", transactionId));
            } else if (stream == S9) {
                // Steam 9 is reserved for generic errors.
                if (endTransaction(transactionId)) {
                    LOG.warn(String.format("Received %s for transaction %d", dataMessage.getType(), transactionId));
                } else {
                    LOG.warn(String.format("Received unexpected %s -- ignored", dataMessage.getType()));
                }
            } else {
                dataMessage.setEquipment(this);
                if (dataMessage instanceof SecsPrimaryMessage) {
                    if (communicationState == CommunicationState.COMMUNICATING || dataMessage instanceof S1F13) {
                        // Redirect primary message to specific message handler.
                        LOG.trace(String.format("Handle primary message %s - %s", dataMessage.getType(), dataMessage.getDescripton()));
                        replyMessage = ((SecsPrimaryMessage) dataMessage).handle();
                        replyMessage.setSessionId(deviceId);
                        replyMessage.setTransactionId(transactionId);
                    } else {
                        // Communication not established yet -- ABORT.
                        SecsMessage sxf0 = new SxF0(stream);
                        sxf0.setTransactionId(transactionId);
                        sendMessage(sxf0, false);
                    }
                } else if (dataMessage instanceof SecsReplyMessage) {
                    // Reply message.
                    // Try to match with active transaction.
                    LOG.trace(String.format("Handle reply message %s - %s", dataMessage.getType(), dataMessage.getDescripton()));
                    synchronized (transactions) {
                        Transaction transaction = transactions.get(transactionId);
                        if (transaction != null) {
                            // Transaction found; set reply message to be processed.
                            transaction.setReplyMessage(dataMessage);
                        }
                    }
                    // Redirect to specific message handler.
                    ((SecsReplyMessage) dataMessage).handle();
                } else {
                    // Internal error (should never happen).
                    throw new SecsException("Invalid type of data message: " + dataMessage);
                }
            }
        }

        return replyMessage;
    }
    
    private void disconnect() {
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        setConnectionState(ConnectionState.NOT_CONNECTED);
        connectionThread.interrupt();
        LOG.info("Disconnected");
    }
    
    private long getNextTransactionId() {
        return nextTransactionId++;
    }
    
    private void updateTransactionId(long transactionId) {
        if (transactionId > nextTransactionId) {
            nextTransactionId = transactionId + 1;
        }
    }
    
    private void startTransaction(Message message) {
        synchronized (transactions) {
            long transactionId = message.getTransactionId();
            transactions.put(transactionId, new Transaction(message));
            LOG.trace(String.format("Transaction %d started for message %s", transactionId, message));
        }
    }
    
    private boolean endTransaction(long transactionId) {
        synchronized (transactions) {
            if (transactions.containsKey(transactionId)) {
                transactions.remove(transactionId);
                LOG.trace(String.format("Transaction %d ended", transactionId));
                return true;
            } else {
                LOG.warn(String.format("Reply message received for unknown transaction %d", transactionId));
                return false;
            }
        }
    }
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

    /**
     * Thread to establish a TCP/IP connection to another equipment (ACTIVE
     * connection mode).
     * 
     * @author Oscar Stigter
     */
    private class ActiveConnectionThread extends Thread {

        @Override
        public void run() {
            while (isEnabled) {
                if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                    LOG.debug(String.format("Connecting to equipment '%s' on port %d", host, port));
                    try {
                        socket = new Socket(host, port);
                        handleConnection();
                    } catch (IOException e) {
                        LOG.debug(String.format("Failed to connect to equipment '%s' on port %d", host, port));
                        SecsEquipment.sleep(t5 * 1000L);
                    }
                } else {
                    SecsEquipment.sleep(POLL_INTERVAL);
                }
            }
        }
    }

    /**
     * Thread to listen for incoming TCP/IP connections from another equipment
     * (PASSIVE connection mode).
     * 
     * @author Oscar Stigter
     */
    private class PassiveConnectionThread extends Thread {

        private static final int SOCKET_TIMEOUT = 100;

        @Override
        public void run() {
            LOG.info(String.format("Listening for incoming connections on port %d", port));
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(SOCKET_TIMEOUT);
                while (isEnabled) {
                    if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                        try {
                            socket = serverSocket.accept();
                            handleConnection();
                        } catch (SocketTimeoutException e) {
                            // No incoming connections, just continue waiting.
                        } catch (IOException e) {
                            LOG.error("Socket connection error: " + e.getMessage());
                            disconnect();
                        }
                    } else {
                        SecsEquipment.sleep(POLL_INTERVAL);
                    }
                }
            } catch (IOException e) {
                LOG.error("Could not start server: " + e.getMessage());
            } finally {
                IOUtils.closeQuietly(serverSocket);
            }
        }
    }

}
