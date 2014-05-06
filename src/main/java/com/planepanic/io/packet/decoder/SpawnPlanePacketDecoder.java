package com.planepanic.io.packet.decoder;

import java.util.Stack;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.waypoint.Waypoint;
import com.planepanic.model.waypoint.WaypointManager;

import io.netty.buffer.ByteBuf;

public final class SpawnPlanePacketDecoder extends Decoder<SpawnPlanePacket> {
	public SpawnPlanePacketDecoder() {
		super(SpawnPlanePacket.ID);
	}

	@Override
	public SpawnPlanePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		byte playerId = buffer.readByte();
		String name = ByteBufUtils.readString(buffer);

		int nWaypoints = buffer.readByte() & 0xFF;
		Stack<Waypoint> waypoints = new Stack<>();
		for (int i = 0; i < nWaypoints; i++) {
			Waypoint wp = WaypointManager.getAll().get(buffer.readByte() & 0xFF);
			waypoints.add(wp);
		}

		float altitude = buffer.readFloat();

		return new SpawnPlanePacket(planeId, playerId, name, waypoints, altitude);
	}

}
