package dev.madcat.m3dc3t.event.events;

import dev.madcat.m3dc3t.event.EventStage;

public class KeyEvent
        extends EventStage {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

