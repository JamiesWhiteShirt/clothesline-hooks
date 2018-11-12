package com.jamieswhiteshirt.clothesline.hooks.plugin;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        BytesTransformer transformer = Transformers.transformers.get(transformedName);
        if (transformer != null) {
            try {
                return transformer.transform(basicClass);
            } catch (TransformException e) {
                FMLLoadingPlugin.LOGGER.error("Failed to transform class " + transformedName, e);
            }
        }
        return basicClass;
    }
}
