package io.anuke.duel.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Laser;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.duel.modules.Renderer;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Timers;

public class Enemy extends Entity implements Collidable, Damageable{
	Player player;
	Vector2 vector = new Vector2(0.5f, 0.5f);
	int health = Duel.health;
	float speed = 6.5f;
	boolean away = true;
	boolean flip = false;
	float angle = 90;
	float standtime = 0;
	
	void combat(){
		if(rand(0.04)){
			attack(Attacks.rcannon);
		}
		
		if(rand(0.002)){
			for(int i = 0; i < 5; i ++){
				Timers.run(i*12, ()->{
					attack(Attacks.trilaser);
				});
			}
			standtime = 60;
		}
	}
	
	void laser(){
		new Laser(this, x, y, player.x, player.y).add();
	}
	
	void attack(Attacks attack){
		attack.use(this);
	}
	
	@Override
	public void update(){
		vector.set(player.x - x, player.y - y);
		
		if(Math.random() < 0.03)
			angle = 0;
		
		if(Math.random() < 0.01)
			angle = 90;
		
		if(vector.len() < 200)
			angle = 90;
		
		if(Math.random() < 0.001) flip = !flip;
		
		vector.rotate(flip ? angle : 360-angle);
		vector.setLength(speed);
		
		if(standtime <= 0){
			x += vector.x*delta();
			y += vector.y*delta();
		}else{
			standtime -= delta();
		}
		
		combat();
	}
	
	boolean rand(double d){
		return Math.random()<d*delta();
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

	@Override
	public int health(){
		return health;
	}
}
