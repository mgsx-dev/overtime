package net.mgsx.overtime.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class ActorIntersector {
	private static final Rectangle r = new Rectangle();
	private static final Rectangle r2 = new Rectangle();
	
	public static Vector2 clamp(Vector2 v, int minX, int minY, int maxX, int maxY)
	{
		v.x = MathUtils.clamp(v.x, minX, maxX);
		v.y = MathUtils.clamp(v.y, minY, maxY);
		return v;
	}
	public static Vector2 clamp(Vector2 v, float minX, float minY, float maxX, float maxY)
	{
		v.x = MathUtils.clamp(v.x, minX, maxX);
		v.y = MathUtils.clamp(v.y, minY, maxY);
		return v;
	}
	
	public static void clampToStage(Vector2 v, Actor actor){
		Stage stage = actor.getStage();
		clamp(v, 0, 0, stage.getWidth() - actor.getWidth(), stage.getHeight() - actor.getHeight());
	}
	
	
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
