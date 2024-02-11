package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class WorldModify extends Module {
    public WorldModify() {
        super("WorldModify", "Change the world something.", Category.RENDER, true, false, false);
    }
    public Setting<Boolean> color = register(new Setting<Boolean>("ColorChanger", Boolean.valueOf(false)));
    private Setting<Integer> red = register(new Setting("Red", 255, 0, 255, v -> color.getValue()));
    private Setting<Integer> green = register(new Setting("Green", 255, 0, 255, v -> color.getValue()));
    private Setting<Integer> blue = register(new Setting("Blue", 255, 0, 255, v -> color.getValue()));
    private Setting<Integer> alpha = register(new Setting("Alpha", 35, 0, 255, v -> color.getValue()));
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (color.getValue()) RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()));
    }
    public Setting<Boolean> timeChanger = register(new Setting<Boolean>("TimeChanger", Boolean.valueOf(false)));
    private Setting<Integer> time = register(new Setting("Time", 0, 0, 24000, v -> timeChanger.getValue()));

    @SubscribeEvent
    public void init(WorldEvent event) {
        if (timeChanger.getValue()) event.getWorld().setWorldTime(this.time.getValue());
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && timeChanger.getValue()) {
            event.setCanceled(true);
        }
    }
    public Setting<Boolean> sky = register(new Setting<Boolean>("FogColor", Boolean.valueOf(false)));
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", true, v -> sky.getValue()));
    private Setting<Integer> red2 = register(new Setting("Red", 255, 0, 255, v -> sky.getValue()));
    private Setting<Integer> green2 = register(new Setting("Green", 255, 0, 255, v -> sky.getValue()));
    private Setting<Integer> blue2 = register(new Setting("Blue", 255, 0, 255, v -> sky.getValue()));

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        if (sky.getValue()) {
            event.setRed(red2.getValue() / 255f);
            event.setGreen(green2.getValue() / 255f);
            event.setBlue(blue2.getValue() / 255f);
        }
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (sky.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    int registered = 0;

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        registered = 0;
    }

    @Override
    public void onUpdate() {
        if (registered == 0) {
            MinecraftForge.EVENT_BUS.register(this);
            registered = 1;
        }
        if (rainbow.getValue()) {
            doRainbow();
        }
    }

    public void doRainbow() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        red2.setValue((color_rgb_o >> 16) & 0xFF);
        green2.setValue((color_rgb_o >> 8) & 0xFF);
        blue2.setValue(color_rgb_o & 0xFF);
    }
}
