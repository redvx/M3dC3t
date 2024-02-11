package dev.madcat.m3dc3t.features.modules.player;


import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class  PacketEat
extends Module {
    private static PacketEat INSTANCE = new PacketEat();

    public PacketEat() {
        super("PacketEat", "Eat without action.", Category.PLAYER, true, false, false);
        this.setInstance();
    }
    private Setting<Boolean> autoEat = this.register(new Setting<Boolean>("OnlyGappleAutoEAT", true));
    public final Setting<Float> health = this.register(new Setting<Float>("Health", Float.valueOf(32.0f), Float.valueOf(0.0f), Float.valueOf(35.9f),  v -> this.autoEat.getValue()));
    public final Setting<Float> hunger = this.register(new Setting<Float>("Hunger", Float.valueOf(19.0f), Float.valueOf(0.0f), Float.valueOf(19.9f),  v -> this.autoEat.getValue()));

    public static PacketEat getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new PacketEat();
        return INSTANCE;
    }
    @Override
    public void onUpdate() {
        if (this.autoEat.getValue().booleanValue()) {
            if (mc.player.isCreative()) {
                return;
            }
            if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= this.health.getValue().floatValue() || mc.player.getFoodStats().getFoodLevel() <= this.hunger.getValue().floatValue()) {
                if (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE) {
                    mc.playerController.processRightClick((EntityPlayer) mc.player, (World) mc.world, EnumHand.MAIN_HAND);
                    return;
                }
                if (mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE) {
                    mc.playerController.processRightClick((EntityPlayer) mc.player, (World) mc.world, EnumHand.OFF_HAND);
                    return;
                }
            }
        }

    }
    private void setInstance() {
        INSTANCE = this;
    }
}

