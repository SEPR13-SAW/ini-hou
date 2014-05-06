package com.planepanic.io.packet.encoder;

import java.util.List;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.waypoint.Waypoint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SpawnPlanePacketEncoder extends Encoder<SpawnPlanePacket> {
	public SpawnPlanePacketEncoder() {
		super(SpawnPlanePacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SpawnPlanePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeByte(packet.getPlayerId());
		ByteBufUtils.writeString(buf, packet.getName());

		List<Waypoint> waypoints = packet.getFlightPlan().subList(0, packet.getFlightPlan().size());
		buf.writeByte(waypoints.size());

		for (Waypoint wp : waypoints) {
			buf.writeByte((byte) wp.getId());
		}

		buf.writeFloat(packet.getAltitude());

		return buf;
	}

}
