package io.anuke.duel.entities;

import com.badlogic.gdx.math.Rectangle;

import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public interface Collidable extends QuadTreeObject{
	boolean collides(Entity other);
	void collision(Entity other);
	float hitboxSize();
	
	@Override
	default void getBoundingBox(Rectangle out){
		//HAX
		Entity entity = (Entity)(Object)this;
		
		out.set(entity.x - hitboxSize()/2, entity.y - hitboxSize()/2, hitboxSize(), hitboxSize());
	}
}
