
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FastElytra
        extends Module {
    private int numOfTotems;
    private int preferredTotemSlot;
    private static FastElytra INSTANCE = new FastElytra();

    public FastElytra() {
        super("FastElytra", "Tweaks for wear Elytra", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static FastElytra getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FastElytra();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (!mc.player.onGround) {
            if (this.findTotems()) {
                if (!mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
                    mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        } else {
            if (this.findTotems2()) {
                if (mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
                    mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    private boolean findTotems() {
        this.numOfTotems = 0;
        AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            int numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.ELYTRA)) {
                numOfTotemsInStack = slotValue.getCount();
                if (preferredTotemSlotStackSize.get() < numOfTotemsInStack) {
                    preferredTotemSlotStackSize.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }

            this.numOfTotems += numOfTotemsInStack;
        });
        if (mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
            this.numOfTotems += 1;
        }

        return this.numOfTotems != 0;
    }

    private boolean findTotems2() {
        this.numOfTotems = 0;
        AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            int numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.DIAMOND_CHESTPLATE)) {
                numOfTotemsInStack = slotValue.getCount();
                if (preferredTotemSlotStackSize.get() < numOfTotemsInStack) {
                    preferredTotemSlotStackSize.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }

            this.numOfTotems += numOfTotemsInStack;
        });
        if (mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.DIAMOND_CHESTPLATE)) {
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

