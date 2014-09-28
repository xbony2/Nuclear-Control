/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.jdom;

import static argo.jdom.JsonConstants.*;

/**
 * Builders for {@code JsonNode}s.
 */
public final class JsonNodeBuilders {

    private JsonNodeBuilders() {
    }

    public static JsonNodeBuilder<JsonNode> aNullBuilder() {
        return NULL;
    }

    public static JsonNodeBuilder<JsonNode> aTrueBuilder() {
        return TRUE;
    }

    public static JsonNodeBuilder<JsonNode> aFalseBuilder() {
        return FALSE;
    }

    /**
     * Builder for a JSON number.  This method will throw an illegal argument exception if the {@code String} specified does not conform to the JSON number specification.
     *
     * @param value a String representing a JSON number.
     * @return a builder for a {@code JsonNode} representing the number given.
     * @throws IllegalArgumentException if the given {@code String} does not conform to the JSON number specification.
     */
    public static JsonNodeBuilder<JsonNode> aNumberBuilder(final String value) {
        return new JsonNumberNode(value);
    }

    /**
     * Builder for a JSON string.
     *
     * @param value a String to convert into a JSON string.
     * @return a builder for a {@code JsonNode} representing the string given.
     */
    public static JsonNodeBuilder<JsonStringNode> aStringBuilder(final String value) {
        return new JsonStringNode(value);
    }

    /**
     * Builder for a JSON object that permits duplicated field names, and preserves field order.
     * Fields in the built JSON object appear in the order they were added to the builder.
     *
     * @return a builder for a {@code JsonRootNode} representing an object.
     */
    public static JsonObjectNodeBuilder anObjectBuilder() {
        return JsonObjectNodeBuilder.duplicateFieldPermittingJsonObjectNodeBuilder();
    }

    /**
     * Builder for a JSON object that has unique field names.
     * Adding two fields with the same name results in an IllegalArgumentException being thrown.
     *
     * @return a builder for a {@code JsonRootNode} representing an object.
     */
    public static JsonObjectNodeBuilder aUniqueFieldNameObjectBuilder() {
        return JsonObjectNodeBuilder.duplicateFieldRejectingJsonObjectNodeBuilder();
    }

    /**
     * Builder for a JSON array.
     *
     * @return a builder for a {@code JsonRootNode} representing an array.
     */
    public static JsonArrayNodeBuilder anArrayBuilder() {
        return new JsonArrayNodeBuilder();
    }
}
