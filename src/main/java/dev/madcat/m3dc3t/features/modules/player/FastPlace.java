package dev.madcat.m3dc3t.features.modules.player;

import dev.madcat.m3dc3t.features.modules.Module;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", "Fast place and use.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        FastPlace.mc.rightClickDelayTimer = 0;
    }
}

