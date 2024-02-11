
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.manager.ModuleManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoArmor
        extends Module {
    private int numOfTotems;
    private int preferredTotemSlot;
    private static AutoArmor INSTANCE = new AutoArmor();

    public AutoArmor() {
        super("AutoArmor", "Tweaks for wear armors", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static AutoArmor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoArmor();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (mc.player.inventoryContainer.getSlot(5).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_HELMET, 5)) {
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 5, 0, ClickType.PICKUP, mc.player);
            return;
        }
        if (mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
            return;
        }
        if (mc.player.inventoryContainer.getSlot(7).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_LEGGINGS, 7)) {
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 7, 0, ClickType.PICKUP, mc.player);
            return;
        }
        if (mc.player.inventoryContainer.getSlot(8).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_BOOTS, 8)) {
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 8, 0, ClickType.PICKUP, mc.player);
            return;
        }
        if (mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA) && mc.player.onGround && !ModuleManager.getModuleByName("FastElytra").isEnabled() && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
        }
    }

    private boolean findTotems(ItemArmor item, int slotId) {
        this.numOfTotems = 0;
        AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            int numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(item)) {
                numOfTotemsInStack = slotValue.getCount();
                if (preferredTotemSlotStackSize.get() < numOfTotemsInStack) {
                    preferredTotemSlotStackSize.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }

            this.numOfTotems += numOfTotemsInStack;
        });
        if (mc.player.inventoryContainer.getSlot(slotId).getStack().getItem().equals(Items.DIAMOND_HELMET)) {
            this.numOfTotems += 1;
        }

        return this.numOfTotems != 0;
    }

    private static Map<Integer, ItemStack> getInventorySlots(int current) {
        HashMap fullInventorySlots;
        for (fullInventorySlots = new HashMap(); current <= 44; ++current) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
        }

        return fullInventorySlots;
    }
}

