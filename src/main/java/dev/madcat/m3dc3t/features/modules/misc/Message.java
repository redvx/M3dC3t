/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package dev.madcat.m3dc3t.features.modules.misc;


import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

public class Message
        extends Module {
    private final Timer timer = new Timer();
    private Setting<String> custom = this.register(new Setting<String>("Custom", "M3dC3t very cute:3"));
    private Setting<Integer> random = this.register(new Setting<Integer>("Random", 1, 1, 20));
    private Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 100, 0, 10000));

    public Message() {
        super("Spammer", "Automatically send messages.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (Message.fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue().intValue())) return;
        Message.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.custom.getValue() + RandomStringUtils.randomAlphanumeric(this.random.getValue())));
        this.timer.reset();
    }
}

