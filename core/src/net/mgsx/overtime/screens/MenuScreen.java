package net.mgsx.overtime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.mgsx.game.core.annotations.Editable;
import net.mgsx.overtime.OverTime;
import net.mgsx.overtime.audio.AudioEngine;
import net.mgsx.overtime.ui.MiniClock;
import net.mgsx.overtime.utils.PixelPerfectViewport;

@Editable
public class MenuScreen extends ScreenAdapter
{
	private Stage stage;
	
	@Editable
	public Color bgColor = new Color(0,0,0,1);
	private Skin skin;

	private MiniClock miniClock;

	private Image title;
	
	private boolean enabled = false;
	
	public MenuScreen() {
		skin = OverTime.i().skin;
		
		stage = new Stage(new PixelPerfectViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		stage.addActor(new Image(skin, "back-purple"));
		
		stage.addActor(title = new Image(skin, "title"));
		
		title.getColor().a = 0;
		
		Image bt = new Image(skin, "start");
		stage.addActor(bt);
		
		stage.addActor(miniClock = new MiniClock(skin));
		miniClock.setPosition(43, 11);
		
		bt.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				start();
				return true;
			}
		});
		
		bt.addAction(Actions.forever(Actions.sequence(Actions.alpha(.3f, .9f), Actions.alpha(1f, .1f))));
		
		bt.setPosition(70, 11);
		
		AudioEngine.i().gameToMenu();
	}
	
	@Override
	public void show() {
		enabled = true;
		Gdx.input.setInputProcessor(stage);
		
		title.addAction(Actions.sequence(
				Actions.delay(.5f),
				AudioEngine.i().sfxBigGlitchAsAction(), 
				AudioEngine.i().sfxSmallGlitchAsAction(),
				Actions.repeat(4, Actions.sequence(
						Actions.alpha(1f, .05f),
						Actions.alpha(.5f, .05f)
						)),
				Actions.alpha(0f),
				Actions.alpha(1f, .2f),
				Actions.forever(
						Actions.sequence(
								Actions.delay(5f),
								AudioEngine.i().sfxSmallGlitchAsAction(),
								Actions.repeat(3, Actions.sequence(
										Actions.alpha(1f, .05f),
										Actions.alpha(.5f, .05f)
										)),
				Actions.alpha(1f, .2f))
				)));
		AudioEngine.i().menuBegin();
	}
	
	@Override
	public void hide() {
		enabled = false;
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
		
		if(enabled){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
				start();
			}
		}
	}
	
	private void start() {
		OverTime.i().setScreen(new GameScreen());
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		super.dispose();
	}
}
