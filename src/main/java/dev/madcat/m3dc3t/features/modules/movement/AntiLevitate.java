
package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraft.potion.Potion;

import java.util.Objects;

public class AntiLevitate
extends Module {
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation effect.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation((String)"levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation((String)"levitation"));
        }
    }
}

