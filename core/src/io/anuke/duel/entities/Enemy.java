package io.anuke.duel.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.duel.modules.Renderer;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;

public class Enemy extends Entity implements Collidable, Damageable{
	Player player;
	Vector2 vector = new Vector2(0.5f, 0.5f);
	int health = 100;
	float speed = 6.5f;
	boolean away = true;
	float angle = 90;
	boolean flip = false;
	
	@Override
	public void update(){
		vector.set(player.x - x, player.y - y);
		
		//if(Math.random() < 0.01)
		//	away = !away;
		
		if(Math.random() < 0.03)
			angle = 0;
		
		if(Math.random() < 0.01)
			angle = 90;
		
		if(vector.len() < 200)
			angle = 90;
		
		if(Math.random() < 0.001) flip = !flip;
		
		vector.rotate(flip ? angle : 360-angle);
		vector.setLength(speed);
		
		x += vector.x*delta();
		y += vector.y*delta();
	}
	
	@Override
	public void added(){
		player = Duel.module(Renderer.class).player;
	}
	
	@Override
	public void draw(){
		Draw.rect("enemy", x, y);
	}

	@Override
	public boolean collides(Entity other){
		return other instanceof Projectile;
	}

	@Override
	public void collision(Entity other){
		health -= ((Projectile)other).damage();
	}

	@Override
	public float hitboxSize(){
		return 10;
	}

	@Override
	public void damage(int amount){
		health -= amount;
	}
}
