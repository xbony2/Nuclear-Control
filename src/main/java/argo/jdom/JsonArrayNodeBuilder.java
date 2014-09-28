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

import java.util.LinkedList;
import java.util.List;

/**
 * Builder for {@code JsonRootNode}s representing JSON arrays.
 */
public final class JsonArrayNodeBuilder implements JsonNodeBuilder<JsonRootNode> {

    private final List<JsonNodeBuilder> elementBuilders = new LinkedList<JsonNodeBuilder>();

    JsonArrayNodeBuilder() {
    }

    /**
     * Adds the given element to the array that will be built.
     *
     * @param elementBuilder a builder for the element to add to the array.
     * @return the modified builder.
     */
    public JsonArrayNodeBuilder withElement(final JsonNodeBuilder elementBuilder) {
        elementBuilders.add(elementBuilder);
        return this;
    }

    public JsonRootNode build() {
        final List<JsonNode> elements = new LinkedList<JsonNode>();
        for (JsonNodeBuilder elementBuilder : elementBuilders) {
            elements.add(elementBuilder.build());
        }
        return JsonNodeFactories.array(elements);
    }
}
