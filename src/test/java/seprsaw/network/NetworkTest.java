package seprsaw.network;

import java.net.InetSocketAddress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import seprini.network.client.Client;
import seprini.network.packet.JoinGamePacket;
import seprini.network.server.Server;

/**
 * Test class for networking
 */
@RunWith(JUnit4.class)
public class NetworkTest {
	
	Client client;
	
	@Test
	public void commsTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new Server(11111).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		client = new Client(new InetSocketAddress("127.0.0.1", 11111), new Runnable() {
			@Override
			public void run() {
				try {
					client.writePacket(new JoinGamePacket("Test"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
}
