/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.combat.BreakCheck;
import dev.madcat.m3dc3t.features.modules.combat.InstantMine;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;


public class AntiPistonKick
        extends Module {
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));

    private final Setting<Boolean> head = this.register(new Setting<Boolean>("TrapHead", false));

    public AntiPistonKick() {
        super("AntiPistonKick", "Anti piston push.", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(0, 1, -1));
            }
            if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR)
                this.perform(pos.add(0, 2, -1));
            if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON)
            mc.playerController.onPlayerDamageBlock(pos.add(0, 1, 1), BlockUtil.getRayTraceFacing(pos.add(0, 1, 1)));
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                if (this.head.getValue()) {
                    this.perform(pos.add(0, 2, 0));
                }
            }
        }
        if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(0, 1, 1));
            }
            if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR)
                this.perform(pos.add(0, 2, 1));
            if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON)
            mc.playerController.onPlayerDamageBlock(pos.add(0, 1, -1), BlockUtil.getRayTraceFacing(pos.add(0, 1, -1)));
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                if (this.head.getValue()) {
                    this.perform(pos.add(0, 2, 0));
                }
            }
        }
        if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(-1, 1, 0));
            }
            if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR)
            this.perform(pos.add(-1, 2, 0));
            if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON)
            mc.playerController.onPlayerDamageBlock(pos.add(1, 1, 0), BlockUtil.getRayTraceFacing(pos.add(1, 1, 0)));
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                if (this.head.getValue()) {
                    this.perform(pos.add(0, 2, 0));
                }
            }
        }
        if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(1, 1, 0));
            }
            if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR)
                this.perform(pos.add(1, 2, 0));
            if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON)
            mc.playerController.onPlayerDamageBlock(pos.add(-1, 1, 0), BlockUtil.getRayTraceFacing(pos.add(-1, 1, 0)));
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                if (this.head.getValue()) {
                    this.perform(pos.add(0, 2, 0));
                }
            }
        }
    }


    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

    BlockPos pos;
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (mc.player != null) {
            if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK)
                return "Working";
            if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK)
                return "Working";
            if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK)
                return "Working";
            if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON || this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK)
                return "Working";
        }
        return null;
    }





    private void perform(BlockPos pos2) {
        int old = mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            }
        }
    }
    
}

