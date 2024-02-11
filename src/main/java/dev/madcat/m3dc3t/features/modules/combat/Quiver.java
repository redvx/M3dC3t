package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionUtils;

import java.util.List;
import java.util.Objects;

public class Quiver extends Module {
    private final Setting<Integer> tickDelay = register(new Setting("TickDelay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(8)));
    public Quiver() {
        super("Quiver", "Rotates and shoots yourself with good potion effects.", Module.Category.COMBAT, true, false, false);
    }
    @Override
    public void onUpdate() {
            if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= tickDelay.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, mc.player.onGround));
                mc.playerController.onStoppedUsingItem(mc.player);
            }
            List<Integer> arrowSlots = InventoryUtil.getItemInventory(Items.TIPPED_ARROW);
            if(arrowSlots.get(0) == -1) return;
            int speedSlot = -1;
            int strengthSlot = -1;
            for (Integer slot : arrowSlots)
            {
                if ((PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName()).getPath().contains("swiftness")) speedSlot = slot;
                else if (Objects.requireNonNull(PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName()).getPath().contains("strength")) strengthSlot = slot;
            }
    }
}
