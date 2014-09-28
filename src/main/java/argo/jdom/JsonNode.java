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

import static argo.jdom.JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException;

/**
 * <p>A node (leaf or otherwise) in a JSON document.</p>
 * <p/>
 * <p>Supplies methods for examining the node, and also examining and navigating the hierarchy at and below this node.
 * Methods for navigating the hierarchy are of the form {@code getXXXValue(Object... pathElements)}.</p>
 * <p/>
 * <p>For example, {@link #getStringValue(Object...)} takes a series of {@code String}s and
 * {@code Integer}s as its argument which tell it how to navigate down a hierarchy to a particular JSON string.
 * The {@code String}s tell it to select a field with the given name from an object, and the {@code Integer}s
 * tell it to select an element with the given index from an array.</p> If no field of that name exists, or the field
 * exists, but it isn't a JSON string, an {@code IllegalArgumentException} is thrown.</p>
 * <p/>
 * <p>Methods for examining the hierarchy work on the same principal as the
 * {@code getXXXValue(Object... pathElements)} methods, but return a {@code boolean} indicating whether
 * or not the element at the given path exists and is of the type specified, for example,
 * {@code getStringValue("my field")} returns {@code true} if the node has a field called
 * "{@code my field}", and its value is a JSON string.</p>
 */
public abstract class JsonNode {

    // Only extensible by classes in this package
    JsonNode() {
    }

    public abstract JsonNodeType getType();

    public abstract boolean hasText();

    /**
     * @return the text associated with this node
     * @throws IllegalStateException if hasText() returns false, indicating this type of node doesn't have text.
     */
    public abstract String getText();

    public abstract boolean hasFields();

    /**
     * Gets the fields associated with this node as a map of name to value.  Note that JSON permits
     * duplicated keys in an object, though in practice this is rare, and in this case, this method
     * will return a map containing a single entry of each unique key.
     *
     * @return the fields associated with this node
     * @throws IllegalStateException if hasFields() returns false, indicating this type of node doesn't support fields.
     */
    public abstract Map<JsonStringNode, JsonNode> getFields();

    /**
     * Gets the fields associated with this node as a list of {@code JsonFields}.  This method allows
     * the retrieval of all fields in an object even when the fields have duplicate keys.  This method
     * also preserves the order of the fields.
     *
     * @return the fields associated with this node
     * @throws IllegalStateException if hasFields() returns false, indicating this type of node doesn't support fields.
     */
    public abstract List<JsonField> getFieldList();

    public abstract boolean hasElements();

    /**
     * @return the elements associated with this node
     * @throws IllegalStateException if hasElements() returns false, indicating this type of node doesn't support elements.
     */
    public abstract List<JsonNode> getElements();

