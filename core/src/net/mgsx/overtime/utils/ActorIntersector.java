package net.mgsx.overtime.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class ActorIntersector {
	private static final Rectangle r = new Rectangle();
	private static final Rectangle r2 = new Rectangle();
	
	public static Rectangle actorToRectangle(Actor actor, Rectangle rect){
		return rect.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
	}
	
	public static boolean intersect(Actor actor, Actor rectActor)
	{
		return intersect(actor, actorToRectangle(rectActor, r));
	}
	
	public static boolean intersect(Actor actor, Rectangle rect){
		return intersect(null, actor, rect, true);
	}
	public static boolean intersect(Array<Actor> result, Actor actor, Rectangle rect, boolean checkTouchable)
	{
		if(checkTouchable && actor.getTouchable() == Touchable.disabled){
			return false;
		}
		
		actorToRectangle(actor, r2);
		
		if(r2.overlaps(rect)){
			if(actor instanceof Group){
				if(checkTouchable && actor.getTouchable() == Touchable.enabled) return true;
				rect.x -= actor.getX();
				rect.y -= actor.getY();
				for(Actor child : ((Group) actor).getChildren()){
					if(intersect(result, child, rect, checkTouchable)){
						return true;
					}
				}
				return false;
			}
			if(result != null) result.add(actor);
			return true;
		}
		
		return false;
	}
}
