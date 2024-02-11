package dev.madcat.m3dc3t.util;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage
extends Event {
    private int stage;

    public EventStage() {
    }

    public EventStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

}

