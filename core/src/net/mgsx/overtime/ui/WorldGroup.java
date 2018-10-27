package net.mgsx.overtime.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class WorldGroup extends Group{
	
	private Skin skin;
	public ClockGroup clock;
	public Group backGroup;
	private Array<Image> backItems = new Array<Image>();
	
	public WorldGroup(Skin skin) {
		this.skin = skin;
		
		clock = new ClockGroup(skin);
		backGroup = new Group();
		
		backGroup.setTouchable(Touchable.childrenOnly);
		
		addActor(backGroup);
		addActor(clock);
		clock.setPosition(15, 24);
		
		setSize(138, 81);
		backGroup.setSize(getWidth(), getHeight());
		setTouchable(Touchable.childrenOnly);
		
		addSlicesV(15);
		addSlicesV(31);
		addSlicesV(41);
		addSlicesV(57);
		addSlicesV(76);
		addSlicesV(92);
		addSlicesV(102);
		addSlicesV(118);
		
//		addSlicesH(24);
//		addSlicesH(38);
//		addSlicesH(52);
		
		for(int i=0 ; i<backItems.size ; i++){
			enableBackSeg(backItems.get(i), false, false);
		}
	}

	private void addSlicesV(int x) {
		addSlice(x, 56, "segment-extra-top");
		addSlice(x, 0, "segment-extra-bottom");
	}

	private void addSlicesH(int y) {
		addSlice(0, y, "segment-extra-left");
		// addSlice(60, y, "segment-middle-h");
		addSlice(121, y, "segment-extra-right");
	}
	
	private void addSlice(int x, int y, String drawableName) {
		Image img = new Image(skin, drawableName);
		img.setPosition(x, y);
		backGroup.addActor(img);
		backItems.add(img);
		img.setUserObject(true);
	}
	
	public void randomizeBackSegs(int numEnabled, boolean smooth){
		
		// XXX full rnadom version
//		if(numEnabled < 0) numEnabled = backItems.size + numEnabled;
//		ArrayUtils.randomize(backItems);
//		for(int i=0 ; i<backItems.size ; i++){
//			enableBackSeg(backItems.get(i), i < numEnabled, smooth);
//		}
		
		
		// XXX one on 2 version
//		backSwitch = (backSwitch+1)%2;
//		for(int i=0 ; i<backItems.size ; i++){
//			boolean enable = (i + backSwitch + i/2) % 2 == 0;
//			enableBackSeg(backItems.get(i), enable, smooth);
//		}
	}
	
	private boolean isBackSegEnable(Image actor) {
		return (Boolean)actor.getUserObject();
	}

	public void enableBack(int clockRank, boolean smooth) {
		for(int i=0 ; i<backItems.size ; i++){
			boolean enable = i/4  == 3 - clockRank;
			enableBackSeg(backItems.get(i), enable, smooth);
		}
	}

	private void enableBackSeg(Image actor, boolean enabled, boolean smooth) 
	{
		float alphaOn = .5f;
		float alphaOff = .05f;
		if(smooth){
			if(enabled != isBackSegEnable(actor))
			{
				actor.setUserObject(enabled);
				actor.clearActions();
				if(enabled){
					// actor.setColor(onColor);
					actor.addAction(Actions.sequence(
							Actions.repeat(4, Actions.sequence(
									Actions.alpha(1f, .05f),
									Actions.alpha(alphaOn, .05f)
									)),
							Actions.touchable(Touchable.enabled)
							));
				}else{
					actor.addAction(Actions.sequence(
							Actions.touchable(Touchable.disabled),
							Actions.alpha(alphaOff, .2f, Interpolation.linear)
							));
				}
			}
		}else{
			actor.setUserObject(enabled);
			// actor.setColor(enabled ? onColor : offColor);
			actor.getColor().a = enabled ? alphaOn : alphaOff;
			actor.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
		}
	}

	
}
