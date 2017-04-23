package io.anuke.duel;

import com.badlogic.gdx.math.Intersector;

import io.anuke.duel.entities.Collidable;
import io.anuke.ucore.entities.Entity;

public class Util{
	public static boolean intersects(Entity entity, float x1, float y1, float x2, float y2){
		float w = ((Collidable)entity).hitboxSize();
		
		float minx = entity.x - w/2, miny = entity.y-w/2;
		float maxx = entity.x + w/2, maxy = entity.y+w/2;
		
		return Intersector.intersectSegments(x1, y1, x2, y2, minx, miny, maxx, miny, null) ||
		Intersector.intersectSegments(x1, y1, x2, y2, minx, miny, minx, maxy, null) ||
		Intersector.intersectSegments(x1, y1, x2, y2, minx, maxy, maxx, maxy, null) ||
		Intersector.intersectSegments(x1, y1, x2, y2, maxx, miny, maxx, maxy, null);
	}
}
