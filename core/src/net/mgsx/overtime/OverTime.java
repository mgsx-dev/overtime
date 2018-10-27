package net.mgsx.overtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.mgsx.overtime.screens.MenuScreen;

public class OverTime extends Game {
	
	public static final boolean DEBUG = true;
	
	public static final int WORLD_WIDTH = 138;
	public static final int WORLD_HEIGHT= 81;

	public static OverTime i(){
		return (OverTime) Gdx.app.getApplicationListener();
	}
	public Skin skin;
	
	@Override
	public void create () 
	{
		skin = new Skin(Gdx.files.internal("skin.json"));
		// setScreen(new GameScreen());
		setScreen(new MenuScreen());
	}
	
	@Override
	public void setScreen(Screen screen)
	{
		Screen previous = this.screen;
		super.setScreen(screen);
		if(previous != null){
			previous.dispose();
		}
	}
	
}
