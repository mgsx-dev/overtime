package net.mgsx.overtime.ui;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClockGroup extends Group
{
	public final Color offColor = new Color( 0, .2f,0, 1f);
	public final Color onColor = Color.GREEN;

	static final int MAX_TIME = 24 * 60;
	
	public enum Mode{
		FREE, REALTIME, FRAMES
	}
	
	private Mode mode = Mode.FREE;
	
	private DigitGroup [] digits = new DigitGroup[4];
	private Image [] dots = new Image[2];
	private Skin skin;
	
	public int clockTime = 0;
	
	public ClockGroup(Skin skin) {
		this.skin = skin;
		
		addDigit(0, 0, 0);
		addDigit(1, 26, 0);
		addDigit(2, 61, 0);
		addDigit(3, 87, 0);
		
		addDot(0, 52, 9);
		addDot(1, 52, 20);
		
		setSize(108, 33);
		setTouchable(Touchable.childrenOnly);
	}

	private void addDigit(int index, int x, int y) {
		DigitGroup dg = new DigitGroup(skin, onColor, offColor);
		dg.setPosition(x, y);
		addActor(dg);
		digits[index] = dg;
	}
	
	private void addDot(int index, int x, int y) {
		Image img = new Image(skin, "dot");
		img.setPosition(x, y);
		addActor(img);
		dots[index] = img;
	}
	
	public void setCurrentTime(){
		Calendar c = Calendar.getInstance();
		int h = c.get(Calendar.HOUR_OF_DAY);
		int m = c.get(Calendar.MINUTE);
		int ms = c.get(Calendar.MILLISECOND);
		
		digits[0].setValue(h/10);
		digits[1].setValue(h%10);
		digits[2].setValue(m/10);
		digits[3].setValue(m%10);
		
		for(Image dot : dots){
			setEnabled(dot, ms < 500);
		}
		
		clockTime = h * 60 + m;
	}
	
	public void setTime(int time) {
		clockTime = (time % MAX_TIME + MAX_TIME) % MAX_TIME;
		updateTime();
	}
	
	public void updateTime() {
		int frame = clockTime;
		
		digits[3].setValue((int)(frame % 10));
		frame /= 10;
		digits[2].setValue((int)(frame % 6));
		frame /= 6;
		
		frame = frame % 24;
		digits[1].setValue((int)(frame % 10));
		frame /= 10;
		digits[0].setValue((int)(frame % 10));
		
		boolean sec = clockTime % 2 == 0;
		for(Image dot : dots){
			setEnabled(dot, sec);
		}
	}

	@Override
	public void act(float delta) 
	{
		if(mode == Mode.REALTIME){
			setCurrentTime();
		}
		else if(mode == Mode.FRAMES){
			long frame = Gdx.graphics.getFrameId() / 6;
			boolean sec = frame % 2 == 0;
			digits[3].setValue((int)(frame % 10));
			frame /= 10;
			digits[2].setValue((int)(frame % 6));
			frame /= 6;
			
			frame = frame % 24;
			digits[1].setValue((int)(frame % 10));
			frame /= 10;
			digits[0].setValue((int)(frame % 10));
			
			for(Image dot : dots){
				setEnabled(dot, sec);
			}
		}
		super.act(delta);
	}
	
	private void setEnabled(Actor actor, boolean enabled){
		actor.setColor(enabled ? onColor : offColor);
		actor.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
	}

	public void randomize() {
		setTime(MathUtils.random(MAX_TIME));
	}

	
}
