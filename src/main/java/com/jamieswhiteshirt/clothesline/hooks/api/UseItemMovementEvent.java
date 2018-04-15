package com.jamieswhiteshirt.clothesline.hooks.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class UseItemMovementEvent extends LivingEvent {
    private final ItemStack itemStack;
    private final int duration;
    private boolean movementSlowed;

    public UseItemMovementEvent(EntityLivingBase entity, @Nonnull ItemStack itemStack, int duration, boolean movementSlowed) {
        super(entity);
        this.itemStack = itemStack;
        this.duration = duration;
        this.movementSlowed = movementSlowed;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isMovementSlowed() {
        return movementSlowed;
    }

    public void setMovementSlowed(boolean movementSlowed) {
        this.movementSlowed = movementSlowed;
    }
}
