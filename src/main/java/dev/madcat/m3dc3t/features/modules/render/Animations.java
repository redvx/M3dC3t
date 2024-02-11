//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class Animations
        extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Anim", Mode.OLD));
    private final Setting<Swing> swing = this.register(new Setting<Swing>("Swing", Swing.Mainhand));
    private final Setting<Boolean> slow = this.register(new Setting<Boolean>("Slow", false));

    public Animations() {
        super("Animations", "Change animations.", Category.RENDER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.OLD) {
            Animations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animations.mc.entityRenderer.itemRenderer.itemStackMainHand = Animations.mc.player.getHeldItemMainhand();
        }
        if (this.slow.getValue().booleanValue()) {
            Animations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000, 3));
        }
        if (!this.slow.getValue().booleanValue()) {
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
    }

    @Override
    public void onDisable() {
        Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
    }

    private static enum Swing {
        Mainhand,
        Offhand,

    }

    private static enum Mode {
        Normal,
        OLD;

    }
}

