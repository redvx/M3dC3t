
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterPortal
        extends Module {
    public Setting<Boolean> portalChat = this.register(new Setting<Boolean>("Chat", Boolean.valueOf(true), "Allows you to chat in portals."));
    public Setting<Boolean> godmode = this.register(new Setting<Boolean>("GodMode", Boolean.valueOf(false), "Portal Godmode."));
    private static BetterPortal INSTANCE = new BetterPortal();

    public BetterPortal() {
        super("BetterPortal", "Tweaks for Portals.", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static BetterPortal getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BetterPortal();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.getValue().booleanValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}

