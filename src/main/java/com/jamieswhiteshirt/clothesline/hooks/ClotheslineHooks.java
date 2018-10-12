package com.jamieswhiteshirt.clothesline.hooks;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.versioning.*;

import java.util.List;
import java.util.Set;

public class ClotheslineHooks extends DummyModContainer {
    public static final String MODID = "clothesline-hooks";
    public static final String VERSION = "1.12.2-0.0.1.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.3.2665,)";

    public ClotheslineHooks() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = MODID;
        metadata.name = "Clothesline Hooks";
        metadata.version = VERSION;
        metadata.authorList.add("JamiesWhiteShirt");
        metadata.description =
                "This is one of those evil core mods, known for burning down your cat and killing your house!\n" +
                "You'll find that Clothesline Hooks actually only adds some necessities to make Clothesline work.";
        metadata.screenshots = new String[0];

        DependencyParser dependencyParser = new DependencyParser(getModId(), FMLCommonHandler.instance().getSide());
        DependencyParser.DependencyInfo info = dependencyParser.parseDependencies(DEPENDENCIES);
        metadata.requiredMods = info.requirements;
        metadata.dependencies = info.dependencies;
        metadata.dependants = info.dependants;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        return getMetadata().requiredMods;
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return getMetadata().dependencies;
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        return getMetadata().dependants;
    }
}
