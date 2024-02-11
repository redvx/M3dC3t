package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;

public class CameraClip  extends Module {
    public CameraClip() {
        super("CameraClip", "Allow Camera pierce through.", Module.Category.RENDER, false, false, false);
    }
    public Setting<Double> distance = register(new Setting<Double>("Distance", 4.0, -10.0, 20.0));
}
