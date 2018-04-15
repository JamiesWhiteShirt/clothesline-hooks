package com.jamieswhiteshirt.clothesline.hooks;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Collections;

public class ClotheslineHooks extends DummyModContainer {
    public static final String MODID = "clothesline-hooks";
    public static final String VERSION = "1.12.2-1.0.0.0-SNAPSHOT";

    public ClotheslineHooks() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = MODID;
        metadata.name = "Clothesline Hooks";
        metadata.version = VERSION;
        metadata.authorList = Collections.singletonList("JamiesWhiteShirt");
        metadata.description =
                "This is one of those evil core mods, known for burning down your cat and killing your house!\n" +
                "You'll find that Clothesline Hooks actually only adds some necessities to make Clothesline work.";
        metadata.screenshots = new String[0];
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
