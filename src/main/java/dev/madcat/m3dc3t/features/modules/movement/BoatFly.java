/*
 * Decompiled with CFR 0.152.
 */
package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;

public class BoatFly
extends Module {
    private final Setting<Double> speed = this.register(new Setting<Double>("Speed", 5.0, 0.1, 20.0));
    private final Setting<Double> yspeed = this.register(new Setting<Double>("YSpeed", 3.0, 0.1, 20.0));
    private final Setting<Boolean> glide = this.register(new Setting<Boolean>("Glide", true));

    public BoatFly() {
        super("BoatFly", "BoatFly.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onDisable() {
        if (BoatFly.mc.player.getRidingEntity() != null) {
            BoatFly.mc.player.getRidingEntity().setNoGravity(false);
        }
    }

    @Override
    public void onUpdate() {
        if (BoatFly.mc.player == null || BoatFly.mc.player.getRidingEntity() == null || BoatFly.mc.world == null) {
            return;
        }
        if (BoatFly.mc.player.getRidingEntity() != null) {
            BoatFly.mc.player.getRidingEntity().setNoGravity(true);
            BoatFly.mc.player.getRidingEntity().motionY = 0.0;
            if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                BoatFly.mc.player.getRidingEntity().onGround = false;
                BoatFly.mc.player.getRidingEntity().motionY = this.yspeed.getValue();
            }
            if (BoatFly.mc.gameSettings.keyBindSprint.isKeyDown()) {
                BoatFly.mc.player.getRidingEntity().onGround = false;
                BoatFly.mc.player.getRidingEntity().motionY = -this.yspeed.getValue();
            }
            double[] normalDir = this.directionSpeed(this.speed.getValue() / 2.0);
            if (BoatFly.mc.player.movementInput.moveStrafe != 0.0f || BoatFly.mc.player.movementInput.moveForward != 0.0f) {
                BoatFly.mc.player.getRidingEntity().motionX = normalDir[0];
                BoatFly.mc.player.getRidingEntity().motionZ = normalDir[1];
            } else {
                BoatFly.mc.player.getRidingEntity().motionX = 0.0;
                BoatFly.mc.player.getRidingEntity().motionZ = 0.0;
            }
            if (this.glide.getValue().booleanValue()) {
                if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (BoatFly.mc.player.ticksExisted % 8 < 2) {
                        BoatFly.mc.player.getRidingEntity().motionY = -0.04f;
                    }
                } else if (BoatFly.mc.player.ticksExisted % 8 < 4) {
                    BoatFly.mc.player.getRidingEntity().motionY = -0.04f;
                }
            }
        }
    }

    private double[] directionSpeed(double speed) {
        float forward = BoatFly.mc.player.movementInput.moveForward;
        float side = BoatFly.mc.player.movementInput.moveStrafe;
        float yaw = BoatFly.mc.player.prevRotationYaw + (BoatFly.mc.player.rotationYaw - BoatFly.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }
}

