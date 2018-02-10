package com.jamieswhiteshirt.clothesline.hooks.impl;

import com.jamieswhiteshirt.clothesline.hooks.api.IActivityMovement;
import net.minecraft.entity.player.EntityPlayer;

public class ActivityMovement implements IActivityMovement {
    @Override
    public boolean preventsMovement(EntityPlayer player) {
        return true;
    }
}
