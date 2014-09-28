/*
 * Copyright 2014 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.format;

import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import static argo.format.JsonEscapedString.escapeString;

/**
 * JsonFormat that formats JSON in a human-readable form.  Instances of this class can safely be shared between threads.
 */
public final class PrettyJsonFormatter implements JsonFormatter {

    private final FieldSorter fieldSorter;

    /**
     * Constructs a {@code JsonFormatter} that formats JSON in a human-readable form, outputting the fields of objects in the order they were defined.
     */
    public PrettyJsonFormatter() {
        this(FieldSorter.DO_NOTHING_FIELD_SORTER);
    }

    private PrettyJsonFormatter(final FieldSorter fieldSorter) {
        this.fieldSorter = fieldSorter;
    }

    /**
     * Gets a {@code JsonFormatter} that formats JSON in a human-readable form, outputting the fields of objects in the order they were defined.
     *
     * @return a {@code JsonFormatter} that formats JSON in a human-readable form, outputting the fields of objects in the order they were defined.
     */
    public static PrettyJsonFormatter fieldOrderPreservingPrettyJsonFormatter() {
        return new PrettyJsonFormatter();
    }

    /**
     * Gets a {@code JsonFormatter} that formats JSON in a human-readable form, outputting the fields of objects in alphabetic order.
     *
     * @return a {@code JsonFormatter} that formats JSON in a human-readable form, outputting the fields of objects in alphabetic order.
     */
    public static PrettyJsonFormatter fieldOrderNormalisingPrettyJsonFormatter() {
        return new PrettyJsonFormatter(FieldSorter.ALPHABETIC_FIELD_SORTER);
    }

    public String format(final JsonRootNode jsonNode) {
        final StringWriter stringWriter = new StringWriter();
        try {
            format(jsonNode, stringWriter);
        } catch (final IOException e) {
            throw new RuntimeException("Coding failure in Argo:  StringWriter threw an IOException", e);
        }
        return stringWriter.toString();
    }

    public void format(final JsonRootNode jsonNode, final Writer writer) throws IOException {
        formatJsonNode(jsonNode, new PrintWriter(writer), 0);
    }

    private void formatJsonNode(final JsonNode jsonNode, final PrintWriter writer, final int indent) throws IOException {
        switch (jsonNode.getType()) {
            case ARRAY:
                writer.append('[');
                final Iterator<JsonNode> elements = jsonNode.getElements().iterator();
                while (elements.hasNext()) {
                    final JsonNode node = elements.next();
                    writer.println();
                    addTabs(writer, indent + 1);
                    formatJsonNode(node, writer, indent + 1);
                    if (elements.hasNext()) {
                        writer.append(",");
                    }
                }
                if (!jsonNode.getElements().isEmpty()) {
                    writer.println();
                    addTabs(writer, indent);
                }
                writer.append(']');
                break;
            case OBJECT:
                writer.append('{');
                final Iterator<JsonField> jsonStringNodes = fieldSorter.sort(jsonNode.getFieldList()).iterator();
                while (jsonStringNodes.hasNext()) {
                    final JsonField field = jsonStringNodes.next();
                    writer.println();
                    addTabs(writer, indent + 1);
                    formatJsonNode(field.getName(), writer, indent + 1);
                    writer.append(": ");
                    formatJsonNode(field.getValue(), writer, indent + 1);
                    if (jsonStringNodes.hasNext()) {
                        writer.append(",");
                    }
                }
                if (!jsonNode.getFieldList().isEmpty()) {
                    writer.println();
                    addTabs(writer, indent);
                }
                writer.append('}');
                break;
            case STRING:
                writer.append('"')
                        .append(escapeString(jsonNode.getText()))
                        .append('"');
                break;
            case NUMBER:
                writer.append(jsonNode.getText());
                break;
            case FALSE:
                writer.append("false");
                break;
            case TRUE:
                writer.append("true");
                break;
            case NULL:
                writer.append("null");
                break;
            default:
                throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + jsonNode.getType() + "];");
        }
    }

    private void addTabs(final PrintWriter writer, final int tabs) {
        for (int i = 0; i < tabs; i++) {
            writer.write('\t');
        }
    }

}
