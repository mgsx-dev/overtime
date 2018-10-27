package net.mgsx.overtime.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClockGroup extends Group
{
	private static final Color offColor = new Color(1,1,1,.2f);
	private static final Color onColor = new Color(1,1,1,1);

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
		
		setSize(108, 33);
		setTouchable(Touchable.childrenOnly);
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
	
	public void setTime(int h1, int h2, int m1, int m2) 
	{
		digits[0].setValue(h1);
		digits[1].setValue(h2);
		digits[2].setValue(m1);
		digits[3].setValue(m2);
	}
	
	public void setDotsState(boolean on){
		for(Image dot : dots){
			setEnabled(dot, on);
		}
	}
	
	private void setEnabled(Actor actor, boolean enabled){
		actor.setColor(enabled ? onColor : offColor);
		actor.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color c = getColor();
		for(Actor child : getChildren()){
			child.setColor(c.r, c.g, c.b, child.getColor().a);
		}
		super.draw(batch, parentAlpha);
	}

	public void blinkDigit(int rank) {
		digits[digits.length - rank-1].addAction(Actions.repeat(4, Actions.sequence(
				Actions.alpha(0.2f, .05f),
				Actions.alpha(1f, .05f)
				)));
	}
	
}
