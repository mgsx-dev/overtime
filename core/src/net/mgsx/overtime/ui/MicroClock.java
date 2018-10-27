package net.mgsx.overtime.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class MicroClock extends Group
{
	private Array<Drawable> digits = new Array<Drawable>();
	private Array<Image> imgClocks = new Array<Image>();
	
	public MicroClock(Skin skin, int nDigits) {
		TextureRegion region = skin.getRegion("micro-digits");
		
		for(int i=0 ; i<10 ; i++){
			TextureRegion r = new TextureRegion(region, i * 6, 0, 5, 9);
			digits.add(new TextureRegionDrawable(r));
		}
		
		for(int i=0 ; i<nDigits ; i++){
			Image img = new Image(digits.first());
			imgClocks.add(img);
			img.setX(i * 6);
			addActor(img);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	private void setValue(Image img, int digit) {
		img.setDrawable(digits.get(digit));
	}

	public void setTime(int time) 
	{
		int value = time;
		for(int i=0 ; i<imgClocks.size ; i++){
			Image img = imgClocks.get(imgClocks.size-i-1);
			setValue(img, value%10);
			value/=10;
		}
	}
}
