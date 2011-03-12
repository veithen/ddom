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
import java.io.OutputStream;

import com.googlecode.ddom.base64.Base64EncodingOutputStream;

public class MultipartWriter {
    private static final int STATE_START = 1;
    private static final int STATE_PART_HEADERS = 2;
    private static final int STATE_PART_CONTENT = 3;
    private static final int STATE_COMPLETE = 4;

    class PartWriterImpl implements PartWriter {
        private boolean active = true;
        private String contentTransferEncoding;
        
        public void addHeader(String name, String value) throws IOException {
            if (!active) {
                throw new IllegalStateException();
            }
            if (name.equalsIgnoreCase("Content-Transfer-Encoding")) {
                if (value.equals("8bit") || value.equals("binary") || value.equals("base64")) {
                    contentTransferEncoding = value;
                } else {
                    value = contentTransferEncoding = "base64";
                }
            }
            writeAscii(name);
            writeAscii(": ");
            writeAscii(value);
            writeAscii("\r\n");
        }

        public OutputStream getOutputStream() throws IOException {
            if (!active) {
                throw new IllegalStateException();
            }
            writeAscii("\r\n");
            active = false;
            state = STATE_PART_CONTENT;
            return new PartOutputStream("base64".equals(contentTransferEncoding) ? new Base64EncodingOutputStream(out) : out);
        }
    }
    
    class PartOutputStream extends OutputStream {
        private final OutputStream parent;
        private boolean closed;

        public PartOutputStream(OutputStream parent) {
            this.parent = parent;
        }
        
        private void checkClosed() throws IOException {
            if (closed) {
                throw new IOException("Stream already closed");
            }
        }
        
        public void write(int b) throws IOException {
            checkClosed();
            parent.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            checkClosed();
            parent.write(b, off, len);
        }

        public void write(byte[] b) throws IOException {
            checkClosed();
            parent.write(b);
        }
        
        public void close() throws IOException {
            if (!closed) {
                if (parent instanceof Base64EncodingOutputStream) {
                    ((Base64EncodingOutputStream)parent).complete();
                }
                writeAscii("\r\n");
                state = STATE_START;
                closed = true;
            }
        }
    }
    
    private final OutputStream out;
    private final String boundary;
    private final byte[] buffer = new byte[256];
    int state = STATE_START;

    public MultipartWriter(OutputStream out, String boundary) {
        this.out = out;
        this.boundary = boundary;
    }

    void writeAscii(String s) throws IOException {
        int count = 0;
        for (int i=0, len=s.length(); i<len; i++) {
            char c = s.charAt(i);
            if (c >= 128) {
                throw new IOException("Illegal character '" + c + "'");
            }
            buffer[count++] = (byte)c;
            if (count == buffer.length) {
                out.write(buffer);
                count = 0;
            }
        }
        if (count > 0) {
            out.write(buffer, 0, count);
        }
    }
    
    public PartWriter startPart() throws IOException {
        if (state != STATE_START) {
            throw new IllegalStateException();
        }
        writeAscii("--");
        writeAscii(boundary);
        writeAscii("\r\n");
        state = STATE_PART_HEADERS;
        return new PartWriterImpl();
    }
    
    public void complete() throws IOException {
        if (state != STATE_START) {
            throw new IllegalStateException();
        }
        writeAscii("--");
        writeAscii(boundary);
        writeAscii("--\r\n");
        state = STATE_COMPLETE;
    }
}
