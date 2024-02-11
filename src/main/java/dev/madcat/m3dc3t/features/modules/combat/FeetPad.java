/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeetPad
        extends Module {
    public EntityPlayer target;
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> air = this.register(new Setting<Boolean>("WhenSelfInAir", true));
    private final Setting<Float> range = this.register(new Setting<Float>("Range", 5.0f, 1.0f, 6.0f));
    private final Setting<Boolean> WEB = this.register(new Setting<Boolean>("Web", false));
    private final Setting<Boolean> feet2 = this.register(new Setting<Boolean>("PlaceRange+", false));

    public FeetPad() {
        super("FeetPad", "Automatically put red stones on the enemy's feet.", Module.Category.COMBAT, true, false, false);

    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onUpdate() {
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) return;
        if (!air.getValue() && !mc.player.onGround)
            return;
        BlockPos pos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        this.perform(pos.add(0, 0, 0));
        if (!feet2.getValue()) return;
        this.perform(pos.add(1, 0, 0));
        this.perform(pos.add(-1, 0, 0));
        this.perform(pos.add(0, 0, 1));
        this.perform(pos.add(0, 0, -1));
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || M3dC3t.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0)
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (mc.player.getDistanceSq(player) >= distance) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }




    

    private void perform(BlockPos pos2) {
        int old = mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
            if (WEB.getValue() && InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockWeb.class);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            } else
            if (InventoryUtil.findItemInHotbar(Items.REDSTONE) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findItemInHotbar(Items.REDSTONE);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            }
        }
    }

}

