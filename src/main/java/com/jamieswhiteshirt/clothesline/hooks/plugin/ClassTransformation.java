package com.jamieswhiteshirt.clothesline.hooks.plugin;

public class ClassTransformation {
    private final byte[] result;
    private final boolean success;

    public ClassTransformation(byte[] result, boolean success) {
        this.result = result;
        this.success = success;
    }

    public byte[] getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }
}
