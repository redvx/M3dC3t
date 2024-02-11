package dev.madcat.m3dc3t.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class HUD extends Module {
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> server;
    private final Setting<Boolean> burrow;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Map<String, Integer> players;
    private final Setting<Boolean> lag;
    public Setting<Boolean> FriendList;
    public Setting<Integer> arraylisty;

    public Setting<Integer> FriendListY;
    public Setting<Integer> sklisty;

    public Setting<Integer> sklistY;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public Setting<Boolean> notifyToggles;
    public Setting<RenderingMode> renderingMode;
    public Setting<Integer> lagTime;
    private int color;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen.", Category.CLIENT, true, false, false);
        this.grayNess = (Setting<Boolean>) this.register(new Setting("Gray", true));
        this.arrayList = (Setting<Boolean>) this.register(new Setting("ActiveModules", true, "Lists the active modules."));
        this.renderingUp = (Setting<Boolean>) this.register(new Setting("RenderingUp", Boolean.TRUE, v -> this.arrayList.getValue()));
        this.arraylisty = (Setting<Integer>) this.register(new Setting("OffsetY", 40, 0, 200, v -> this.arrayList.getValue() && this.renderingUp.getValue()));
        this.coords = (Setting<Boolean>) this.register(new Setting("Coords", true, "Your current coordinates"));
        this.speed = (Setting<Boolean>) this.register(new Setting("Speed", false, "Your Speed"));
        this.server = (Setting<Boolean>) this.register(new Setting("IP", false, "Shows the server"));
        this.ping = (Setting<Boolean>) this.register(new Setting("Ping", false, "Your response time to the server."));
        this.tps = (Setting<Boolean>) this.register(new Setting("TPS", false, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>) this.register(new Setting("FPS", false, "Your frames per second."));
        this.players = new HashMap<String, Integer>();
        this.lag = (Setting<Boolean>) this.register(new Setting("LagNotifier", true, "The time"));
        this.FriendList = (Setting<Boolean>) this.register(new Setting("FriendList", Boolean.FALSE));
        this.FriendListY = (Setting<Integer>) this.register(new Setting("FriendListY", 173, 0, 530, v -> this.FriendList.getValue()));
        this.bracketColor = (Setting<TextUtil.Color>) this.register(new Setting("BracketColor", TextUtil.Color.WHITE));
        this.commandColor = (Setting<TextUtil.Color>) this.register(new Setting("NameColor", TextUtil.Color.WHITE));
        this.notifyToggles = (Setting<Boolean>) this.register(new Setting("ChatNotify", true, "notifys in chat"));
        this.renderingMode = (Setting<RenderingMode>) this.register(new Setting("Ordering", RenderingMode.Length));
        this.lagTime = (Setting<Integer>) this.register(new Setting("LagTime", 1000, 0, 2000));
        this.burrow = (Setting<Boolean>) this.register(new Setting("BurrowWarner", false, "Your Speed"));
        this.setInstance();
    }
    public final Setting<Boolean> moduleInfo = register(new Setting<Boolean>("ModuleInfo", true));
    final Setting<Boolean> waterMark = (Setting<Boolean>) this.register(new Setting("Watermark", false, "displays watermark"));
    public final Setting<Boolean> watermark2 = register(new Setting<Boolean>("SkeetWatermark", false, v -> waterMark.getValue()));
    public final Setting<Integer> compactX = register(new Setting<Object>("SkeetMarkX", 20, 0, 1080, v -> waterMark.getValue() && watermark2.getValue()));
    public final Setting<Integer> compactY = register(new Setting("SkeetMarkY", 20, 0, 530, v -> waterMark.getValue() && watermark2.getValue()));
    public final Setting<Integer> alpha = register(new Setting("Alpha", 50, 0, 255, v -> waterMark.getValue() && watermark2.getValue()));

    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (burrow.getValue()) {
            BlockPos burrowpos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (burrowpos != null) {
                if (mc.world.getBlockState(burrowpos).getBlock() == Blocks.OBSIDIAN | mc.world.getBlockState(burrowpos.add(0, 0.4, 0)).getBlock() == Blocks.ENDER_CHEST) {
                    M3dC3t.textManager.drawString(ChatFormatting.DARK_RED  + "You are in Burrow!", renderer.scaledWidth / 2 - 45, renderer.scaledHeight / 2 - 20, 200, true);
                }
            }
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.waterMark.getValue() && !watermark2.getValue()) {
            final String string = ClickGui.getInstance().clientName.getValueAsString() + " " + M3dC3t.ID;
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, 2, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    final int[] arrayOfInt = {1};
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 2, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0f, 2, this.color, true);
            }
        }

        if (waterMark.getValue() && watermark2.getValue()) {
            if (alpha.getValue() >= 0) RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + "Sense | FPS:" + Minecraft.debugFPS + " | TPS:" + M3dC3t.serverManager.getTPS() + " | Ping:" + M3dC3t.serverManager.getPing()), 15, ColorUtil.toRGBA(20, 20, 20, alpha.getValue()));
            final String string = ClickGui.getInstance().clientName.getValueAsString() + "Sense | FPS:" + Minecraft.debugFPS + " | TPS:" + M3dC3t.serverManager.getTPS() + " | Ping:" + M3dC3t.serverManager.getPing();
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            if (ClickGui.getInstance().rainbow.getValue()) {
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue().intValue(), 0 + (int)this.compactY.getValue().intValue(), 10 + renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + "Sense | FPS:" + Minecraft.debugFPS + " | TPS:" + M3dC3t.serverManager.getTPS() + " | Ping:" + M3dC3t.serverManager.getPing()), 1, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB());
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, this.compactX.getValue().intValue() + 5, this.compactY.getValue().intValue() + 4, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    } else {
                    final int[] arrayOfInt = {1};
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), this.compactX.getValue().intValue() + 5 + f, this.compactY.getValue() + 4, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            } else {
                this.renderer.drawString(string, this.compactX.getValue().intValue() + 5, this.compactY.getValue().intValue() + 4, this.color, true);
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue().intValue(), (int)this.compactY.getValue().intValue(), 10 + renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + "Sense | FPS:" + Minecraft.debugFPS + " | TPS:" + M3dC3t.serverManager.getTPS() + " | Ping:" + M3dC3t.serverManager.getPing()), 1, this.color);

            }
        }
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        int y = 10;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.FriendList.getValue()) {
            this.renderFriends();
        }
        final int[] counter1 = {1};
        int j = (HUD.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < M3dC3t.moduleManager.sortedModulesABC.size(); ++k) {
                        final String str = M3dC3t.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (float) (width - 2 - this.renderer.getStringWidth(str)), (float) (2 + j * 4 + this.arraylisty.getValue()), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        j = j+3;
                        ++counter1[0];
                    }
                } else {
                    for (int k = 0; k < M3dC3t.moduleManager.sortedModules.size(); ++k) {
                        final Module module = M3dC3t.moduleManager.sortedModules.get(k);
                        final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str2, (float) (width - 2 - this.renderer.getStringWidth(str2)), (float) (2 + j * 4 + this.arraylisty.getValue()), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        j = j+3;
                        ++counter1[0];
                    }
                }
            } else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < M3dC3t.moduleManager.sortedModulesABC.size(); ++k) {
                    final String str = M3dC3t.moduleManager.sortedModulesABC.get(k);
                    j += 12;
                    this.renderer.drawString(str, (float) (width - 2 - this.renderer.getStringWidth(str)), (float) (height - j), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            } else {
                for (int k = 0; k < M3dC3t.moduleManager.sortedModules.size(); ++k) {
                    final Module module = M3dC3t.moduleManager.sortedModules.get(k);
                    final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 12;
                    this.renderer.drawString(str2, (float) (width - 2 - this.renderer.getStringWidth(str2)), (float) (height - j), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                i += 10;
                this.renderer.drawString(sText, (float) (width - this.renderer.getStringWidth(sText) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + M3dC3t.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float) (width - this.renderer.getStringWidth(str2) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + M3dC3t.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float) (width - this.renderer.getStringWidth(str2) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String sText2 = grayString + "Server " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + M3dC3t.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float) (width - this.renderer.getStringWidth(str4) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float) (width - this.renderer.getStringWidth(fpsText) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            } else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float) (width - this.renderer.getStringWidth(fpsText) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float) (width - this.renderer.getStringWidth(str4) - 2), (float) (height - 2 - i), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        } else {
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                this.renderer.drawString(sText, (float) (width - this.renderer.getStringWidth(sText) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + M3dC3t.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float) (width - this.renderer.getStringWidth(str2) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + M3dC3t.serverManager.getTPS();
                this.renderer.drawString(str2, (float) (width - this.renderer.getStringWidth(str2) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str5 = grayString + "Ping " + ChatFormatting.WHITE + M3dC3t.serverManager.getPing();
            if (this.renderer.getStringWidth(str5) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str5, (float) (width - this.renderer.getStringWidth(str5) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float) (width - this.renderer.getStringWidth(fpsText) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            } else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float) (width - this.renderer.getStringWidth(fpsText) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str5, (float) (width - this.renderer.getStringWidth(str5) - 2), (float) (2 + i++ * 10), ((boolean) ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int) HUD.mc.player.posX;
        final int posY = (int) HUD.mc.player.posY;
        final int posZ = (int) HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int) (HUD.mc.player.posX * nether);
        final int hposZ = (int) (HUD.mc.player.posZ * nether);
        i = ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.coords.getValue() ? M3dC3t.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : "";
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float) (height - i - 11), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float) (height - i), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                final int[] counter2 = {1};
                final char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (final char c2 : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c2), 2.0f + s, (float) (height - i - 11), ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c2));
                    ++counter2[0];
                }
                final int[] counter3 = {1};
                final char[] stringToCharArray3 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + u, (float) (height - i), ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c3));
                    ++counter3[0];
                }
            }
        } else {
            this.renderer.drawString(direction, 2.0f, (float) (height - i - 11), this.color, true);
            this.renderer.drawString(coords, 2.0f, (float) (height - i), this.color, true);
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (M3dC3t.serverManager.isServerNotResponding()) {
            final String text = ChatFormatting.RED + "Server not responding " + MathUtil.round(M3dC3t.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    private void renderFriends() {
        final List<String> friends = new ArrayList<String>();
        for (final EntityPlayer player : HUD.mc.world.playerEntities) {
            if (M3dC3t.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                int y = this.FriendListY.getValue();
                if (friends.isEmpty()) {
                    this.renderer.drawString("No friends", 0.0f, (float) y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    this.renderer.drawString("Friends:", 0.0f, (float) y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                    for (final String friend : friends) {
                        this.renderer.drawString(friend, 0.0f, (float) y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += 12;
                    }
                }
            }
        } else {
            int y = this.FriendListY.getValue();
            if (friends.isEmpty()) {
                this.renderer.drawString("No Webstas online", 0.0f, (float) y, this.color, true);
            } else {
                this.renderer.drawString("Webstas near you:", 0.0f, (float) y, this.color, true);
                y += 12;
                for (final String friend : friends) {
                    this.renderer.drawString(friend, 0.0f, (float) y, this.color, true);
                    y += 12;
                }
            }
        }
    }

    static {
        HUD.INSTANCE = new HUD();
    }

    public enum RenderingMode {
        Length,
        ABC;
    }
    private void setInstance() {
        RenderUtil.prepare();
        HUD.INSTANCE = this;
    }
}