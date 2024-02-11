/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.item.ItemStack
 */
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import net.minecraft.inventory.ContainerChest;

public class AntiChestGui
extends Module {
    public AntiChestGui() {
        super("AntiChestGui", "AntiChestGui.", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player.openContainer instanceof ContainerChest) {
            mc.player.closeScreen();
        }
    }
}

