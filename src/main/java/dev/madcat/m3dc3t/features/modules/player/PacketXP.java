package dev.madcat.m3dc3t.features.modules.player;


import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Bind;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @author Madmegsox1
 * @since 30/04/2021
 */

public class PacketXP extends Module {
    public PacketXP() {
        super("PacketXP", "Silent use experience.", Category.PLAYER, false, false, false);
    }
    private final Setting<Bind> bind = this.register(new Setting<Bind>("PacketBind", new Bind(-1)));
    private int delay_count;
    int prvSlot;
    public static   Boolean inft=false;
    public static Bind binds;


    @Override
    public void onEnable() {
        delay_count = 0;
        return;
    }

    @Override
    public void onUpdate() {
        binds=bind.getValue();

        if (findExpInHotbar() == -1) {
            return;
        }

        if (bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(bind.getValue().getKey()) && mc.currentScreen == null) {
                if(findExpInHotbar()==-1){
                    return;
                }
                usedXp();
            }
        } else if (bind.getValue().getKey() < -1) {
            if (Mouse.isButtonDown(convertToMouse(bind.getValue().getKey())) && mc.currentScreen == null) {
                if(findExpInHotbar()==-1){
                    return;
                }
                usedXp();

            }
        }

    }

    public static int convertToMouse(int key) {
        switch (key) {
            case -2:
                return 0;
            case -3:
                return 1;
            case -4:
                return 2;
            case -5:
                return 3;
            case -6:
                return 4;
        }
        return -1;
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private void usedXp() {

        int oldPitch = (int) mc.player.rotationPitch;
        prvSlot = mc.player.inventory.currentItem; //TODO add better rotations
        mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        mc.player.rotationPitch = -90;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.rotationPitch = oldPitch;
        mc.player.inventory.currentItem = prvSlot;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(prvSlot));

    }

    public Boolean notInInv(Item itemOfChoice) {
        int n;
        n = 0;
        if (itemOfChoice == mc.player.getHeldItemOffhand().getItem()) return true;

        for (int i = 35; i >= 0; i--) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;

            } else if (item != itemOfChoice) {
                n++;
            }
        }
        if (n >= 35) {

            return false;
        }
        return true;
    }


}
