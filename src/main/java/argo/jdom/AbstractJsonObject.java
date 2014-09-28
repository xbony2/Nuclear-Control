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

abstract class AbstractJsonObject extends JsonRootNode {

    public JsonNodeType getType() {
        return JsonNodeType.OBJECT;
    }

    public boolean hasText() {
        return false;
    }

    public String getText() {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    public boolean hasFields() {
        return true;
    }

    public boolean hasElements() {
        return false;
    }

    public List<JsonNode> getElements() {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || !AbstractJsonObject.class.isAssignableFrom(that.getClass())) return false;

        final AbstractJsonObject thatJsonObject = (AbstractJsonObject) that;
        return this.getFieldList().equals(thatJsonObject.getFieldList());
    }

    @Override
    public int hashCode() {
        return getFieldList().hashCode();
    }

    @Override
    public String toString() {
        return "JsonObject{fields=" + getFieldList() + "}";
    }
}
