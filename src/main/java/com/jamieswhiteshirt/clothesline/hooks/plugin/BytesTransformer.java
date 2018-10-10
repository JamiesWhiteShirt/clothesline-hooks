package com.jamieswhiteshirt.clothesline.hooks.plugin;

@FunctionalInterface
public interface BytesTransformer {
    byte[] transform(ClassLoader classLoader, byte[] basicClass) throws TransformException;
}
