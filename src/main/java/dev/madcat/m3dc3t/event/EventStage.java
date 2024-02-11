package dev.madcat.m3dc3t.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage
        extends Event {
    private int stage;

    public EventStage() {
    }
    private boolean cancelled=true;
    public EventStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

}

