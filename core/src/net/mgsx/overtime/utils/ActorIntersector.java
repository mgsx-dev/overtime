package net.mgsx.overtime.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

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
	
	public static boolean intersect(Actor actor, Rectangle rect)
	{
		if(actor.getTouchable() == Touchable.disabled){
			return false;
		}
		
		actorToRectangle(actor, r2);
		
		if(r2.overlaps(rect)){
			if(actor instanceof Group){
				if(actor.getTouchable() == Touchable.enabled) return true;
				rect.x -= actor.getX();
				rect.y -= actor.getY();
				for(Actor child : ((Group) actor).getChildren()){
					if(intersect(child, rect)){
						return true;
					}
				}
				return false;
			}
			return true;
		}
		
		return false;
	}
}
