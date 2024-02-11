package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AntiBurrow
        extends Module {
    private final Setting<Double> range = this.register(new Setting<Double>("Range", 6.0, 1.0, 8.0));
    public static BlockPos pos;
    int ticked = 0;

    public AntiBurrow() {
        super("AntiBurrow", "Automatically dig the enemy's burrow.", Module.Category.COMBAT, true, false, false);
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || M3dC3t.speedManager.getPlayerSpeed(player) > 10.0) continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (mc.player.getDistanceSq(player) >= distance) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }

    EntityPlayer player;
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        if (mc.currentScreen instanceof GuiHopper) {
            return;
        }
        player = this.getTarget(this.range.getValue());
        if (player == null) {
            return;
        }
        pos = new BlockPos(player.posX, player.posY + 0.5, player.posZ);
        if (ticked >= 0) ticked = ticked + 1;
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals(pos) && ticked >= 60 && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos).getBlock() != Blocks.AIR && mc.world.getBlockState(pos).getBlock() != Blocks.WEB && mc.world.getBlockState(pos).getBlock() != Blocks.REDSTONE_WIRE && !this.isOnLiquid() && !this.isInLiquid() && mc.world.getBlockState(pos).getBlock() != Blocks.WATER && mc.world.getBlockState(pos).getBlock() != Blocks.LAVA) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));
            ticked = 1;
        }
        if (InstantMine.breakPos2 != null) {
            if (InstantMine.breakPos2.equals(pos)) {
                return;
            }
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(pos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ))) {
                return;
            }
            if (mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos).getBlock() != Blocks.WEB && mc.world.getBlockState(pos).getBlock() != Blocks.REDSTONE_WIRE && !this.isOnLiquid() && !this.isInLiquid() && mc.world.getBlockState(pos).getBlock() != Blocks.WATER && mc.world.getBlockState(pos).getBlock() != Blocks.LAVA) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));
            ticked = 1;
        }
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (player != null) {
            return player.getName();
        }
        return null;
    }

    private boolean isOnLiquid() {
        double y = mc.player.posY - 0.03;
        for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); ++x) {
            for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        double y = mc.player.posY + 0.01;
        for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); ++x) {
            for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

}

