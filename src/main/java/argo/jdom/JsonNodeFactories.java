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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Factories for {@code JsonNode}s.
 */
public final class JsonNodeFactories {

    private JsonNodeFactories() {
    }

    /**
     * @return a JSON null
     */
    public static JsonNode nullNode() {
        return JsonConstants.NULL;
    }

    /**
     * @return a JSON true
     */
    public static JsonNode trueNode() {
        return JsonConstants.TRUE;
    }

    /**
     * @return a JSON false
     */
    public static JsonNode falseNode() {
        return JsonConstants.FALSE;
    }

    /**
     * @param value the Java String to represent as a JSON string
     * @return a JSON string representation of the given String
     */
    public static JsonStringNode string(final String value) {
        return new JsonStringNode(value);
    }

    /**
     * @param value a Java String to represent as a JSON number
     * @return a JSON number representation of the given String
     */
    public static JsonNode number(final String value) {
        return new JsonNumberNode(value);
    }

    /**
     * @param value a Java BigDecimal to represent as a JSON number
     * @return a JSON number representation of the given BigDecimal
     */
    public static JsonNode number(final BigDecimal value) {
        return new JsonNumberNode(value.toString());
    }

    /**
     * @param value a Java BigInteger to represent as a JSON number
     * @return a JSON number representation of the given BigInteger
     */
    public static JsonNode number(final BigInteger value) {
        return new JsonNumberNode(value.toString());
    }

    /**
     * @param value a Java long to represent as a JSON number
     * @return a JSON number representation of the given BigInteger
     */
    public static JsonNode number(final long value) {
        return new JsonNumberNode(Long.toString(value));
    }

    /**
     * @param elements {@code JsonNode}s that will populate the array
     * @return a JSON array of the given {@code JsonNode}s
     */
    public static JsonRootNode array(final Iterable<? extends JsonNode> elements) {
        return new JsonArray(elements);
    }

    /**
     * @param elements {@code JsonNode}s that will populate the array
     * @return a JSON array of the given {@code JsonNode}s
     */
    public static JsonRootNode array(final JsonNode... elements) {
        return array(asList(elements));
    }

    /**
     * Generates an array where the members are only evaluated on request.  This means that arrays generated
     * by this method might not be immutable, depending on the implementation of {@code List} used in the
     * argument.  If you supply a list of elements, and then add an item to the list, that item will also be
     * part of the array.
     *
     * @param elements {@code JsonNode}s that will populate the array
     * @return a JSON array of the given {@code JsonNode}s
     */
    public static JsonRootNode lazyArray(final List<? extends JsonNode> elements) {
        return new AbstractJsonArray() {
            @Override
            public List<JsonNode> getElements() {
                return new AbstractList<JsonNode>() {
                    @Override
                    public JsonNode get(int i) {
                        return elements.get(i);
                    }

                    @Override
                    public int size() {
                        return elements.size();
                    }
                };
            }
        };
    }

    /**
     * @param fields {@code JsonField}s that the object will contain
     * @return a JSON object containing the given fields
     */
    public static JsonRootNode object(final Map<JsonStringNode, ? extends JsonNode> fields) {
        return new JsonObject(new ArrayList<JsonField>() {{
            for (final Map.Entry<JsonStringNode, ? extends JsonNode> entry : fields.entrySet()) {
                add(field(entry.getKey(), entry.getValue()));
            }
        }});
    }

    /**
     * @param fields {@code JsonField}s that the object will contain
     * @return a JSON object containing the given fields
     */
    public static JsonRootNode object(final JsonField... fields) {
        return object(asList(fields));
    }

    /**
     * @param fields {@code JsonField}s that the object will contain
     * @return a JSON object containing the given fields
     */
    public static JsonRootNode object(final Iterable<JsonField> fields) {
        return new JsonObject(fields);
    }

    /**
     * Generates an object where the members are only evaluated on request.  This means that objects generated
     * by this method might not be immutable, depending on the implementation of {@code List} used in the
     * argument.  If you supply a list of fields, and then add an item to the list, that item will also be
     * part of the object.
     *
     * @param fields {@code JsonField}s that the object will contain
     * @return a JSON object containing the given fields
     */
    public static JsonRootNode lazyObject(final List<JsonField> fields) {
        return new AbstractJsonObject() {
            @Override
            public Map<JsonStringNode, JsonNode> getFields() {
                final Iterator<JsonField> fieldIterator = fields.iterator();
                return new AbstractMap<JsonStringNode, JsonNode>() {
                    @Override
                    public Set<Entry<JsonStringNode, JsonNode>> entrySet() {
                        return new AbstractSet<Entry<JsonStringNode, JsonNode>>() {
                            @Override
                            public Iterator<Entry<JsonStringNode, JsonNode>> iterator() {
                                return new Iterator<Entry<JsonStringNode, JsonNode>>() {
                                    public boolean hasNext() {
                                        return fieldIterator.hasNext();
                                    }

                                    public Entry<JsonStringNode, JsonNode> next() {
                                        JsonField jsonField = fieldIterator.next();
                                        return new SimpleEntry<JsonStringNode, JsonNode>(jsonField.getName(), jsonField.getValue());
                                    }

                                    public void remove() {
                                        throw new UnsupportedOperationException("Removal not supported");
                                    }
                                };
                            }

                            @Override
                            public int size() {
                                return fields.size();
                            }
                        };
                    }
                };
            }

            @Override
            public List<JsonField> getFieldList() {
                return unmodifiableList(fields);
            }
        };
    }

    /**
     * @param name  the name of the field
     * @param value the value of the field
     * @return a JSON field with the given name and value
     */
    public static JsonField field(final String name, final JsonNode value) {
        return new JsonField(string(name), value);
    }

    /**
     * @param name  the name of the field
     * @param value the value of the field
     * @return a JSON field with the given name and value
     */
    public static JsonField field(final JsonStringNode name, final JsonNode value) {
        return new JsonField(name, value);
    }

    /**
     * @param value the Java boolean to represent as a JSON Boolean
     * @return a JSON Boolean representation of the given boolean
     */
    public static JsonNode booleanNode(final boolean value) {
        return value ? trueNode() : falseNode();
    }
}
