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

package org.ozsoft.secs.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.ozsoft.secs.CommunicationState;
import org.ozsoft.secs.ConnectMode;
import org.ozsoft.secs.ConnectionState;
import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.Message;
import org.ozsoft.secs.SecsConstants;
import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsEquipmentListener;
import org.ozsoft.secs.SecsException;

/**
 * Test tool with Swing GUI to simulate a SECS equipment and interactively test communicating with other SECS equipment.
 * 
 * @author Oscar Stigter
 */
public class TestTool implements SecsEquipmentListener {
    
    private static final String TITLE = "SECS Test Tool";
    
    private static final int DEFAULT_WIDTH = 800;
    
    private static final int DEFAULT_HEIGHT = 600;
    
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final SecsEquipment equipment = new SecsEquipment();
    
    private JFrame frame;
    
    private JRadioButton activeButton;
    
    private JRadioButton passiveButton;
    
    private JTextField hostText;
    
    private JTextField portText;
    
    private JButton enableButton;
    
    private JButton disableButton;
    
    private JTextField connectionStateText;
    
    private JTextField communicationStateText;
    
    private JTextField controlStateText;
    
    private JTextArea sendText;
    
    private JButton sendButton;
    
    private JTextArea traceText;
    
    private JButton clearButton;
    
    private String lastHost = SecsConstants.DEFAULT_HOST;
    
    public static void main(String[] args) {
        new TestTool();
    }
    
    public TestTool() {
        initUI();
        
        connectionStateChanged(equipment.getConnectionState());
        communicationStateChanged(equipment.getCommunicationState());
        controlStateChanged(equipment.getControlState());

        equipment.addListener(this);
    }
    
    @Override
    public void connectionStateChanged(ConnectionState connectionState) {
        appendTraceLog("Connection State set to " + connectionState);
        connectionStateText.setText(connectionState.name());
    }

    @Override
    public void communicationStateChanged(CommunicationState communicationState) {
        appendTraceLog("Communication State set to " + communicationState);
        communicationStateText.setText(communicationState.name());
    }

    @Override
    public void controlStateChanged(ControlState controlState) {
        appendTraceLog("Control State set to " + controlState);
        controlStateText.setText(controlState.name());
    }

    @Override
    public void messageReceived(Message message) {
        appendTraceLog("R>> " + message.toString());
    }

    @Override
    public void messageSent(Message message) {
        appendTraceLog("S<< " + message.toString());
    }
    
    private void initUI() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Configuration"));
        
        JLabel label = new JLabel("Connection Mode:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(label, gbc);
        
        activeButton = new JRadioButton("Active", false);
        activeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hostText.setEnabled(true);
                hostText.setText(lastHost);
            } 
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(activeButton, gbc);
        
        passiveButton = new JRadioButton("Passive", true);
        passiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastHost = hostText.getText();
                hostText.setText(SecsConstants.DEFAULT_HOST);
                hostText.setEnabled(false);
            } 
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(passiveButton, gbc);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(activeButton);
        bg.add(passiveButton);
        passiveButton.setSelected(true);
        
        label = new JLabel("Host:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(label, gbc);
        
        hostText = new JTextField(String.valueOf(SecsConstants.DEFAULT_HOST), 16);
        hostText.setFont(FONT);
        hostText.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(hostText, gbc);
        
        label = new JLabel("Port:");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(label, gbc);
        
        portText = new JTextField(String.valueOf(SecsConstants.DEFAULT_PORT), 6);
        portText.setFont(FONT);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(portText, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 0, 5);
        frame.getContentPane().add(panel, gbc);
        
        panel = new JPanel(new FlowLayout());
        panel.setBorder(new TitledBorder("Control"));
        
        enableButton = new JButton("Enable");
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enable();
            }
        });
        panel.add(enableButton);
        
        disableButton = new JButton("Disable");
        disableButton.setEnabled(false);
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disable();
            }
        });
        panel.add(disableButton);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        frame.getContentPane().add(panel, gbc);
        
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Status"));
        
        label = new JLabel("Connection State:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel.add(label, gbc);
        
        connectionStateText = new JTextField(14);
        connectionStateText.setFont(FONT);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(connectionStateText, gbc);
        
        label = new JLabel("Communication State:");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel.add(label, gbc);
        
        communicationStateText = new JTextField(18);
        communicationStateText.setFont(FONT);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(communicationStateText, gbc);
        
        label = new JLabel("Control State:");
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel.add(label, gbc);
        
        controlStateText = new JTextField(18);
        controlStateText.setFont(FONT);
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(controlStateText, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        frame.getContentPane().add(panel, gbc);
        
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Send Message"));
        
        sendText = new JTextArea();
        sendText.setEnabled(false);
        sendText.setFont(FONT);
        sendText.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(sendText);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(scrollPane, gbc);
        
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(sendButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(panel, gbc);
        
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Trace log"));
        
        traceText = new JTextArea();
        traceText.setEditable(false);
        traceText.setFont(FONT);
        traceText.setLineWrap(false);
        scrollPane = new JScrollPane(traceText);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(scrollPane, gbc);
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traceText.setText("");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(clearButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(panel, gbc);
        
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void enable() {
        enableButton.setEnabled(false);
        String host = hostText.getText();
        int port = Integer.parseInt(portText.getText());
        try {
            if (activeButton.isSelected()) {
                equipment.setConnectMode(ConnectMode.ACTIVE);
            } else {
                equipment.setConnectMode(ConnectMode.PASSIVE);
            }
            equipment.setHost(host);
            equipment.setPort(port);
            equipment.setEnabled(true);
            disableButton.setEnabled(true);
        } catch (SecsException e) {
            JOptionPane.showMessageDialog(frame, "Could not configure or enable equipment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            enableButton.setEnabled(true);
        }
    }
    
    private void disable() {
        disableButton.setEnabled(false);
        try {
            equipment.setEnabled(false);
        } catch (SecsException e) {
            JOptionPane.showMessageDialog(frame, "Could not disable equipment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        enableButton.setEnabled(true);
    }

    private void appendTraceLog(final String message) {
        final String timestamp = DATE_FORMAT.format(new Date());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                traceText.append(String.format("[%s] %s\n", timestamp, message));
                traceText.setCaretPosition(traceText.getText().length());
            }
        });
    }

}
