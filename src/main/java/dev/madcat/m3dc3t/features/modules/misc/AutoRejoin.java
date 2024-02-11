package dev.madcat.m3dc3t.features.modules.misc;


import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.MathUtil;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRejoin
        extends Module {
    private static ServerData serverData;
    private static AutoRejoin INSTANCE;

    static {
        INSTANCE = new AutoRejoin();
    }

    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5));

    public AutoRejoin() {
        super("AutoRejoin", "Reconnects you if you disconnect.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static AutoRejoin getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoRejoin();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void sendPacket(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            this.updateLastConnectedServer();
                GuiDisconnected disconnected = (GuiDisconnected) event.getGui();
                event.setGui(new GuiDisconnectedHook(disconnected));
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.updateLastConnectedServer();
    }

    public void updateLastConnectedServer() {
        ServerData data = mc.getCurrentServerData();
        if (data != null) {
            serverData = data;
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }


    private class GuiDisconnectedHook
            extends GuiDisconnected {
        private final Timer timer;

        public GuiDisconnectedHook(GuiDisconnected disconnected) {
            super(disconnected.parentScreen, disconnected.reason, disconnected.message);
            this.timer = new Timer();
            this.timer.reset();
        }

        public void updateScreen() {
            if (this.timer.passedS(AutoRejoin.this.delay.getValue().intValue())) {
                this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, this.mc, serverData == null ? this.mc.currentServerData : serverData));
            }
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            String s = "Reconnecting in " + MathUtil.round((double) ((long) (AutoRejoin.this.delay.getValue() * 1000) - this.timer.getPassedTimeMs()) / 1000.0, 1);
            AutoRejoin.this.renderer.drawString(s, this.width / 2 - AutoRejoin.this.renderer.getStringWidth(s) / 2, this.height - 16, 0xFFFFFF, true);
        }
    }
}

