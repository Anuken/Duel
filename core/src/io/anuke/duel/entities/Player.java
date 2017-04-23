package io.anuke.duel.entities;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Bullet;
import io.anuke.duel.entities.effect.BulletType;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Angles;

public class Player extends Entity implements Collidable, Damageable{
	private Vector2 vector = new Vector2();
	float bounds = 100;
	float speed = 7;
	float dashspeed = 20;
	int health = 100;
	
	public void update(){
		
		if(UInput.keyUp("weapon1")){
			attack(Attacks.tricannon);
		}
		
		if(UInput.keyUp("weapon2")){
			attack(Attacks.shot);
		}
		
		if(UInput.keyUp("weapon3")){
			attack(Attacks.shadow);
		}
		
		if(UInput.keyUp("weapon4")){
			attack(Attacks.lock);
		}
		
		if(UInput.keyUp("weapon5")){
			attack(Attacks.trilaser);
		}
		
		vector.set(0, 0);
		
		if(UInput.keyDown("up")){
			vector.y += speed;
		}
		
		if(UInput.keyDown("down")){
			vector.y -= speed;
		}
		
		if(UInput.keyDown("left")){
			vector.x -= speed;
		}
		
		if(UInput.keyDown("right")){
			vector.x += speed;
		}
		
		vector.limit(speed);
		
		if(UInput.keyUp("dash"))
			vector.setLength(dashspeed);
		
		x += vector.x*delta();
		y += vector.y*delta();
		
		//x = Mathf.clamp(x, -bounds, bounds);
		//y = Mathf.clamp(y, -bounds, bounds);
		
		if(UInput.buttonUp(Buttons.LEFT)){
			new Bullet(this, BulletType.test, Angles.mouseAngle(x, y)).add();
		}
		
	}
	
	void attack(Attacks attack){
		attack.use(this);
	}
	
	Enemy enemy(){
		return Duel.enemy();
	}
	
	float enemyAngle(){
		return vector.set(Duel.enemy().x - x, Duel.enemy().y - y).angle();
	}
	
	void shoot(BulletType type, float angle){
		new Bullet(this, type, angle).add();
	}
	
	public void draw(){
		Draw.rect("player", x, y);
	}

	@Override
	public boolean collides(Entity other){
		return other instanceof Projectile;
	}

	@Override
	public void collision(Entity other){
	}

	@Override
	public float hitboxSize(){
		return 8;
	}

	@Override
	public void damage(int amount){
		health -= amount;
		
	}
}
