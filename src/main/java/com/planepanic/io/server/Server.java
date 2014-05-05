package com.planepanic.io.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.planepanic.io.FrameDecoder;
import com.planepanic.io.FrameEncoder;
import com.planepanic.io.FrameHandler;
import com.planepanic.io.packet.Packet;
import com.planepanic.model.Airspace;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.controllers.aircraft.AircraftController;
import com.planepanic.model.controllers.aircraft.ServerAircraftController;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Server for listening for incoming connections and sending / receiving packets.
 * @author Jonathan
 *
 */
public class Server implements Runnable {
	public final static int PORT = 11111;

	private final static int MAX_PLAYERS = 2;

	private final int port;
	private final Map<Integer, Player> players = new HashMap<>();

	private AircraftController controller = null;

	public Server(int port) {
		this.port = port;
	}

	public boolean playerConnected(String name) {
		return players.containsValue(name);
	}

	public int getUnusedId() throws IOException {
		for (int id = 0; id < MAX_PLAYERS; id++) {
			if (players.containsKey(id)) continue;
			return id;
		}

		throw new IOException("Server is full.");
	}

	public void addPlayer(Player player) {
		players.put(player.getId(), player);
	}

	public Map<Integer, Player> getPlayers() {
		return players;
	}

	@Override
	public void run() {
		final Server server = this;
		
		Airspace airspace = new Airspace();
		setController(new ServerAircraftController(GameDifficulty.EASY, airspace, this));

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									ch.pipeline().addLast(new FrameDecoder(), new FrameEncoder(), new FrameHandler(new Player(server, ch)));
								}
							})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync();

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void broadcast(Packet p) throws IOException {
		for (Entry<Integer, Player> entry : players.entrySet()) {
			entry.getValue().writePacket(p);
		}
	}

	public AircraftController getController() {
		return controller;
	}

	public void setController(AircraftController controller) {
		this.controller = controller;
	}
}
