

package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

    public final class HitMarkers extends Module {
        int renderTicks = 80;
        int regi = 0;

        public HitMarkers() {
            super("HitMarkers", "hitmarker thingys.", Category.CLIENT, false, false, false);
        }

        public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
        public Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
        public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
        public Setting<Integer> thickness = this.register(new Setting<Integer>("Thickness", 2, 1, 6));
        public Setting<Integer> time = this.register(new Setting<Integer>("Time", 40, 0, 80));
        public Setting<Boolean> iq2 = register(new Setting<Boolean>("Debug", Boolean.valueOf(false), "debug"));


        public void onEnable() {
            MinecraftForge.EVENT_BUS.register(this);
            regi = 1;
        }

        public void onDisable() {
            MinecraftForge.EVENT_BUS.unregister(this);
            regi = 0;
        }

        @Override
        public void onUpdate() {
            if (regi == 0) {
                MinecraftForge.EVENT_BUS.register(this);
                regi = 1;
            }
        }

        @Override
        public void onRender2D(Render2DEvent event) {
            if (this.renderTicks < time.getValue()) {
                final ScaledResolution resolution = new ScaledResolution(dev.madcat.m3dc3t.features.modules.client.HitMarkers.mc);
                RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
                RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
                RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
                RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
            }
        }

        @SubscribeEvent
        public void onAttackEntity(AttackEntityEvent event) {
            renderTicks = 0;
        }

        @Override
        public void onTick() {
            ++renderTicks;
        }
    }
