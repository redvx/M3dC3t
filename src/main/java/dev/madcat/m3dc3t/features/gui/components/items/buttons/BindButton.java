package dev.madcat.m3dc3t.features.gui.components.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.gui.M3dC3tGui;
import dev.madcat.m3dc3t.features.modules.client.ClickGui;
import dev.madcat.m3dc3t.features.modules.client.FontMod;
import dev.madcat.m3dc3t.features.setting.Bind;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BindButton
        extends Button {
    private final Setting setting;
    public boolean isListening;

    public BindButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int color = ColorUtil.toARGB(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255);
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515) : (!this.isHovering(mouseX, mouseY) ? M3dC3t.colorManager.getColorWithAlpha(M3dC3t.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : M3dC3t.colorManager.getColorWithAlpha(M3dC3t.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())));
        if (this.isListening) {
            if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
                M3dC3t.textManager.drawStringWithShadow("Press a Key...", this.x + 2.3f, this.y - 1.7f - (float) M3dC3tGui.getClickGui().getTextOffset(), -1);
            } else {
                M3dC3t.textManager.drawStringClickGui("Press a Key...", this.x + 2.3f, this.y - 0.7f - (float) M3dC3tGui.getClickGui().getTextOffset(), -1);
            }
        } else {
            if ((!FontMod.getInstance().cfont.getValue() && M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) || !M3dC3t.moduleManager.getModuleByName("CustomFont").isEnabled()) {
                M3dC3t.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 1.7f - (float) M3dC3tGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
            } else {
                M3dC3t.textManager.drawStringClickGui(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 0.7f - (float) M3dC3tGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}

