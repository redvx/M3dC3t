/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package dev.madcat.m3dc3t.event.events;


import dev.madcat.m3dc3t.event.EventStage;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent
        extends EventStage {
    public final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }

    @Cancelable
    public static class Receive
            extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    @Cancelable
    public static class Send
            extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }
}

