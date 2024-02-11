package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.event.events.Render3DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.combat.InstantMine;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.RenderUtil;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BreakESP
        extends Module {
    private static BreakESP INSTANCE;
    BlockPos pos;
    public BreakESP() {
        super("BreakESP", "Show enemy's break packet.", Category.RENDER, true, false, false);
        this.setInstance();
    }

    private Setting<Integer> red = register(new Setting("Red", 255, 0, 255));
    private Setting<Integer> green = register(new Setting("Green", 255, 0, 255));
    private Setting<Integer> blue = register(new Setting("Blue", 255, 0, 255));
    private Setting<Integer> alpha2 = register(new Setting("Alpha", 100, 20, 255));
    private Setting<Integer> alpha = register(new Setting("OutlineAlpha", 100, 20, 255));

    public static BreakESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BreakESP();
        }
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
        if (pos != null && pos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF()))) return;
        if (mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) return;
        pos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
        manxi = 0;
    }

    @SubscribeEvent
    public void BrokenBlock2(PlaySoundEvent event) {
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())))
            return;
        if (!event.getName().endsWith("break")) return;
        if (event.getName().endsWith("potion.break")) return;
        if (pos != null && pos.equals(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF()))) return;
        if (mc.world.getBlockState(new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) return;
        pos = new BlockPos(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF());
        manxi = 0;
    }

    public final Timer imerS = new Timer();
    double manxi=0;

    @Override
    public void onRender3D(Render3DEvent event) {
        if (pos != null) {
            if (imerS.passedMs(10)) {
                if (manxi <= 10) {
                    manxi = manxi + 0.11;
                }
                imerS.reset();
            }

            AxisAlignedBB axisAlignedBB = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos);
            double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
            double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
            double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
            double progressValX = manxi * ((axisAlignedBB.maxX - centerX) / 10);
            double progressValY = manxi * ((axisAlignedBB.maxY - centerY) / 10);
            double progressValZ = manxi * ((axisAlignedBB.maxZ - centerZ) / 10);
            AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                    RenderUtil.drawBBBox(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), alpha.getValue());
                    RenderUtil.drawBBFill(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha2.getValue()), alpha2.getValue());
        }
    }

}

