package com.jamieswhiteshirt.clothesline.hooks.plugin;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.CertificateHelper;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.security.cert.Certificate;
import java.util.Map;

@IFMLLoadingPlugin.Name("clothesline-hooks")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("com.jamieswhiteshirt.clothesline.hooks.plugin")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    private static final String CERTIFICATE_FINGERPRINT = "3bae2d07b93a5971335cb2de15230c19c103db32";

    public static final Logger LOGGER = LogManager.getLogger("clothesline-hooks");

    public FMLLoadingPlugin() {
        Certificate[] certificates = FMLLoadingPlugin.class.getProtectionDomain().getCodeSource().getCertificates();
        ImmutableList<String> certList = CertificateHelper.getFingerprints(certificates);
        if (!certList.contains(CERTIFICATE_FINGERPRINT)) {
            LOGGER.error("Expecting signature {}, however there is no signature matching that description", CERTIFICATE_FINGERPRINT);
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.jamieswhiteshirt.clothesline.hooks.plugin.ClassTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "com.jamieswhiteshirt.clothesline.hooks.ClotheslineHooks";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
