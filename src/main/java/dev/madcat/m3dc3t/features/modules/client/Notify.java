package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.RenderUtil;

public class Notify extends Module {

    public Notify() {
        super("Notify", "Notify.", Category.CLIENT, true, false, false);
    }

    boolean notify;
    private final Setting<Integer> wy = register(new Setting<>("Y", 18, 25, 500));

    private final Setting<Integer> tick = register(new Setting<>("Tick", 60, 0, 120));
    private final Setting<Integer> alpha = register(new Setting<>("Alpha", 160, 0, 255));
    int delay;
    int delay2;

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Module.test == null) return;
        String string = Module.test;
        int y = renderer.scaledHeight - wy.getValue();
        int x = renderer.scaledWidth - 10 - renderer.getStringWidth(string);
        if (Module.test2) {
            notify = true;
            delay = 0;
            Module.test2 = false;
            delay2 = tick.getValue();
        }
        if (notify) {
            if (alpha.getValue() >= 0) RenderUtil.drawRectangleCorrectly(x, y, 10 + renderer.getStringWidth(string), 15, ColorUtil.toRGBA(20, 20, 20, alpha.getValue()));
            int color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            renderer.drawString(string, 5 + x, 4 + y, color, true);
            RenderUtil.drawRectangleCorrectly(x, y + 14, (10 + renderer.getStringWidth(string)) * delay2 / tick.getValue(), 1, color);
        }
    }

    @Override
    public void onTick() {
        if (delay >= (tick.getValue() - 1)) {
            notify = false;
            delay = 0;
        }
        if (notify) {
            delay = delay + 1;
            delay2 = delay2 - 1;
        }
    }
}