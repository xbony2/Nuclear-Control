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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JsonEscapedString {

    private static final Pattern ESCAPE_CHARACTERS_PATTERN = Pattern.compile("(\\\\|\\\"|\b|\\f|\\n|\\r|\\t)");

    private JsonEscapedString() {
    }

    static String escapeString(final String unescapedString) {
        final Matcher matcher = ESCAPE_CHARACTERS_PATTERN.matcher(unescapedString);
        final StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            final String match = matcher.group();
            if ("\\".equals(match)) {
                matcher.appendReplacement(sb, "\\\\\\\\");
            } else if ("\"".equals(match)) {
                matcher.appendReplacement(sb, "\\\\\\\"");
            } else if ("\b".equals(match)) {
                matcher.appendReplacement(sb, "\\\\b");
            } else if ("\f".equals(match)) {
                matcher.appendReplacement(sb, "\\\\f");
            } else if ("\n".equals(match)) {
                matcher.appendReplacement(sb, "\\\\n");
            } else if ("\r".equals(match)) {
                matcher.appendReplacement(sb, "\\\\r");
            } else if ("\t".equals(match)) {
                matcher.appendReplacement(sb, "\\\\t");
            } else {
                throw new RuntimeException("Coding failure in Argo: JSON String escaping matched a character [" + match + "] for which no replacement is defined.");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
