package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;

public class Flight extends Module {
    public Flight() {
        super("Flight", "Allow you fly.", Module.Category.MOVEMENT, true, false, false);
    }

    public Setting<Float> speed =register(new Setting<Float>("Speed", 10f,0f,50f));


    @Override
    public void onEnable() {
        if (mc.player == null) return;

        mc.player.capabilities.isFlying = true;
        if (mc.player.capabilities.isCreativeMode) return;
        mc.player.capabilities.allowFlying = true;

    }

    @Override
    public void onTick() {
        mc.player.capabilities.setFlySpeed(speed.getValue() / 100f);
        mc.player.capabilities.isFlying = true;
        if (mc.player.capabilities.isCreativeMode) return;
        mc.player.capabilities.allowFlying = true;
    }

    @Override
    public void onDisable() {

        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (mc.player.capabilities.isCreativeMode) return;
        mc.player.capabilities.allowFlying = false;

    }


}

