/*
 * Copyright 2009-2011 Andreas Veithen
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
 * Reads MIME multipart bodies and gives access to the raw content of the parts. This class
 * contains the logic to split the message into parts, read the MIME headers and
 * access the raw content of each part. Use {@link MultipartReader} to get access to the
 * decoded content.
 * <p>
 * The structure of a multipart body is defined by the following production from RFC 2046:
 * <pre>
 * multipart-body := [preamble CRLF]
 *                   dash-boundary transport-padding CRLF
 *                   body-part *encapsulation
 *                   close-delimiter transport-padding
 *                   [CRLF epilogue]
 * 
 * dash-boundary := "--" boundary
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
 * The implementation uses the <a href="http://www-igm.univ-mlv.fr/~lecroq/string/node19.html#SECTION00190">Quick Search</a>
 * algorithm to locate the <tt>delimiter</tt> tokens.
 * <p>
 * To read a MIME part, use the following sequence of calls:
 * <ol>
 * <li>Call {@link #nextPart()} to position the stream at the beginning of the next unread part.
 * <li>Repeatedly call {@link #nextHeader()} until it returns false. After each call to that method,
 * the header name and value can be retrieved using {@link #getHeaderName()} and {@link #getHeaderValue()}.
 * <li>Use one of the <code>read</code> methods to read the raw content of the part. A return value of
 * <code>-1</code> indicates that the end of the part has been reached.
 * </ol>
 */
// TODO: specify the behavior of the close() method
public class MultipartInputStream extends InputStream {
    /**
     * Indicates that we are at the start of the stream. It has not yet been determined whether the
     * stream starts with <tt>preamble CRLF dash-boundary</tt> (i.e. <tt>preamble delimiter</tt>) or
     * with <tt>dash-boundary</tt>.
     */
    private static final int STATE_START = 1;
    
    private static final int STATE_PREAMBLE = 2;
    
    /**
     * Indicates that the stream is positioned in the headers section of a MIME part.
     */
    private static final int STATE_HEADERS = 3;
    
    private static final int STATE_CONTENT = 4;
    
    private static final int STATE_END = 5;
    
    private final InputStream in;
    private int state = STATE_START;
    
    /**
     * Contains the value of the <tt>delimiter</tt> token, i.e. <tt>CRLF "--" boundary</tt>.
     */
    private final byte[] delimiter;
    
    /**
     * Contains the shift values used by the Quick Search algorithm to locate the <tt>delimiter</tt>
     * token.
     */
    private final int[] shift = new int[256];
    
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
    
    /**
     * The number of bytes available in the buffer that are known not to be part of a
     * <tt>delimiter</tt> token. This attribute is only meaningful if {@link #state} is
     * {@link #STATE_PREAMBLE} or {@link #STATE_CONTENT}.
     */
    private int nonDelimiterBytes;
    
    /**
     * Indicates whether a <tt>delimiter</tt> token has been found in the buffer. If this attribute
     * is <code>true</code>, then {@link #nonDelimiterBytes} indicates the position of the
     * delimiter. This attribute is only meaningful if {@link #state} is {@link #STATE_PREAMBLE} or
     * {@link #STATE_CONTENT}.
     */
    private boolean delimiterFound;
    
    private final StringBuilder builder = new StringBuilder(256);
    
