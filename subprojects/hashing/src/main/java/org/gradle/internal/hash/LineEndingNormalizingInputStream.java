/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.hash;

import java.io.IOException;
import java.io.InputStream;

public class LineEndingNormalizingInputStream extends InputStream {
    int peekAhead = -1;
    private final InputStream delegate;

    public LineEndingNormalizingInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    public int read() throws IOException {
        // Get our next byte from the peek ahead buffer if it contains anything
        int next = peekAhead;

        // If there was something in the peek ahead buffer, use it, otherwise read the next byte
        if (next != -1) {
            peekAhead = -1;
        } else {
            next = delegate.read();
        }

        // If the next bytes are '\r' or '\r\n', replace it with '\n'
        if (next == '\r') {
            peekAhead = delegate.read();
            if (peekAhead == '\n') {
                peekAhead = -1;
            }
            next = '\n';
        }

        return next;
    }

    @Override
    public void close() throws IOException {
        super.close();
        delegate.close();
    }
}
