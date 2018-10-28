package net.mgsx.overtime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.mgsx.overtime.gfx.GFXDissolve;
import net.mgsx.overtime.screens.MenuScreen;

public class OverTime extends Game {
	
	public static final boolean DEBUG = false;
	
	public static final int WORLD_WIDTH = 138;
	public static final int WORLD_HEIGHT= 81;

	public GFXDissolve gfx;
	
	public static OverTime i(){
		return (OverTime) Gdx.app.getApplicationListener();
	}
	public Skin skin;
	
	private Screen screenA, screenB;
	
	private float gfxTime;
	
	@Override
	public void create () 
	{
		skin = new Skin(Gdx.files.internal("skin.json"));
		
		gfx = new GFXDissolve();
		
		// setScreen(new GameScreen());
		setScreen(new MenuScreen());
	}
	
	@Override
	public void setScreen(Screen screen)
	{
		// first screen
		if(this.screen == null){
			screenB = screen;
			gfxTime = 1;
			this.screen = screen;
			this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}else{
			gfxTime = 0;
			this.screen.hide();
			screenA = this.screen;
			screenB = screen;
			screenB.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}
	
	@Override
	public void render() {
		super.render();
		float delta = Math.min(1f / 15f, Gdx.graphics.getDeltaTime());
		float speed = 1f;
		
		// diseapear sequence
		if(screenA != null){
			gfxTime += delta;
			if(gfxTime > 1){
				gfxTime = 1;
				this.screen.dispose();
				screenA = null;
				this.screen = screenB;
			}
			gfx.setTime(gfxTime);
			gfx.render(delta);
		}
		// appear sequence
		else if(screenB != null){
			gfxTime -= delta * speed;
			if(gfxTime < 0){
				gfxTime = 0;
				this.screen.show();
				screenB = null;
			}
			gfx.setTime(gfxTime);
			gfx.render(delta);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		gfx.resize(width, height);
		super.resize(width, height);
	}
	
}
