/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.staj;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Stack;

/**
 * Parses a JSON character stream into an {@code Iterator} of {@code JsonStreamElement}s.
 */
public final class StajParser implements Iterator<JsonStreamElement> {

    private final PositionTrackingPushbackReader pushbackReader;
    private final Stack<JsonStreamElementType> stack = new Stack<JsonStreamElementType>();
    private JsonStreamElement current;
    private JsonStreamElement next;

    /**
     * Constructs a StajParser reading from the specified {@code Reader}.
     *
     * @param in the {@code Reader} to convert into {@code JsonStreamElement}s.
     */
    public StajParser(final Reader in) {
        this.pushbackReader = new PositionTrackingPushbackReader(in);
    }

    /**
     * Constructs a StajParser reading from the given {@code String}.
     *
     * @param json the {@code String} to convert into {@code JsonStreamElement}s.
     */
    public StajParser(final String json) {
        this.pushbackReader = new PositionTrackingPushbackReader(new StringReader(json));
    }

    /**
     * Determines whether there are any more elements.
     *
     * @return true if there are more elements.
     * @throws InvalidSyntaxRuntimeException if the next element could not be read, for example if the next element turns out not to be valid JSON
     * @throws JsonStreamException           if the underlying character stream failed.
     */
    public boolean hasNext() {
        if (current != null && current.jsonStreamElementType().equals(JsonStreamElementType.END_DOCUMENT)) {
            return false;
        } else if (next == null) {
            next = getNextElement();
        }
        return true;
    }

    /**
     * Gets the next element in the stream.
     *
     * @return the next element in the stream.
     * @throws InvalidSyntaxRuntimeException if the next element could not be read, for example if the next element turns out not to be valid JSON
     * @throws JsonStreamException           if the underlying character stream failed.
     * @throws java.util.NoSuchElementException
     *                                       if there are no more elements to read.
     */
    public JsonStreamElement next() {
        if (next != null) {
            current = next;
            next = null;
        } else {
            current = getNextElement();
        }
        return current;
    }

    private JsonStreamElement getNextElement() {
        if (current != null) {
            return current.jsonStreamElementType().parseNext(pushbackReader, stack);
        } else {
            return JsonStreamElementType.parseFirstElement(pushbackReader);
        }
    }

    /**
     * Not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException("StajParser cannot remove elements from JSON it has parsed.");
    }
}
