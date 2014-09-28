/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.jdom;

import argo.saj.InvalidSyntaxException;
import argo.saj.JsonListener;
import argo.saj.SajParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Parses a JSON character stream into a {@code JsonRootNode} object.  Instances of this class can safely be shared
 * between threads.
 */
public final class JdomParser {

    static interface JsonListenerBasedParser {
        void parse(JsonListener jsonListener) throws IOException, InvalidSyntaxException;
    }

    /**
     * Parse the specified JSON {@code String} into a {@code JsonRootNode} object.
     *
     * @param json the {@code String} to parse.
     * @return a {@code JsonRootNode} representing the JSON read from the specified {@code String}.
     * @throws InvalidSyntaxException if the characters streamed from the specified {@code String} does not represent valid JSON.
     */
    public JsonRootNode parse(final String json) throws InvalidSyntaxException {
        final JsonRootNode result;
        try {
            result = parse(new StringReader(json));
        } catch (final IOException e) {
            throw new RuntimeException("Coding failure in Argo:  StringReader threw an IOException", e);
        }
        return result;
    }

    /**
     * Parse the character stream from the specified {@code Reader} into a {@code JsonRootNode} object.
     *
     * @param reader the {@code Reader} to parse.
     * @return a {@code JsonRootNode} representing the JSON read from the specified {@code Reader}.
     * @throws IOException            bubbled up from exceptions thrown reading from the specified {@code Reader}.
     * @throws InvalidSyntaxException if the characters streamed from the specified {@code Reader} does not represent valid JSON.
     */
    public JsonRootNode parse(final Reader reader) throws IOException, InvalidSyntaxException {
        return parse(new JsonListenerBasedParser() {
            public void parse(JsonListener jsonListener) throws IOException, InvalidSyntaxException {
                new SajParser().parse(reader, jsonListener);
            }
        });
    }

    JsonRootNode parse(final JsonListenerBasedParser jsonListenerBasedParser) throws IOException, InvalidSyntaxException {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerBasedParser.parse(jsonListenerToJdomAdapter);
        return jsonListenerToJdomAdapter.getDocument();
    }

}
