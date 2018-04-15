package com.jamieswhiteshirt.clothesline.hooks;

import com.jamieswhiteshirt.clothesline.hooks.api.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHooks {
    public static void onGetMouseOver(float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new GetMouseOverEvent(partialTicks));
    }

    public static void onRenderEntities(ICamera camera, float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new RenderEntitiesEvent(camera, partialTicks));
    }

    public static boolean onStoppedUsingItem() {
        return MinecraftForge.EVENT_BUS.post(new ClientStoppedUsingItemEvent());
    }

    public static boolean isUseItemMovementSlowed(EntityPlayerSP player) {
        if (player.isHandActive()) {
            UseItemMovementEvent event = new UseItemMovementEvent(player, player.getActiveItemStack(), player.getItemInUseCount(), true);
            MinecraftForge.EVENT_BUS.post(event);
            return event.isMovementSlowed();
        }
        return false;
    }
}
