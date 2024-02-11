
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
public class Speedmine
        extends Module {
    private final Setting<Boolean> slow = this.register(new Setting<Boolean>("NoBreakDelay", true));
    public Setting<Float> damage = this.register(new Setting<Object>("Damage", Float.valueOf(0.7f), Float.valueOf(0.1f), Float.valueOf(1.0f)));
    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (slow.getValue())
        mc.playerController.blockHitDelay = 0;

        if (mc.playerController.curBlockDamageMP >= this.damage.getValue().floatValue()) mc.playerController.curBlockDamageMP = 1.0f;
    }
    }

