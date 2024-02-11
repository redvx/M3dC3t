package dev.madcat.m3dc3t.util;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author bon
 * @since 11/21/20
 */

@Cancelable
public class PacketSendEvent extends PacketEvent {
	public PacketSendEvent(Packet<?> packet, Stage stage) {
		super(packet, stage);
	}
}
