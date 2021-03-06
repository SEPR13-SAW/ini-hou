package com.planepanic.io.client;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.planepanic.io.Frame;
import com.planepanic.io.FrameDecoder;
import com.planepanic.io.FrameEncoder;
import com.planepanic.io.FrameHandler;
import com.planepanic.io.packet.Packet;
import com.planepanic.io.packet.encoder.Encoder;
import com.planepanic.model.Airspace;

import lombok.Getter;
import lombok.Setter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Client for sending and receiving packets.
 * @author Jonathan
 *
 */
public class Client implements Runnable {
	private Channel channel;
	private final SocketAddress host;
	private final Runnable onConnect;
	@Getter private final Map<Integer, Player> players = new HashMap<>();
	@Getter private final String name;
	@Getter private Player player;
	@Getter @Setter private Airspace airspace;
	
	public Client(SocketAddress host, Runnable onConnect, String name) {
		this.host = host;
		this.onConnect = onConnect;
		this.name = name;

		new Thread(this).start();
	}

	public void addPlayer(Player player) {
		players.put(player.getId(), player);
	}

	public void removePlayer(Player player) {
		players.remove(player.getId());
	}

	public Client(SocketAddress host, String name) {
		this(host, null, name);
	}

	@Override
	public void run() {
		final Client client = this;
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.channel(NioSocketChannel.class);
			b.group(workerGroup);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new FrameDecoder(), new FrameEncoder(), new FrameHandler(client));
				}
			});

			try {
				// Start the client.
				channel = b.connect(host).sync().channel();
				
				if (onConnect != null) {
					onConnect.run();
				}
				
				// Wait until the connection is closed.
				channel.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
	
	public void writePacket(Packet packet) throws InterruptedException {
		synchronized (this) {
			Encoder<?> encoder = Encoder.get(packet.getId());
			ByteBuf buf = encoder.encodeGeneric(channel.alloc(), packet);
			Frame frame = new Frame(packet.getId(), buf);
	
			channel.writeAndFlush(frame);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		player.setClient(this);
	}
}
