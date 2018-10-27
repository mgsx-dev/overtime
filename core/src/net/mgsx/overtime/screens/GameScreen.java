package net.mgsx.overtime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.overtime.OverTime;
import net.mgsx.overtime.ui.WorldGroup;
import net.mgsx.overtime.utils.ActorIntersector;
import net.mgsx.overtime.utils.UniControl;

public class GameScreen extends ScreenAdapter
{
	private Stage stage;
	
	private Image hero;
	
	private WorldGroup world;
	
	private Vector2 heroPosition = new Vector2();
	
	private Rectangle heroRectangle = new Rectangle();
	
	private Array<Actor> intersectedActors = new Array<Actor>();
	
	public GameScreen() {
		Skin skin = new Skin(Gdx.files.internal("skin.json"));
		
		stage = new Stage(new FitViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		hero = new Image(skin, "dot");
		hero.setColor(Color.RED);
		
		world = new WorldGroup(skin);
		stage.addActor(world);
		
		stage.addActor(hero);
		
		// stage.setDebugAll(true);
	}
	
	@Override
	public void render(float delta) {
		
		// XXX DEBUG
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			world.randomizeBackSegs(7, true);
		}
		// XXX DEBUG
		
		
		
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
		
		heroRectangle.set((int)heroPosition.x + 1, (int)heroPosition.y + 1, (int)hero.getWidth() - 2, (int)hero.getHeight() - 2);
		
		if(ActorIntersector.intersect(world, heroRectangle)){
			heroPosition.set(hero.getX(), hero.getY());
			hero.setColor(Color.RED);
		}else{
			hero.setPosition((int)heroPosition.x, (int)heroPosition.y);
			hero.setColor(Color.BLUE);
		}
		
		heroRectangle.set((int)heroPosition.x + 1, (int)heroPosition.y + 1, (int)hero.getWidth() - 2, (int)hero.getHeight() - 2);
		
		intersectedActors.clear();
		if(ActorIntersector.intersect(intersectedActors, world, heroRectangle, false)){
			for(Actor actor : intersectedActors){
				if(actor.getTouchable() == Touchable.disabled || true){
					hero.setColor(Color.ORANGE);
					// actor.setColor(Color.ORANGE);
					// actor.setVisible(false);
				}
			}
		}
		
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
}
