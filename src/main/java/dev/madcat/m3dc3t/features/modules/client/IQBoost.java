package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.Feature;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IQBoost extends Module {
    public IQBoost() {
        super("IQBoost", "Increase your IQ.", Category.CLIENT, true, false, false);
    }
    public static final ResourceLocation mark = new ResourceLocation("textures/setting2.png");
    private int color;
    private final Setting<Float> iq = this.register(new Setting<Float>("IQ", Float.valueOf(0.0f), Float.valueOf(-1000.0f), Float.valueOf(1000.0f)));
    public Setting<Boolean> iq2 = register(new Setting<Boolean>("IQ+++", Boolean.valueOf(false), "Cleans your chat"));
    public Setting<Integer> imageX = this.register(new Setting<Integer>("x", 18, 0, 300, v -> this.iq2.getValue()));
    public Setting<Integer> imageY = this.register(new Setting<Integer>("y", 50, 0, 300, v -> this.iq2.getValue()));
    

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        return this.iq.getValue().floatValue() + "IQ";
    }

    public void renderLogo() {
        int x = this.imageX.getValue();
        int y = this.imageY.getValue();
        Util.mc.renderEngine.bindTexture(mark);
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
        Gui.drawScaledCustomSizeModalRect((int)(x - 2), (int)(y - 36), (float)7.0f, (float)7.0f, (int)(60 - 7), (int)(60 - 7), (int)60, (int)60, (float)60, (float)60);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (this.iq2.getValue()) {
            if (!Feature.fullNullCheck()) {
                this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
                if (((Boolean) this.enabled.getValue()).booleanValue()) {
                    this.renderLogo();
                }
            }
        }
    }
}
