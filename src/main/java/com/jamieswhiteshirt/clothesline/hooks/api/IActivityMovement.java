package com.jamieswhiteshirt.clothesline.hooks.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IActivityMovement {
    boolean preventsMovement(EntityPlayer player);
}
