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

import argo.saj.JsonListener;

import java.util.Stack;

import static argo.jdom.JsonFieldBuilder.aJsonFieldBuilder;
import static argo.jdom.JsonNodeBuilders.*;

final class JsonListenerToJdomAdapter implements JsonListener {

    private final Stack<NodeContainer> stack = new Stack<NodeContainer>();
    private JsonNodeBuilder<JsonRootNode> root;

    JsonRootNode getDocument() {
        return root.build();
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startArray() {
        final JsonArrayNodeBuilder arrayBuilder = anArrayBuilder();
        addRootNode(arrayBuilder);
        stack.push(new NodeContainer() {
            public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
                arrayBuilder.withElement(jsonNodeBuilder);
            }

            public void addField(final JsonFieldBuilder jsonFieldBuilder) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
            }
        });
    }

    public void endArray() {
        stack.pop();
    }

    public void startObject() {
        final JsonObjectNodeBuilder objectNodeBuilder = anObjectBuilder();
        addRootNode(objectNodeBuilder);
        stack.push(new NodeContainer() {
            public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
            }

            public void addField(final JsonFieldBuilder jsonFieldBuilder) {
                objectNodeBuilder.withFieldBuilder(jsonFieldBuilder);
            }
        });
    }

    public void endObject() {
        stack.pop();
    }

    public void startField(final String name) {
        final JsonFieldBuilder fieldBuilder = aJsonFieldBuilder().withKey(JsonNodeFactories.string(name));
        stack.peek().addField(fieldBuilder);
        stack.push(new NodeContainer() {
            public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
                fieldBuilder.withValue(jsonNodeBuilder);
            }

            public void addField(final JsonFieldBuilder jsonFieldBuilder) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
            }
        });
    }

    public void endField() {
        stack.pop();
    }

    public void numberValue(final String value) {
        addValue(aNumberBuilder(value));
    }

    public void trueValue() {
        addValue(aTrueBuilder());
    }

    public void stringValue(final String value) {
        addValue(aStringBuilder(value));
    }

    public void falseValue() {
        addValue(aFalseBuilder());
    }

    public void nullValue() {
        addValue(aNullBuilder());
    }

    private void addRootNode(final JsonNodeBuilder<JsonRootNode> rootNodeBuilder) {
        if (root == null) {
            root = rootNodeBuilder;
        } else {
            addValue(rootNodeBuilder);
        }
    }

    private void addValue(final JsonNodeBuilder nodeBuilder) {
        stack.peek().addNode(nodeBuilder);
    }

    private static interface NodeContainer {

        void addNode(JsonNodeBuilder jsonNodeBuilder);

        void addField(JsonFieldBuilder jsonFieldBuilder);

    }
}
