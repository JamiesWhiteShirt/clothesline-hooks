package com.jamieswhiteshirt.clothesline.hooks.plugin;

import org.objectweb.asm.tree.MethodNode;

@FunctionalInterface
public interface MethodNodeTransformer {
    void transform(MethodNode methodNode) throws TransformException;
}
