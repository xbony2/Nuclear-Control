/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.saj;

import argo.staj.InvalidSyntaxRuntimeException;
import argo.staj.JsonStreamElement;
import argo.staj.StajParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Converts a character stream into calls to a {@code JsonListener}.
 * <p/>
 * Instances of {@code SajParser} are threadsafe in that concurrent calls to {@code parse} are safe, provided
 * each call is made with a different {@code Reader} and a different {@code JsonListener}.
 *
 * @see JsonListener
 */
public final class SajParser {

    public SajParser() {
    }

    /**
     * Parses the given JSON {@code String} into calls to the given JsonListener.
     *
     * @param json         the {@code String} to parse.
     * @param jsonListener the JsonListener to notify of parsing events
     * @throws IOException            bubbled up from exceptions thrown reading from {@code in}
     * @throws InvalidSyntaxException thrown to indicate the characters read from {@code in} did not constitute valid JSON.
     */
    public void parse(final String json, final JsonListener jsonListener) throws IOException, InvalidSyntaxException {
        parse(jsonListener, new StajParser(new StringReader(json)));
    }

    /**
     * Parses the given character stream into calls to the given JsonListener.
     *
     * @param in           the character stream to parse
     * @param jsonListener the JsonListener to notify of parsing events
     * @throws IOException            bubbled up from exceptions thrown reading from {@code in}
     * @throws InvalidSyntaxException thrown to indicate the characters read from {@code in} did not constitute valid JSON.
     */
    public void parse(final Reader in, final JsonListener jsonListener) throws IOException, InvalidSyntaxException {
        parse(jsonListener, new StajParser(in));
    }

    void parse(final JsonListener jsonListener, final StajParser stajParser) throws InvalidSyntaxException {
        try {
            while (stajParser.hasNext()) {
                final JsonStreamElement jsonStreamElement = stajParser.next();
                switch (jsonStreamElement.jsonStreamElementType()) {
                    case START_DOCUMENT:
                        jsonListener.startDocument();
                        break;
                    case END_DOCUMENT:
                        jsonListener.endDocument();
                        break;
                    case START_ARRAY:
                        jsonListener.startArray();
                        break;
                    case END_ARRAY:
                        jsonListener.endArray();
                        break;
                    case START_OBJECT:
                        jsonListener.startObject();
                        break;
                    case END_OBJECT:
                        jsonListener.endObject();
                        break;
                    case START_FIELD:
                        jsonListener.startField(jsonStreamElement.text());
                        break;
                    case END_FIELD:
                        jsonListener.endField();
                        break;
                    case NULL:
                        jsonListener.nullValue();
                        break;
                    case TRUE:
                        jsonListener.trueValue();
                        break;
                    case FALSE:
                        jsonListener.falseValue();
                        break;
                    case STRING:
                        jsonListener.stringValue(jsonStreamElement.text());
                        break;
                    case NUMBER:
                        jsonListener.numberValue(jsonStreamElement.text());
                        break;
                    default:
                        throw new IllegalStateException("Got a JsonStreamElement of unexpected type: " + jsonStreamElement);
                }
            }
        } catch (final InvalidSyntaxRuntimeException e) {
            throw e.asInvalidSyntaxException();
        }
    }

}
