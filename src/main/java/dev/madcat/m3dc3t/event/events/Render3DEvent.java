package dev.madcat.m3dc3t.event.events;

import dev.madcat.m3dc3t.event.EventStage;

public class Render3DEvent extends EventStage
{
    private float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

