package com.jamieswhiteshirt.clothesline.hooks.plugin;

@FunctionalInterface
public interface BytesTransformer {
    byte[] transform(byte[] basicClass) throws TransformException;
}
