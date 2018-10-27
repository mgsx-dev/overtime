package net.mgsx.overtime.utils;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ActionsUtils {

	public static Action drawable(Skin skin, String drawableName) {
		return drawable(skin.getDrawable(drawableName));
	}
	
	public static Action drawable(final Drawable drawable) {
		return new RunnableAction(){
			@Override
			public void run() {
				if(actor instanceof Image){
					((Image) actor).setDrawable(drawable);
				}
			}
		};
	}

}
