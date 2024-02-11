/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package dev.madcat.m3dc3t.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.command.Command;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Collectors;

public class Surround
        extends Module {
    public static boolean isPlacing = false;
    private final Setting<Boolean> old = register(new Setting<Boolean>("OldMode", false));
    private final Setting<Boolean> face2 = register(new Setting<Boolean>("AntiFace", false, v -> !old.getValue()));
    private final Setting<Boolean> face = register(new Setting<Boolean>("AntiFace+", false, v -> !old.getValue()));
    private final Setting<Integer> blocksPerTick = register(new Setting<Integer>("BlocksPerTick", 12, 1, 20, v -> old.getValue()));
    private final Setting<Integer> delay = register(new Setting<Integer>("Delay", 0, 0, 250, v -> old.getValue()));
    private final Setting<Boolean> noGhost = register(new Setting<Boolean>("PacketPlace", true, v -> old.getValue()));
    private final Setting<Boolean> breakcrystal = register(new Setting<Boolean>("BreakCrystal", true, v -> old.getValue()));
    private final Setting<Double> safehealth = register(new Setting<Double>("Safe Health", Double.valueOf(12.5), Double.valueOf(1.0), Double.valueOf(36.0), v -> breakcrystal.getValue() && old.getValue()));
    private final Setting<Boolean> center = register(new Setting<Boolean>("TPCenter", true));
    private final Setting<Boolean> rotate = register(new Setting<Boolean>("Rotate", false));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    BlockPos CrystalPos = null;
    BlockPos FeetPos;
    private int obbySlot = -1;
    private boolean offHand = false;
int CevHigh;
    public Surround() {
        super("Surround", "Surrounds you with Obsidian.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
        if (center.getValue().booleanValue()) {
            M3dC3t.positionManager.setPositionPacket((double)startPos.getX() + 0.5, startPos.getY(), (double)startPos.getZ() + 0.5, true, true, true);
        }
        retries.clear();
        retryTimer.reset();
    }

    @Override
    public void onTick() {
        if (startPos == null) {
            disable();
            return;
        } else if (!startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
            disable();
            return;
        }
        if (old.getValue()) {
            if (breakcrystal.getValue().booleanValue() && (double) Surround.mc.player.getHealth() >= safehealth.getValue() && isSafe == 0) {
                breakcrystal();
            }
            doFeetPlace();
            return;
        }
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        EntityPlayer target = getTarget();
        if (target != null)
            FeetPos = new BlockPos(target.posX, target.posY, target.posZ);
        else FeetPos = null;
        Entity crystal = getEndCrystal();
        if (crystal != null) {
            CrystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(3, 0, 0)))) {
                CevHigh = 1;
            }
            if (CevHigh == 1 && checkCrystal(EntityUtil.getVarOffsets(3, 0, 0)) == null) {
                perform(pos.add(3, 0, 0));
                if (mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() != Blocks.AIR) CevHigh = 0;
            }
            if (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-3, 0, 0)))) {
                CevHigh = 2;
            }
            if (CevHigh == 2 && checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null) {
                perform(pos.add(-3, 0, 0));
                if (mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() != Blocks.AIR) CevHigh = 0;
            }
            if (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, 3)))) {
                CevHigh = 3;
            }
            if (CevHigh == 3 && checkCrystal(EntityUtil.getVarOffsets(0, 0, 3)) == null) {
                perform(pos.add(0, 0, 3));
                if (mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() != Blocks.AIR) CevHigh = 0;
            }
            if (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, -3)))) {
                CevHigh = 4;
            }
            if (CevHigh == 4 && checkCrystal(EntityUtil.getVarOffsets(0, 0, -3)) == null) {
                perform(pos.add(0, 0, -3));
                if (mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() != Blocks.AIR) CevHigh = 0;
            }

            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, 1)))) AttackCrystal(pos.add(0, 0, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, -1)))) AttackCrystal(pos.add(0, 0, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, 0)))) AttackCrystal(pos.add(1, 0, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, 0)))) AttackCrystal(pos.add(-1, 0, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, 1)))) AttackCrystal(pos.add(0, -1, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, -1))))
                AttackCrystal(pos.add(0, -1, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, 0)))) AttackCrystal(pos.add(1, -1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, 0))))
                AttackCrystal(pos.add(-1, -1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, 2)))) AttackCrystal(pos.add(0, 0, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, -2)))) AttackCrystal(pos.add(0, 0, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, 0)))) AttackCrystal(pos.add(2, 0, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, 0)))) AttackCrystal(pos.add(-2, 0, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, 2)))) AttackCrystal(pos.add(0, 1, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, -2)))) AttackCrystal(pos.add(0, 1, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 1, 0)))) AttackCrystal(pos.add(2, 1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 1, 0)))) AttackCrystal(pos.add(-2, 1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, 2)))) AttackCrystal(pos.add(0, -1, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, -2))))
                AttackCrystal(pos.add(0, -1, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, -1, 0)))) AttackCrystal(pos.add(2, -1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, -1, 0))))
                AttackCrystal(pos.add(-2, -1, 0));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, 1)))) AttackCrystal(pos.add(1, 0, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, -1)))) AttackCrystal(pos.add(1, 0, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, -1))))
                AttackCrystal(pos.add(-1, 0, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, 1)))) AttackCrystal(pos.add(-1, 0, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, 1)))) AttackCrystal(pos.add(1, -1, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, -1))))
                AttackCrystal(pos.add(1, -1, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, -1))))
                AttackCrystal(pos.add(-1, -1, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, 1))))
                AttackCrystal(pos.add(-1, -1, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, 1)))) AttackCrystal(pos.add(2, 0, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, -1)))) AttackCrystal(pos.add(2, 0, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, -1))))
                AttackCrystal(pos.add(-2, 0, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, 1)))) AttackCrystal(pos.add(-2, 0, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, 2)))) AttackCrystal(pos.add(1, 0, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, -2)))) AttackCrystal(pos.add(1, 0, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, -2))))
                AttackCrystal(pos.add(-1, 0, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, 2)))) AttackCrystal(pos.add(-1, 0, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, -1, 1)))) AttackCrystal(pos.add(2, -1, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, -1, -1))))
                AttackCrystal(pos.add(2, -1, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, -1, -1))))
                AttackCrystal(pos.add(-2, -1, -1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, -1, 1))))
                AttackCrystal(pos.add(-2, -1, 1));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, 2)))) AttackCrystal(pos.add(1, -1, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, -2))))
                AttackCrystal(pos.add(1, -1, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, -2))))
                AttackCrystal(pos.add(-1, -1, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, 2))))
                AttackCrystal(pos.add(-1, -1, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, 2)))) AttackCrystal(pos.add(2, 0, 2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, -2)))) AttackCrystal(pos.add(2, 0, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, -2))))
                AttackCrystal(pos.add(-2, 0, -2));
            else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, 2)))) AttackCrystal(pos.add(-2, 0, 2));
        }
        if (mc.world.getBlockState(pos.add(0, -1, -1)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -1)))) && mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, -1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -2, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, -1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, -1, -1)))) && mc.world.getBlockState(pos.add(0, -1, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(0, -1, -1));
        if (mc.world.getBlockState(pos.add(-1, -1, 0)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 0)))) && mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, 0, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -2, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, -1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, -1, 0)))) && mc.world.getBlockState(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, -1, 0));
        if (mc.world.getBlockState(pos.add(1, -1, 0)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 0)))) && mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(1, 0, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -2, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, -1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, -1, 0)))) && mc.world.getBlockState(pos.add(1, -1, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(1, -1, 0));
        if (mc.world.getBlockState(pos.add(0, -1, 1)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 1)))) && mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, 1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -2, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, -1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, -1, 1)))) && mc.world.getBlockState(pos.add(0, -1, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(0, -1, 1));
        else if (mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && checkCrystal(EntityUtil.getVarOffsets(0, 0, 2)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, 2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 2)))) && mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, 2));
        else if (mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && checkCrystal(EntityUtil.getVarOffsets(0, 0, -2)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, -2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -2)))) && mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, -2));
        else if (mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && checkCrystal(EntityUtil.getVarOffsets(2, 0, 0)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(2, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(2, 0, 0)))) && mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(2, 0, 0));
        else if (mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-2, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-2, 0, 0)))) && mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(-2, 0, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 1)))) && mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(1, 0, 1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 1)))) && mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, 0, 1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, -1)))) && mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(1, 0, -1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, -1)))) && mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, 0, -1));
        else if (mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, 2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, 2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, 2)))) && mc.world.getBlockState(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
            perform(pos.add(0, 1, 2));
            if (checkCrystal(EntityUtil.getVarOffsets(1, 1, 2)) == null &&mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR&&mc.world.getBlockState(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                perform(pos.add(1, 0, 2));
                perform(pos.add(1, 1, 2));
            }
        } else if (mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, -2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, -2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, -2)))) && mc.world.getBlockState(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
            perform(pos.add(0, 1, -2));
            if (checkCrystal(EntityUtil.getVarOffsets(-1, 1, -2)) == null &&mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR&&mc.world.getBlockState(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                perform(pos.add(-1, 0, -2));
                perform(pos.add(-1, 1, -2));
            }
        } else if (mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(2, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(2, 1, 0)))) && mc.world.getBlockState(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
            perform(pos.add(2, 1, 0));
            if (checkCrystal(EntityUtil.getVarOffsets(2, 1, 1)) == null &&mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR&&mc.world.getBlockState(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                perform(pos.add(2, 0, 1));
                perform(pos.add(2, 1, 1));
            }
        } else if (mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.AIR && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-2, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-2, 1, 0)))) && mc.world.getBlockState(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
            perform(pos.add(-2, 1, 0));
            if (checkCrystal(EntityUtil.getVarOffsets(-2, 1, -1)) == null &&mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR&&mc.world.getBlockState(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                perform(pos.add(-2, 0, -1));
                perform(pos.add(-2, 1, -1));
            }
        } else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR || ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 0)))))))
            perform(pos.add(-1, 1, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, 0)))) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR || ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 0)))))))
            perform(pos.add(1, 1, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, 1)))) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR || ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 1)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 1)))))))
            perform(pos.add(0, 1, 1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, -1)))) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR || ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -1)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -1)))))))
            perform(pos.add(0, 1, -1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 2, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, 0)))) && (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, 0)))) && mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR)
            perform(pos.add(-1, 2, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 2, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, 0)))) && (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, 0)))) && mc.world.getBlockState(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR)
            perform(pos.add(1, 2, 0));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 2, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 2, 1)))) && (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 2, 1)))) && mc.world.getBlockState(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR)
            perform(pos.add(0, 2, 1));
        else if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 2, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 2, -1)))) && (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 2, -1)))) && mc.world.getBlockState(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR)
            perform(pos.add(0, 2, -1));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-3, -1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(-3, 0, 0));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(3, 0, 0)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(3, -1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(3, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(3, 0, 0)))) && mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() == Blocks.AIR)
            perform(pos.add(3, 0, 0));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, 3)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, 3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, 3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 3)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 3)))) && mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, 3));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 0, -3)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, -1, -3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, -3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -3)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -3)))) && mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() == Blocks.AIR)
            perform(pos.add(0, 0, -3));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, 1)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, 0, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-2, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-2, 0, 1)))) && mc.world.getBlockState(pos.add(-2, 0, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(-2, 0, 1));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, 2)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 2)))) && mc.world.getBlockState(pos.add(-1, 0, 2)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, 0, 2));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, 1)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, 0, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(2, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(2, 0, 1)))) && mc.world.getBlockState(pos.add(2, 0, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(2, 0, 1));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, 2)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 2)))) && mc.world.getBlockState(pos.add(1, 0, 2)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR)
            perform(pos.add(1, 0, 2));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 0, -2)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, -2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, -2)))) && mc.world.getBlockState(pos.add(-1, 0, -2)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(-1, 0, -2));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-2, 0, -1)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, -1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-2, 0, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-2, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-2, 0, -1)))) && mc.world.getBlockState(pos.add(-2, 0, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(-2, 0, -1));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(2, 0, -1)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(2, 0, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(2, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(2, 0, -1)))) && mc.world.getBlockState(pos.add(2, 0, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(2, 0, -1));
        else if ((CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 0, -2)))) && (CrystalPos == null || !new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, -1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, -2)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, -2)))) && mc.world.getBlockState(pos.add(1, 0, -2)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR)
            perform(pos.add(1, 0, -2));
        else if (checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-3, 0, 0)))) && mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-2, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-2, 0, 0))))))
            perform(pos.add(-3, 0, 0));
        else if (checkCrystal(EntityUtil.getVarOffsets(3, 0, 0)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(3, 0, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(3, 0, 0)))) && mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(2, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(2, 0, 0))))))
            perform(pos.add(3, 0, 0));
        else if (checkCrystal(EntityUtil.getVarOffsets(0, 0, 3)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, 3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 3)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 3)))) && mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 2)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 2))))))
            perform(pos.add(0, 0, 3));
        else if (checkCrystal(EntityUtil.getVarOffsets(0, 0, -3)) == null && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -3)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, -1, -3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -3)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -3)))) && mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -2)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -2))))))
            perform(pos.add(0, 0, -3));
        if (CrystalPos != null)
            if (face.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, 1)))) {
                AttackCrystal(pos.add(1, 1, 1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, 1)))) && mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 1, 1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, 1)))) && mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 2, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 2, 1));
            } else if (face.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, -1)))) {
                AttackCrystal(pos.add(1, 1, -1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, -1)))) && mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 1, -1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, -1)))) && mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 2, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 2, -1));
            } else if (face.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, -1)))) {
                AttackCrystal(pos.add(-1, 1, -1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, -1)))) && mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 1, -1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, -1)))) && mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 2, -1));
            } else if (face.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, 1)))) {
                AttackCrystal(pos.add(-1, 1, 1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, 1)))) && mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 1, 1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, 1)))) && mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 2, 1));
            } else if (face2.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, 1)))) {
                AttackCrystal(pos.add(0, 1, 1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, 1)))) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(0, 1, 1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 2, 1)))) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(0, 2, 1));
            } else if (face2.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, -1)))) {
                AttackCrystal(pos.add(0, 1, -1));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(0, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, -1)))) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(0, 1, -1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 2, -1)))) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(0, 2, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(0, 2, -1));

            } else if (face2.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, 0)))) {
                AttackCrystal(pos.add(1, 1, 0));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, 0)))) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 1, 0));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, 0)))) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 2, 0));
            } else if (face2.getValue() && new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, 0)))) {
                AttackCrystal(pos.add(-1, 1, 0));
                if ((FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (FeetPos == null || !new BlockPos(FeetPos).equals(new BlockPos(pos.add(-1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, 0)))) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 1, 0));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, 0)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, 0)))) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 2, 0));
            }
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
    }

    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        switch (isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
        }
        return ChatFormatting.GREEN + "Safe";
    }

    public static void AttackCrystal(BlockPos pos) {
        for (Entity crystal : mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistanceSq(pos) <= 1.0)) continue;
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    private Entity getEndCrystal() {
        Entity crystal = null;
        for (Entity player : mc.world.loadedEntityList) {
            if (!(player instanceof EntityEnderCrystal)) continue;
            crystal = player;
        }
        return crystal;
    }

    private void perform(BlockPos pos) {
        int old = mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) return;
        mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        mc.player.inventory.currentItem = old;
        mc.playerController.updateController();
    }


    Entity checkCrystal(Vec3d[] list) {
        Entity crystal = null;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = list[var6];
            BlockPos position = new BlockPos(mc.player.getPositionVector()).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : AntiCev.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }


    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = 12;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, 12) || M3dC3t.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0)
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

    public static void breakcrystal() {
        for (Entity crystal : Surround.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> Float.valueOf(Surround.mc.player.getDistance(e)))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(Surround.mc.player.getDistance(crystal) <= 4.0f)) continue;
            Surround.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
            Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }

    private void doFeetPlace() {
        if (check()) {
            return;
        }
        if (!EntityUtil.isSafe((Entity)Surround.mc.player, 0, true)) {
            isSafe = 0;
            if (AntiCity.getInstance().Check == 0)
            placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true), true, false, false);
        } else if (!EntityUtil.isSafe((Entity)Surround.mc.player, -1, false)) {
            isSafe = 1;
            if (AntiCity.getInstance().Check == 0)
            placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, -1, false), false, false, true);
        } else {
            isSafe = 2;
        }
        processExtendingBlocks();
        if (didPlace) {
            timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (extendingBlocks.size() == 2 && extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = placements;
            if (areClose(array) != null) {
                placeBlocks(areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore < placements) {
                extendingBlocks.clear();
            }
        } else if (extendingBlocks.size() > 2 || extenders >= 1) {
            extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true)) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        boolean gotHelp = true;
        block5: for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (retries.get(position) == null || retries.get(position) < 4) {
                        placeBlock(position);
                        retries.put(position, retries.get(position) == null ? 1 : retries.get(position) + 1);
                        retryTimer.reset();
                        continue block5;
                    }
                    if (M3dC3t.speedManager.getSpeedKpH() != 0.0 || isExtending || extenders >= 1) continue block5;
                    placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    extendingBlocks.add(vec3d);
                    ++extenders;
                    continue block5;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (Surround.nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            toggle();
        }
        offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        didPlace = false;
        extenders = 1;
        placements = 0;
        obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (isOff()) {
            return true;
        }
        if (retryTimer.passedMs(2500L)) {
            retries.clear();
            retryTimer.reset();
        }
        if (obbySlot == -1 && !offHand && echestSlot == -1) {
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            disable();
            return true;
        }
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        if (Surround.mc.player.inventory.currentItem != lastHotbarSlot && Surround.mc.player.inventory.currentItem != obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
                disable();
            return true;
        }
        return !timer.passedMs(delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (placements < blocksPerTick.getValue()) {
            int originalSlot = Surround.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                toggle();
            }
            isPlacing = true;
            Surround.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.playerController.updateController();
            isSneaking = BlockUtil.placeBlock(pos, offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            didPlace = true;
            ++placements;
        }
    }
}

