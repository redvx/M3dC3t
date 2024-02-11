/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockInteractionHelper;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Burrow
        extends Module {

    private Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Double> offset = this.register(new Setting<Double>("Offset", 1.2, -5.0, 10.0));
    private boolean isSneaking = false;

    public Burrow() {
        super("Burrow", "Like hamster.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    private BlockPos originalPos;

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        Vec3d[] var4 = list;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = var4[var6];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : AntiCev.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }

    @Override
    public void onEnable() {
        this.originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    public void onTick() {
        if (mc.player != null && mc.world != null) {
            if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1 && InventoryUtil.findHotbarBlock(Blocks.ENDER_CHEST) == -1) {
                this.disable();
                return;
            }
            if (!mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && !intersectsWithEntity(this.originalPos)) {
                if (mc.world.getBlockState(new BlockPos(mc.player.getPositionVector().add(0.0, 2.0, 0.0))).getBlock() != Blocks.AIR) {
                    this.disable();
                    return;
                }
            } else {
                this.disable();
                return;
            }
            Vec3d a = AntiCev.mc.player.getPositionVector();
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 0)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 0)), true);
                return;
            }
            int oldSlot = mc.player.inventory.currentItem;
            if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                mc.playerController.updateController();
            } else {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.ENDER_CHEST);
                mc.playerController.updateController();
            }
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, true));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997, mc.player.posZ, true));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, true));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.06610926093821, mc.player.posZ, true));
            this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), false);
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + this.offset.getValue(), mc.player.posZ, false));
            mc.player.inventory.currentItem = oldSlot;
            mc.playerController.updateController();
            this.toggle();
        }
    }

    private static boolean intersectsWithEntity(BlockPos pos) {
        Iterator var1 = mc.world.loadedEntityList.iterator();

        Entity entity;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            entity = (Entity)var1.next();
        } while(entity.equals(mc.player) || entity instanceof EntityItem || !(new AxisAlignedBB(pos)).intersects(entity.getEntityBoundingBox()));

        return true;
    }

    public List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList();
        if (mc.world != null && pos != null) {
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                EnumFacing side = var3[var5];
                BlockPos neighbour = pos.offset(side);
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (blockState != null && blockState.getBlock().canCollideCheck(blockState, false) && !blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }

            return facings;
        } else {
            return facings;
        }
    }

    public void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }

        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;
    }

    public boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = null;
        Iterator<EnumFacing> iterator = this.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            side = (EnumFacing)iterator.next();
        }

        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
            if (!mc.player.isSneaking() && (BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock))) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.setSneaking(true);
                sneaking = true;
            }

            if (rotate) {
                BlockInteractionHelper.faceVectorPacketInstant(hitVec);
            }

            this.rightClickBlock(neighbour, hitVec, hand, opposite, true);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }
}

