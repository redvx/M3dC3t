/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.event.events.RenderItemEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel
        extends Module {
    private static ViewModel INSTANCE = new ViewModel();
    public Setting<Settings> settings = this.register(new Setting<Settings>("Settings", Settings.TRANSLATE));
    public Setting<Boolean> noEatAnimation = this.register(new Setting<Object>("NoEatAnimation", Boolean.valueOf(false), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> eatX = this.register(new Setting<Object>("EatX", Double.valueOf(3.5), Double.valueOf(-5.0), Double.valueOf(15.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Double> eatY = this.register(new Setting<Object>("EatY", Double.valueOf(2.1), Double.valueOf(-5.0), Double.valueOf(15.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Boolean> doBob = this.register(new Setting<Object>("ItemBob", Boolean.valueOf(true), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> mainX = this.register(new Setting<Object>("MainX", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainY = this.register(new Setting<Object>("MainY", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainZ = this.register(new Setting<Object>("MainZ", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offX = this.register(new Setting<Object>("OffX", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offY = this.register(new Setting<Object>("OffY", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offZ = this.register(new Setting<Object>("OffZ", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public ViewModel() {
        super("ViewModel", "Change the position of the arm.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static ViewModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(RenderItemEvent event) {
        event.setMainX(this.mainX.getValue());
        event.setMainY(this.mainY.getValue());
        event.setMainZ(this.mainZ.getValue());
        event.setOffX(-this.offX.getValue().doubleValue());
        event.setOffY(this.offY.getValue());
        event.setOffZ(this.offZ.getValue());
    }

    private static enum Settings {
        TRANSLATE,
        TWEAKS;

    }
}

