/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.ddom.woodstox;

import javax.xml.stream.XMLStreamException;

import com.ctc.wstx.sr.BasicStreamReader;

public privileged aspect CDATAMinSegmentSize {
    // TODO: this should be configurable
    final int BasicStreamReader.mShortestCDATASegment = Integer.MAX_VALUE;
    
    void around(BasicStreamReader reader, boolean deferErrors) throws XMLStreamException: execution(void BasicStreamReader.finishToken(boolean)) && this(reader) && args(deferErrors) {
        if (reader.mCurrToken == BasicStreamReader.CDATA && !reader.mCfgCoalesceText) {
            if (reader.readCDataSecondary(reader.mShortestCDATASegment)) {
                reader.mTokenState = BasicStreamReader.TOKEN_FULL_SINGLE;
            } else {
                reader.mTokenState = BasicStreamReader.TOKEN_PARTIAL_SINGLE;
            }
        } else {
            proceed(reader, deferErrors);
        }
    }
    
    // This fixes the token state after a call to readCDataPrimary. This is necessary because readCDataPrimary
    // refers to mShortestTextSegment.
    after(BasicStreamReader reader) returning (boolean complete): execution(boolean BasicStreamReader.readCDataPrimary(char)) && this(reader) {
        if (!complete && !reader.mCfgCoalesceText
                && reader.mTokenState == BasicStreamReader.TOKEN_PARTIAL_SINGLE
                && reader.mTextBuffer.size() < reader.mShortestCDATASegment) {
            reader.mTokenState = BasicStreamReader.TOKEN_STARTED;
        }
    }
}
