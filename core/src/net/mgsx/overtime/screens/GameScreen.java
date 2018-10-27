package net.mgsx.overtime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.overtime.OverTime;
import net.mgsx.overtime.ui.WorldGroup;
import net.mgsx.overtime.utils.ActorIntersector;
import net.mgsx.overtime.utils.ArrayUtils;
import net.mgsx.overtime.utils.UniControl;

public class GameScreen extends ScreenAdapter
{
	enum BonusMode{
		INC, DEC, LEFT, RIGHT, RANDOM, FREEZE, SWAP
	}
	private Stage stage;
	
	private Color bgColor = new Color();
	
	private Image hero;
	
	private WorldGroup world;
	
	private Vector2 heroPosition = new Vector2();
	
	private Rectangle heroRectangle = new Rectangle();
	
	private Array<Actor> intersectedActors = new Array<Actor>();
	
	private Array<Image> clockBonus = new Array<Image>();
	private Array<Image> clockBonusHidden = new Array<Image>();
	
	private float timeout;

	private int clockRank;

	private Skin skin;

	private int timeInc = 1;
	
	private boolean frozen;
	
	private int nextBack;
	
	public GameScreen() {
		skin = new Skin(Gdx.files.internal("skin.json"));
		
		stage = new Stage(new FitViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		hero = new Image(skin, "dot");
		hero.setColor(Color.RED);
		
		world = new WorldGroup(skin);
		stage.addActor(world);
		
		addBonusLine(16);
		addBonusLine(30);
		addBonusLine(44);
		addBonusLine(58);
		
		spwanBonus(12);
		
		stage.addActor(hero);
		
		heroPosition.set(130, 2);
		
		world.clock.setCurrentTime();
		// stage.setDebugAll(true);
	}
	
	private void spwanBonus(int num) {
		if(num < 0) num = clockBonusHidden.size + num;
		ArrayUtils.randomize(clockBonusHidden);
		while(num > 0){
			Image bonus = clockBonusHidden.pop();
			spawnBonus(bonus);
			num--;
		}
	}

	private void addBonusLine(int y) {
		addBonus(6, y);
		addBonus(22, y);
		addBonus(48, y);
		addBonus(83, y);
		addBonus(109, y);
		addBonus(125, y);
	}

	private void addBonus(int x, int y) {
		Image img = new Image(skin, "clocks-random");
		stage.addActor(img);
		img.setVisible(false);
		img.setTouchable(Touchable.disabled);
		img.setPosition(x, y);
		clockBonusHidden.add(img);
		clockBonus.add(img);
	}

	@Override
	public void render(float delta) {
		
		// XXX DEBUG
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			genBack();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			incClock(1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
			incClock(-1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
			shiftClock(-1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
			shiftClock(1);
		}
		// XXX DEBUG
		
		// TODO FREEZE bonus ?
		timeout += delta;
		if(timeout >= 1){
			timeout = 0;
			frozen = false;
			
			nextBack--;
			if(nextBack <= 0){
				genBack();
			}
			
			incClock(timeInc);
			// world.clock.setTime(world.clock.clockTime + timeInc); 
		}
		
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
		
		// clamp hero
		heroRectangle.set((int)heroPosition.x + 1, (int)heroPosition.y + 1, (int)hero.getWidth() - 2, (int)hero.getHeight() - 2);
		
		if(ActorIntersector.intersect(world, heroRectangle)){
			heroPosition.set(hero.getX(), hero.getY());
			hero.setColor(Color.RED);
		}else{
			hero.setPosition((int)heroPosition.x, (int)heroPosition.y);
			hero.setColor(Color.BLUE);
		}
		
		
		// check some soft collisions
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
		
		// check clock bonus
		for(Image img : clockBonus){
			if(img.getTouchable() == Touchable.enabled){
				if(ActorIntersector.intersect(img, hero)){
					BonusMode mode = (BonusMode) img.getUserObject();
					switch(mode){
					case DEC:
						incClock(-1);
						break;
					case INC:
						incClock(1);
						break;
					case LEFT:
						shiftClock(1);
						break;
					case RIGHT:
						shiftClock(-1);
						break;
					case FREEZE:
						timeout = -10;
						frozen = true;
						break;
					case RANDOM:
						world.clock.randomize();
						break;
					case SWAP:
						timeInc = -timeInc;
						break;
					}
					
					genBack();
					
					ArrayUtils.randomize(clockBonusHidden);
					spawnBonus(clockBonusHidden.pop());
					hideBonus(img);
				}
			}
		}
		
		
		if(frozen){
			// TODO maybe orange / yellow depending on timeInc
			if(timeInc > 0)
			{
				bgColor.set(0, .1f, .1f, 1);
				world.clock.onColor.set(0,1f, .5f, 1f);
				world.clock.offColor.set(0,.2f, .1f, 1f);
			}else{
				bgColor.set(.1f, 0, .1f, 1);
				world.clock.onColor.set(1f,0, .5f , 1f);
				world.clock.offColor.set(.2f,0, .1f, 1f);
			}
		}
		else if(timeInc > 0){
			bgColor.set(0, .1f, 0, 1);
			world.clock.onColor.set(0, 1f,0, 1f);
			world.clock.offColor.set(0, .2f,0, 1f);
		}else{
			bgColor.set(.1f, 0, 0, 1);
			world.clock.onColor.set(1f,0, 0 , 1f);
			world.clock.offColor.set(.2f,0, 0, 1f);
		}
		
		world.clock.updateTime();
		
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
	
	private void genBack() {
		world.randomizeBackSegs(-12, true);
		nextBack = 10;
	}

	private void shiftClock(int shift){
		clockRank = ((clockRank + shift) % 4 + 4) % 4;
	}
	
	private void incClock(int inc){
		int clockMul = new int[]{1, 10, 60, 600}[clockRank];
		world.clock.setTime(world.clock.clockTime + clockMul * inc); 
	}
	private void hideBonus(Image clockBonus) 
	{
		clockBonusHidden.add(clockBonus);
		clockBonus.setTouchable(Touchable.disabled);
		clockBonus.addAction(Actions.sequence(
			Actions.alpha(0f, .5f)
			));
	}
	private void spawnBonus(Image clockBonus) {
		
		BonusMode mode = ArrayUtils.random(BonusMode.values());
		clockBonus.setUserObject(mode);
		
		String name;
		switch(mode){
		case DEC:
			name = "inc-up";
			break;
		case INC:
			name = "inc-down";
			break;
		case LEFT:
			name = "shift-left";
			break;
		case RIGHT:
			name = "shift-right";
			break;
		case FREEZE:
			name = "freeze";
			break;
		case RANDOM:
			name = "random";
			break;
		default:
		case SWAP:
			name = "swap";
			break;
		}
		
		clockBonus.setVisible(true);
		clockBonus.setDrawable(skin, "clocks-" + name);
		clockBonus.addAction(Actions.sequence(
				Actions.touchable(Touchable.enabled),
				Actions.alpha(1, .5f)
				));
	
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
}
