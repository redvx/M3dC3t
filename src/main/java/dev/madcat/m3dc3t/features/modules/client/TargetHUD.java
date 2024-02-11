/*
 * Copyright (c) 2021-2021
 * CakeSlayers Reversing Team and M3dC3t Development Team.
 * All Rights Reserved.
 *
 * CakeSlayers' Github website: https://github.com/CakeSlayers
 * This file was created by SagiriXiguajerry at 2021/11/11
 */

package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class TargetHUD extends Module {
    private final Setting<Integer> x = register(new Setting<>("X", 50, 0, 2000));
    private final Setting<Integer> y = register(new Setting<>("Y", 50, 0, 2000));
    private final Setting<Integer> backgroundAlpha = register(new Setting<>("Alpha", 80, 0, 255));

    private static double applyAsDouble(EntityLivingBase entityLivingBase) {
        return entityLivingBase.getDistance(mc.player);
    }

    private static boolean checkIsNotPlayer(Entity entity) {
        return !entity.equals(mc.player);
    }

    public TargetHUD() {
        super("TargetHUD", "Target indicator.", Category.CLIENT, true, false, false);
    }

    EntityLivingBase target = mc.player;

    @Override
    public synchronized void onTick() {
        if (!fullNullCheck()) {
            List<EntityLivingBase> entities = new LinkedList<>();
            mc.world.loadedEntityList
                    .stream()
                    .filter(EntityPlayer.class::isInstance)
                    .filter(TargetHUD::checkIsNotPlayer)
                    .map(EntityLivingBase.class::cast)
                    .sorted(Comparator.comparingDouble(TargetHUD::applyAsDouble))
                    .forEach(entities::add);
            if (!entities.isEmpty()) target = entities.get(0); else target = mc.player;
            if (mc.currentScreen instanceof GuiChat) {
                target = mc.player;
            }
        }
    }

    @Override
    public synchronized void onRender2D(Render2DEvent event) {
        if (target != null && !target.isDead) {
                    if (!TargetHUD.fullNullCheck() && target != null) {
                        final FontRenderer fr = mc.fontRenderer;

                        // HealthBar's color
                        int color = (target.getHealth() / target.getMaxHealth() > 0.66f) ? 0xff00ff00 : (target.getHealth() / target.getMaxHealth() > 0.33f) ? 0xffff9900 : 0xffff0000;

                        /* Target Model */
                        GlStateManager.color(1, 1, 1);
                        GuiInventory.drawEntityOnScreen(x.getValue() + 15, y.getValue() + 32, 15, 1f, 1f, target);
                        /* Target Model */

                        /* Target Armor&HeldItem */
                        List<ItemStack> armorList = new LinkedList<>();
                        List<ItemStack> _armorList = new LinkedList<>();
                        target.getArmorInventoryList().forEach(itemStack -> {
                            if (!itemStack.isEmpty()) _armorList.add(itemStack);
                        });
                        for (int i = _armorList.size() - 1; i >= 0; --i) {
                            armorList.add(_armorList.get(i));
                        }
                        int armorSize = 0;
                        switch (armorList.size()) {
                            case 0: {
                                if (!target.getHeldItemMainhand().isEmpty() && !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemMainhand(), x.getValue() + 28, y.getValue() + 18);
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemOffhand(), x.getValue() + 43, y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                } else if (!target.getHeldItemMainhand().isEmpty() || !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                            target.getHeldItemMainhand().isEmpty() ? target.getHeldItemOffhand() : target.getHeldItemMainhand(),
                                            x.getValue() + 28,
                                            y.getValue() + 18
                                    );
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 1: {
                                armorSize = 15;
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(0), x.getValue() + 28, y.getValue() + 18);
                                if (!target.getHeldItemMainhand().isEmpty() && !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemMainhand(), x.getValue() + 43, y.getValue() + 18);
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemOffhand(), x.getValue() + 58, y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                } else if (!target.getHeldItemMainhand().isEmpty() || !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                            target.getHeldItemMainhand().isEmpty() ? target.getHeldItemOffhand() : target.getHeldItemMainhand(),
                                            x.getValue() + 43,
                                            y.getValue() + 18
                                    );
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                armorSize = 30;
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(0), x.getValue() + 28, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(1), x.getValue() + 43, y.getValue() + 18);
                                if (!target.getHeldItemMainhand().isEmpty() && !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemMainhand(), x.getValue() + 58, y.getValue() + 18);
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemOffhand(), x.getValue() + 73, y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                } else if (!target.getHeldItemMainhand().isEmpty() || !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                            target.getHeldItemMainhand().isEmpty() ? target.getHeldItemOffhand() : target.getHeldItemMainhand(),
                                            x.getValue() + 58,
                                            y.getValue() + 18
                                    );
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 3: {
                                armorSize = 45;
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(0), x.getValue() + 28, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(1), x.getValue() + 43, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(2), x.getValue() + 58, y.getValue() + 18);
                                if (!target.getHeldItemMainhand().isEmpty() && !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemMainhand(), x.getValue() + 73, y.getValue() + 18);
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemOffhand(), x.getValue() + 98, y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                } else if (!target.getHeldItemMainhand().isEmpty() || !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                            target.getHeldItemMainhand().isEmpty() ? target.getHeldItemOffhand() : target.getHeldItemMainhand(),
                                            x.getValue() + 73,
                                            y.getValue() + 18
                                    );
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 4: {
                                armorSize = 60;
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(0), x.getValue() + 28, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(1), x.getValue() + 43, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(2), x.getValue() + 58, y.getValue() + 18);
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList.get(3), x.getValue() + 73, y.getValue() + 18);
                                if (!target.getHeldItemMainhand().isEmpty() && !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemMainhand(), x.getValue() + 98, y.getValue() + 18);
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItemOffhand(), x.getValue() + 113, y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                } else if (!target.getHeldItemMainhand().isEmpty() || !target.getHeldItemOffhand().isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                            target.getHeldItemMainhand().isEmpty() ? target.getHeldItemOffhand() : target.getHeldItemMainhand(),
                                            x.getValue() + 98,
                                            y.getValue() + 18
                                    );
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                        }
                        /* Target Armor&HeldItem */

                        /* Background */
                        int backgroundStopY = y.getValue() + 35;
                        int stringWidth;
                            stringWidth = fr.getStringWidth(target.getName()) + 30;
                        int backgroundStopX;
                        if (fr.getStringWidth(target.getName()) > armorSize)
                            backgroundStopX = x.getValue() + stringWidth;
                        else
                            backgroundStopX = x.getValue() + armorSize + 30;
                        backgroundStopX += 5;
                        backgroundStopY += 5;
                        Gui.drawRect(x.getValue() - 2, y.getValue(), backgroundStopX, backgroundStopY, new Color(0, 0, 0, backgroundAlpha.getValue()).getRGB());
                        /* Background */

                        /* HealthBar */
                        int healthBarLength = (int) ((target.getHealth() / target.getMaxHealth()) * (backgroundStopX - x.getValue()));
                        Gui.drawRect(x.getValue() - 2, backgroundStopY - 2, x.getValue() + healthBarLength, backgroundStopY, color);
                        /* HealthBar */

                        //Target Name
                        fr.drawString(target.getName(), x.getValue() + 30, y.getValue() + 8, new Color(255, 255, 255).getRGB(), true);
                    }
        }
    }
}
