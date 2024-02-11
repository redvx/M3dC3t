package dev.madcat.m3dc3t.features.modules.movement;


import dev.madcat.m3dc3t.event.events.UpdateWalkingPlayerEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.MathUtil;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow
        extends Module {

    public NoSlow() {
        super("NoSlow", "No item use slow down.", Module.Category.MOVEMENT, true, false, false);
    }
    public Setting<Boolean> slow = this.register(new Setting<Boolean>("Slow", true));
    public Setting<Boolean> shift = this.register(new Setting<Boolean>("Sneak", false));
    public Setting<Boolean> web = this.register(new Setting<Boolean>("Web", false));
    private final Setting<Float> speed = this.register(new Setting<Float>("Factor", 10.0f, 1.0f, 10.0f, v -> web.getValue()));
    private final Setting<Float> speed2 = this.register(new Setting<Float>("Factor", 0.0f, 1.0f, 1.0f, v -> web.getValue()));

    @SubscribeEvent
    public void Slow(InputUpdateEvent event) {
        if (!mc.player.isSneaking() && mc.player.isHandActive() && slow.getValue() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
            return;
        }
        if (shift.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 1) {
            if (web.getValue() && mc.player.isInWeb) {
                double[] calc = MathUtil.directionSpeed((double) this.speed.getValue() / 10.0);
                mc.player.motionX = calc[0];
                mc.player.motionZ = calc[1];
                mc.player.motionY -= this.speed2.getValue();
            }
        }
    }
}

