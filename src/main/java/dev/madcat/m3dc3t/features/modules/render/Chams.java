/*
 * Decompiled with CFR 0.152.
 */
package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;

public class Chams
        extends Module {
    private static Chams INSTANCE = new Chams();

    public Chams() {
        super("Chams", "Player behind rendered wall.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Chams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

