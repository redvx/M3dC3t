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
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Flatten
        extends Module {
    public EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<Float>("Range", 5.0f, 1.0f, 6.0f));
    private final Setting<Boolean> negative = this.register(new Setting<Boolean>("Chest Place", false));
    private final Setting<Boolean> air = this.register(new Setting<Boolean>("WhenSelfInAir", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));

    public Flatten() {
        super("Flatten", "Automatically pave the road for the enemy.", Module.Category.COMBAT, true, false, false);
    }

    
    BlockPos feet;

    @Override
    public void onUpdate() {
        if (Flatten.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (!air.getValue() && !mc.player.onGround) {
            return;
        }
        feet = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        this.perform(feet.add(0, -1, 0));
        this.perform(feet.add(1, -1, 0));
        this.perform(feet.add(-1, -1, 0));
        this.perform(feet.add(0, -1, 1));
        this.perform(feet.add(0, -1, -1));
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : Flatten.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || M3dC3t.friendManager.isFriend(player.getName()) || Flatten.mc.player.posY - player.posY >= 5.0)
                continue;
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq(player);
                continue;
            }
            if (EntityUtil.mc.player.getDistanceSq(player) >= distance) continue;
            target = player;
            distance = EntityUtil.mc.player.getDistanceSq(player);
        }
        return target;
    }





    private void perform(BlockPos pos2) {
        int old = mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
            if (new BlockPos(feet).equals(new BlockPos(pos2))) return;
            if (new BlockPos(feet).equals(new BlockPos(pos2.add(0, -1, 0)))) return;
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
            if (negative.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            } else
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            }
        }
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }
}

