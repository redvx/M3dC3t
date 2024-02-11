package dev.madcat.m3dc3t.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat extends Module {
    private static Chat INSTANCE = new Chat();
    public Setting<Boolean> clean = register(new Setting<Boolean>("NoChatBackground", Boolean.valueOf(true), "Cleans your chat"));
    public Setting<Boolean> infinite = register(new Setting<Boolean>("InfiniteChat", Boolean.valueOf(true), "Makes your chat infinite."));
    public Setting<Boolean> time = register(new Setting<Boolean>("ChatTime", Boolean.valueOf(false), "Makes your chat infinite."));
    private Setting<Integer> red = register(new Setting("Red", 20, 0, 255));
    private Setting<Integer> green = register(new Setting("Green", 20, 0, 255));
    private Setting<Integer> blue = register(new Setting("Blue", 20, 0, 255));
    private Setting<Integer> alpha = register(new Setting("Alpha", 35, 0, 255));
    public boolean check;

    public Chat() {
        super("BetterChat", "Modifies your chat box.", Category.CLIENT, true, false, false);
        setInstance();
    }

    public static Chat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chat();
        }
        return INSTANCE;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) {
            RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()));
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            check = !s.startsWith(M3dC3t.commandManager.getPrefix());
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString("");
        if (this.time.getValue().booleanValue()) {
            time = new TextComponentString(ChatFormatting.RED + "<" + ChatFormatting.GRAY + strDate + ChatFormatting.RED + ">" + ChatFormatting.RESET + " ");
        }
        event.setMessage(time.appendSibling(event.getMessage()));
    }
}

