package com.planepanic.io.packet.encoder;

import java.util.List;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.waypoint.Waypoint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SpawnPlanePacketEncoder extends Encoder<SpawnPlanePacket> {
	public SpawnPlanePacketEncoder() {
		super(0x81);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SpawnPlanePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeByte(packet.getPlayerId());
		ByteBufUtils.writeString(buf, packet.getName());

		List<Waypoint> waypoints = packet.getFlightPlan();
		buf.writeByte(waypoints.size());

		for (Waypoint wp : waypoints) {
			buf.writeFloat(wp.getX());
			buf.writeFloat(wp.getY());
		}

		buf.writeInt(packet.getAltitude());

		return buf;
	}

}
