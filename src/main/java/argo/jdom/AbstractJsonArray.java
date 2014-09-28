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

abstract class AbstractJsonArray extends JsonRootNode {
    public JsonNodeType getType() {
        return JsonNodeType.ARRAY;
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
        return true;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || !AbstractJsonArray.class.isAssignableFrom(that.getClass())) return false;

        final AbstractJsonArray thatJsonArray = (AbstractJsonArray) that;
        return this.getElements().equals(thatJsonArray.getElements());
    }

    @Override
    public int hashCode() {
        return getElements().hashCode();
    }

    @Override
    public String toString() {
        return "JsonArray{elements=" + getElements() + "}";
    }
}
