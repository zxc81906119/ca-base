package com.redhat.cleanbase.convert.json.constant;

public final class JsonConstants {
    private JsonConstants() {
        throw new UnsupportedOperationException();
    }

    public static final class Split {
        public static final String DOT = ".";
        public static final String DOT_REGEXP = "\\" + DOT;
        public static final String LEFT_QUART = "[";
        public static final String RIGHT_QUART = "]";
        public static final String QUART_REGEXP = "\\" + LEFT_QUART + "(\\d*)" + RIGHT_QUART;
        private Split() {
            throw new UnsupportedOperationException();
        }
    }
}