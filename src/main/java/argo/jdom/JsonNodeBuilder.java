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

/**
 * Classes that implement {@code JsonNodeBuilder} provide methods for specifying details of a {@code JsonNode} and then constructing it.
 *
 * @param <T> the type of {@code JsonNode} that will be built.
 */
public interface JsonNodeBuilder<T extends JsonNode> {

    /**
     * Generates an instance of {@code T}, as specified by calls to any other methods provided by implementations of {@code JsonNodeBuilder}.
     *
     * @return an instance of {@code T}.
     */
    T build();
}
