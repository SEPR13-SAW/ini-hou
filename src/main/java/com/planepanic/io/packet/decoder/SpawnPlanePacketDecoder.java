package com.planepanic.io.packet.decoder;

import java.util.ArrayList;
import java.util.List;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.Waypoint;

import io.netty.buffer.ByteBuf;

public final class SpawnPlanePacketDecoder extends Decoder<SpawnPlanePacket> {
	public SpawnPlanePacketDecoder() {
		super(0x81);
	}

	@Override
	public SpawnPlanePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		byte playerId = buffer.readByte();
		String name = ByteBufUtils.readString(buffer);

		int nWaypoints = buffer.readByte() & 0xFF;
		List<Waypoint> waypoints = new ArrayList<>();
		for (int i = 0; i < nWaypoints; i++) {
			waypoints.add(new Waypoint(buffer.readFloat(), buffer.readFloat(), false));
		}

		int altitude = buffer.readInt();

		return new SpawnPlanePacket(planeId, playerId, name, waypoints, altitude);
	}

}
