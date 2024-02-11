/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.event.PlayerDamageBlockEvent;
import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.event.events.Render3DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Bind;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.InventoryUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InstantMine extends Module {
    private final Timer breakSuccess = new Timer();
    private static InstantMine INSTANCE = new InstantMine();
    private Setting<Boolean> creativeMode = this.register(new Setting<Boolean>("CreativeMode", true));
    private Setting<Boolean> ghostHand = this.register(new Setting<Boolean>("GhostHand", Boolean.valueOf(true), v -> this.creativeMode.getValue()));
    private Setting<Boolean> render = this.register(new Setting<Boolean>("Fill", true));
    private Setting<Integer> falpha = register(new Setting("FillAlpha", 30, 0, 255, v -> this.render.getValue()));
    private Setting<Boolean> render2 = this.register(new Setting<Boolean>("Box", true));
    private Setting<Integer> balpha = register(new Setting("BoxAlpha", 100, 0, 255, v -> this.render2.getValue()));
    private final Setting<Boolean> crystal = this.register(new Setting<Boolean>("Crystal", Boolean.TRUE));
    private final Setting<Boolean> crystalp = this.register(new Setting<Boolean>("Crystal on Break", Boolean.TRUE, v -> this.crystal.getValue()));
    public final Setting<Boolean> attackcrystal = this.register(new Setting<Object>("Attack Crystal", Boolean.TRUE, v -> this.crystal.getValue()));
    public final Setting<Bind> bind = this.register(new Setting<Object>("ObsidianBind", new Bind(-1), v -> this.crystal.getValue()));
    public Setting<Boolean> db = this.register(new Setting<Boolean>("Silent Double", Boolean.valueOf(true)));
    public final Setting<Float> health = this.register(new Setting<Float>("Health", Float.valueOf(18.0f), Float.valueOf(0.0f), Float.valueOf(35.9f), v -> db.getValue()));
    private Setting<Integer> red = register(new Setting("Red", 255, 0, 255));
    private Setting<Integer> green = register(new Setting("Green", 255, 0, 255));
    private Setting<Integer> blue = register(new Setting("Blue", 255, 0, 255));
    private Setting<Integer> alpha = register(new Setting("BoxAlpha", 150, 0, 255));
    private Setting<Integer> alpha2 = register(new Setting("FillAlpha", 70, 0, 255));
    private final List<Block> godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
    private boolean cancelStart = false;
    private boolean empty = false;
    private EnumFacing facing;
    public static BlockPos breakPos;
    int slotMain2;
    int swithc2;
    public static BlockPos breakPos2;

    public InstantMine() {
        super("InstantMine", "Crazy packet miner.", Category.COMBAT, true, false, false);
        this.setInstance();
    }

    public static InstantMine getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new InstantMine();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (mc.player.isCreative()) return;
        slotMain2 = mc.player.inventory.currentItem;
            if (ticked <= 86 && ticked >= 0) {
                ticked = ticked + 1;
            }
        if (breakPos2 == null) {
            manxi2 = 0;
        }
            if (breakPos2 != null) {
                if (ticked >= 65 || (ticked >= 20 && mc.world.getBlockState(breakPos).getBlock() == Blocks.ENDER_CHEST)) {
                    if (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE || mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.CHORUS_FRUIT) {
                        if (!Mouse.isButtonDown(1)) {
                            if (mc.player.getHealth() + mc.player.getAbsorptionAmount() >= this.health.getValue()) {
                                if ((InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) && db.getValue()) {
                                    mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE)));
                                    swithc2 = 1;
                                    ticked = ticked + 1;
                                }
                            } else {
                                if (swithc2 == 1){
                                    mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(slotMain2));
                                    swithc2 = 0;
                                }
                            }
                        } else {
                            if (swithc2 == 1){
                                mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(slotMain2));
                                swithc2 = 0;
                            }
                        }
                    } else {
                        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() >= this.health.getValue()) {
                            if ((InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) && db.getValue()) {
                                mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE)));
                                swithc2 = 1;
                                ticked = ticked + 1;
                            }
                        } else {
                            if (swithc2 == 1){
                                mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(slotMain2));
                                swithc2 = 0;
                            }
                        }
                    }
                }
            }
        if (breakPos2 != null) {
            if (mc.world.getBlockState(breakPos2).getBlock() == Blocks.AIR) {
                if (swithc2 == 1) {
                    mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(slotMain2));
                    swithc2 = 0;
                }
                breakPos2 = null;
                manxi2 = 0;
                ticked = 0;
            }
        }
        if (ticked == 0) {
            manxi2 = 0;
            breakPos2 = null;
        }
        if (ticked >= 140) {
            if (swithc2 == 1) {
                mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(slotMain2));
                swithc2 = 0;
            }
            manxi2 = 0;
            breakPos2 = null;
            ticked = 0;
        }

        if (breakPos != null) {
            if (mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR && breakPos2 == null) {
                ticked = 0;
            }
        }
        if (fullNullCheck()) {
            return;
        }
        if (this.creativeMode.getValue() == false) return;
        if (!this.cancelStart) return;
        if (this.crystal.getValue().booleanValue() && this.attackcrystal.getValue().booleanValue() && mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            attackcrystal();
        }
        if (this.bind.getValue().isDown() && this.crystal.getValue().booleanValue() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 && mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int old = mc.player.inventory.currentItem;
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(breakPos, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (InventoryUtil.getItemHotbar(Items.END_CRYSTAL) != -1 && this.crystal.getValue().booleanValue() && mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN && !breakPos.equals((Object) AntiBurrow.pos)) {
            if (this.empty) {
                BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
            } else {
                if (!this.crystalp.getValue().booleanValue()) {
                    BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
                }
            }
        }
        if (this.godBlocks.contains(mc.world.getBlockState(this.breakPos).getBlock())) return;
        if (mc.world.getBlockState(this.breakPos).getBlock() != Blocks.WEB) {
            if (this.ghostHand.getValue().booleanValue() && (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) && InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) != -1) {
                int slotMain = mc.player.inventory.currentItem;
                if (mc.world.getBlockState(this.breakPos).getBlock() == Blocks.OBSIDIAN) {
                    if (!this.breakSuccess.passedMs(1234L)) return;
                    mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                    mc.playerController.updateController();
                    mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                    mc.player.inventory.currentItem = slotMain;
                    mc.playerController.updateController();
                    return;
                }
                mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                mc.playerController.updateController();
                mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                mc.player.inventory.currentItem = slotMain;
                mc.playerController.updateController();
                return;
            }
        } else {
            if (this.ghostHand.getValue().booleanValue() && (InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1) && InventoryUtil.getItemHotbars(Items.DIAMOND_SWORD) != -1) {
                int slotMain = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
                mc.playerController.updateController();
                mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                mc.player.inventory.currentItem = slotMain;
                mc.playerController.updateController();
                return;
            }
        }
        mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
    }


    double manxi=0;
    double manxi2=0;

    private void switchToSlot(int slot) {
        FeetPad.mc.player.inventory.currentItem = slot;
        FeetPad.mc.playerController.updateController();
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        if (!mc.player.isCreative()) {
            if (breakPos2 != null) {
                AxisAlignedBB axisAlignedBB = mc.world.getBlockState(breakPos2).getSelectedBoundingBox(mc.world, breakPos2);
                double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
                double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
                double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
                double progressValX = getInstance().manxi2 * ((axisAlignedBB.maxX - centerX) / 10);
                double progressValY = getInstance().manxi2 * ((axisAlignedBB.maxY - centerY) / 10);
                double progressValZ = getInstance().manxi2 * ((axisAlignedBB.maxZ - centerZ) / 10);
                AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                if (breakPos != null) {
                    if (!InstantMine.breakPos2.equals((Object) breakPos)) {
                        RenderUtil.drawBBBox(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), alpha.getValue());
                        RenderUtil.drawBBFill(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha2.getValue()), alpha2.getValue());
                    }
                } else {
                    RenderUtil.drawBBBox(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), alpha.getValue());
                    RenderUtil.drawBBFill(axisAlignedBB1, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha2.getValue()), alpha2.getValue());
                }
            }
            if (this.creativeMode.getValue().booleanValue() && this.cancelStart) {
                if (this.godBlocks.contains(mc.world.getBlockState(this.breakPos).getBlock())) {
                    this.empty = true;
                }


                if (imerS.passedMs(15)) {
                    if (manxi <= 10) {
                        manxi = manxi + 0.11;
                    }
                    imerS.reset();
                }

                if (imerS2.passedMs(22)) {
                    if (manxi2 <= 10 && manxi2 >= 0) {
                        manxi2 = manxi2 + 0.11;
                    }
                    imerS2.reset();
                }

                AxisAlignedBB axisAlignedBB = mc.world.getBlockState(breakPos).getSelectedBoundingBox(mc.world, breakPos);
                double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
                double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
                double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
                double progressValX = manxi * ((axisAlignedBB.maxX - centerX) / 10);
                double progressValY = manxi * ((axisAlignedBB.maxY - centerY) / 10);
                double progressValZ = manxi * ((axisAlignedBB.maxZ - centerZ) / 10);
                AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                if (this.render.getValue().booleanValue()) {
                    RenderUtil.drawBBFill(axisAlignedBB1, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.falpha.getValue());
                }
                if (this.render2.getValue().booleanValue()) {
                    RenderUtil.drawBBBox(axisAlignedBB1, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.balpha.getValue());
                }

            }
        }
    }

    public final Timer imerS = new Timer();
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (mc.player.isCreative()) return;
        if (!(event.getPacket() instanceof CPacketPlayerDigging)) return;
        CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
        if (packet.getAction() != CPacketPlayerDigging.Action.START_DESTROY_BLOCK) return;
        event.setCanceled(this.cancelStart);
    }

    public final Timer imerS2 = new Timer();

    public static void attackcrystal() {
        for (Entity crystal : mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> Float.valueOf(mc.player.getDistance(e)))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistanceSq(breakPos) <= 2.0)) continue;
            mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
            mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    long times = 0;
    static int ticked = 0;
    @SubscribeEvent
    public void onBlockEvent(PlayerDamageBlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (mc.player.isCreative()) return;
        if (!BlockUtil.canBreak(event.pos)) return;
        if(breakPos!=null){
            if(breakPos.getX()==event.pos.getX()&&breakPos.getY()==event.pos.getY()&&breakPos.getZ()==event.pos.getZ()){
                return;
            }
        }
        if (ticked == 0) {
            ticked = 1;
        }
        if (manxi2 == 0) {
            manxi2 = 0.11;
        }
        if (breakPos != null) {
            if (breakPos2 == null) {
                if (mc.world.getBlockState(breakPos).getBlock() != Blocks.AIR) {
                    this.breakPos2 = breakPos;
                }
            }
        }
        if (breakPos == null) {
            if (breakPos2 == null) {
                breakPos2 = event.pos;
            }
        }
        manxi = 0;
        this.empty = false;
        this.cancelStart = false;
        this.breakPos = event.pos;
        this.breakSuccess.reset();
        this.facing = event.facing;
        if (this.breakPos == null) return;
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakPos, this.facing));
        this.cancelStart = true;
        mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
        event.setCanceled(true);
    }
}

