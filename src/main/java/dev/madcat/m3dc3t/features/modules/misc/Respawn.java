package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraft.client.gui.GuiGameOver;

public class Respawn extends Module {
    public Respawn() {
        super("Respawn",  "Resurrection at the time of death.", Category.MISC,true,false,false);
    }
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }

}