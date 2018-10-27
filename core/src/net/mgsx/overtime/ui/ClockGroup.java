package net.mgsx.overtime.ui;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClockGroup extends Group
{
	public enum Mode{
		FREE, REALTIME, FRAMES
	}
	
	private Mode mode = Mode.FRAMES;
	
	private DigitGroup [] digits = new DigitGroup[4];
	private Image [] dots = new Image[2];
	private Skin skin;
	
	public ClockGroup(Skin skin) {
		this.skin = skin;
		
		addDigit(0, 0, 0);
		addDigit(1, 26, 0);
		addDigit(2, 61, 0);
		addDigit(3, 87, 0);
		
		addDot(0, 52, 9);
		addDot(1, 52, 20);
	}

	private void addDigit(int index, int x, int y) {
		DigitGroup dg = new DigitGroup(skin);
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
	
	public void blink(boolean blink){
		for(Image dot : dots){
			dot.addAction(
				Actions.forever(
					Actions.sequence(
						Actions.alpha(0), 
						Actions.delay(1f), 
						Actions.alpha(1), 
						Actions.delay(1f))));
		}
	}
	
	@Override
	public void act(float delta) 
	{
		if(mode == Mode.REALTIME){
			
			Calendar c = Calendar.getInstance();
			int h = c.get(Calendar.HOUR_OF_DAY);
			int m = c.get(Calendar.MINUTE);
			int ms = c.get(Calendar.MILLISECOND);
			
			digits[0].setValue(h/10);
			digits[1].setValue(h%10);
			digits[2].setValue(m/10);
			digits[3].setValue(m%10);
			
			for(Image dot : dots){
				dot.setColor(ms < 500 ? Color.WHITE : Color.DARK_GRAY);
			}
		}
		else if(mode == Mode.FRAMES){
			long frame = Gdx.graphics.getFrameId() / 10;
			boolean sec = frame % 2 == 0;
			digits[3].setValue((int)(frame % 10));
			frame /= 10;
			digits[2].setValue((int)(frame % 10));
			frame /= 10;
			
			frame = frame % 24;
			digits[1].setValue((int)(frame % 10));
			frame /= 10;
			digits[0].setValue((int)(frame % 10));
			
			for(Image dot : dots){
				dot.setColor(sec ? Color.WHITE : Color.DARK_GRAY);
			}
		}
		super.act(delta);
	}
}
