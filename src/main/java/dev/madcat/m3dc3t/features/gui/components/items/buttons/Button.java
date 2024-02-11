package dev.madcat.m3dc3t.features.gui.components.items.buttons;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.gui.M3dC3tGui;
import dev.madcat.m3dc3t.features.gui.components.Component;
import dev.madcat.m3dc3t.features.gui.components.items.Item;
import dev.madcat.m3dc3t.features.modules.client.ClickGui;
import dev.madcat.m3dc3t.features.modules.client.FontMod;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class Button
        extends Item {
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? M3dC3t.colorManager.getColorWithAlpha(M3dC3t.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : M3dC3t.colorManager.getColorWithAlpha(M3dC3t.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
            M3dC3t.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float) M3dC3tGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            M3dC3t.textManager.drawStringClickGui(this.getName(), this.x + 2.3f, this.y - 1.0f - (float) M3dC3tGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : M3dC3tGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}

