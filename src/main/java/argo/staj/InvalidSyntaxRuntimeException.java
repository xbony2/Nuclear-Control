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

import argo.saj.InvalidSyntaxException;

/**
 * Thrown to indicate a given character stream is not valid JSON.
 */
public abstract class InvalidSyntaxRuntimeException extends RuntimeException {

    static final char END_OF_STREAM = (char) -1;

    private final int column;
    private final int row;

    private InvalidSyntaxRuntimeException(final String s, final ThingWithPosition thingWithPosition) {
        super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s);
        this.column = thingWithPosition.getColumn();
        this.row = thingWithPosition.getRow();
    }

    private InvalidSyntaxRuntimeException(final String s, final Throwable throwable, final ThingWithPosition thingWithPosition) {
        super("At line " + thingWithPosition.getRow() + ", column " + thingWithPosition.getColumn() + ":  " + s, throwable);
        this.column = thingWithPosition.getColumn();
        this.row = thingWithPosition.getRow();
    }

    static InvalidSyntaxRuntimeException invalidSyntaxRuntimeException(final String s, final ThingWithPosition thingWithPosition) {
        return new InvalidSyntaxRuntimeException(s, thingWithPosition) {
            @Override
            public InvalidSyntaxException asInvalidSyntaxException() {
                return new InvalidSyntaxException(s, thingWithPosition.getRow(), thingWithPosition.getColumn());
            }
        };
    }

    static InvalidSyntaxRuntimeException invalidSyntaxRuntimeException(final String s, final Throwable throwable, final ThingWithPosition thingWithPosition) {
        return new InvalidSyntaxRuntimeException(s, throwable, thingWithPosition) {
            @Override
            public InvalidSyntaxException asInvalidSyntaxException() {
                return new InvalidSyntaxException(s, throwable, thingWithPosition.getRow(), thingWithPosition.getColumn());
            }
        };
    }

    static InvalidSyntaxRuntimeException unexpectedCharacterInvalidSyntaxRuntimeException(final String expectation, final char actual, final ThingWithPosition thingWithPosition) {
        return new InvalidSyntaxRuntimeException(expectation, thingWithPosition) {
            @Override
            public InvalidSyntaxException asInvalidSyntaxException() {
                final String message = expectation + (END_OF_STREAM == actual ? " but reached end of input." : " but got [" + actual + "].");
                return new InvalidSyntaxException(message, thingWithPosition.getRow(), thingWithPosition.getColumn());
            }
        };
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return row;
    }

    public abstract InvalidSyntaxException asInvalidSyntaxException();
}
