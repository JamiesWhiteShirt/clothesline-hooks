package com.jamieswhiteshirt.clothesline.hooks.plugin;

import com.jamieswhiteshirt.clothesline.hooks.ClotheslineHooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FMLLoadingPluginTest {
    FMLLoadingPlugin fmlLoadingPlugin = new FMLLoadingPlugin();

    @Test
    void providesClassTransformer() {
        String[] classTransformers = fmlLoadingPlugin.getASMTransformerClass();
        Assertions.assertEquals(1, classTransformers.length);
        Assertions.assertEquals(ClassTransformer.class.getCanonicalName(), classTransformers[0]);
    }

    @Test
    void providesModContainer() {
        Assertions.assertEquals(ClotheslineHooks.class.getCanonicalName(), fmlLoadingPlugin.getModContainerClass());
    }
}
