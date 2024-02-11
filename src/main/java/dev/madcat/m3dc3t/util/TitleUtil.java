package dev.madcat.m3dc3t.util;

import dev.madcat.m3dc3t.M3dC3t;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class TitleUtil {
    int ticks = 0;
    int bruh = 0;
    int breakTimer = 0;
    String bruh1;
    boolean qwerty = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (bruh1 != null) {
            if (bruh1 != dev.madcat.m3dc3t.features.modules.client.Title.getInstance().titleName.getValue()) {
                ticks = 0;
                bruh = 0;
                breakTimer = 0;
                qwerty = false;
            }
        }
        bruh1 = dev.madcat.m3dc3t.features.modules.client.Title.getInstance().titleName.getValue();
        if (M3dC3t.moduleManager.isModuleEnabled("ClientTitle")) {
            if (bruh1 != null) {
                ++ticks;
                if (ticks % 17 == 0) {
                    Display.setTitle((bruh1.substring(0, bruh1.length() - bruh)));
                    if ((bruh == bruh1.length() && breakTimer != 2) || (bruh == 0 && breakTimer != 4)) {
                        breakTimer++;
                        return;
                    } else breakTimer = 0;
                    if (bruh == bruh1.length()) qwerty = true;
                    if (qwerty) --bruh;
                    else ++bruh;
                    if (bruh == 0) qwerty = false;
                }
            }
        } else {
            Display.setTitle("Minecraft 1.12.2");
        }
    }
}
