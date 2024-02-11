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
package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.stream.Collectors;


public class AntiCity
        extends Module {
    private static AntiCity INSTANCE = new AntiCity();
    public EntityPlayer target;
    public Entity crystal;
    int Check = 0;
    
    BlockPos CrystalPos;
    BlockPos feet;
    BlockPos pos;
    int obsidian = -1;
    BlockPos startPos;
    int checke = 0;
    public Setting<Boolean> rotate = register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> packet = register(new Setting<Boolean>("Packet", true));

    public Setting<Boolean> old = register(new Setting<Boolean>("OldMode", false));
    public Setting<Boolean> ac2 = register(new Setting<Boolean>("SurroundExtend", true, v -> old.getValue()));
    public Setting<Boolean> ac = register(new Setting<Boolean>("SurroundExtend+", true, v -> ac2.getValue() && old.getValue()));
    public Setting<Boolean> sm = register(new Setting<Boolean>("Smart", true, v -> old.getValue()));

    public AntiCity() {
        super("AntiCity", "Very mart surround extend.", Module.Category.COMBAT, true, false, false);
        setInstance();
    }

    public static AntiCity getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new AntiCity();
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if (!old.getValue()) return;
        if (startPos == null && checke == 1) {
            checke = 0;
            return;
        }
        if (checke == 1 && !startPos.equals((Object) EntityUtil.getRoundedBlockPos((Entity) mc.player)))
            checke = 0;
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (!sm.getValue()) return;
        if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
                    if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
                        if (M3dC3t.moduleManager.isModuleEnabled("Surround")) {
                            M3dC3t.moduleManager.disableModule("Surround");
                        }
                        if (checke == 1) return;
                        checke = 1;
                        startPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                    }
                }
            }
        }
    }
    private void setInstance() {
        INSTANCE = this;
    }


    @Override
    public void onEnable() {
        checke = 0;
    }
    
    @Override
    public void onDisable() {
        checke = 0;
        Check = 0;
    }

    @Override
    public void onUpdate() {
        if (!old.getValue()) {
            pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (M3dC3t.moduleManager.isModuleEnabled("Surround")) {
                if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                        if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
                            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
                                Check = 1;
                            }
                        }
                    }
                }
            } else {
                Check = 0;
                return;
            }
            if (Check == 0) return;
            target = getTarget();
            if (target == null) return;
            feet = new BlockPos(target.posX, target.posY, target.posZ);
            if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) return;
            perform(pos.add(0, 0, 1));
            perform(pos.add(0, 0, -1));
            perform(pos.add(1, 0, 0));
            perform(pos.add(-1, 0, 0));
            perform(pos.add(1, -1, 0));
            perform(pos.add(-1, -1, 0));
            perform(pos.add(0, -1, 1));
            perform(pos.add(0, -1, -1));
            perform(pos.add(0, 0, 2));
            perform(pos.add(0, 0, -2));
            perform(pos.add(2, 0, 0));
            perform(pos.add(-2, 0, 0));
            perform(pos.add(1, 0, 1));
            perform(pos.add(-1, 0, 1));
            perform(pos.add(1, 0, -1));
            perform(pos.add(-1, 0, -1));

            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 0))))
                perform(pos.add(1, 1, 0));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 0))))
                perform(pos.add(-1, 1, 0));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, 1))))
                perform(pos.add(0, 1, 1));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 0, -1))))
                perform(pos.add(0, 1, -1));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, 1))))
                perform(pos.add(1, 1, 1));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 0, -1))))
                perform(pos.add(1, 1, -1));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, 1))))
                perform(pos.add(-1, 1, 1));
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 0, -1))))
                perform(pos.add(-1, 1, -1));

            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 0))))
                perform(pos.add(1, 1, 0));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 0))))
                perform(pos.add(-1, 1, 0));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, 1))))
                perform(pos.add(0, 1, 1));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 0, -1))))
                perform(pos.add(0, 1, -1));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, 1))))
                perform(pos.add(1, 1, 1));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 0, -1))))
                perform(pos.add(1, 1, -1));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, 1))))
                perform(pos.add(-1, 1, 1));
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 0, -1))))
                perform(pos.add(-1, 1, -1));

            if (new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0))))
                perform(pos.add(2, 0, 0));
            perform(pos.add(2, 1, 0));
            perform(pos.add(3, 0, 0));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0))))
                perform(pos.add(-2, 0, 0));
            perform(pos.add(-2, 1, 0));
            perform(pos.add(-3, 0, 0));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1))))
                perform(pos.add(0, 0, 2));
            perform(pos.add(0, 1, 2));
            perform(pos.add(0, 0, 3));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1))))
                perform(pos.add(0, 0, -2));
            perform(pos.add(0, 1, -2));
            perform(pos.add(0, 0, -3));

            if (new BlockPos(feet).equals(new BlockPos(pos.add(2, -1, 0))))
                perform(pos.add(1, 0, 0));
            perform(pos.add(2, 1, 0));
            perform(pos.add(3, 0, 0));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(-2, -1, 0))))
                perform(pos.add(-1, 0, 0));
            perform(pos.add(-2, 1, 0));
            perform(pos.add(-3, 0, 0));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 2))))
                perform(pos.add(0, 0, 1));
            perform(pos.add(0, 1, 2));
            perform(pos.add(0, 0, 3));
            if (new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -2))))
                perform(pos.add(0, 0, -1));
            perform(pos.add(0, 1, -2));
            perform(pos.add(0, 0, -3));
            crystal = getEndCrystal();
            if (crystal == null) return;
            CrystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, 1)))) {
                AttackCrystal(pos.add(1, 1, 1));
                if ((feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, 1)))) && (feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, 1)))) && mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 1, 1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, 1)))) && mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 2, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 2, 1));
            } else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, -1)))) {
                AttackCrystal(pos.add(1, 1, -1));
                if ((feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, -1)))) && (feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, -1)))) && mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 1, -1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 2, -1)))) && mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(1, 2, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(1, 2, -1));
            } else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, -1)))) {
                AttackCrystal(pos.add(-1, 1, -1));
                if ((feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, -1)))) && (feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, -1)))) && mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 1, -1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, -1)))) && mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, -1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 2, -1));
            } else if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, 1)))) {
                AttackCrystal(pos.add(-1, 1, 1));
                if ((feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, 1)))) && (feet == null || !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, 1)))) && mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 1, 1));
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 2, 1)))) && mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.add(-1, 2, 1)).getBlock() == Blocks.AIR)
                    perform(pos.add(-1, 2, 1));
            }
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, 1)))) {
                AttackCrystal(pos.add(0, 1, 1));
                perform(pos.add(0, 1, 1));
                if (getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 2, 1));
                    else if (getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 1, 1));
                }
                if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, 1)))) {
                    perform(pos.add(0, 2, 1));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, 1)))) {
                    perform(pos.add(0, 2, 1));
                }
            }
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(0, 1, -1)))) {
                AttackCrystal(pos.add(0, 1, -1));
                perform(pos.add(0, 1, -1));
                if (getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 2, -1));
                    else if (getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 1, -1));
                }
                if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(0, 1, -1)))) {
                    perform(pos.add(0, 2, -1));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(0, 1, -1)))) {
                    perform(pos.add(0, 2, -1));
                }
            }
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(1, 1, 0)))) {
                AttackCrystal(pos.add(1, 1, 0));
                perform(pos.add(1, 1, 0));
                if (getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 2, 1));
                    else if (getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 1, 1));
                }
                if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(1, 1, 0)))) {
                    perform(pos.add(1, 2, 0));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(1, 1, 0)))) {
                    perform(pos.add(1, 2, 0));
                }
            }
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos.add(-1, 1, 0)))) {
                AttackCrystal(pos.add(-1, 1, 0));
                perform(pos.add(-1, 1, 0));
                if (getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 2, -1));
                    else if (getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 1, -1));
                }
                if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos.add(-1, 1, 0)))) {
                    perform(pos.add(-1, 2, 0));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos.add(-1, 1, 0)))) {
                    perform(pos.add(-1, 2, 0));
                }
            }
        } else {
            Vec3d a = mc.player.getPositionVector();
            obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
            target = getTarget();
            if (target == null) {
                return;
            }
            BlockPos feet = new BlockPos(target.posX, target.posY, target.posZ);
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (obsidian == -1) {
                return;
            }
            if (ac2.getValue()) {
                if (M3dC3t.moduleManager.isModuleEnabled("Surround")) {
                    if (getBlock(pos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                        perform(pos.add(0, 0, -1));
                        perform(pos.add(0, 0, -2));
                        perform(pos.add(1, 0, -1));
                        perform(pos.add(-1, 0, -1));
                        if (ac.getValue()) {
                            perform(pos.add(0, 1, -2));
                            perform(pos.add(0, 1, -1));
                        }
                    }
                    if (getBlock(pos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                        perform(pos.add(0, 0, 1));
                        perform(pos.add(0, 0, 2));
                        perform(pos.add(1, 0, 1));
                        perform(pos.add(-1, 0, 1));
                        if (ac.getValue()) {
                            perform(pos.add(0, 1, 2));
                            perform(pos.add(0, 1, 1));
                        }
                    }
                    if (getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                        perform(pos.add(1, 0, 0));
                        perform(pos.add(2, 0, 0));
                        perform(pos.add(1, 0, 1));
                        perform(pos.add(1, 0, -1));
                        if (ac.getValue()) {
                            perform(pos.add(2, 1, 0));
                            perform(pos.add(1, 1, 0));
                        }
                    }
                    if (getBlock(pos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                        perform(pos.add(-1, 0, 0));
                        perform(pos.add(-2, 0, 0));
                        perform(pos.add(-1, 0, 1));
                        perform(pos.add(-1, 0, -1));
                        if (ac.getValue()) {
                            perform(pos.add(-2, 1, 0));
                            perform(pos.add(-1, 1, 0));
                        }
                    }
                }
            }
            if (checke == 0) return;
            if (!sm.getValue()) return;
            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)), true);
                    place(a, EntityUtil.getVarOffsets(0, 1, 1));
                    place(a, EntityUtil.getVarOffsets(0, 1, 2));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)), true);
                    place(a, EntityUtil.getVarOffsets(0, 1, -1));
                    place(a, EntityUtil.getVarOffsets(0, 1, -2));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)), true);
                    place(a, EntityUtil.getVarOffsets(1, 1, 0));
                    place(a, EntityUtil.getVarOffsets(2, 1, 0));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)), true);
                    place(a, EntityUtil.getVarOffsets(-1, 1, 0));
                    place(a, EntityUtil.getVarOffsets(-2, 1, 0));
                }
            }

            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
                    place(a, EntityUtil.getVarOffsets(0, 0, 1));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)), true);
                    place(a, EntityUtil.getVarOffsets(0, 0, -1));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
                    place(a, EntityUtil.getVarOffsets(1, 0, 0));
                }
            }
            if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
                    place(a, EntityUtil.getVarOffsets(-1, 0, 0));
                }
            }

            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(2, 1, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(2, 0, 0))))
                    perform(pos.add(2, 1, 0));
            }
            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-2, 1, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-2, 0, 0))))
                    perform(pos.add(-2, 1, 0));
            }
            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 1, 2))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 2))))
                    perform(pos.add(0, 1, 2));
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 1, -2))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -2))))
                    perform(pos.add(0, 1, -2));
            }

            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, -1, 1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -2, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                        perform(pos.add(0, -1, 1));
                        perform(pos.add(0, 0, 1));
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, -1, -1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -2, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                        perform(pos.add(0, -1, -1));
                        perform(pos.add(0, 0, -1));
                    }
                }
            }
            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, -1, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -2, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                        perform(pos.add(1, -1, 0));
                        perform(pos.add(1, 0, 0));
                    }
                }
            }
            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -2, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                        perform(pos.add(-1, -1, 0));
                        perform(pos.add(-1, 0, 0));
                    }
                }
            }

            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 2))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 2)))) {
                        if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) == null) {
                            perform(pos.add(0, 0, 1));
                            perform(pos.add(0, 0, 2));
                            perform(pos.add(0, -1, 2));
                            perform(pos.add(0, 0, 3));
                        } else {
                            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null && checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                                EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)), true);
                                perform(pos.add(0, 0, 1));
                                perform(pos.add(0, 0, 2));
                                perform(pos.add(0, -1, 2));
                                perform(pos.add(0, 0, 3));
                            }
                        }
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -2))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -2)))) {
                        if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) == null) {
                            perform(pos.add(0, 0, -1));
                            perform(pos.add(0, 0, -2));
                            perform(pos.add(0, -1, -2));
                            perform(pos.add(0, 0, -3));
                        } else {
                            if (checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null && checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                                EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)), true);
                                perform(pos.add(0, 0, -1));
                                perform(pos.add(0, 0, -2));
                                perform(pos.add(0, -1, -2));
                                perform(pos.add(0, 0, -3));
                            }
                        }
                    }
                }
            }
            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(2, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(2, -1, 0)))) {
                        if (checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) == null) {
                            perform(pos.add(1, 0, 0));
                            perform(pos.add(2, 0, 0));
                            perform(pos.add(2, -1, 0));
                            perform(pos.add(3, 0, 0));
                        } else {
                            if (checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null && checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                                EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)), true);
                                perform(pos.add(1, 0, 0));
                                perform(pos.add(2, 0, 0));
                                perform(pos.add(2, -1, 0));
                                perform(pos.add(3, 0, 0));
                            }
                        }
                    }
                }
            }
            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-2, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-2, -1, 0)))) {
                        if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) == null) {
                            perform(pos.add(-1, 0, 0));
                            perform(pos.add(-2, 0, 0));
                            perform(pos.add(-2, -1, 0));
                            perform(pos.add(-3, 0, 0));
                        } else {
                            if (checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null && checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                                EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)), true);
                                perform(pos.add(-1, 0, 0));
                                perform(pos.add(-2, 0, 0));
                                perform(pos.add(-2, -1, 0));
                                perform(pos.add(-3, 0, 0));
                            }
                        }
                    }
                }
            }

            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1)))) {
                        perform(pos.add(0, 0, 1));
                        perform(pos.add(0, 1, 1));
                        perform(pos.add(1, 1, 1));
                        perform(pos.add(0, 1, 2));
                    }
                } else {
                    if (getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                        perform(pos.add(0, 0, 2));
                        perform(pos.add(0, 1, 2));
                        perform(pos.add(1, 0, 2));
                        perform(pos.add(1, 1, 2));
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1)))) {
                        perform(pos.add(0, 0, -1));
                        perform(pos.add(0, 1, -1));
                        perform(pos.add(-1, 1, -1));
                        perform(pos.add(0, 1, -2));
                    }
                } else {
                    if (getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                        perform(pos.add(0, 0, -2));
                        perform(pos.add(0, 1, -2));
                        perform(pos.add(1, 0, -2));
                        perform(pos.add(1, 1, -2));
                    }
                }
            }
            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0)))) {
                        perform(pos.add(1, 0, 0));
                        perform(pos.add(1, 1, 0));
                        perform(pos.add(1, 1, 1));
                        perform(pos.add(2, 1, 0));
                    }
                } else {
                    if (getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                        perform(pos.add(2, 0, 0));
                        perform(pos.add(2, 1, 0));
                        perform(pos.add(2, 0, 1));
                        perform(pos.add(2, 1, 1));
                    }
                }
            }
            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0)))) {
                        perform(pos.add(-1, 0, 0));
                        perform(pos.add(-1, 1, 0));
                        perform(pos.add(-1, 1, -1));
                        perform(pos.add(-2, 1, 0));
                    }
                } else {
                    if (getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                        perform(pos.add(-2, 0, 0));
                        perform(pos.add(-2, 1, 0));
                        perform(pos.add(-2, 0, 1));
                        perform(pos.add(-2, 1, 1));
                    }
                }
            }

            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                        perform(pos.add(1, 0, 0));
                        perform(pos.add(1, 0, 1));
                    }
                }
            }
            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                        perform(pos.add(0, 0, 1));
                        perform(pos.add(1, 0, 1));
                    }
                }
            }

            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                        perform(pos.add(-1, 0, 0));
                        perform(pos.add(-1, 0, -1));
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                        perform(pos.add(0, 0, -1));
                        perform(pos.add(-1, 0, -1));
                    }
                }
            }

            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                        perform(pos.add(-1, 0, 0));
                        perform(pos.add(-1, 0, 1));
                    }
                }
            }
            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                        perform(pos.add(0, 0, 1));
                        perform(pos.add(-1, 0, 1));
                    }
                }
            }

            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                        perform(pos.add(1, 0, 0));
                        perform(pos.add(1, 0, -1));
                    }
                }

            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
                if (checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null) {
                    EntityUtil.attackEntity(checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
                }
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                        perform(pos.add(0, 0, -1));
                        perform(pos.add(1, 0, -1));
                    }
                }

            }


            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                            perform(pos.add(1, 0, 0));
                            perform(pos.add(1, 0, 1));
                            perform(pos.add(1, 1, 1));
                        }
                    }
                }
            }
            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 1)))) {
                            perform(pos.add(0, 0, 1));
                            perform(pos.add(1, 0, 1));
                            perform(pos.add(1, 1, 1));
                        }
                    }
                }
            }

            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                            perform(pos.add(-1, 0, 0));
                            perform(pos.add(-1, 0, -1));
                            perform(pos.add(-1, 1, -1));
                        }
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, -1)))) {
                            perform(pos.add(0, 0, -1));
                            perform(pos.add(-1, 0, -1));
                            perform(pos.add(-1, 1, -1));
                        }
                    }
                }
            }

            if (getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 0)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                            perform(pos.add(-1, 0, 0));
                            perform(pos.add(-1, 0, 1));
                            perform(pos.add(-1, 1, 1));
                        }
                    }
                }
            }
            if (getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, 1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, 1)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(-1, -1, 1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(-1, 0, 1)))) {
                            perform(pos.add(0, 0, 1));
                            perform(pos.add(-1, 0, 1));
                            perform(pos.add(-1, 1, 1));
                        }
                    }
                }
            }

            if (getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, 0))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, 0)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                            perform(pos.add(1, 0, 0));
                            perform(pos.add(1, 0, -1));
                            perform(pos.add(1, 1, -1));
                        }
                    }
                }
            }
            if (getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, 1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                    if (!new BlockPos(feet).equals(new BlockPos(pos.add(0, 0, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(0, -1, -1)))) {
                        if (!new BlockPos(feet).equals(new BlockPos(pos.add(1, -1, -1))) && !new BlockPos(feet).equals(new BlockPos(pos.add(1, 0, -1)))) {
                            perform(pos.add(0, 0, -1));
                            perform(pos.add(1, 0, -1));
                            perform(pos.add(1, 1, -1));
                        }
                    }
                }
            }

        }
    }


    private void perform(BlockPos pos2) {
        int old = mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
            if (new BlockPos(feet).equals(new BlockPos(pos2))) return;
            if (new BlockPos(feet).equals(new BlockPos(pos2.add(0, -1, 0)))) return;
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
            mc.player.inventory.currentItem = old;
            mc.playerController.updateController();
        }
        crystal = getEndCrystal();
        if (crystal == null) return;
        CrystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (new BlockPos(CrystalPos).equals(new BlockPos(pos2))) AttackCrystal(pos2);
    }

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        Vec3d[] var4 = list;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = var4[var6];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }

    boolean isSneaking;
    private void place(Vec3d pos, Vec3d[] list) {
        Vec3d[] var3 = list;
        int var4 = list.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            Vec3d vec3d = var3[var5];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            int a = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            mc.playerController.updateController();
            isSneaking = BlockUtil.placeBlock(position, EnumHand.MAIN_HAND, false, packet.getValue(), true);
            mc.player.inventory.currentItem = a;
            mc.playerController.updateController();
        }
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

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }




    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = 12;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, 12) || M3dC3t.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0) continue;
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
}

