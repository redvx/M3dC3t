package dev.madcat.m3dc3t.features.modules.client;

import dev.madcat.m3dc3t.event.events.Render2DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStats extends Module {

    public ItemStats() {
        super("CombatStats", "Show your items count.", Category.CLIENT, true, false, false);
    }

    public Setting<Boolean> combatCount = register(new Setting<>("ItemsCount", true));
    public Setting<Integer> combatCountX = register(new Setting<>("X", 125, 0, 300, v -> combatCount.getValue()));
    public Setting<Integer> combatCountY = register(new Setting<>("Y", 18, 0, 500, v -> combatCount.getValue()));
    public Setting<Boolean> armor = register(new Setting<>("Armor", true));

    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ItemStack Crystal = new ItemStack(Items.END_CRYSTAL);
    private static final ItemStack xp = new ItemStack(Items.EXPERIENCE_BOTTLE);
    private static final ItemStack ap = new ItemStack(Items.GOLDEN_APPLE);
    private static final ItemStack obs = new ItemStack(Blocks.OBSIDIAN);

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (combatCount.getValue()) {
            int width = renderer.scaledWidth;
            int height = renderer.scaledHeight;
            int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
            if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
                totems += mc.player.getHeldItemOffhand().getCount();
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - combatCountY.getValue();
            int x = i + combatCountX.getValue();
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - renderer.getStringWidth(totems + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int Crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.END_CRYSTAL)).mapToInt(ItemStack::getCount).sum();
            if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
                Crystals += mc.player.getHeldItemOffhand().getCount();
            x = x + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(Crystal, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, Crystal, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(Crystals + "", (x + 19 - 2 - renderer.getStringWidth(Crystals + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();

            int EXP = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)).mapToInt(ItemStack::getCount).sum();
            if (mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)
                EXP += mc.player.getHeldItemOffhand().getCount();
            x = x + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(xp, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, xp, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(EXP + "", (x + 19 - 2 - renderer.getStringWidth(EXP + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();

            int GP = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.GOLDEN_APPLE)).mapToInt(ItemStack::getCount).sum();
            if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE)
                GP += mc.player.getHeldItemOffhand().getCount();
            x = x + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ap, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, ap, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(GP + "", (x + 19 - 2 - renderer.getStringWidth(GP + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();

            int OBS = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN))).mapToInt(ItemStack::getCount).sum();
            x = x + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(obs, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, obs, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(OBS + "", (x + 19 - 2 - renderer.getStringWidth(OBS + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (armor.getValue()) {
            final int width = this.renderer.scaledWidth;
            final int height = this.renderer.scaledHeight;
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) {
                    continue;
                }
                final int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
                RenderUtil.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
                this.renderer.drawStringWithShadow(s, (float) (x + 19 - 2 - this.renderer.getStringWidth(s)), (float) (y + 9), 16777215);
                final float green = (is.getMaxDamage() - (float) is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                int dmg = 100 - (int) (red * 100.0f);
                if (dmg == -2147483547) {
                    dmg = 100;
                }
                this.renderer.drawStringWithShadow(dmg + "", (float) (x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float) (y - 11), ColorUtil.toRGBA((int) (red * 255.0f), (int) (green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
}