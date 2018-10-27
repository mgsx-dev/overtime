package net.mgsx.overtime;

import com.badlogic.gdx.Game;

import net.mgsx.overtime.screens.GameScreen;

public class OverTime extends Game {
	
	public static final int WORLD_WIDTH = 138;
	public static final int WORLD_HEIGHT= 81;

	@Override
	public void create () {
		setScreen(new GameScreen());
	}
	
}
