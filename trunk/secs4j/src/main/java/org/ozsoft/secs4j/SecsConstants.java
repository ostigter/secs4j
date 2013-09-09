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
 * Generic SECS constants.
 * 
 * @author Oscar Stigter
 */
public interface SecsConstants {
    
    /** Default model name. */
    String DEFAULT_MDLN = "SECS Equipment";
    
    /** Default software revision. */
    String DEFAULT_SOFTREV = "1.0";
    
    /** Default connect mode. */
    ConnectMode DEFAULT_CONNECT_MODE = ConnectMode.PASSIVE;
    
    /** Default host. */
    String DEFAULT_HOST = "localhost";

    /** Default port. */
    int DEFAULT_PORT = 5555;
    
    /** Default active state (true means active, false means passive). */
    boolean IS_ACTIVE = false;
    
    /** Default device ID (session ID). */
    int DEFAULT_DEVICE_ID = 1;
    
    /** T3 (Reply) timeout in miliseconds. */
    int DEFAULT_T3 = 120000; // 2 minutes
    
    /** T5 (Connect Separation) timeout in miliseconds. */
    int DEFAULT_T5 = 10000; // 10 seconds
    
    /** T6 (Control Transaction) timeout in miliseconds. */
    int DEFAULT_T6 = 5000; // 5 seconds
    
    /** T7 (Connect Idle) timeout in miliseconds. */
    int DEFAULT_T7 = 10000; // 10 seconds
    
    /** SECS header length in bytes. */
    int HEADER_LENGTH = 10;
    
}
