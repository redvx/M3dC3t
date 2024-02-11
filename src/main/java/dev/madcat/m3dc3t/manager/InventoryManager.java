package dev.madcat.m3dc3t.manager;

import dev.madcat.m3dc3t.features.modules.combat.AntiCev;
import dev.madcat.m3dc3t.util.Util;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryManager
        implements Util {
    public int currentPlayerItem;
    private int recoverySlot = -1;

    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.player.inventory.currentItem = this.recoverySlot;
            int i = InventoryManager.mc.player.inventory.currentItem;
            if (i != this.currentPlayerItem) {
                this.currentPlayerItem = i;
                AntiCev.mc.player.inventory.currentItem = this.currentPlayerItem;
                AntiCev.mc.playerController.updateController();
            }
            this.recoverySlot = -1;
        }
    }

    public void recoverSilent(int slot) {
        this.recoverySlot = slot;
    }
}

