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

public class Sprint
        extends Module {
    public Setting<Boolean> shift = this.register(new Setting<Boolean>("Stair++", false));
    public Sprint() {
        super("Sprint", "Force sprint.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {
            if (!mc.player.isSprinting()) {
                mc.player.setSprinting(true);
            }
        }
        if (shift.getValue() && mc.player.onGround && mc.player.posY - Math.floor(mc.player.posY) > 0.0 && mc.player.moveForward != 0.0f) {
            mc.player.jump();
        }
    }
}

