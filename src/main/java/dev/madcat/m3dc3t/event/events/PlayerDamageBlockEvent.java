/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package dev.madcat.m3dc3t.event.events;

import dev.madcat.m3dc3t.event.EventStage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerDamageBlockEvent
extends EventStage {
    public BlockPos pos;
    public EnumFacing facing;

    public PlayerDamageBlockEvent(int stage, BlockPos pos, EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }

    public final BlockPos getPos() {
        return this.pos;
    }
}

