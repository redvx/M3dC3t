/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPickaxe
 */
package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;

public class PhaseTrace
        extends Module {
    private static PhaseTrace INSTANCE = new PhaseTrace();
    public boolean noTrace;

    public PhaseTrace() {
        super("PhaseTrace", "No entities hit box.", Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static PhaseTrace getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PhaseTrace();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
            this.noTrace = true;
            return;
    }
}

