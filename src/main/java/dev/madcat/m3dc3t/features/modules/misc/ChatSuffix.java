package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.manager.ModuleManager;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix
        extends Module {
    public ChatSuffix() {
        super("ChatSuffix", "Message with suffix.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        if(ModuleManager.getModuleByName("ChatQueue").isEnabled()) return;
        String message;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage && !(message = (packet = (CPacketChatMessage)event.getPacket()).getMessage()).startsWith("/")) {
            packet.message = message + " \u267f \u1d04\u1d1c\u1d07\u1d1b \u1d04\u1d00\u1d1b \u267f";
        }
    }
}

