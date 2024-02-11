package dev.madcat.m3dc3t.mixin.mixins;

import dev.madcat.m3dc3t.features.modules.client.ClickGui;
import dev.madcat.m3dc3t.features.modules.client.Peek;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiScreen.class})
public class MixinGuiScreen
        extends Gui {
    @Inject(method = {"renderToolTip"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (Peek.getInstance().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            Peek.getInstance().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
    }

    @Inject(method={"drawWorldBackground"}, at={@At(value="HEAD")}, cancellable=true)
    public void drawWorldBackground(int tint, CallbackInfo ci) {
        if (Minecraft.getMinecraft().player == null) return;
        if (ClickGui.getInstance().isEnabled() && ClickGui.getInstance().background.getValue())
        ci.cancel();
    }
}

