//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 */
package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Shaders
        extends Module {
    private static final Shaders INSTANCE = new Shaders();
    public Setting<Mode> shader = this.register(new Setting<Mode>("Mode", Mode.green));

    public Shaders() {
        super("Shaders", "Very cool frame.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (Shaders.mc.entityRenderer.getShaderGroup() != null) {
                Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                Shaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + (Object)((Object)this.shader.getValue()) + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Shaders.mc.entityRenderer.getShaderGroup() != null && Shaders.mc.currentScreen == null) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        return this.shader.currentEnumName();
    }

    @Override
    public void onDisable() {
        if (Shaders.mc.entityRenderer.getShaderGroup() != null) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public static enum Mode {
        notch,
        antialias,
        art,
        bits,
        blobs,
        blobs2,
        blur,
        bumpy,
        color_convolve,
        creeper,
        deconverge,
        desaturate,
        flip,
        fxaa,
        green,
        invert,
        ntsc,
        pencil,
        phosphor,
        sobel,
        spider,
        wobble;

    }
}

