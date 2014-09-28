/*
 * Copyright 2011 Mark Slater
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

final class JsonNodeDoesNotMatchChainedJsonNodeSelectorException extends JsonNodeDoesNotMatchJsonNodeSelectorException {

    static JsonNodeDoesNotMatchJsonNodeSelectorException createJsonNodeDoesNotMatchJsonNodeSelectorException(final Functor failedNode) {
        return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(failedNode, new LinkedList<JsonNodeSelector>());
    }

    static JsonNodeDoesNotMatchJsonNodeSelectorException createChainedJsonNodeDoesNotMatchJsonNodeSelectorException(final JsonNodeDoesNotMatchChainedJsonNodeSelectorException e,
                                                                                                                    final JsonNodeSelector parentJsonNodeSelector) {
        final LinkedList<JsonNodeSelector> chainedFailPath = new LinkedList<JsonNodeSelector>(e.failPath);
        chainedFailPath.add(parentJsonNodeSelector);
        return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(e.failedNode, chainedFailPath);
    }

    static JsonNodeDoesNotMatchJsonNodeSelectorException createUnchainedJsonNodeDoesNotMatchJsonNodeSelectorException(final JsonNodeDoesNotMatchChainedJsonNodeSelectorException e,
                                                                                                                      final JsonNodeSelector parentJsonNodeSelector) {
        final LinkedList<JsonNodeSelector> unchainedFailPath = new LinkedList<JsonNodeSelector>();
        unchainedFailPath.add(parentJsonNodeSelector);
        return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(e.failedNode, unchainedFailPath);
    }

    final Functor failedNode;
    final List<JsonNodeSelector> failPath;

    private JsonNodeDoesNotMatchChainedJsonNodeSelectorException(final Functor failedNode, final List<JsonNodeSelector> failPath) {
        super("Failed to match any JSON node at [" + getShortFormFailPath(failPath) + "]");
        this.failedNode = failedNode;
        this.failPath = failPath;
    }

    static String getShortFormFailPath(final List<JsonNodeSelector> failPath) {
        StringBuilder result = new StringBuilder();
        for (int i = failPath.size() - 1; i >= 0; i--) {
            result.append(failPath.get(i).shortForm());
            if (i != 0) {
                result.append(".");
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "JsonNodeDoesNotMatchJsonNodeSelectorException{" +
                "failedNode=" + failedNode +
                ", failPath=" + failPath +
                '}';
    }
}
