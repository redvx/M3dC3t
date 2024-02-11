/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.entity.Entity;

public class ReverseStep
        extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 8, 1, 20));
    private final Setting<Boolean> inliquid = this.register(new Setting<Boolean>("Liquid", false));
    private final Setting<Cancel> canceller = this.register(new Setting<Cancel>("CancelType", Cancel.None));

    public ReverseStep() {
        super("ReverseStep", "Rapid decline.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ReverseStep getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isSneaking() || ReverseStep.mc.player.isDead || ReverseStep.mc.player.collidedHorizontally || !ReverseStep.mc.player.onGround || ReverseStep.mc.player.isInWater() && this.inliquid.getValue() == false || ReverseStep.mc.player.isInLava() && this.inliquid.getValue() == false || ReverseStep.mc.player.isOnLadder() || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() || M3dC3t.moduleManager.isModuleEnabled("Burrow") || ReverseStep.mc.player.noClip || M3dC3t.moduleManager.isModuleEnabled("Packetfly") || M3dC3t.moduleManager.isModuleEnabled("Phase") || ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Shift || ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Both || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Space || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Both || M3dC3t.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        for (double y = 0.0; y < 90.5; y += 0.01) {
            if (ReverseStep.mc.world.getCollisionBoxes((Entity)ReverseStep.mc.player, ReverseStep.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) continue;
            ReverseStep.mc.player.motionY = (float)(-this.speed.getValue().intValue()) / 10.0f;
            break;
        }
    }

    public static enum Cancel {
        None,
        Space,
        Shift,
        Both;

    }
}

