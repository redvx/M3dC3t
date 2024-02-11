package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCiv extends Module {
    public static EntityPlayer target;
    private static AutoCiv INSTANCE = new AutoCiv();
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
    private Setting<Integer> balpha = register(new Setting("Tick", 15, 7, 20));
    int ticked;

    public AutoCiv() {
        super("AutoCiv", "Automatic oblique angle explosion.", Category.COMBAT, true, false, false);
        setInstance();
    }

    public static AutoCiv Instance() {
        if (INSTANCE == null)
            INSTANCE = new AutoCiv();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onTick() {
        if (fullNullCheck())
            return;
        target = getTarget(this.range.getValue());
        surroundMine();
    }

    @Override
    public void onEnable() {
        ticked = 20;
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    private void surroundMine() {
        if (target == null)
            return;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            return;
        }
        int old = Flatten.mc.player.inventory.currentItem;
        BlockPos feet = new BlockPos(target.posX, target.posY, target.posZ);
        ticked = ticked + 1;
        if (ticked >= balpha.getValue()) {
            if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 3, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 4, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(feet.add(1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
                if (getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR)
                    surroundMine(feet.add(1, 1, 0));
            } else {
                if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 3, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 4, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.switchToSlot(obbySlot);
                    BlockUtil.placeBlock(feet.add(-1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                    this.switchToSlot(old);
                    if (getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR)
                        surroundMine(feet.add(-1, 1, 0));
                } else {
                    if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 3, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 4, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                        this.switchToSlot(obbySlot);
                        BlockUtil.placeBlock(feet.add(0, 1, 1), EnumHand.MAIN_HAND, false, true, false);
                        this.switchToSlot(old);
                        if (getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR)
                            surroundMine(feet.add(0, 1, 1));
                    } else {
                        if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 3, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 4, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                            this.switchToSlot(obbySlot);
                            BlockUtil.placeBlock(feet.add(0, 1, -1), EnumHand.MAIN_HAND, false, true, false);
                            this.switchToSlot(old);
                            if (getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR)
                                surroundMine(feet.add(0, 1, -1));
                        } else {
                            if (InstantMine.breakPos2 != null) {
                                if (!(getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(0, 2, 1)))) {
                                    surroundMine2(feet.add(0, 2, 1));
                                } else {
                                    if (!(getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(0, 3, 1)))) {
                                        surroundMine2(feet.add(0, 3, 1));
                                    } else {
                                        if (!(getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(0, 2, -1)))) {
                                            surroundMine2(feet.add(0, 2, -1));
                                        } else {
                                            if (!(getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 3, -1)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(0, 3, -1)))) {
                                                surroundMine2(feet.add(0, 3, -1));
                                            } else {
                                                if (!(getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(1, 2, 0)))) {
                                                    surroundMine2(feet.add(1, 2, 0));
                                                } else {
                                                    if (!(getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(1, 3, 0)))) {
                                                        surroundMine2(feet.add(1, 3, 0));
                                                    } else {
                                                        if (!(getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(-1, 2, 0)))) {
                                                            surroundMine2(feet.add(-1, 2, 0));
                                                        } else {
                                                            if (!(getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK && InstantMine.breakPos2.equals(feet.add(-1, 3, 0)))) {
                                                                surroundMine2(feet.add(-1, 3, 0));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                                    surroundMine2(feet.add(0, 2, 1));
                                } else {
                                    if (getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.BEDROCK) {
                                        surroundMine2(feet.add(0, 3, 1));
                                    } else {
                                        if (getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                                            surroundMine2(feet.add(0, 2, -1));
                                        } else {
                                            if (getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 3, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 3, -1)).getBlock() != Blocks.BEDROCK) {
                                                surroundMine2(feet.add(0, 3, -1));
                                            } else {
                                                if (getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                    surroundMine2(feet.add(1, 2, 0));
                                                } else {
                                                    if (getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 3, 0)).getBlock() != Blocks.BEDROCK) {
                                                        surroundMine2(feet.add(1, 3, 0));
                                                    } else {
                                                        if (getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                            surroundMine2(feet.add(-1, 2, 0));
                                                        } else {
                                                            if (getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK) {
                                                                surroundMine2(feet.add(-1, 3, 0));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ticked = 0;
        }
    }


    private void surroundMine(BlockPos position) {

        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(position))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(target.posX, target.posY, target.posZ)) && mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() != Blocks.AIR)
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)))
                return;
            if (mc.player.rotationPitch <= 90 && mc.player.rotationPitch >= 80)
                return;
            if (mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB)
                return;
        }
        mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }
    private void surroundMine2(BlockPos position) {
        if (InstantMine.breakPos2 != null) {
            if (InstantMine.breakPos2.equals(position))
                return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(position))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(target.posX, target.posY, target.posZ)) && mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() != Blocks.AIR)
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)))
                return;
            if (mc.player.rotationPitch <= 90 && mc.player.rotationPitch >= 80)
                return;
            if (mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB)
                return;
        }
        mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }

    private void switchToSlot(int slot) {
        Flatten.mc.player.inventory.currentItem = slot;
        Flatten.mc.playerController.updateController();
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || M3dC3t.speedManager.getPlayerSpeed(player) > 10.0) continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (mc.player.getDistanceSq((Entity)player) >= distance) continue;
            target = player;
            distance = mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

}