    private String headerName;
    private String headerValue;
    
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
        Arrays.fill(shift, delimiterLen+1);
        for (int i=0; i<delimiterLen; i++) {
            shift[delimiter[i]] = delimiterLen-i;
        }
    }
    
    /**
     * Fill the buffer. This method will read data from the underlying stream until the number of
     * bytes in the buffer is greater or equal to the specified minimum.
     * 
     * @throws IOException
     */
    private void fillBuffer(int minLen) throws IOException {
        int bufferSize = buffer.length;
        while (bufferLen < minLen) {
            int off = (bufferPosition + bufferLen) % bufferSize;
            int len = Math.min(bufferSize-off, bufferSize-bufferLen);
            int read = in.read(buffer, off, len);
            if (read == -1) {
                throw new IOException("Unexpected end of stream");
            }
            bufferLen += read;
        }
    }
    
    private int lookAhead(int delta) throws IOException {
        // TODO: check that delta is within bounds (<buffer.length)
        fillBuffer(delta+1);
        return buffer[(bufferPosition + delta) % buffer.length] & 0xFF;
    }
    
    private void searchDelimiter() throws IOException {
        final byte[] delimiter = this.delimiter;
        final byte[] buffer = this.buffer;
        final int delimiterLength = delimiter.length;
        fillBuffer(delimiterLength+1);
        final int bufferPosition = this.bufferPosition;
        final int bufferLen = this.bufferLen;
        final int bufferSize = buffer.length;
        int nonDelimiterBytes = this.nonDelimiterBytes;
        while (bufferLen > nonDelimiterBytes + delimiterLength) {
            boolean found = true;
            int pos = bufferPosition + nonDelimiterBytes;
            for (int i=0; i<delimiterLength; i++) {
                if (pos >= bufferSize) {
                    pos -= bufferSize;
                }
                if (buffer[pos] != delimiter[i]) {
                    found = false;
                    break;
                }
                pos++;
            }
            if (found) {
                delimiterFound = true;
                break;
            } else {
                nonDelimiterBytes += shift[buffer[(bufferPosition + nonDelimiterBytes + delimiterLength) % bufferSize] & 0xFF];
            }
        }
        this.nonDelimiterBytes = nonDelimiterBytes;
    }
    
    /**
     * Discard a given number of bytes from the buffer.
     * 
     * @param bytes the number of bytes to discard
     */
    private void discard(int bytes) {
        bufferPosition = (bufferPosition + bytes) % buffer.length;
        bufferLen -= bytes;
    }
    
    private String readAscii(int length) {
        int bufferSize = buffer.length;
        int bufferPosition = this.bufferPosition;
        builder.ensureCapacity(length);
        for (int i=0; i<length; i++) {
            builder.append((char)buffer[bufferPosition]);
            bufferPosition++;
            if (bufferPosition > bufferSize) {
                bufferPosition = 0;
            }
        }
        this.bufferPosition = bufferPosition;
        bufferLen -= length;
        String result = builder.toString();
        builder.setLength(0);
        return result;
    }
    
    private void processDelimiter() throws IOException {
        discard(delimiter.length);
        if (lookAhead(0) == '-' && lookAhead(1) == '-') {
            discard(2);
            state = STATE_END;
        } else if (lookAhead(0) == '\r' && lookAhead(1) == '\n') {
            discard(2);
            state = STATE_HEADERS;
        } else {
            throw new IOException("Unexpected characters after delimiter");
        }
    }
    
    public boolean nextPart() throws IOException {
        int delimiterLen = delimiter.length;
        fillBuffer(delimiterLen);
        if (state == STATE_START) {
            for (int i=0; i<delimiterLen-2; i++) {
                if (buffer[i] != delimiter[i+2]) {
                    state = STATE_PREAMBLE;
                    break;
                }
            }
            if (state != STATE_PREAMBLE) {
                if (buffer[delimiterLen-2] == '\r' && buffer[delimiterLen-1] == '\n') {
                    discard(delimiterLen);
                    state = STATE_HEADERS;
                } else {
                    state = STATE_PREAMBLE;
                }
            }
            if (state == STATE_PREAMBLE) {
                do {
                    searchDelimiter();
                    discard(nonDelimiterBytes);
                    nonDelimiterBytes = 0;
                } while (!delimiterFound);
                processDelimiter();
            }
        } else if (state == STATE_CONTENT && delimiterFound && nonDelimiterBytes == 0) {
            processDelimiter();
        }
        return state == STATE_HEADERS;
    }
    
    public boolean nextHeader() throws IOException {
        if (state != STATE_HEADERS) {
            throw new IllegalStateException();
        }
        if (lookAhead(0) == '\r' && lookAhead(1) == '\n') {
            discard(2);
            headerName = null;
            headerValue = null;
            state = STATE_CONTENT;
            delimiterFound = false;
            nonDelimiterBytes = 0;
            return false;
        } else {
            int len = 0;
            while (lookAhead(len) != ':') {
                len++;
            }
            headerName = readAscii(len);
            discard(1);
            while (lookAhead(0) == ' ') {
                discard(1);
            }
            len = 0;
            while (lookAhead(len) != '\r' || lookAhead(len+1) != '\n') {
                len++;
            }
            headerValue = readAscii(len);
            discard(2);
            return true;
        }
    }
    
    public String getHeaderName() {
        return headerName;
    }
    
    public String getHeaderValue() {
        return headerValue;
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (state != STATE_CONTENT) {
            throw new IllegalStateException();
        }
        if (delimiterFound && nonDelimiterBytes == 0) {
            return -1;
        } else {
            if (nonDelimiterBytes == 0) {
                searchDelimiter();
            }
            int c = Math.min(nonDelimiterBytes, len);
            int currentBufferPosition = bufferPosition;
            int newBufferPosition = currentBufferPosition + c;
            int bufferSize = buffer.length;
            if (newBufferPosition > bufferSize) {
                newBufferPosition -= bufferSize;
                int c1 = bufferSize - currentBufferPosition;
                System.arraycopy(buffer, currentBufferPosition, b, off, c1);
                System.arraycopy(buffer, 0, b, off+c1, c-c1);
            } else {
                System.arraycopy(buffer, currentBufferPosition, b, off, c);
            }
            bufferPosition = newBufferPosition;
            bufferLen -= c;
            nonDelimiterBytes -= c;
            return c;
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
