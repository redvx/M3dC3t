package dev.madcat.m3dc3t.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.event.events.ClientEvent;
import dev.madcat.m3dc3t.features.command.Command;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FontMod extends Module {
    private static FontMod INSTANCE = new FontMod();
    public Setting<Boolean> cfont = this.register(new Setting<Boolean>("ClickGuiFont", Boolean.valueOf(true)));
    public Setting<Boolean> clientFont = register(new Setting<Boolean>("ClientFont", Boolean.valueOf(true), "test."));
    public Setting<String> fontName = register(new Setting<String>("FontName", "Arial", v -> clientFont.getValue()));
    public Setting<Boolean> antiAlias = register(new Setting<Boolean>("AntiAlias", Boolean.valueOf(true), v -> clientFont.getValue()));
    public Setting<Boolean> fractionalMetrics = register(new Setting<Boolean>("Metrics", Boolean.valueOf(true), v -> clientFont.getValue()));
    public Setting<Integer> fontSize = register(new Setting<Integer>("Size", Integer.valueOf(18), Integer.valueOf(12), Integer.valueOf(30), v -> clientFont.getValue()));
    public Setting<Integer> fontStyle = register(new Setting<Integer>("Style", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), v -> clientFont.getValue()));
    private boolean reloadFont = false;

    public FontMod() {
        super("CustomFont", "Modify the font of client text.", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static FontMod getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FontMod();
        }
        return INSTANCE;
    }

    public static boolean checkFont(String font, boolean message) {
        String[] fonts;
        for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (!message) continue;
            Command.sendMessage(s);
        }
        return false;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (!clientFont.getValue()) return;
        Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !FontMod.checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage(ChatFormatting.RED + "That font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            reloadFont = true;
        }
    }

    @Override
    public void onTick() {
        if (!clientFont.getValue()) return;
        if (reloadFont) {
            M3dC3t.textManager.init(false);
            reloadFont = false;
        }
    }
}

