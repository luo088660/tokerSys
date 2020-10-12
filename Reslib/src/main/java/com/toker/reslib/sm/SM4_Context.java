package com.toker.reslib.sm;

class SM4_Context {
    int mode;

    long[] sk;

    boolean isPadding;

    SM4_Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }
}