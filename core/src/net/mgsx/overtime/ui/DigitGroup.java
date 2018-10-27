package net.mgsx.overtime.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DigitGroup extends Group{
	
	private static final int [] DIGIT_0 = {1,1,1,1,1,1,0};
	private static final int [] DIGIT_1 = {0,1,1,0,0,0,0};
	private static final int [] DIGIT_2 = {1,1,0,1,1,0,1};
	private static final int [] DIGIT_3 = {1,1,1,1,0,0,1};
	private static final int [] DIGIT_4 = {0,1,1,0,0,1,1};
	private static final int [] DIGIT_5 = {1,0,1,1,0,1,1};
	private static final int [] DIGIT_6 = {1,0,1,1,1,1,1};
	private static final int [] DIGIT_7 = {1,1,1,0,0,0,0};
	private static final int [] DIGIT_8 = {1,1,1,1,1,1,1};
	private static final int [] DIGIT_9 = {1,1,1,1,0,1,1};

	private static final int [] DIGIT_NONE = {0,0,0,0,0,0,0};
	private static final int [] DIGIT_ALL =  {1,1,1,1,1,1,1};
	
	private static final int [][] matrices = {
		DIGIT_0,DIGIT_1,DIGIT_2,DIGIT_3,DIGIT_4,DIGIT_5,DIGIT_6,DIGIT_7,DIGIT_8,DIGIT_9
	};
	
	private Image [] segs = new Image[7];
	private Skin skin;
	private static final Color onColor = new Color(Color.WHITE);
	private static final Color offColor = new Color(1,1,1,.2f);
	
	public DigitGroup(Skin skin) 
	{
		this.skin = skin;
		
		addSegH('a', 3, 14 * 2);
		addSegH('g', 3, 14);
		addSegH('d', 3, 0);
		
		addSegV('e', 0, 4);
		addSegV('f', 0, 18);
		
		addSegV('c', 16, 4);
		addSegV('b', 16, 18);
		
		setSize(21, 33);
		setTouchable(Touchable.childrenOnly);
	}

	private void addSegH(char name, int x, int y) {
		addSeg(name, "segment-h", x, y);
	}
	private void addSegV(char name, int x, int y) {
		addSeg(name, "segment-v", x, y);
	}

	private void addSeg(char name, String drawableName, int x, int y) {
		Image img = new Image(skin, drawableName);
		img.setPosition(x, y);
		addActor(img);
		int index = name - 'a';
		segs[index] = img;
	}

	public void setValue(int value) 
	{
		int index = (value%10 + 10) % 10;
		int[] matrix = matrices[index];
		for(int i=0 ; i<matrix.length ; i++){
			setOn(i, matrix[i] != 0);
		}
	}

	private void setOn(int index, boolean on) {
		setEnabled(segs[index], on);
	}
	
	private void setEnabled(Actor actor, boolean enabled){
		actor.setColor(enabled ? onColor : offColor);
		actor.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		for(Actor child : getChildren()){
			Color c = getColor();
			child.setColor(c.r, c.g, c.b, c.a * child.getColor().a);
		}
		super.draw(batch, parentAlpha);
	}
}
