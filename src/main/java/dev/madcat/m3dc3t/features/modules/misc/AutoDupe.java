package dev.madcat.m3dc3t.features.modules.misc;

import dev.madcat.m3dc3t.features.modules.combat.InstantMine;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoDupe extends Module {
    public AutoDupe() {
        super("AutoDupe","Automatically places Shulker.", Category.MISC, true,false,false);
    }

    public Setting<Boolean> packet = register(new Setting<Boolean>("Packet", false));
    public Setting<Boolean> rotate = register(new Setting<Boolean>("Rotate", false));
    private final Setting<Integer> delay = register(new Setting<Integer>("Delay", 0, 0, 2000));
    private final Timer timer = new Timer();



    private BlockPos pos;
    int Im;

    @Override
    public void onEnable() {

    }
    @Override
    public void onDisable() {

    }

    @Override
    public void onUpdate() {
        if(InstantMine.breakPos==null){
            return;
        }
        pos = InstantMine.breakPos;
        IBlockState blockState = mc.world.getBlockState(pos);
        Im = getItemShulkerBox();
        if(blockState.getBlock()== Blocks.AIR&&Im!=-1){
            mc.player.inventory.currentItem = Im;
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), false);
            timer.passedDms(delay.getValue());
        }
    }

    public int getItemShulkerBox(){
        int fus = -1;
        for (int x = 0; x <= 8; ++x) {
            Item item = mc.player.inventory.getStackInSlot(x).getItem();
            if (item instanceof ItemShulkerBox) {
                fus= x;
            }
        }
        return fus;
    }

}





