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
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.overtime.OverTime;

public class MenuScreen extends ScreenAdapter
{
	private Stage stage;
	
	private Color bgColor = new Color(0,0,0,1);
	private Skin skin;
	public MenuScreen() {
		skin = new Skin(Gdx.files.internal("skin.json"));
		
		stage = new Stage(new FitViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		stage.addActor(new Image(skin, "title"));
		
		Image bt = new Image(skin, "start");
		stage.addActor(bt);
		
		bt.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				start();
				return true;
			}
		});
		
		bt.addAction(Actions.forever(Actions.sequence(Actions.alpha(.3f, .9f), Actions.alpha(1f, .1f))));
		
		bt.setPosition(71, 13);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void render(float delta) {
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			start();
		}
		
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
	
	private void start() {
		// TODO FX and switch to screen
		OverTime.i().setScreen(new GameScreen());
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
}
