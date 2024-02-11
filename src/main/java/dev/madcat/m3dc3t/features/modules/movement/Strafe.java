/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.movement;



import dev.madcat.m3dc3t.event.events.MoveEvent;
import dev.madcat.m3dc3t.event.events.UpdateWalkingPlayerEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.manager.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Strafe
        extends Module {
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NORMAL));
    private static Strafe INSTANCE = new Strafe();
    private double lastDist;
    private double moveSpeed;
    int stage;

    public Strafe() {
        super("Strafe", "mobility more flexible.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Strafe getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new Strafe();
        return INSTANCE;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1 && Strafe.fullNullCheck()) {
            return;
        }
        this.lastDist = Math.sqrt((Strafe.mc.player.posX - Strafe.mc.player.prevPosX) * (Strafe.mc.player.posX - Strafe.mc.player.prevPosX) + (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ) * (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ));
    }

    @SubscribeEvent
    public void onStrafe(MoveEvent event) {
        if (Anchor.Anchoring) return;
        if (Strafe.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (!Strafe.mc.player.onGround || !ModuleManager.getModuleByName("AutoJump").isEnabled() || !Strafe.mc.gameSettings.keyBindJump.isKeyDown())
                    break;
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (double) ((float) (Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
                }
                Strafe.mc.player.motionY = motionY;
                event.setY(Strafe.mc.player.motionY);
                this.moveSpeed *= this.mode.getValue() == Mode.NORMAL ? 1.67 : 2.149;
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - (this.mode.getValue() == Mode.NORMAL ? 0.6896 : 0.795) * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
                if ((Strafe.mc.world.getCollisionBoxes((Entity) Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically) && this.stage > 0) {
                    this.stage = Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f ? 1 : 0;
                }
                this.moveSpeed = this.lastDist - this.lastDist / (this.mode.getValue() == Mode.NORMAL ? 730.0 : 159.0);
            }
        }
        this.moveSpeed = (!Strafe.mc.gameSettings.keyBindJump.isKeyDown() || !ModuleManager.getModuleByName("AutoJump").isEnabled()) && Strafe.mc.player.onGround ? this.getBaseMoveSpeed() : Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        double n = Strafe.mc.player.movementInput.moveForward;
        double n2 = Strafe.mc.player.movementInput.moveStrafe;
        double n3 = Strafe.mc.player.rotationYaw;
        if (n == 0.0 && n2 == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (n != 0.0 && n2 != 0.0) {
            n *= Math.sin(0.7853981633974483);
            n2 *= Math.cos(0.7853981633974483);
        }
        double n4 = this.mode.getValue() == Mode.NORMAL ? 0.993 : 0.99;
        event.setX((n * this.moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * this.moveSpeed * Math.cos(Math.toRadians(n3))) * n4);
        event.setZ((n * this.moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * this.moveSpeed * -Math.sin(Math.toRadians(n3))) * n4);
        ++this.stage;
        event.setCanceled(true);
    }

    public double getBaseMoveSpeed() {
        double n = 0.2873;
        if (!Strafe.mc.player.isPotionActive(MobEffects.SPEED)) return n;
        n *= 1.0 + 0.2 * (double) (Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1);
        return n;
    }

    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        NORMAL,
        Strict;

    }
}

