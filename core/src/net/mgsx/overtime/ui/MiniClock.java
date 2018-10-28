package net.mgsx.overtime.ui;

import java.util.Date;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class MiniClock extends Group
{
	private Array<Drawable> digits = new Array<Drawable>();
	private Array<Drawable> separators = new Array<Drawable>();
	private Image imgClock1, imgClock2, imgClock3, imgClock4;
	private Image imgSeparator;
	
	public MiniClock(Skin skin) {
		TextureRegion region = skin.getRegion("mini-digits");
		
		for(int i=0 ; i<10 ; i++){
			TextureRegion r = new TextureRegion(region, i * 6, 0, 5, 9);
			digits.add(new TextureRegionDrawable(r));
		}
		separators.add(new TextureRegionDrawable(new TextureRegion(region, 62, 0, 1, 9)));
		separators.add(new TextureRegionDrawable(new TextureRegion(region, 60, 0, 1, 9)));
		
		imgClock1 = new Image(digits.first());
		imgClock2 = new Image(digits.first());
		imgClock3 = new Image(digits.first());
		imgClock4 = new Image(digits.first());
		imgSeparator = new Image(separators.first());
		
		int x = 0;
		imgClock1.setX(x); x+= 6;
		imgClock2.setX(x); x += 6;
		imgSeparator.setX(x); x+=2;
		imgClock3.setX(x);x += 6;
		imgClock4.setX(x);
		
		addActor(imgClock1);
		addActor(imgClock2);
		addActor(imgClock3);
		addActor(imgClock4);
		addActor(imgSeparator);
	}
	
	@Override
	public void act(float delta) 
	{
		Date date = new Date();
		
		int h = date.getHours();
		int m = date.getMinutes();
		long ms = date.getTime() % 1000;
		
		setValue(imgClock1, h/10);
		setValue(imgClock2, h%10);
		setValue(imgClock3, m/10);
		setValue(imgClock4, m%10);
		
		imgSeparator.setDrawable(ms < 500 ? separators.first() : separators.peek());
		super.act(delta);
	}

	private void setValue(Image img, int digit) {
		img.setDrawable(digits.get(digit));
	}
}
