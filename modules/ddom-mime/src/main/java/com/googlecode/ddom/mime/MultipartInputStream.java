/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.googlecode.ddom.mime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 
 * From RFC 2046:
 * <pre>
 * dash-boundary := "--" boundary
 *
 * multipart-body := [preamble CRLF]
 *                   dash-boundary transport-padding CRLF
 *                   body-part *encapsulation
 *                   close-delimiter transport-padding
 *                   [CRLF epilogue]
 * 
 * transport-padding := *LWSP-char
 * 
 * encapsulation := delimiter transport-padding
 *                  CRLF body-part
 * 
 * delimiter := CRLF dash-boundary
 * 
 * body-part := MIME-part-headers [CRLF *OCTET]
 * </pre>
 * RFC 2046 makes the following statement about <tt>transport-padding</tt>:
 * <blockquote>
 * Composers MUST NOT generate non-zero length transport padding, but receivers MUST
 * be able to handle padding added by message transports.
 * </blockquote>
 * It is assumed that the transports that are used for SOAP don't add any padding. Therefore this
 * grammar may be rewritten as follows:
 * <pre>
 * dash-boundary := "--" boundary
 * 
 * multipart-body := [preamble CRLF]
 *                   dash-boundary CRLF
 *                   body-part *encapsulation
 *                   close-delimiter
 *                   [CRLF epilogue]
 * 
 * encapsulation := delimiter CRLF body-part
 * 
 * delimiter := CRLF dash-boundary
 * 
 * close-delimiter := delimiter "--"
 * 
 * body-part := MIME-part-headers [CRLF *OCTET]
 * </pre>
 * 
 * 
 * The implementation uses the <a href="http://www-igm.univ-mlv.fr/~lecroq/string/node19.html#SECTION00190">Quick Search</a>
 * algorithm to locate the <tt>delimiter</tt> tokens.
 */
public class MultipartInputStream extends InputStream {
    /**
     * Indicates that we are at the start of the stream. It has not yet been determined whether the
     * stream starts with <tt>preamble CRLF dash-boundary</tt> (i.e. <tt>preamble delimiter</tt>) or
     * with <tt>dash-boundary</tt>.
     */
    private static final int STATE_START = 1;
    
    private static final int STATE_PREAMBLE = 2;
    private static final int STATE_HEADERS = 3;
    private static final int STATE_CONTENT = 4;
    
    private final InputStream in;
    private int state = STATE_START;
    
    /**
     * Contains the value of the <tt>delimiter</tt> token, i.e. <tt>CRLF "--" boundary</tt>.
     */
    private final byte[] delimiter;
    
    private final int[] skip = new int[256];
    
    /**
     * The read buffer. Note that this buffer is used as a ring buffer, i.e. the next byte is not at
     * position 0, but is referenced by the {@link #bufferPosition} attribute.
     */
    private final byte[] buffer = new byte[4096];
    
    /**
     * The current position in the buffer.
     */
    private int bufferPosition;
    
    /**
     * The number of bytes available in the buffer.
     */
    private int bufferLen;
    
    public MultipartInputStream(InputStream in, String boundary) {
        this.in = in;
        int boundaryLen = boundary.length();
        int delimiterLen = boundaryLen+4;
        delimiter = new byte[delimiterLen];
        delimiter[0] = '\r';
        delimiter[1] = '\n';
        delimiter[2] = '-';
        delimiter[3] = '-';
        for (int i=0; i<boundaryLen; i++) {
            delimiter[4+i] = (byte)boundary.charAt(i);
        }
        Arrays.fill(skip, delimiterLen);
        for (int i=0; i<delimiterLen-1; i++) {
            skip[delimiter[i]] = delimiterLen-i-1;
        }
        
    }
    
    /**
     * Fill the buffer. This method will read data from the underlying stream until the number of
     * bytes in the buffer is greater or equal to the length of the delimiter.
     * 
     * @throws IOException
     */
    private void fillBuffer() throws IOException {
        int delimiterLen = delimiter.length;
        int bufferSize = buffer.length;
        while (bufferLen < delimiterLen) {
            int off = (bufferPosition + bufferLen) % bufferSize;
            int len = Math.min(bufferSize-off, bufferSize-bufferLen);
            int read = in.read(buffer, off, len);
            if (read == -1) {
                throw new IOException("Unexpected end of stream");
            }
            len += read;
        }
    }
    
    public void nextPart() throws IOException {
        fillBuffer();
        if (state == STATE_START) {
            int delimiterLen = delimiter.length;
            for (int i=0; i<delimiterLen-2; i++) {
                if (buffer[i] != delimiter[i+2]) {
                    state = STATE_PREAMBLE;
                    break;
                }
            }
            if (state != STATE_PREAMBLE) {
                if (buffer[delimiterLen-2] == '\r' && buffer[delimiterLen-1] == '\n') {
                    bufferPosition = delimiterLen;
                    bufferLen -= delimiterLen;
                    state = STATE_HEADERS;
                } else {
                    state = STATE_PREAMBLE;
                }
            }
        }
    }
    
    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
