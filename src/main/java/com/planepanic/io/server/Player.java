package com.planepanic.io.server;

import java.util.Map.Entry;

import com.planepanic.io.Frame;
import com.planepanic.io.packet.Packet;
import com.planepanic.io.packet.UpdateScorePacket;
import com.planepanic.io.packet.encoder.Encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * Server-side representation of a player.
 * @author Jonathan
 *
 */
public final class Player extends com.planepanic.io.client.Player {
	@Getter @Setter private int id;
	@Getter @Setter private String name;
	@Getter private final Server server;
	@Getter private final Channel channel;
	@Getter @Setter private int score;

	public Player(Server server, Channel channel) {
		super(0, "");
		this.server = server;
		this.channel = channel;
	}

	public void writePacket(Packet packet) {
		synchronized (this) {
			Encoder<?> encoder = Encoder.get(packet.getId());
			ByteBuf buf = encoder.encodeGeneric(channel.alloc(), packet);
			Frame frame = new Frame(packet.getId(), buf);
	
			channel.writeAndFlush(frame);
		}
	}

	public void addScore(int delta) {
		score += delta;

		for (Entry<Integer, Player> entry : server.getPlayers().entrySet()) {
			Player client = entry.getValue();
			client.writePacket(new UpdateScorePacket(client.getId(), score));
		}
	}
}
