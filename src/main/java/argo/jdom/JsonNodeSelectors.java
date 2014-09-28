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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static argo.jdom.JsonNodeType.*;

/**
 * <p>Factories for {@code JsonNodeSelectors}.</p>
 * <p/>
 * <p>Methods in this class fall into two broad categories - those that take a varargs argument of {@code Object}s
 * and navigate down an entire hierarchy, and those that address a single node in a hierarchy.</p>
 * <p>For example,
 * {@code aStringNode(Object... pathElements)} takes a series of {@code String}s and
 * {@code Integer}s as its argument which tell it how to navigate down a hierarchy to a particular JSON string.
 * The {@code String}s tell it to select a field with the given name from an object, and the {@code Integer}s
 * tell it to select an element with the given index from an array.</p>
 * <p/>
 * <p>By contrast, {@code anArrayNodeWithElement(int index)} addresses a single array node only, selecting the
 * element at the given index from it.</p>
 */
public final class JsonNodeSelectors {

    private JsonNodeSelectors() {
    }

    public static JsonNodeSelector<JsonNode, JsonNode> anyNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, JsonNode>(new LeafFunctor<JsonNode, JsonNode>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return true;
            }

            public String shortForm() {
                return "A node";
            }

            public JsonNode typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode;
            }

            @Override
            public String toString() {
                return ("any node");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, JsonRootNode> anyRootNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, JsonRootNode>(new LeafFunctor<JsonNode, JsonRootNode>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return ARRAY == jsonNode.getType() || OBJECT == jsonNode.getType();
            }

            public String shortForm() {
                return "A root node";
            }

            public JsonRootNode typeSafeApplyTo(final JsonNode jsonNode) {
                return (JsonRootNode) jsonNode;
            }

            @Override
            public String toString() {
                return ("any root node");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, String> aStringNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, String>(new LeafFunctor<JsonNode, String>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return STRING == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form string";
            }

            public String typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode.getText();
            }

            @Override
            public String toString() {
                return ("a value that is a string");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, String> aNullableStringNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, String>(new LeafFunctor<JsonNode, String>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return STRING == jsonNode.getType() || NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form nullable string";
            }

            public String typeSafeApplyTo(final JsonNode jsonNode) {
                return NULL == jsonNode.getType() ? null : jsonNode.getText();
            }

            @Override
            public String toString() {
                return ("a value that is a string");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, String> aNumberNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, String>(new LeafFunctor<JsonNode, String>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return NUMBER == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form nullable number";
            }

            public String typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode.getText();
            }

            @Override
            public String toString() {
                return ("a value that is a number");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, String> aNullableNumberNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, String>(new LeafFunctor<JsonNode, String>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return NUMBER == jsonNode.getType() || NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form nullable number";
            }

            public String typeSafeApplyTo(final JsonNode jsonNode) {
                return NULL == jsonNode.getType() ? null : jsonNode.getText();
            }

            @Override
            public String toString() {
                return ("a value that is a number");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, Boolean> aBooleanNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, Boolean>(new LeafFunctor<JsonNode, Boolean>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return TRUE == jsonNode.getType() || FALSE == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form boolean";
            }

            public Boolean typeSafeApplyTo(final JsonNode jsonNode) {
                return TRUE == jsonNode.getType();
            }

            @Override
            public String toString() {
                return ("a true or false");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, Boolean> aNullableBooleanNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, Boolean>(new LeafFunctor<JsonNode, Boolean>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return TRUE == jsonNode.getType() || FALSE == jsonNode.getType() || NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form nullable boolean";
            }

            public Boolean typeSafeApplyTo(final JsonNode jsonNode) {
                final Boolean result;
                if (TRUE == jsonNode.getType()) {
                    result = Boolean.TRUE;
                } else if (FALSE == jsonNode.getType()) {
                    result = Boolean.FALSE;
                } else {
                    result = null;
                }
                return result;
            }

            @Override
            public String toString() {
                return ("a true or false");
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, JsonNode> aNullNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, JsonNode>(new LeafFunctor<JsonNode, JsonNode>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return argo.jdom.JsonNodeType.NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form null";
            }

            public JsonNode typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode;
            }

            @Override
            public String toString() {
                return "a null";
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, List<JsonNode>> anArrayNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, List<JsonNode>>(new LeafFunctor<JsonNode, List<JsonNode>>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return ARRAY == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form array";
            }

            public List<JsonNode> typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode.getElements();
            }

            @Override
            public String toString() {
                return "an array";
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, List<JsonNode>> aNullableArrayNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, List<JsonNode>>(new LeafFunctor<JsonNode, List<JsonNode>>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return ARRAY == jsonNode.getType() || NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form array";
            }

            public List<JsonNode> typeSafeApplyTo(final JsonNode jsonNode) {
                final List<JsonNode> result;
                if (ARRAY == jsonNode.getType()) {
                    result = jsonNode.getElements();
                } else {
                    result = null;
                }
                return result;
            }

            @Override
            public String toString() {
                return "an array or null";
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, Map<JsonStringNode, JsonNode>> anObjectNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, Map<JsonStringNode, JsonNode>>(new LeafFunctor<JsonNode, Map<JsonStringNode, JsonNode>>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return OBJECT == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form object";
            }

            public Map<JsonStringNode, JsonNode> typeSafeApplyTo(final JsonNode jsonNode) {
                return jsonNode.getFields();
            }

            @Override
            public String toString() {
                return "an object";
            }
        }));
    }

    public static JsonNodeSelector<JsonNode, Map<JsonStringNode, JsonNode>> aNullableObjectNode(final Object... pathElements) {
        return chainOn(pathElements, new JsonNodeSelector<JsonNode, Map<JsonStringNode, JsonNode>>(new LeafFunctor<JsonNode, Map<JsonStringNode, JsonNode>>() {
            public boolean matchesNode(final JsonNode jsonNode) {
                return OBJECT == jsonNode.getType() || NULL == jsonNode.getType();
            }

            public String shortForm() {
                return "A short form nullable object";
            }

            public Map<JsonStringNode, JsonNode> typeSafeApplyTo(final JsonNode jsonNode) {
                final Map<JsonStringNode, JsonNode> result;
                if (OBJECT == jsonNode.getType()) {
                    result = jsonNode.getFields();
                } else {
                    result = null;
                }
                return result;
            }


            @Override
            public String toString() {
                return "an object or null";
            }
        }));
    }

    public static JsonNodeSelector<Map<JsonStringNode, JsonNode>, JsonNode> aField(final String fieldName) {
        return aField(JsonNodeFactories.string(fieldName));
    }

    public static JsonNodeSelector<Map<JsonStringNode, JsonNode>, JsonNode> aField(final JsonStringNode fieldName) {
        return new JsonNodeSelector<Map<JsonStringNode, JsonNode>, JsonNode>(new LeafFunctor<Map<JsonStringNode, JsonNode>, JsonNode>() {
            public boolean matchesNode(Map<JsonStringNode, JsonNode> jsonNode) {
                return jsonNode.containsKey(fieldName);
            }

            public String shortForm() {
                return "\"" + fieldName.getText() + "\"";
            }

            public JsonNode typeSafeApplyTo(Map<JsonStringNode, JsonNode> jsonNode) {
                return jsonNode.get(fieldName);
            }

            @Override
            public String toString() {
                return "a field called [\"" + fieldName.getText() + "\"]";
            }
        });
    }

    public static JsonNodeSelector<JsonNode, JsonNode> anObjectNodeWithField(final JsonStringNode fieldName) {
        return anObjectNode().with(aField(fieldName));
    }

    public static JsonNodeSelector<JsonNode, JsonNode> anObjectNodeWithField(final String fieldName) {
        return anObjectNode().with(aField(fieldName));
    }

    public static JsonNodeSelector<List<JsonNode>, JsonNode> anElement(final int index) {
        return new JsonNodeSelector<List<JsonNode>, JsonNode>(new LeafFunctor<List<JsonNode>, JsonNode>() {
            public boolean matchesNode(final List<JsonNode> jsonNode) {
                return jsonNode.size() > index;
            }

            public String shortForm() {
                return Integer.toString(index);
            }

            public JsonNode typeSafeApplyTo(final List<JsonNode> jsonNode) {
                return jsonNode.get(index);
            }

            @Override
            public String toString() {
                return "an element at index [" + index + "]";
            }
        });
    }

    public static JsonNodeSelector<JsonNode, JsonNode> anArrayNodeWithElement(final int index) {
        return anArrayNode().with(anElement(index));
    }

    private static <T> JsonNodeSelector<JsonNode, T> chainOn(final Object[] pathElements, JsonNodeSelector<JsonNode, T> parentSelector) {
        JsonNodeSelector<JsonNode, T> result = parentSelector;
        for (int i = pathElements.length - 1; i >= 0; i--) {
            if (pathElements[i] instanceof Integer) {
                result = chainedJsonNodeSelector(anArrayNodeWithElement((Integer) pathElements[i]), result);
            } else if (pathElements[i] instanceof String) {
                result = chainedJsonNodeSelector(anObjectNodeWithField((String) pathElements[i]), result);
            } else {
                throw new IllegalArgumentException("Element [" + pathElements[i] + "] of path elements" +
                        " [" + Arrays.toString(pathElements) + "] was of illegal type [" + pathElements[i].getClass().getCanonicalName()
                        + "]; only Integer and String are valid.");
            }
        }
        return result;
    }

    private static <T, U, V> JsonNodeSelector<T, V> chainedJsonNodeSelector(final JsonNodeSelector<T, U> parent, final JsonNodeSelector<U, V> child) {
        return new JsonNodeSelector<T, V>(new ChainedFunctor<T, U, V>(parent, child));
    }
}
