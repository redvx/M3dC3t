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
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import dev.madcat.m3dc3t.util.MathUtil;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class PistonKicker
        extends Module {
    public static EntityPlayer target;

    public PistonKicker() {
        super("PistonKicker", "Automatically kick out the enemy's hole.", Category.COMBAT, true, false, false);
    }

    private final Setting<Float> range = this.register(new Setting<>("Range", 8.0f, 1.0f, 12.0f));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Disable", true));
    private final Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", true));

    @Override
    public void onUpdate() {
        if (InventoryUtil.findHotbarBlock(BlockPistonBase.class) == -1) {
            if (disable.getValue()) {
                this.toggle();
            }
            return;
        }
        if ((InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1)) {
            if (disable.getValue()) {
                this.toggle();
            }
            return;
        }
        target = getTarget(range.getValue());
        if (target == null) {
            if (disable.getValue()) {
                this.toggle();
            }
            return;
        }
        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        if (angle[1] >= -71 && angle[1] <= 71 && angle[0] >= -51 && angle[0] <= 51 && getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && (getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR | getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(0, 1, 1));
            if (getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 2, 1));
            } else if (getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 1, 1));
            } else if (getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 1, 2));
            } else if (getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 1, 1));
            }
        } else if (angle[1] >= -71 && angle[1] <= 71 && (angle[0] >= 129 | angle[0] <= -129) && getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && (getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR | getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(0, 1, -1));
            if (getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 2, -1));
            } else if (getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 1, -1));
            } else if (getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 1, -2));
            } else if (getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 1, -1));
            }
        } else if (angle[1] >= -71 && angle[1] <= 71 && angle[0] <= -51 && angle[0] >= -129 && getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && (getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR | getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(1, 1, 0));
            if (getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 2, 0));
            } else if (getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 1, 1));
            } else if (getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(2, 1, 0));
            } else if (getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 1, -1));
            }
        } else if (angle[1] >= -71 && angle[1] <= 71 && angle[0] >= 51 && angle[0] <= 129 && getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && (getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR | getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(-1, 1, 0));
            if (getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 2, 0));
            } else if (getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 1, 1));
            } else if (getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-2, 1, 0));
            } else if (getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
            } else if (getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 1, -1));
            }
        } else {
            if (disable.getValue()) {
                this.toggle();
            }
        }
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!debug.getValue()) return;
        if (InventoryUtil.findHotbarBlock(BlockPistonBase.class) == -1) return;
        if ((InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1)) return;
        target = getTarget(range.getValue());
        if (target == null)
            return;
        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        mc.fontRenderer.drawString(angle[0] + "   " + angle[1], 200, 200, 255, true);
    }


    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity) player, range) || M3dC3t.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq((Entity) player);
                continue;
            }
            if (mc.player.getDistanceSq((Entity) player) >= distance) continue;
            target = player;
            distance = mc.player.getDistanceSq((Entity) player);
        }
        return target;
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

    private void perform(BlockPos pos) {
        int old = AntiCity.mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockPistonBase.class);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            mc.player.inventory.currentItem = old;
            mc.playerController.updateController();
        }
    }

    private void perform1(BlockPos pos) {
        int old = AntiCity.mc.player.inventory.currentItem;
        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            mc.player.inventory.currentItem = old;
            mc.playerController.updateController();
        }
    }
}

