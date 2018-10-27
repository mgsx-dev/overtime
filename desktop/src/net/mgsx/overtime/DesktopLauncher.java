package net.mgsx.overtime;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = OverTime.WORLD_WIDTH * 8;
		config.height = OverTime.WORLD_HEIGHT * 8;
		new LwjglApplication(new OverTime(), config);
	}
}
