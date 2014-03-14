package seprini.network.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import seprini.network.FrameDecoder;
import seprini.network.FrameEncoder;
import seprini.network.FrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server implements Runnable {
	private final static int MAX_CLIENTS = 4;

	private final int port;
	private final Map<Integer, Client> clients = new HashMap<>();

	public Server(int port) {
		this.port = port;
	}

	public boolean playerConnected(String name) {
		return clients.containsValue(name);
	}

	public int getUnusedId() throws IOException {
		for (int id = 0; id < MAX_CLIENTS; id++) {
			if (clients.containsKey(id)) continue;
			return id;
		}

		throw new IOException("Server is full.");
	}

	public void addClient(Client client) {
		clients.put(client.getId(), client);
	}

	public Map<Integer, Client> getClients() {
		return clients;
	}

	@Override
	public void run() {
		final Server server = this;

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									ch.pipeline().addLast(new FrameDecoder(), new FrameEncoder(), new FrameHandler(new Client(server, ch)));
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
}
