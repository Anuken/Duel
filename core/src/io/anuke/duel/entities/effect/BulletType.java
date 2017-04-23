package io.anuke.duel.entities.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.Player;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Mathf;

public enum BulletType{
	test(1f, 5),
	swarm(6f, 5){
		{lifetime = 300;}
		void draw(){
			super.draw();
			
			Entity target = b.target();
			vector.set(target.x - b.x, target.y - b.y).setLength(0.3f);
			b.velocity.add(vector);
			b.velocity.limit(speed);
			
			if(b.life > 300){
				b.remove();
			}
		}
		
		void destroy(){
			Effects.effect(EffectType.particle, b);
		}
	}, 
	blast(2f, 60){
		{
			hitsize=20;
			lifetime = 300;
		}
		
		void draw(){
			b.velocity.scl(1.05f);
			
			Draw.thickness(3f);
			Draw.color(color());
			Draw.circle(b.x, b.y, 10);
			Draw.thickness(1f);
			
			Draw.color();
		}
		
		void destroy(){
			Effects.effect(EffectType.spikes, b);
			Effects.shake(5f, 5f);
		}
	},
	particle(7f, 12){
		void destroy(){
			Effects.effect(EffectType.particle, b);
		}
	},
	split1(5f, 3){
		{lifetime = 50;}
		void destroy(){
			int shots = 3;
			vector.set(0, 1);
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				vector.setAngle(angle+90);
				//new Bullet(b.owner, BulletType.split2, angle).set(b.x, b.y).add();
				new Laser(b.owner, x, y, x + vector.x, y+ vector.y).add();
				Effects.effect(EffectType.rinwave, b);
			}
		}
		
		void draw(){
			Draw.color(color());
			Draw.thickness(3);
			Draw.circle(b.x, b.y, 10);
			Draw.spike(x, y, 10, 20, 3, 90);
			Draw.thickness(1f);
			Draw.color();
		}
	},
	ball(5f, 30){
		{lifetime = 100;}
		void destroy(){
			int shots = 3;
			
			for(int i = 0; i < shots; i ++){
				//float angle = 360f/shots*i;
				//new Bullet(b.owner, BulletType.split3, angle).set(b.x, b.y).add();
				
			}
			Effects.effect(EffectType.rspark, b);
		}
		
		void draw(){
			Draw.color(color());
			Draw.thickness(5);
			Draw.circle(b.x, b.y, 15);
			Draw.thickness(1f);
			Draw.color();
		}
	},
	laserball(3f, 3){
		{lifetime = 60;}
		
		void destroy(){
			Effects.effect(EffectType.rinwave, b);
			new Laser(b.owner, x, y, Duel.other(b.owner).x, Duel.other(b.owner).y).add();
		}
		
		void draw(){
			Draw.color(color());
			Draw.thickness(5);
			Draw.circle(b.x, b.y, 15);
			Draw.thickness(1f);
			Draw.color();
		}
	},
	lock(3f, 10){
		{lifetime=300;}
		void draw(){
			super.draw();
			
			if(b.life > 30 && b.life < 34){
				Effects.effect(EffectType.swave, b);
				b.velocity.setLength(11f);
				b.velocity.setAngle(Duel.angleTo(b, Duel.other(b.owner)));
			}
		}
		
		void destroy(){
			Effects.effect(EffectType.smspikes, b);
			Effects.shake(5f, 5f);
		}
	},
	shadow(4f, 6){
		{lifetime=300;}
		void draw(){
			super.draw();
			
			if(b.life > 30 && b.life < 34){
				if(Math.random() < 0.2)
					new Bullet(b.owner, BulletType.shadow, Mathf.random(360f)).set(b.x, b.y).add();
			}
			
			Entity target = b.target();
			vector.set(target.x - b.x, target.y - b.y).setLength(0.1f);
			b.velocity.add(vector);
			b.velocity.limit(speed);
			
		}
		
		void destroy(){
			Effects.effect(EffectType.particle, b);
		}
	};
	Bullet b;
	float x, y;
	
	int damage;
	float speed;
	float lifetime = 150;
	int hitsize = 8;
	
	static Vector2 vector = new Vector2();
	
	private BulletType(float speed, int damage){
		this.damage = damage;
		this.speed = speed;
	}
	
	Color color(){
		return b.owner instanceof Player ? Duel.pcolor : Duel.ecolor;
	}
	
	void set(Bullet b){
		this.b = b;
		this.x = b.x;
		this.y = b.y;
	}
	
	void draw(){
		Draw.color(color());
		Draw.rect(name()+"bullet", b.x, b.y, b.velocity.angle());
		Draw.color();
	}
	
	void update(){}
	
	void destroy(){}
}
