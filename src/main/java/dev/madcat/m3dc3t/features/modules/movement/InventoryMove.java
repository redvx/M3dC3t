/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.movement;


import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;

public class InventoryMove
        extends Module {
    private static InventoryMove INSTANCE = new InventoryMove();

    public InventoryMove() {
        super("InvMove", "Allow walking on the interface.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }
    
    public Setting<Boolean> shift = this.register(new Setting<Boolean>("Sneak", false));

    public static InventoryMove getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryMove();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

