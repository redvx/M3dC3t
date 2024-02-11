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
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.stream.Collectors;

public class AntiCev
        extends Module {
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    public Setting<Boolean> highCiv = this.register(new Setting<Boolean>("HighCiv", false));
    public Setting<Boolean> oldCev = this.register(new Setting<Boolean>("OldCev", false));
    int CevHigh = 0;
BlockPos startPos;
    public AntiCev() {
        super("AntiCev", "Anti straight line explosion and oblique angle explosion.", Module.Category.COMBAT, true, false, false);
    }

    BlockPos crystal;

    @Override
    public void onTick() {
        if (!fullNullCheck() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            Vec3d a = AntiCev.mc.player.getPositionVector();
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            Entity target = this.getTarget();
            if (target != null) crystal = new BlockPos(target.posX, target.posY, target.posZ);
            else crystal = null;
            if (oldCev.getValue()) {
                if (crystal == null) return;
                if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, 1)))) {
                    perform(pos.add(0, 1, 1));
                    perform(pos.add(0, 2, 1));
                }
                if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, -1)))) {
                    perform(pos.add(0, 1, -1));
                    perform(pos.add(0, 2, -1));
                }
                if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(1, 3, 0)))) {
                    perform(pos.add(1, 1, 0));
                    perform(pos.add(1, 2, 0));
                }
                if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(-1, 3, 0)))) {
                    perform(pos.add(-1, 1, 0));
                    perform(pos.add(-1, 2, 0));
                }
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, 0)))) {
                    mc.player.jump();
                    if (!mc.player.onGround) {
                        attackCrystal(pos.add(0, 3, 0));
                        perform(pos.add(0, 3, 0));
                        if (this.getBlock(pos.add(1, 3, 0)).getBlock() != Blocks.AIR)
                            perform(pos.add(1, 4, 0));
                        else if (this.getBlock(pos.add(1, 2, 0)).getBlock() != Blocks.AIR)
                            perform(pos.add(1, 3, 0));
                        else if (this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.AIR)
                            perform(pos.add(1, 2, 0));
                        else if (this.getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.AIR)
                            perform(pos.add(1, 1, 0));
                        perform(pos.add(0, 4, 0));
                    }
                }
                if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 4, 0)))) {
                    perform(pos.add(0, 2, 0));
                    perform(pos.add(0, 3, 0));
                }
                if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 5, 0)))) {
                    perform(pos.add(0, 4, 0));
                    perform(pos.add(0, 3, 0));
                }
            } else {
                if (crystal != null && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, 0)))) {
                    CevHigh = 1;
                }
                if (CevHigh == 1 && crystal != null && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, 0)))) {
                    mc.player.jump();
                    if (!mc.player.onGround) attackCrystal(pos.add(0, 3, 0));
                }
                if (CevHigh == 1 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)) == null) {
                    perform(pos.add(0, 3, 0));
                    if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.AIR) CevHigh = 0;
                }
                if (crystal != null && this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 4, 0)))) {
                    CevHigh = 2;
                }
                if (CevHigh == 2 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 4, 0)) == null) {
                    perform(pos.add(0, 4, 0));
                    if (this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.AIR) CevHigh = 0;
                }
                if (crystal != null && this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 5, 0)))) {
                    CevHigh = 3;
                }
                if (CevHigh == 3 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 5, 0)) == null) {
                    perform(pos.add(0, 5, 0));
                    if (this.getBlock(pos.add(0, 5, 0)).getBlock() != Blocks.AIR) CevHigh = 0;
                }

                if (crystal == null) return;
                if (M3dC3t.moduleManager.isModuleEnabled("Surround") && highCiv.getValue()) {
                    if (this.getBlock(pos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, 1)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        M3dC3t.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(0, 3, 1));
                        perform(pos.add(0, 3, 1));
                    }

                    if (this.getBlock(pos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 3, -1)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        M3dC3t.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(0, 3, -1));
                        perform(pos.add(0, 3, -1));
                    }

                    if (this.getBlock(pos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(1, 3, 0)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        M3dC3t.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(1, 3, 0));
                        perform(pos.add(1, 3, 0));
                    }

                    if (this.getBlock(pos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos(crystal).equals(new BlockPos(pos.add(-1, 3, 0)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        M3dC3t.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(-1, 3, 0));
                        perform(pos.add(-1, 3, 0));
                    }
                }


            }
            if (crystal == null) return;
            if (this.getBlock(pos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 2, 1)))) {
                attackCrystal(pos.add(0, 2, 1));
                perform(pos.add(0, 2, 1));
                perform(pos.add(0, 3, 1));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 2, 1));
                    else if (this.getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 1, 1));
                }
            }
            if (this.getBlock(pos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(0, 2, -1)))) {
                attackCrystal(pos.add(0, 2, -1));
                perform(pos.add(0, 2, -1));
                perform(pos.add(0, 3, -1));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 2, -1));
                    else if (this.getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 1, -1));
                }
            }
            if (this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(1, 2, 0)))) {
                attackCrystal(pos.add(1, 2, 0));
                perform(pos.add(1, 2, 0));
                perform(pos.add(1, 3, 0));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 2, 1));
                    else if (this.getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR)
                        perform(pos.add(1, 1, 1));
                }
            }
            if (this.getBlock(pos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(crystal).equals(new BlockPos(pos.add(-1, 2, 0)))) {
                attackCrystal(pos.add(-1, 2, 0));
                perform(pos.add(-1, 2, 0));
                perform(pos.add(-1, 3, 0));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 2, -1));
                    else if (this.getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR)
                        perform(pos.add(-1, 1, -1));
                }
            }

        }
    }


    public static void attackCrystal(BlockPos pos) {
        for (Entity crystal : mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistanceSq(pos) <= 1.0)) continue;
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    private Entity getTarget() {
        Entity target = null;
        for (Entity player : mc.world.loadedEntityList) {
            if (!(player instanceof EntityEnderCrystal)) continue;
            target = player;
        }
        return target;
    }

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = list[var6];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : AntiCev.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }





    

    private void perform(BlockPos pos2) {
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            int old = mc.player.inventory.currentItem;
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) return;
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) return;
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                mc.player.inventory.currentItem = old;
                mc.playerController.updateController();
            }
        }
    }
}