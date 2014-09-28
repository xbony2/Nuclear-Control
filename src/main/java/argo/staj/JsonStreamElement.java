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

/**
 * An element of a JSON document.
 */
public abstract class JsonStreamElement {

    private static final JsonStreamElement START_DOCUMENT = nonTextJsonStreamElement(JsonStreamElementType.START_DOCUMENT);
    private static final JsonStreamElement END_DOCUMENT = nonTextJsonStreamElement(JsonStreamElementType.END_DOCUMENT);
    private static final JsonStreamElement START_ARRAY = nonTextJsonStreamElement(JsonStreamElementType.START_ARRAY);
    private static final JsonStreamElement END_ARRAY = nonTextJsonStreamElement(JsonStreamElementType.END_ARRAY);
    private static final JsonStreamElement START_OBJECT = nonTextJsonStreamElement(JsonStreamElementType.START_OBJECT);
    private static final JsonStreamElement END_OBJECT = nonTextJsonStreamElement(JsonStreamElementType.END_OBJECT);
    private static final JsonStreamElement END_FIELD = nonTextJsonStreamElement(JsonStreamElementType.END_FIELD);
    private static final JsonStreamElement TRUE = nonTextJsonStreamElement(JsonStreamElementType.TRUE);
    private static final JsonStreamElement FALSE = nonTextJsonStreamElement(JsonStreamElementType.FALSE);
    private static final JsonStreamElement NULL = nonTextJsonStreamElement(JsonStreamElementType.NULL);

    private static JsonStreamElement nonTextJsonStreamElement(final JsonStreamElementType jsonStreamElementType) {
        return new JsonStreamElement(jsonStreamElementType) {
            @Override
            public boolean hasText() {
                return false;
            }

            @Override
            public String text() {
                throw new IllegalStateException(jsonStreamElementType().name() + " does not have text associated with it");
            }

            @Override
            public String toString() {
                return "JsonStreamElement jsonStreamElementType: " + jsonStreamElementType + "";
            }
        };
    }

    private static JsonStreamElement textJsonStreamElement(final JsonStreamElementType jsonStreamElementType, final String text) {
        return new JsonStreamElement(jsonStreamElementType) {
            @Override
            public boolean hasText() {
                return true;
            }

            @Override
            public String text() {
                return text;
            }

            @Override
            public String toString() {
                return "JsonStreamElement jsonStreamElementType: " + jsonStreamElementType + ", text: " + text;
            }
        };
    }

    static JsonStreamElement startDocument() {
        return START_DOCUMENT;
    }

    static JsonStreamElement endDocument() {
        return END_DOCUMENT;
    }

    static JsonStreamElement startArray() {
        return START_ARRAY;
    }

    static JsonStreamElement endArray() {
        return END_ARRAY;
    }

    static JsonStreamElement startObject() {
        return START_OBJECT;
    }

    static JsonStreamElement endObject() {
        return END_OBJECT;
    }

    static JsonStreamElement startField(final String text) {
        return textJsonStreamElement(JsonStreamElementType.START_FIELD, text);
    }

    static JsonStreamElement endField() {
        return END_FIELD;
    }

    static JsonStreamElement string(final String text) {
        return textJsonStreamElement(JsonStreamElementType.STRING, text);
    }

    static JsonStreamElement number(final String text) {
        return textJsonStreamElement(JsonStreamElementType.NUMBER, text);
    }

    static JsonStreamElement trueValue() {
        return TRUE;
    }

    static JsonStreamElement falseValue() {
        return FALSE;
    }

    static JsonStreamElement nullValue() {
        return NULL;
    }

    private final JsonStreamElementType jsonStreamElementType;

    private JsonStreamElement(JsonStreamElementType jsonStreamElementType) {
        this.jsonStreamElementType = jsonStreamElementType;
    }

    /**
     * Gets the type of this element.
     *
     * @return the type of the element.
     */
    public final JsonStreamElementType jsonStreamElementType() {
        return jsonStreamElementType;
    }

    /**
     * Determines whether the element has text.
     *
     * @return true if the element has text.
     */
    public abstract boolean hasText();

    /**
     * Gets the text associated with the element.
     *
     * @return the text associated with the element.
     * @throws IllegalStateException if the element doesn't have any text associated with it.
     */
    public abstract String text();
}