    /**
     * Determines whether the node at the given path exists.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON node exists at the path given.
     */
    public boolean isNode(final Object... pathElements) {
        return JsonNodeSelectors.anyNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code JsonNode} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code JsonNode} at the path given.
     * @throws IllegalArgumentException if there is no node at the given path.
     */
    public JsonNode getNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.anyNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a root type - either a JSON object or JSON array.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON root node exists at the path given.
     */
    public boolean isRootNode(final Object... pathElements) {
        return JsonNodeSelectors.anyRootNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code JsonRootNode} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code JsonRootNode} at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON root node.
     */
    public JsonRootNode getRootNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.anyRootNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON boolean.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON boolean exists at the path given.
     */
    public final boolean isBooleanValue(final Object... pathElements) {
        return JsonNodeSelectors.aBooleanNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code Boolean} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code Boolean} at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON boolean.
     */
    public final Boolean getBooleanValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aBooleanNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON boolean or a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON boolean or a JSON null exists at the path given.
     */
    public final boolean isNullableBooleanValue(final Object... pathElements) {
        return JsonNodeSelectors.aNullableBooleanNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code Boolean} or {@code null} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code Boolean} at the path given, or null, if there is a JSON null at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON boolean or a JSON null.
     */
    public final Boolean getNullableBooleanValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullableBooleanNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON string.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON string exists at the path given.
     */
    public final boolean isStringValue(final Object... pathElements) {
        return JsonNodeSelectors.aStringNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code String} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code String} at the path given
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON string.
     */
    public final String getStringValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aStringNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON string or a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON string or a JSON null exists at the path given.
     */
    public final boolean isNullableStringValue(final Object... pathElements) {
        return JsonNodeSelectors.aNullableStringNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code String} or {@code null} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the {@code String} at the path given, or null, if there is a JSON null at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON string or a JSON null.
     */
    public final String getNullableStringValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullableStringNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON number.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON number exists at the path given.
     */
    public final boolean isNumberValue(final Object... pathElements) {
        return JsonNodeSelectors.aNumberNode(pathElements).matches(this);
    }

    /**
     * Gets a numeric {@code String} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the numeric {@code String} at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON number.
     */
    public final String getNumberValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNumberNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON number or a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON number or a JSON null exists at the path given.
     */
    public final boolean isNullableNumberNode(final Object... pathElements) {
        return JsonNodeSelectors.aNullableNumberNode(pathElements).matches(this);
    }

    /**
     * Gets a numeric {@code String} or {@code null} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return the numeric {@code String} at the path given, or null, if there is a JSON null at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON number or a JSON null.
     */
    public final String getNullableNumberValue(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullableNumberNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON null exists at the path given.
     */
    public final boolean isNullNode(final Object... pathElements) {
        return JsonNodeSelectors.aNullNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code JsonNode} representing null by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return a {@code JsonNode} representing null.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON null.
     */
    public final JsonNode getNullNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON object.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON object exists at the path given.
     */
    public final boolean isObjectNode(final Object... pathElements) {
        return JsonNodeSelectors.anObjectNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code Map} of {@code String} field names to {@code JsonNode}s, representing a JSON object, by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return a {@code Map} of {@code String} field names to {@code JsonNode}s representing a JSON object.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON object.
     */
    public final Map<JsonStringNode, JsonNode> getObjectNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.anObjectNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON object or a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON object or a JSON null exists at the path given.
     */
    public final boolean isNullableObjectNode(final Object... pathElements) {
        return JsonNodeSelectors.aNullableObjectNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code Map} of {@code String} field names to {@code JsonNode}s, representing a JSON object, or {@code null} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return a {@code Map} of {@code String} field names to {@code JsonNode}s representing a JSON object at the path given, or null, if there is a JSON null at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON object or a JSON null.
     */
    public final Map<JsonStringNode, JsonNode> getNullableObjectNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullableObjectNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON array.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON array exists at the path given.
     */
    public final boolean isArrayNode(final Object... pathElements) {
        return JsonNodeSelectors.anArrayNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code List} of {@code JsonNode}s, representing a JSON array, by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return a {@code List} of {@code JsonNode}s representing a JSON array.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON array.
     */
    public final List<JsonNode> getArrayNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.anArrayNode(pathElements), this, pathElements);
    }

    /**
     * Determines whether the node at the given path exists and is a JSON array or a JSON null.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return whether a JSON array or a JSON null exists at the path given.
     */
    public final boolean isNullableArrayNode(final Object... pathElements) {
        return JsonNodeSelectors.aNullableArrayNode(pathElements).matches(this);
    }

    /**
     * Gets a {@code List} of {@code JsonNode}s, representing a JSON array, or {@code null} by navigating the hierarchy below this node.
     *
     * @param pathElements a series of {@code String}s, representing the names of fields on objects, and {@code Integer}s, representing elements of arrays indicating how to navigate from this node.
     * @return a {@code List} of {@code JsonNode}s representing a JSON array, or null, if there is a JSON null at the path given.
     * @throws IllegalArgumentException if there is no node at the given path, or the node at the given path is not a JSON array or a JSON null.
     */
    public final List<JsonNode> getNullableArrayNode(final Object... pathElements) {
        return wrapExceptionsFor(JsonNodeSelectors.aNullableArrayNode(pathElements), this, pathElements);
    }

    private <T, V extends JsonNode> T wrapExceptionsFor(final JsonNodeSelector<V, T> value, final V node, final Object[] pathElements) throws JsonNodeDoesNotMatchPathElementsException {
        try {
            return value.getValue(node);
        } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException e) {
            throw jsonNodeDoesNotMatchPathElementsException(e, pathElements, JsonNodeFactories.array(node));
        }
    }

}