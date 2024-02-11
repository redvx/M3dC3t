package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.combat.InstantMine;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.MathUtil;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class Nuker
        extends Module {
    private final Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 1, 6));
    private Setting<Boolean> logout = this.register(new Setting<Boolean>("BreakAll", false));
    private Setting<Boolean> hp = this.register(new Setting<Boolean>("32kMode", false));
    private final Setting<Integer> saferange = this.register(new Setting<Integer>("SafeRange", 2, 0, 6, v -> !logout.getValue()));

    public Nuker() {
        super("Nuker", "Crazy dig.", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        for (BlockPos blockPos : this.breakPos(this.range.getValue().intValue())) {
            if (mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6) && hp.getValue() && InstantMine.breakPos != null && mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper)
                return;
            if (mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6) && InstantMine.breakPos != null && mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockShulkerBox)
                return;
            if (mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6) && hp.getValue() && InstantMine.breakPos != null && mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper)
                return;
            if (mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6) && logout.getValue() && mc.world.getBlockState(InstantMine.breakPos).getBlock() != Blocks.AIR)
                return;
            if (!logout.getValue()) {
                if (Nuker.mc.player.getDistanceSq(blockPos) < MathUtil.square(this.saferange.getValue().intValue()) || blockPos == null)
                    continue;
                if (hp.getValue())
                    if (Nuker.mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper) {
                        Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                    } else if (Nuker.mc.world.getBlockState(blockPos).getBlock() instanceof BlockShulkerBox) {
                        Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                    }
            } else if (Nuker.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && Nuker.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
                Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
            }
        }
    }

    private NonNullList<BlockPos> breakPos(float placeRange) {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(Nuker.mc.player.posX), Math.floor(Nuker.mc.player.posY), Math.floor(Nuker.mc.player.posZ)), placeRange, 0, false, true, 0));
        return positions;
    }
}
