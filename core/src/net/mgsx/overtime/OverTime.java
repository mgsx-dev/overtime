package net.mgsx.overtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import net.mgsx.overtime.screens.GameScreen;

public class OverTime extends Game {
	
	public static final int WORLD_WIDTH = 138;
	public static final int WORLD_HEIGHT= 81;

	@Override
	public void create () {
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
}
