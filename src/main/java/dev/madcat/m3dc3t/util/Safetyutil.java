/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 */
package dev.madcat.m3dc3t.util;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dev.madcat.m3dc3t.features.Feature;
import dev.madcat.m3dc3t.features.modules.combat.AutoCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

public class Safetyutil extends Feature implements Runnable
{
    private final Timer syncTimer;
    private static boolean SAFE = false;


    private ScheduledExecutorService service;

    public Safetyutil() {
        this.syncTimer = new Timer();
        this.SAFE = false;
    }

    @Override
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }

    public static void doSafetyCheck() {
        if (!Feature.fullNullCheck()) {
            boolean safe = true;
            final ArrayList<Entity> crystals = new ArrayList<Entity>(Safetyutil.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (DamageUtil.calculateDamage(crystal, (Entity)Safetyutil.mc.player) <= 4.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            Safetyutil.SAFE = safe;
        }
    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        if (this.SAFE) {
            return "§aSecure";
        }
        return "§cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE;
    }

    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        return service;
    }
}
