/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.render;


import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.event.events.NoRenderEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
        extends Module {

    public static NoRender INSTANCE = new NoRender();
    public Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    public Setting<Boolean> fire = this.register(new Setting<Boolean>("Fire", true));
    public Setting<Boolean> blind = this.register(new Setting<Boolean>("Blind", true));
    public Setting<Boolean> nausea = this.register(new Setting<Boolean>("Nausea", true));
    public Setting<Boolean> fog = this.register(new Setting<Boolean>("Fog", true));
    public Setting<Boolean> noWeather = this.register(new Setting<Boolean>("Weather", Boolean.valueOf(true), "AntiWeather"));
    public Setting<Boolean> hurtCam = this.register(new Setting<Boolean>("HurtCam", true));
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPop", Boolean.valueOf(true), "Removes the Totem overlay."));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Block", true));

    public NoRender() {
        super("NoRender", "Prevent some animation.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new NoRender();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.blind.getValue().booleanValue() && mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() == false) return;
        if (this.noWeather.getValue().booleanValue()) {
            mc.world.getWorldInfo().setRaining(false);
        }
        if (!mc.player.isPotionActive(MobEffects.NAUSEA)) return;
        mc.player.removePotionEffect(MobEffects.NAUSEA);
    }

    @SubscribeEvent
    public void NoRenderEventListener(NoRenderEvent event) {
        if (event.getStage() == 0 && this.armor.getValue().booleanValue()) {
            event.setCanceled(true);
            return;
        }
        if (event.getStage() != 1) return;
        if (this.hurtCam.getValue() == false) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (!this.fog.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockOverlayEventListener(RenderBlockOverlayEvent event) {
        if (this.fire.getValue() == false) return;
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.FIRE) return;
        event.setCanceled(true);
    }
}

