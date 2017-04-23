package io.anuke.duel.entities.effect;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.Collidable;
import io.anuke.ucore.entities.Entity;

public class Bullet extends Projectile implements Collidable{
	BulletType type;
	public Vector2 velocity;
	float life;
	
	public Bullet(Entity owner, BulletType type, float rotation){
		super(owner);
		velocity = new Vector2().set(type.speed, 0).rotate(rotation);
		this.type = type;
	}
	
	public void draw(){
		type.draw(this);
	}
	
	public void update(){
		x += velocity.x*delta();
		y += velocity.y*delta();
		life += delta();
		
		if(life > type.lifetime)
			remove();
	}
	
	public Entity target(){
		return Duel.other(owner);
	}
	
	@Override
	public void removed(){
		type.destroy(this);
	}

	@Override
	public boolean collides(Entity other){
		return other != owner && !(other instanceof Projectile);
	}

	@Override
	public void collision(Entity other){
		remove();
	}

	@Override
	public float hitboxSize(){
		return type.hitsize;
	}

	@Override
	public int damage(){
		return type.damage;
	}
}
