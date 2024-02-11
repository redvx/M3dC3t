package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", "All bright.", Module.Category.RENDER, false, false, false);
    }
    public Setting<SwingMode> mode = register(new Setting<SwingMode>("Swing", SwingMode.Gamma));

    public enum SwingMode {

        Gamma,
        Potion

    }
    float oldBright;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == SwingMode.Potion)
            mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        oldBright = mc.gameSettings.gammaSetting;

        if (mode.getValue() == SwingMode.Gamma)
            mc.gameSettings.gammaSetting = +100;
        return;
    }

    @Override
    public void onDisable() {
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

        if (mode.getValue() ==SwingMode.Gamma)
            mc.gameSettings.gammaSetting = oldBright;
    }
}
