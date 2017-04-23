package io.anuke.duel.entities.effect;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.entities.Entity;

public abstract class Projectile extends Entity{
	public static Vector2 vector = new Vector2();
	public Entity owner;
	
	public Projectile(Entity owner){
		this.x = owner.x;
		this.y = owner.y;
		this.owner = owner;
	}
	
	public abstract int damage();
}
