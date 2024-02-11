/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 */
package dev.madcat.m3dc3t.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.command.Command;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.MathUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HoleSnap
extends Module {
    private static HoleSnap INSTANCE = new HoleSnap();
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(8.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    private final Setting<Float> range2 = this.register(new Setting<Float>("Timer", Float.valueOf(2.0f), Float.valueOf(0.5f), Float.valueOf(8.0f)));

    public HoleSnap() {
        super("HoleSnap", "Teleports you in a hole.", Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static HoleSnap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleSnap();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        BlockPos pos;
        M3dC3t.holeManager.update();
        List<BlockPos> holes = M3dC3t.holeManager.getSortedHoles();
        if (!holes.isEmpty() && HoleSnap.mc.player.getDistanceSq(pos = holes.get(0)) <= MathUtil.square(this.range.getValue().floatValue())) {
            BlockPos playpos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (playpos == null) return;
            if (this.getBlock(playpos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(playpos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                if (this.getBlock(playpos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(playpos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
                    if (this.getBlock(playpos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(playpos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
                        if (this.getBlock(playpos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(playpos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
                            this.disable();
                            mc.timer.tickLength = 50.0f;
                            return;
                        }
                    }
                }
            }
            if (playpos.getZ() == pos.getY() && playpos.getZ() == pos.getY() && playpos.getX() == pos.getX()) {
                this.disable();
                mc.timer.tickLength = 50.0f;
                return;
            }
            M3dC3t.rotationManager.lookAtPos(pos);
            event.getMovementInput().moveForward = 1.0f;
            mc.timer.tickLength = 50.0f / range2.getValue();
        } else {
this.disable();
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No Hole in range");
        }
    }

    @Override
    public void onDisable() {
        if (mc.timer.tickLength == 50.0f / range2.getValue()) {
            mc.timer.tickLength = 50.0f;
        }
    }
}

