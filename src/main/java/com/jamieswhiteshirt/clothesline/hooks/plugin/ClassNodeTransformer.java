package com.jamieswhiteshirt.clothesline.hooks.plugin;

import org.objectweb.asm.tree.ClassNode;

@FunctionalInterface
public interface ClassNodeTransformer {
    void transform(ClassNode classNode) throws TransformException;
}
