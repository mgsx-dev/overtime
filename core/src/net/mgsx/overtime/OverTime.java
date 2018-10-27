package net.mgsx.overtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.mgsx.overtime.screens.MenuScreen;

public class OverTime extends Game {
	
	public static final boolean DEBUG = true;
	
	public static final int WORLD_WIDTH = 138;
	public static final int WORLD_HEIGHT= 81;

	public static OverTime i(){
		return (OverTime) Gdx.app.getApplicationListener();
	}
	@Override
	public void create () {
		// setScreen(new GameScreen());
		setScreen(new MenuScreen());
	}
	
}
