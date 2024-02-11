package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.event.events.UpdateWalkingPlayerEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockFly
        extends Module {
    private final Timer timer = new Timer();
    public Setting<Boolean> rotation = this.register(new Setting<Boolean>("Rotate", Boolean.FALSE));
    private BlockPos pos;

    public BlockFly() {
        super("BlockFly", "Places Blocks underneath you.", Module.Category.MOVEMENT, true, false, false);
    }


    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
        BlockPos playerBlock;
        if (this.isOff() || BlockFly.fullNullCheck() || event.getStage() == 0) {
            return;
        }
        if (!BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.timer.reset();
        }
        if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).add(0, -1, 0))) {
            if (BlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
            } else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
            } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
            } else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
            } else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
            } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
            } else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                }
                this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
            } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
            } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
            }
        }
    }

    public void place(BlockPos posI, EnumFacing face) {
        Block block;
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        } else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        } else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        } else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        int oldSlot = BlockFly.mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BlockFly.mc.player.inventory.getStackInSlot(i);
            if (InventoryUtil.isNull(stack) || !(stack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem((Item)stack.getItem()).getDefaultState().isFullBlock()) continue;
            newSlot = i;
            break;
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        if (!BlockFly.mc.player.isSneaking() && BlockUtil.blackList.contains(block = BlockFly.mc.world.getBlockState(pos).getBlock())) {
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockFly.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(BlockFly.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            BlockFly.mc.player.inventory.currentItem = newSlot;
            BlockFly.mc.playerController.updateController();
        }
        if (BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            BlockFly.mc.player.motionX *= 0.3;
            BlockFly.mc.player.motionZ *= 0.3;
            BlockFly.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                BlockFly.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(BlockFly.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.normalizeAngle((int)((int)angle[1]), (int)360), BlockFly.mc.player.onGround));
        }
        BlockFly.mc.playerController.processRightClickBlock(BlockFly.mc.player, BlockFly.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        BlockFly.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockFly.mc.player.inventory.currentItem = oldSlot;
        BlockFly.mc.playerController.updateController();
        if (crouched) {
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockFly.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}

