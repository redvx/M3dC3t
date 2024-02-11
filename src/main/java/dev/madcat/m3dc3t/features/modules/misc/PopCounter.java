/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 */
package dev.madcat.m3dc3t.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.command.Command;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.HashMap;

public class PopCounter
        extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private final Setting<Boolean> atez = this.register(new Setting<Boolean>("AutoEZ", false));
    private final Setting<Boolean> atez2 = this.register(new Setting<Boolean>("SelfAutoEZ", false));
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                if (PopCounter.mc.player.equals((Object)player)) {
                    if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem!");
                        if (atez2.getValue()) {
                            mc.player.connection.sendPacket(new CPacketChatMessage("Im so EZ died after popping " + l_Count + " Totem!"));
                        }
                    }
                } else {
                    if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem!");
                        if (atez.getValue()) {
                            mc.player.connection.sendPacket(new CPacketChatMessage("EZ " + player.getName() + " died after popping " + l_Count + " Totem!"));
                        }
                    }
                }
            } else if (PopCounter.mc.player.equals((Object)player)) {
                if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems!");
                    if (atez2.getValue()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage("Im so EZ died after popping " + l_Count + " Totem!"));
                    }
                }
            } else {
                if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems!");
                    if (atez.getValue()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage("EZ " + player.getName() + " died after popping " + l_Count + " Totem!"));
                    }
                }
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            if (PopCounter.mc.player.equals((Object)player)) {
                if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem.");
                }
            } else {
                    if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem.");
                    }
            }
        } else if (PopCounter.mc.player.equals((Object)player)) {
            if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems.");
            }
        } else {
                if (M3dC3t.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems.");
                }
        }
    }
}

