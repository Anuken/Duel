package io.anuke.duel.entities;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.effect.Bullet;
import io.anuke.duel.entities.effect.BulletType;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Timers;

public class Player extends Fighter implements Collidable, Damageable{
	private Vector2 vector = new Vector2();
	float maxdashspeed = 30;
	float dashspeed = 30;
	float drag = 0.4f;
	public AttackInfo[] attacks = new AttackInfo[4];
	public int maxmana = 500;
	public int regen = 3;
	
	public Player(){
		speed = 9f;
		
		for(int i = 0; i < 4; i ++)
			attacks[i] = new AttackInfo(Attacks.tricannon);
		
		attacks[1] = new AttackInfo(Attacks.lock);
		attacks[2] = new AttackInfo(Attacks.mark);
		attacks[3] = new AttackInfo(Attacks.portal);
	}
	
	public void update(){
		
		for(int i = 0; i < 4; i ++){
			//int index = i;
			//Attacks attack = attacks[i].attack;
			if(UInput.keyUp("weapon"+(i+1)) && attacks[i].cooldown <= 0){
				attack(attacks[i].attack);
				attacks[i].cooldown = attacks[i].attack.cooldown;
			}
			
			if(attacks[i].cooldown <= delta() && attacks[i].cooldown > 0){
				Effects.overlay(100, f->{
					Draw.tcolor(1, 1, 1, 1f-f/100f);
					Draw.tscl(0.5f);
					//Draw.text(attack.name(), -80+index*40,-Gdx.graphics.getHeight()/2+50);
					Draw.tcolor();
					Draw.tscl(1f);
				});
			}
			
			attacks[i].cooldown -= delta();
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
		
		if(UInput.keyDown("dash")){
			if(Timers.get(4) && dashspeed > 10)
				Effects.effect(EffectType.portalwave, this);
			vector.setLength(dashspeed);
			dashspeed -= drag;
		}else{
			dashspeed += delta();
		}
		
		dashspeed = Mathf.clamp(dashspeed, speed, maxdashspeed);
		
		x += vector.x*delta();
		y += vector.y*delta();
		
		time += vector.x < 0 ? delta()*4 : vector.x > 0 ? -delta()*4 : 0;
		
		//x = Mathf.clamp(x, -bounds, bounds);
		//y = Mathf.clamp(y, -bounds, bounds);
		
		if(UInput.buttonUp(Buttons.LEFT)){
			new Bullet(this, BulletType.test, Angles.mouseAngle(x, y)).add();
		}
		
	}
	
	public class AttackInfo{
		public int charges;
		public Attacks attack;
		public float cooldown;
		
		public AttackInfo(Attacks attack){
			this.attack = attack;
		}
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
}
