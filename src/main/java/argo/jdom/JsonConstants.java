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

import java.util.List;
import java.util.Map;

final class JsonConstants extends JsonNode implements JsonNodeBuilder<JsonNode> {

    static final JsonConstants NULL = new JsonConstants(JsonNodeType.NULL);
    static final JsonConstants TRUE = new JsonConstants(JsonNodeType.TRUE);
    static final JsonConstants FALSE = new JsonConstants(JsonNodeType.FALSE);

    private final JsonNodeType jsonNodeType;

    private JsonConstants(JsonNodeType jsonNodeType) {
        this.jsonNodeType = jsonNodeType;
    }

    public JsonNodeType getType() {
        return jsonNodeType;
    }

    public boolean hasText() {
        return false;
    }

    public String getText() {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    public boolean hasFields() {
        return false;
    }

    public Map<JsonStringNode, JsonNode> getFields() {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    @Override
    public List<JsonField> getFieldList() {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    public boolean hasElements() {
        return false;
    }

    public List<JsonNode> getElements() {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
    }

    public JsonNode build() {
        return this;
    }

    @Override
    public String toString() {
        return "JsonNode{jsonNodeType=" + jsonNodeType + '}';
    }
}
