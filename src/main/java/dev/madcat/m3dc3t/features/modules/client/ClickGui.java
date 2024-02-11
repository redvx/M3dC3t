/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.madcat.m3dc3t.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.ClientEvent;
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.command.Command;
import dev.madcat.m3dc3t.features.gui.M3dC3tGui;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    private final Setting<Settings> setting = this.register(new Setting<Settings>("Settings", Settings.Gui));
    public Setting<String> prefix = this.register(new Setting<Object>("Prefix", ".", v -> this.setting.getValue() == Settings.Gui));
    public Setting<String> clientName = this.register(new Setting<String>("ClientName", "CuteCat", v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(140), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(140), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(225), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> alpha = this.register(new Setting<Object>("HoverAlpha", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> alphaBox = this.register(new Setting<Object>("AlphaBox", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Color));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> moduleDescription = this.register(new Setting<Boolean>("Description", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> moduleIcon = this.register(new Setting<Boolean>("Icon", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> snowing = this.register(new Setting<Boolean>("Snowing", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> iconmode = this.register(new Setting<Integer>("SettingIcon", 0, 0, 5, v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> moduleiconmode = this.register(new Setting<Integer>("ModuleIcon", 0, 0, 2, v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> moduleWidth = this.register(new Setting<Integer>("ModuleWidth", 0, 0, 40, v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> moduleDistance = this.register(new Setting<Integer>("ModuleDistance", 30, 0, 50, v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> rainbowg = this.register(new Setting<Object>("Rainbow", Boolean.FALSE, v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.FALSE, v -> this.setting.getValue() == Settings.Color));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HUD", (Object)rainbowMode.Static, v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Color));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ArrayList", (Object)rainbowModeArray.Up, v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Color));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(600), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Color));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Color));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Color));
    public Setting<Boolean> background = this.register(new Setting<Boolean>("BackGround", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> blur = this.register(new Setting<Boolean>("Blur", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> g_red = this.register(new Setting<Object>("RedL", Integer.valueOf(105), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_green = this.register(new Setting<Object>("GreenL", Integer.valueOf(162), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_blue = this.register(new Setting<Object>("BlueL", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_red1 = this.register(new Setting<Object>("RedR", Integer.valueOf(143), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_green1 = this.register(new Setting<Object>("GreenR", Integer.valueOf(140), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_blue1 = this.register(new Setting<Object>("BlueR", Integer.valueOf(213), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_alpha = this.register(new Setting<Object>("AlphaL", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_alpha1 = this.register(new Setting<Object>("AlphaR", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));

    public ClickGui() {
        super("ClickGui", "Module interface.", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (background.getValue()) {
            RenderUtil.drawRect(0, 0, 1000, 550, ColorUtil.toRGBA(20, 20, 20, 150));
            RenderUtil.drawGradientRect(0, 200, 1000, 350, ColorUtil.toRGBA(red.getValue(), green.getValue(), blue.getValue(), 0), ColorUtil.toRGBA(red.getValue(), green.getValue(), blue.getValue(), 255));
        }
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
       if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                M3dC3t.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + M3dC3t.commandManager.getPrefix());
            }
            M3dC3t.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    public String getCommandMessage() {
        return TextUtil.coloredString("[", HUD.getInstance().bracketColor.getPlannedValue()) + TextUtil.coloredString(ClickGui.getInstance().clientName.getValueAsString(), HUD.getInstance().commandColor.getPlannedValue()) + TextUtil.coloredString("]", HUD.getInstance().bracketColor.getPlannedValue());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)M3dC3tGui.getClickGui());
    }

    @Override
    public void onLoad() {
        M3dC3t.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        M3dC3t.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onUpdate() {
        if (blur.getValue()) {
            if (OpenGlHelper.shadersSupported && Util.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (Util.mc.entityRenderer.getShaderGroup() != null) {
                    Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                try {
                    Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (Util.mc.entityRenderer.getShaderGroup() != null && Util.mc.currentScreen == null) {
                Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
        else if (Util.mc.entityRenderer.getShaderGroup() != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public void onTick() {
        M3dC3t.commandManager.setClientMessage(this.getCommandMessage());
        if (!(ClickGui.mc.currentScreen instanceof M3dC3tGui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof M3dC3tGui) {
            Util.mc.displayGuiScreen(null);
        }
        if (Util.mc.entityRenderer.getShaderGroup() != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }


    public static enum rainbowModeArray {
        Static,
        Up;

    }

    public static enum rainbowMode {
        Static,
        Sideway;

    }

    public static enum Settings {
        Gui,
        Color,
        Gradient,

    }
}

