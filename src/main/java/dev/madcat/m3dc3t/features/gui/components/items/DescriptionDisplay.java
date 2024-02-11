/*
 * Decompiled with CFR 0.152.
 */
package dev.madcat.m3dc3t.features.gui.components.items;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.client.FontMod;
import dev.madcat.m3dc3t.util.RenderUtil;

public class DescriptionDisplay
extends Item {
    private String description;
    private boolean draw;

    public DescriptionDisplay(String description, float x, float y) {
        super("DescriptionDisplay");
        this.description = description;
        this.setLocation(x, y);
        if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            this.width = M3dC3t.textManager.getStringWidth(this.description) + 4;
        } else {
            this.width = M3dC3t.textManager.getStringCWidth(this.description) + 4;
        }
        this.height = M3dC3t.textManager.getFontHeight() + 4;
        this.draw = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            this.width = M3dC3t.textManager.getStringWidth(this.description) + 4;
        } else {
            this.width = M3dC3t.textManager.getStringCWidth(this.description) + 5;
        }
        this.height = M3dC3t.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, -704643072);
        if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            M3dC3t.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 0xFFFFFF, true);
        } else {
            M3dC3t.textManager.drawStringClickGui(this.description, this.x + 2.0f, this.y + 3, 0xFFFFFF);
        }
    }

    public boolean shouldDraw() {
        return this.draw;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}

