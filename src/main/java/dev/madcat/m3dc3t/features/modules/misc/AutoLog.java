
package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class AutoLog
        extends Module {
    private final Setting<Float> health = this.register(new Setting<Float>("Health", 16.0f, 0.1f, 36.0f));
    public Setting<Integer> totems = this.register(new Setting<Integer>("Totems", 0, 0, 10));
    private final Setting<Boolean> logout = this.register(new Setting<Boolean>("LogoutOff", true));

    public AutoLog() {
        super("AutoLog", "Logout when in danger.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onTick() {
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += mc.player.getHeldItemOffhand().getCount();
        }
        if (mc.player.getHealth() <= this.health.getValue()) {
            if (totems <= this.totems.getValue().floatValue() || totems == this.totems.getValue().floatValue()) {
                Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
                if (this.logout.getValue()) {
                    this.disable();
                }
            }
        }
    }
}

