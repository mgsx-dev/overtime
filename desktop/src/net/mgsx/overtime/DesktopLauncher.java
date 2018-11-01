package net.mgsx.overtime;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.kit.app.GameWrapper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = OverTime.WORLD_WIDTH * 8;
		config.height = OverTime.WORLD_HEIGHT * 8;
		new LwjglApplication(new GameWrapper(new OverTime()), config);
	}
}
