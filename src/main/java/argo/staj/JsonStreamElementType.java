/*
 * Copyright 2013 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.staj;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Stack;

import static argo.staj.InvalidSyntaxRuntimeException.*;
import static argo.staj.JsonStreamElement.*;

/**
 * Types of element a {@code StajParser} can produce.
 */
public enum JsonStreamElementType {
    START_ARRAY {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            final char secondChar = (char) readNextNonWhitespaceChar(pushbackReader);
            if (secondChar != ']') {
                pushbackReader.unread(secondChar);
                return aJsonValue(pushbackReader, stack);
            }
            stack.pop();
            return endArray();
        }
    },
    END_ARRAY {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromTheEndOfARootNode(pushbackReader, stack);
        }
    },
    START_OBJECT {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFieldOrObjectEnd(pushbackReader, stack);
        }
    },
    END_OBJECT {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromTheEndOfARootNode(pushbackReader, stack);
        }
    },
    START_FIELD {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            final char separatorChar = (char) readNextNonWhitespaceChar(pushbackReader);
            if (separatorChar != ':') {
                throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected object identifier to be followed by :", separatorChar, pushbackReader);
            }
            return aJsonValue(pushbackReader, stack);
        }
    },
    END_FIELD {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFieldOrObjectEnd(pushbackReader, stack);
        }
    },
    STRING {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromEndOfNode(pushbackReader, stack);
        }
    },
    TRUE {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromEndOfNode(pushbackReader, stack);
        }
    },
    FALSE {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromEndOfNode(pushbackReader, stack);
        }
    },
    NULL {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromEndOfNode(pushbackReader, stack);
        }
    },
    NUMBER {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            return parseFromEndOfNode(pushbackReader, stack);
        }
    },
    START_DOCUMENT {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            final char nextChar = (char) pushbackReader.read();
            switch (nextChar) {
                case '{':
                    stack.push(START_OBJECT);
                    return startObject();
                case '[':
                    stack.push(START_ARRAY);
                    return startArray();
                default:
                    throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected either [ or {", nextChar, pushbackReader);
            }
        }
    },
    END_DOCUMENT {
        @Override
        JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
            throw new NoSuchElementException("Document complete");
        }
    };

    abstract JsonStreamElement parseNext(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack);

    static JsonStreamElement parseFirstElement(final PositionTrackingPushbackReader pushbackReader) throws InvalidSyntaxRuntimeException {
        final char nextChar = (char) pushbackReader.read();
        if (nextChar == '{' || nextChar == '[') {
            pushbackReader.unread(nextChar);
            return startDocument();
        } else {
            throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected either [ or {", nextChar, pushbackReader);
        }
    }

    private static final char DOUBLE_QUOTE = '"';
    private static final char BACK_SLASH = '\\';
    private static final char BACKSPACE = '\b';
    private static final char TAB = '\t';
    private static final char NEWLINE = '\n';
    private static final char CARRIAGE_RETURN = '\r';
    private static final char FORM_FEED = '\f';

    private static JsonStreamElement parseFieldOrObjectEnd(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
        final char nextChar = (char) readNextNonWhitespaceChar(pushbackReader);
        if (nextChar != '}') {
            pushbackReader.unread(nextChar);
            return aFieldToken(pushbackReader, stack);
        }
        stack.pop();
        return endObject();
    }

    private static JsonStreamElement parseFromTheEndOfARootNode(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
        final int nextChar = readNextNonWhitespaceChar(pushbackReader);
        if (stack.isEmpty()) {
            if (nextChar != -1) {
                throw invalidSyntaxRuntimeException("Got unexpected trailing character [" + (char) nextChar + "].", pushbackReader);
            }
            return endDocument();
        } else {
            pushbackReader.unread((char) nextChar);
            return parseFromEndOfNode(pushbackReader, stack);
        }
    }

    private static JsonStreamElement parseFromEndOfNode(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
        final int nextChar = readNextNonWhitespaceChar(pushbackReader);
        final JsonStreamElementType peek = stack.peek();
        if (peek.equals(START_OBJECT)) {
            switch (nextChar) {
                case ',':
                    return aJsonValue(pushbackReader, stack);
                case '}':
                    stack.pop();
                    return endObject();
                default:
                    throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected either , or }", (char) nextChar, pushbackReader);
            }
        } else if (peek.equals(START_ARRAY)) {
            switch (nextChar) {
                case ',':
                    return aJsonValue(pushbackReader, stack);
                case ']':
                    stack.pop();
                    return endArray();
                default:
                    throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected either , or ]", (char) nextChar, pushbackReader);
            }
        } else {
            switch (nextChar) {
                case ',':
                    stack.pop();
                    return endField();
                case '}':
                    stack.pop();
                    pushbackReader.unread((char) nextChar);
                    return endField();
                default:
                    throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected either , or ]", (char) nextChar, pushbackReader);
            }
        }
    }

    private static int readNextNonWhitespaceChar(final PositionTrackingPushbackReader in) {
        int nextChar;
        boolean gotNonWhitespace = false;
        do {
            nextChar = in.read();
            switch (nextChar) {
                case ' ':
                case TAB:
                case NEWLINE:
                case CARRIAGE_RETURN:
                    break;
                default:
                    gotNonWhitespace = true;
            }
        } while (!gotNonWhitespace);
        return nextChar;
    }

    private static JsonStreamElement aJsonValue(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
        final char nextChar = (char) readNextNonWhitespaceChar(pushbackReader);
        switch (nextChar) {
            case '"':
                pushbackReader.unread(nextChar);
                return string(stringToken(pushbackReader));
            case 't':
                final char[] remainingTrueTokenCharacters = new char[3];
                final int trueTokenCharactersRead = pushbackReader.read(remainingTrueTokenCharacters);
                if (trueTokenCharactersRead != 3 || remainingTrueTokenCharacters[0] != 'r' || remainingTrueTokenCharacters[1] != 'u' || remainingTrueTokenCharacters[2] != 'e') {
                    pushbackReader.uncount(remainingTrueTokenCharacters);
                    throw invalidSyntaxRuntimeException("Expected 't' to be followed by [[r, u, e]], but got [" + Arrays.toString(Arrays.copyOf(remainingTrueTokenCharacters, trueTokenCharactersRead)) + "].", pushbackReader);
                } else {
                    return trueValue();
                }
            case 'f':
                final char[] remainingFalseTokenCharacters = new char[4];
                final int falseTokenCharactersRead = pushbackReader.read(remainingFalseTokenCharacters);
                if (falseTokenCharactersRead != 4 || remainingFalseTokenCharacters[0] != 'a' || remainingFalseTokenCharacters[1] != 'l' || remainingFalseTokenCharacters[2] != 's' || remainingFalseTokenCharacters[3] != 'e') {
                    pushbackReader.uncount(remainingFalseTokenCharacters);
                    throw invalidSyntaxRuntimeException("Expected 'f' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(Arrays.copyOf(remainingFalseTokenCharacters, falseTokenCharactersRead)) + "].", pushbackReader);
                } else {
                    return falseValue();
                }
            case 'n':
                final char[] remainingNullTokenCharacters = new char[3];
                final int nullTokenCharactersRead = pushbackReader.read(remainingNullTokenCharacters);
                if (nullTokenCharactersRead != 3 || remainingNullTokenCharacters[0] != 'u' || remainingNullTokenCharacters[1] != 'l' || remainingNullTokenCharacters[2] != 'l') {
                    pushbackReader.uncount(remainingNullTokenCharacters);
                    throw invalidSyntaxRuntimeException("Expected 'n' to be followed by [[u, l, l]], but got [" + Arrays.toString(Arrays.copyOf(remainingNullTokenCharacters, nullTokenCharactersRead)) + "].", pushbackReader);
                } else {
                    return nullValue();
                }
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                pushbackReader.unread(nextChar);
                return number(numberToken(pushbackReader));
            case '{':
                stack.push(START_OBJECT);
                return startObject();
            case '[':
                stack.push(START_ARRAY);
                return startArray();
            default:
                throw invalidSyntaxRuntimeException(END_OF_STREAM == nextChar ? "Unexpectedly reached end of input at start of value." : "Invalid character at start of value [" + nextChar + "].", pushbackReader);
        }
    }

    private static JsonStreamElement aFieldToken(final PositionTrackingPushbackReader pushbackReader, final Stack<JsonStreamElementType> stack) {
        final char nextChar = (char) readNextNonWhitespaceChar(pushbackReader);
        if (DOUBLE_QUOTE != nextChar) {
            throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected object identifier to begin with [\"]", nextChar, pushbackReader);
        }
        pushbackReader.unread(nextChar);
        stack.push(START_FIELD);
        return startField(stringToken(pushbackReader));
    }

    private static String stringToken(final PositionTrackingPushbackReader in) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) in.read();
        if (DOUBLE_QUOTE != firstChar) {
            throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected [" + DOUBLE_QUOTE + "]", firstChar, in);
        }
        final ThingWithPosition openDoubleQuotesPosition = in.snapshotOfPosition();
        boolean stringClosed = false;
        while (!stringClosed) {
            final int nextInt = in.read();
            if (-1 == nextInt) {
                throw invalidSyntaxRuntimeException("Got opening [" + DOUBLE_QUOTE + "] without matching closing [" + DOUBLE_QUOTE + "]", openDoubleQuotesPosition);
            }
            final char nextChar = (char) nextInt;
            switch (nextChar) {
                case DOUBLE_QUOTE:
                    stringClosed = true;
                    break;
                case BACK_SLASH:
                    final char escapedChar = escapedStringChar(in);
                    result.append(escapedChar);
                    break;
                default:
                    result.append(nextChar);
            }
        }
        return result.toString();
    }

    private static char escapedStringChar(final PositionTrackingPushbackReader in) {
        final char result;
        final char firstChar = (char) in.read();
        switch (firstChar) {
            case DOUBLE_QUOTE:
                result = DOUBLE_QUOTE;
                break;
            case BACK_SLASH:
                result = BACK_SLASH;
                break;
            case '/':
                result = '/';
                break;
            case 'b':
                result = BACKSPACE;
                break;
            case 'f':
                result = FORM_FEED;
                break;
            case 'n':
                result = NEWLINE;
                break;
            case 'r':
                result = CARRIAGE_RETURN;
                break;
            case 't':
                result = TAB;
                break;
            case 'u':
                result = (char) hexadecimalNumber(in);
                break;
            default:
                throw invalidSyntaxRuntimeException(END_OF_STREAM == firstChar ? "Unexpectedly reached end of input during escaped character." : "Unrecognised escape character [" + firstChar + "].", in);
        }
        return result;
    }

    private static int hexadecimalNumber(final PositionTrackingPushbackReader in) {
        final char[] resultCharArray = new char[4];
        final int readSize = in.read(resultCharArray);
        if (readSize != 4) {
            throw invalidSyntaxRuntimeException("Expected a 4 digit hexadecimal number but got only [" + readSize + "], namely [" + String.valueOf(resultCharArray, 0, readSize) + "].", in);
        }
        int result;
        try {
            result = Integer.parseInt(String.valueOf(resultCharArray), 16);
        } catch (final NumberFormatException e) {
            in.uncount(resultCharArray);
            throw invalidSyntaxRuntimeException("Unable to parse [" + String.valueOf(resultCharArray) + "] as a hexadecimal number.", e, in);
        }
        return result;
    }

    private static String numberToken(final PositionTrackingPushbackReader in) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) in.read();
        if ('-' == firstChar) {
            result.append('-');
        } else {
            in.unread(firstChar);
        }
        result.append(nonNegativeNumberToken(in));
        return result.toString();
    }

    private static String nonNegativeNumberToken(final PositionTrackingPushbackReader in) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) in.read();
        if ('0' == firstChar) {
            result.append('0');
            result.append(possibleFractionalComponent(in));
            result.append(possibleExponent(in));
        } else {
            in.unread(firstChar);
            result.append(nonZeroDigitToken(in));
            result.append(digitString(in));
            result.append(possibleFractionalComponent(in));
            result.append(possibleExponent(in));
        }
        return result.toString();
    }

    private static char nonZeroDigitToken(final PositionTrackingPushbackReader in) {
        final char result;
        final char nextChar = (char) in.read();
        switch (nextChar) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                result = nextChar;
                break;
            default:
                throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected a digit 1 - 9", nextChar, in);
        }
        return result;
    }

    private static char digitToken(final PositionTrackingPushbackReader in) {
        final char result;
        final char nextChar = (char) in.read();
        switch (nextChar) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                result = nextChar;
                break;
            default:
                throw unexpectedCharacterInvalidSyntaxRuntimeException("Expected a digit 1 - 9", nextChar, in);
        }
        return result;
    }

    private static String digitString(final PositionTrackingPushbackReader in) {
        final StringBuilder result = new StringBuilder();
        boolean gotANonDigit = false;
        while (!gotANonDigit) {
            final char nextChar = (char) in.read();
            switch (nextChar) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    result.append(nextChar);
                    break;
                default:
                    gotANonDigit = true;
                    in.unread(nextChar);
            }
        }
        return result.toString();
    }

    private static String possibleFractionalComponent(final PositionTrackingPushbackReader pushbackReader) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) pushbackReader.read();
        if (firstChar == '.') {
            result.append('.');
            result.append(digitToken(pushbackReader));
            result.append(digitString(pushbackReader));
        } else {
            pushbackReader.unread(firstChar);
        }
        return result.toString();
    }

    private static String possibleExponent(final PositionTrackingPushbackReader pushbackReader) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) pushbackReader.read();
        switch (firstChar) {
            case '.':
            case 'E':
                result.append('E');
                result.append(possibleSign(pushbackReader));
                result.append(digitToken(pushbackReader));
                result.append(digitString(pushbackReader));
                break;
            case 'e':
                result.append('e');
                result.append(possibleSign(pushbackReader));
                result.append(digitToken(pushbackReader));
                result.append(digitString(pushbackReader));
                break;
            default:
                pushbackReader.unread(firstChar);
                break;
        }
        return result.toString();
    }

    private static String possibleSign(final PositionTrackingPushbackReader pushbackReader) {
        final StringBuilder result = new StringBuilder();
        final char firstChar = (char) pushbackReader.read();
        if (firstChar == '+' || firstChar == '-') {
            result.append(firstChar);
        } else {
            pushbackReader.unread(firstChar);
        }
        return result.toString();
    }

}
