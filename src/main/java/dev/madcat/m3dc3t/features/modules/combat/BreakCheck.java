package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.features.modules.Module;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakCheck extends Module {
    private static BreakCheck INSTANCE = new BreakCheck();
    public BreakCheck() {
        super("BreakCheck", "Check instant mine.", Category.COMBAT, true, false, false);
        setInstance();
    }

    public static BreakCheck Instance() {
        if (INSTANCE == null)
            INSTANCE = new BreakCheck();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void BrokenBlock(PlaySoundEvent event) {
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())))
            return;
        if (!event.getName().endsWith("hit")) return;
        if (event.getName().endsWith("arrow.hit")) return;
        if (event.getName().endsWith("stand.hit")) return;
        if (mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) return;
        BrokenPos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
    }

    @SubscribeEvent
    public void BrokenBlock2(PlaySoundEvent event) {
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())))
            return;
        if (!event.getName().endsWith("break")) return;
        if (event.getName().endsWith("potion.break")) return;
        if (mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) return;
        BrokenPos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
    }

    public BlockPos BrokenPos;
    
}


