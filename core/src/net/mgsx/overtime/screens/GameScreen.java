package net.mgsx.overtime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.overtime.OverTime;
import net.mgsx.overtime.ui.ClockGroup;
import net.mgsx.overtime.utils.UniControl;

public class GameScreen extends ScreenAdapter
{
	private Stage stage;
	
	private Image hero;
	
	private ClockGroup clock;
	
	private Vector2 heroPosition = new Vector2();
	
	public GameScreen() {
		Skin skin = new Skin(Gdx.files.internal("skin.json"));
		
		stage = new Stage(new FitViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		stage.addActor(new Image(skin, "demo"));
		
		hero = new Image(skin, "dot");
		hero.setColor(Color.RED);
		
		clock = new ClockGroup(skin);
		clock.setPosition(15, 24);
		stage.addActor(clock);
		
		stage.addActor(hero);
		
	}
	
	@Override
	public void render(float delta) {
		
		float speed = 30f;
		
		if(UniControl.isPressed(UniControl.RIGHT)){
			heroPosition.x += delta * speed;
		}
		if(UniControl.isPressed(UniControl.LEFT)){
			heroPosition.x -= delta * speed;
		}
		if(UniControl.isPressed(UniControl.UP)){
			heroPosition.y += delta * speed;
		}
		if(UniControl.isPressed(UniControl.DOWN)){
			heroPosition.y -= delta * speed;
		}
		
		hero.setPosition((int)heroPosition.x, (int)heroPosition.y);
		
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
}
