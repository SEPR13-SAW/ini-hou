package seprini;

import seprini.data.Config;
import seprini.network.server.Server;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("server")) {
			new Server(7777).run();
		} else {
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = Config.GAME_TITLE;
			cfg.useGL20 = false;
			cfg.width = Config.SCREEN_WIDTH;
			cfg.height = Config.SCREEN_HEIGHT;
	
			cfg.resizable = Config.RESIZABLE;
	
			cfg.vSyncEnabled = Config.VSYNC;
	
			cfg.addIcon("data/ico32x32.png", FileType.Internal);
	 
			new LwjglApplication(new ATC(), cfg);
		}
	}
}
