/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraft.entity.player.EntityPlayer;

public class Reach
        extends Module {
    private final Setting<Integer> Reach = this.register(new Setting<Integer>("Reach", 6, 5, 10));

    public Reach() {
        super("Reach", "Increase reach range.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        dev.madcat.m3dc3t.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue((double)this.Reach.getValue().intValue());
    }

    @Override
    public void onDisable() {
        dev.madcat.m3dc3t.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(5.0);
    }
}

