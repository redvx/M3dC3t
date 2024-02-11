package dev.madcat.m3dc3t.features.modules.movement;

import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.Timer;

public class Timers extends Module {
    private final Setting<Boolean> tickMode = register(new Setting<Boolean>("TickMode",false));
    public final Setting<Integer> disableTicks = this.register(new Setting<Integer>("Disable Ticks", 30, 1, 100, v -> tickMode.getValue()));
    public final Setting<Integer> noPauseTicks = this.register(new Setting<Integer>("UnPause Ticks", 30, 1, 100, v -> tickMode.getValue()));
    public int ticksPassed = 0;
    public int unPauseTicks = 0;
    public boolean pause = false;
    private final Setting<Float> tickNormal = register(new Setting<Float>("Speed", 1.2f, 1.0f, 10.0f));
    private final Setting<Boolean> bypass = register(new Setting<Boolean>("Bypass",true, v -> !tickMode.getValue()));
    public final Setting<Float> speedvalue = register(new Setting<Float>("SpeedValue", 0.0f, 0.0f, 100.0f, v -> !tickMode.getValue() && this.bypass.getValue()));

    public int i = 0;
    public int x = 0;

    private final Timer timer = new Timer();

    public Timers() {
        super("Timer", "Change client running speed.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50.0f;
    }

    @Override
    public void onEnable() {
        mc.timer.tickLength = 50.0f;
    }

    @Override
    public void onUpdate() {
        if (tickMode.getValue()) {
            ++this.ticksPassed;
            if (!this.pause) {
                mc.timer.tickLength = 50.0f / tickNormal.getValue();
            }
            if (this.pause) {
                ++this.unPauseTicks;
                mc.timer.tickLength = 50.0f;
            }
            if (this.ticksPassed >= this.disableTicks.getValue()) {
                this.ticksPassed = 0;
                    if (this.unPauseTicks <= this.noPauseTicks.getValue()) {
                        this.pause = true;
                    } else if (this.unPauseTicks >= this.noPauseTicks.getValue()) {
                        this.pause = false;
                        this.unPauseTicks = 0;
                    }
            }
        } else {
            if (bypass.getValue()) {
                if (this.i <= this.speedvalue.getValue()) {
                    ++this.i;
                    mc.timer.tickLength = 50.0f / this.tickNormal.getValue().floatValue();
                    this.x = 0;
                } else if (this.x <= this.speedvalue.getValue() - this.speedvalue.getValue() / 2 / 2) {
                    ++this.x;
                    mc.timer.tickLength = 50.0f;
                } else {
                    this.i = 0;
                }
            } else {
                mc.timer.tickLength = 50.0f / this.tickNormal.getValue().floatValue();
            }
        }
    }

    @Override
    public void onLogin() {
        this.timer.reset();
    }
}

