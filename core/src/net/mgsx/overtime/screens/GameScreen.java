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
import net.mgsx.overtime.logic.ClockControl;
import net.mgsx.overtime.logic.GameState;
import net.mgsx.overtime.ui.WorldGroup;
import net.mgsx.overtime.utils.ActorIntersector;
import net.mgsx.overtime.utils.ArrayUtils;
import net.mgsx.overtime.utils.UniControl;

public class GameScreen extends ScreenAdapter
{
	enum BonusMode{
		INC, DEC, LEFT, RIGHT, RANDOM, FREEZE, SWAP
	}
	private static final BonusMode[] allBonusMode = {
			BonusMode.INC, 
			BonusMode.DEC, 
			BonusMode.LEFT, 
			BonusMode.RIGHT, 
			// BonusMode.RANDOM, 
			BonusMode.FREEZE, 
			BonusMode.SWAP
			};
	
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
	
	private ClockControl clockControl;
	
	public GameScreen() {
		skin = new Skin(Gdx.files.internal("skin.json"));
		
		stage = new Stage(new FitViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT));
		
		hero = new Image(skin, "dot");
		hero.setColor(Color.RED);
		
		world = new WorldGroup(skin);
		stage.addActor(world);
		
		// XXX addBonusLine(16);
		addBonusLine(30);
		addBonusLine(44);
		// XXX addBonusLine(58);
		
		spwanBonus(clockBonus.size/2);
		
		stage.addActor(hero);
		
		heroPosition.set(130, 2);
		
		world.enableBack(clockRank, false);
		
		clockControl = new ClockControl(world.clock);
		clockControl.setCurrentTime();
		
		// XXX
		// clockControl.setTime(23, 59);
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
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			applyInc();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
			applyDec();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
			applyShiftRight();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
			applyShiftLeft();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			applyFreeze();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			applyRandom();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.I)){
			applySwap();
		}
		// XXX DEBUG
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			OverTime.i().setScreen(new MenuScreen());
		}
		
		// LOGIC
		GameState state = clockControl.update(delta);
		if(state != GameState.TIME_RUN){
			// TODO game over
		}
		
		// TODO FREEZE bonus ?
		timeout += delta;
		if(timeout >= 1){
			timeout = 0;
			frozen = false;
			
			nextBack--;
			if(nextBack <= 0){
				genBack();
			}
			
			incClock(timeInc, false);
		}
		
		float speed = 30f;
		boolean moving = false;
		if(UniControl.isPressed(UniControl.RIGHT)){
			heroPosition.x += delta * speed;
			moving = true;
		}
		if(UniControl.isPressed(UniControl.LEFT)){
			heroPosition.x -= delta * speed;
			moving = true;
		}
		if(UniControl.isPressed(UniControl.UP)){
			heroPosition.y += delta * speed;
			moving = true;
		}
		if(UniControl.isPressed(UniControl.DOWN)){
			heroPosition.y -= delta * speed;
			moving = true;
		}
		
		// clamp hero
		heroRectangle.set((int)heroPosition.x + 1, (int)heroPosition.y + 1, (int)hero.getWidth() - 2, (int)hero.getHeight() - 2);
		
		if(ActorIntersector.intersect(world, heroRectangle)){
			if(!moving){
				// System.out.println("stuck");
				// TODO game over !
			}
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
						applyDec();
						break;
					case INC:
						applyInc();
						break;
					case LEFT:
						applyShiftLeft();
						break;
					case RIGHT:
						applyShiftRight();
						break;
					case FREEZE:
						applyFreeze();
						break;
					case RANDOM:
						applyRandom();
						break;
					case SWAP:
						applySwap();
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
				world.clock.setColor(0, 1f, .5f, 1f);
			}else{
				bgColor.set(.1f, 0, .1f, 1);
				world.clock.setColor(1f,0, .5f , 1f);
			}
		}
		else if(timeInc > 0){
			bgColor.set(0, .1f, 0, 1);
			world.clock.setColor(0, 1f,0, 1f);
		}else{
			bgColor.set(.1f, 0, 0, 1);
			world.clock.setColor(1f,0, 0 , 1f);
		}
		
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
	
	private void applySwap() {
		timeInc = -timeInc;
	}

	private void applyRandom() {
		// world.clock.randomize();
		incClock(timeInc, 0);
		incClock(timeInc, 1);
		incClock(timeInc, 2);
		incClock(timeInc, 3);
	}

	private void applyShiftLeft() {
		shiftClock(1);
	}
	private void applyShiftRight() {
		shiftClock(-1);
	}

	private void applyDec() {
		incClock(-1, true);
	}

	private void applyInc() {
		incClock(1, true);
	}

	private void applyFreeze() {
		timeout = -10;
		frozen = true;
	}

	private void genBack() {
		world.randomizeBackSegs(-12, true);
		nextBack = 10;
	}

	private void shiftClock(int shift){
		clockRank = ((clockRank + shift) % 4 + 4) % 4;
		world.enableBack(clockRank, true);
	}
	
	private void incClock(int inc, boolean atLeft){
		incClock(inc, atLeft ? (clockRank+1)%4 : clockRank);
	}
	private void incClock(int inc, int rank){
		int clockMul = new int[]{1, 10, 60, 600}[rank];
		clockControl.increment(clockMul * inc); 
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
		
		BonusMode mode = ArrayUtils.random(allBonusMode);
		clockBonus.setUserObject(mode);
		
		String name;
		switch(mode){
		case DEC:
			name = "inc-down";
			break;
		case INC:
			name = "inc-up";
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
