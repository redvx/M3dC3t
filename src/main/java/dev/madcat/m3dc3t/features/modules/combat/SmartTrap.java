package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SmartTrap
        extends Module {
    public EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
    private final Setting<Boolean> web = this.register(new Setting<>("WebHead", false));
    private final Setting<Boolean> feet = this.register(new Setting<>("Feet", false));
    

    public SmartTrap() {
        super("AutoTrap", "Automatically trap the enemy.", Category.COMBAT, true, false, false);
    }





    @Override
    public void onTick() {
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals((Object) new BlockPos(people.add(0, 2, 0))))
            return;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            return;
        }
        int webSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (webSlot == -1) {
            return;
        }
        if (this.web.getValue()) {
            webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
            if (webSlot == -1) {
                webSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                if (webSlot == -1) {
                    return;
                }
            }
        }
        int old = mc.player.inventory.currentItem;
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(people.add(0, 2, 0)))) return;
        if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(people.add(0, 2, 0)))) return;
        if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            if (this.getBlock(people.add(1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, 1)).getBlock() != Blocks.AIR || this.getBlock(people.add(-1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(webSlot);
                BlockUtil.placeBlock(people.add(0, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 1, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 1, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            } else if (this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 0, -1), EnumHand.MAIN_HAND, false, true, false);
                BlockUtil.placeBlock(people.add(0, 0, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
        }
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || M3dC3t.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0) continue;
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

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    private void switchToSlot(int slot) {
        SmartTrap.mc.player.inventory.currentItem = slot;
        SmartTrap.mc.playerController.updateController();
    }

}

