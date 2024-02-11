package dev.madcat.m3dc3t.event.events;

import dev.madcat.m3dc3t.event.EventStage;

public class PlayerJumpEvent extends EventStage {
    public double motionX;
    public double motionY;

    public PlayerJumpEvent(double motionX, double motionY)
    {
        super();
        this.motionX = motionX;
        this.motionY = motionY;
    }
}