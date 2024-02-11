// Decompiled with: Procyon 0.6.0
// Class Version: 8
package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.event.events.Render3DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.client.ClickGui;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleFiller extends Module
{
    private static BlockPos PlayerPos;
    private static boolean togglePitch;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private static HoleFiller INSTANCE;
    private final Setting<Double> range;
    private final Setting<Double> smartRange;
    private final Setting<Boolean> web;
    private final Setting<Boolean> rainbow;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> outlineAlpha;
    private final long systemTime;
    private final boolean switchCooldown;
    private final boolean isAttacking;
    double d;
    private BlockPos render;
    private Entity renderEnt;
    private EntityPlayer closestTarget;
    private boolean caOn;
    private int newSlot;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around enemy.", Category.COMBAT, true, false, true);
        this.range = this.register(new Setting("Range", 4.5, 0.1, 6.0));

        this.smartRange = this.register(new Setting("HoleRange", 3.0, 0.1, 6.0));
        this.web = this.register(new Setting("WEB", false));
        this.rainbow = this.register(new Setting("Rainbow", false));
        this.red = this.register(new Setting("Red", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.green = this.register(new Setting("Green", 255, 0, 255, v -> !this.rainbow.getValue()));
        this.blue = this.register(new Setting("Blue", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.alpha = this.register(new Setting("Alpha", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.outlineAlpha = this.register(new Setting("OL-Alpha", 0, 0, 255, v -> !this.rainbow.getValue()));
        this.systemTime = -1L;
        this.switchCooldown = false;
        this.isAttacking = false;
        this.setInstance();
        final Packet[] packet = { null };
    }


    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (closestTarget != null) {
            return closestTarget.getName();
        }
        return null;
    }

    public static HoleFiller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleFiller();
        }
        return INSTANCE;
    }

    



    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onUpdate() {
        if (mc.world == null) {
            return;
        }
            this.findClosestTarget();
        final List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        final double dist = 0.0;
        final double prevDist = 0.0;
        int obsidianSlot;
        final int n = obsidianSlot = ((mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) ? mc.player.inventory.currentItem : -1);
        if (obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                    obsidianSlot = l;
                    break;
                }
            }
        }
        if (this.web.getValue().booleanValue()) {
            obsidianSlot = ((mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.WEB)) ? mc.player.inventory.currentItem : -1);
            if (obsidianSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.WEB)) {
                        obsidianSlot = l;
                        break;
                    }
                }
            }
        }
        if (obsidianSlot == -1) {
            if (this.web.getValue().booleanValue()) {
                obsidianSlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                if (obsidianSlot == -1) {
                    return;
                }
            } else {
                return;
            }
        }
        for (final BlockPos blockPos : blocks) {
            if (!mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
                continue;
            }
            if (this.isInRange(blockPos)) {
                q = blockPos;
            }
            else {
                q = blockPos;
            }
        }
        this.render = q;
        if (q != null && mc.player.onGround) {
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(this.render))) return;
            final int oldSlot = mc.player.inventory.currentItem;
            this.switchToSlot(obsidianSlot);
            BlockUtil.placeBlock(this.render, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(oldSlot);
        }
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render != null) {
            RenderUtil.drawBoxESP(this.render, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue()), 3.5f, true, true, this.alpha.getValue());
        }
    }

    private void switchToSlot(int slot) {
        SmartTrap.mc.player.inventory.currentItem = slot;
        SmartTrap.mc.playerController.updateController();
    }

    private double getDistanceToBlockPos(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }

    public boolean IsHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK) && mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK);
    }

    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ));
        }
        return null;
    }

    private void findClosestTarget() {
        final List<EntityPlayer> playerList = mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target != mc.player && !M3dC3t.friendManager.isFriend(target.getName()) && EntityUtil.isLiving((Entity)target)) {
                if (target.getHealth() <= 0.0f) {
                    continue;
                }
                if (this.closestTarget == null) {
                    this.closestTarget = target;
                }
                else {
                    if (mc.player.getDistance((Entity)target) >= mc.player.getDistance((Entity)this.closestTarget)) {
                        continue;
                    }
                    this.closestTarget = target;
                }
            }
        }
    }

    private boolean isInRange(final BlockPos blockPos) {
        final NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        return positions.contains((Object)blockPos);
    }

    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = NonNullList.create();
        if (this.closestTarget != null) {
            positions.addAll(this.getSphere(this.getClosestTargetPos(), this.smartRange.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        }
        return (List<BlockPos>)positions;
    }

    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = (float)y;
                    final float f2 = sphere ? (cy + r) : ((float)(cy + h));
                    if (f >= f2) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }

    @Override
    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        resetRotation();
        super.onDisable();
    }

    static {
        INSTANCE = new HoleFiller();
        togglePitch = false;
    }
}
 