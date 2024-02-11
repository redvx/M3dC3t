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


import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.event.events.PushEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
        extends Module {
    public Setting<Boolean> antiKnockBack = this.register(new Setting<Boolean>("KnockBack", true));
    public Setting<Boolean> noEntityPush = this.register(new Setting<Boolean>("No PlayerPush", true));
    public Setting<Boolean> noBlockPush = this.register(new Setting<Boolean>("No BlockPush", true));
    public Setting<Boolean> noWaterPush = this.register(new Setting<Boolean>("No LiquidPush", true));

    public Velocity() {
        super("Velocity", "Anti push and knock back.", Category.MOVEMENT, true, false, false);
    }


    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (Velocity.fullNullCheck()) {
            return;
        }
        if (this.antiKnockBack.getValue().booleanValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (Velocity.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0 && this.noEntityPush.getValue().booleanValue() && event.entity.equals((Object) Velocity.mc.player)) {
            event.x = -event.x * 0.0;
            event.y = -event.y * 0.0;
            event.z = -event.z * 0.0;
        } else if (event.getStage() == 1 && this.noBlockPush.getValue().booleanValue()) {
            event.setCanceled(true);
        } else if (event.getStage() == 2 && this.noWaterPush.getValue().booleanValue() && Velocity.mc.player != null && Velocity.mc.player.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
}

