/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
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
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;
import java.util.stream.Collectors;

public class TrapSelf
        extends Module {
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    private final Setting<Boolean> cev = this.register(new Setting<>("HighCev", false));
    private final Setting<Boolean> civ = this.register(new Setting<>("Civ", false));
    private final Setting<Boolean> cev2 = this.register(new Setting<>("HighCev+", false));
    private final Setting<Boolean> head = this.register(new Setting<>("Head", false));

    private final Setting<Boolean> headB = this.register(new Setting<>("HeadButton", false, v -> head.getValue()));

    private final Setting<Boolean> center = this.register(new Setting<>("TPCenter", true));
    private BlockPos startPos;

    public TrapSelf() {
        super("TrapSelf", "Trap self.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.startPos = EntityUtil.getRoundedBlockPos(mc.player);
        if (this.center.getValue())
            M3dC3t.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
    }

    @Override
    public void onTick() {
        if (startPos == null) {
            this.disable();
            return;
        }
        else if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
            this.disable();
            return;
        }
        BlockPos pos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY, TrapSelf.mc.player.posZ);
        perform(pos.add(1, 0, 0), EntityUtil.getVarOffsets(1, 0,0));
        perform(pos.add(1, 1, 0), EntityUtil.getVarOffsets(1, 1,0));
        perform(pos.add(-1, 0, 0), EntityUtil.getVarOffsets(-1, 0,0));
        perform(pos.add(-1, 1, 0), EntityUtil.getVarOffsets(-1, 1,0));
        perform(pos.add(0, 0, 1), EntityUtil.getVarOffsets(0, 0, 1));
        perform(pos.add(0, 1, 1), EntityUtil.getVarOffsets(0, 1, 1));
        perform(pos.add(0, 0, -1), EntityUtil.getVarOffsets(0, 0, -1));
        perform(pos.add(0, 1, -1), EntityUtil.getVarOffsets(0, 1, -1));
        if (this.cev.getValue()) {
            perform(pos.add(0, 2, -1), EntityUtil.getVarOffsets(0, 2, -1));
            perform(pos.add(0, 3, -1), EntityUtil.getVarOffsets(0, 3, -1));
            perform(pos.add(0, 3, 0), EntityUtil.getVarOffsets(0, 3, 0));
        }
        if (this.cev2.getValue()) {
            perform(pos.add(0, 3, -1), EntityUtil.getVarOffsets(0, 3, -1));
            perform(pos.add(0, 4, -1), EntityUtil.getVarOffsets(0, 4, -1));
            perform(pos.add(0, 4, 0), EntityUtil.getVarOffsets(0, 4, 0));
        }
        if (this.head.getValue()) {
            perform(pos.add(0, 2, -1), EntityUtil.getVarOffsets(0, 2, -1));
            if (this.headB.getValue())
                BPerform(pos.add(0, 2, 0), EntityUtil.getVarOffsets(0, 2, 0));
            else
                perform(pos.add(0, 2, 0), EntityUtil.getVarOffsets(0, 2, 0));
        }
        if (this.civ.getValue()) {
            perform(pos.add(0, 2, -1), EntityUtil.getVarOffsets(0, 2, -1));
            perform(pos.add(0, 2, 1), EntityUtil.getVarOffsets(0, 2, 1));
            perform(pos.add(1, 2, 0), EntityUtil.getVarOffsets(1, 2, 0));
            perform(pos.add(-1, 2, 0), EntityUtil.getVarOffsets(-1, 2, 0));
        }
    }

    private void perform(BlockPos pos2, Vec3d[] list) {
        int old = mc.player.inventory.currentItem;
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
        if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) return;
            if (this.checkCrystal(mc.player.getPositionVector(), list) != null) attackCrystal(pos2);
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            mc.player.inventory.currentItem = old;
            mc.playerController.updateController();
        }
    }

    private void BPerform(BlockPos pos2, Vec3d[] list) {
        int old = mc.player.inventory.currentItem;
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
        if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON) == -1) {
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) return;
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            } else {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON);
            }
            if (this.checkCrystal(mc.player.getPositionVector(), list) != null) attackCrystal(pos2);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            mc.player.inventory.currentItem = old;
            mc.playerController.updateController();
        }
    }

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = list[var6];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : AntiCev.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }

    public static void attackCrystal(BlockPos pos) {
        for (Entity crystal : mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistanceSq(pos) <= 1.0)) continue;
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }




    
}

