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
	swarm(4f, 5){
		{lifetime = 300;}
		void draw(){
			super.draw();
			
			Entity target = b.target();
			vector.set(target.x - b.x, target.y - b.y).setLength(0.2f);
			b.velocity.add(vector);
			b.velocity.limit(speed);
			
			if(b.life > 300){
				b.remove();
			}
		}
		
		void destroy(){
			Effects.effect(EffectType.spark, b);
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
	particle(5f, 7){
		void destroy(){
			Effects.effect(EffectType.particle, b);
		}
	},
	split1(4f, 4){
		{lifetime = 50;}
		void destroy(){
			int shots = 4;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				new Bullet(b.owner, BulletType.split2, angle).set(b.x, b.y).add();
			}
		}
		
		void draw(){
			Draw.color(color());
			Draw.thickness(3);
			Draw.circle(b.x, b.y, 10);
			Draw.thickness(1f);
			Draw.color();
		}
	},
	split2(3f, 2){
		{lifetime = 60;}
		void destroy(){
			int shots = 3;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				new Bullet(b.owner, BulletType.split3, angle).set(b.x, b.y).add();
			}
		}
		
		void draw(){
			Draw.color(color());
			Draw.circle(b.x, b.y, 8);
			Draw.color();
		}
	},
	split3(3f, 3){
		{lifetime = 60;}
		
		void destroy(){
			Effects.effect(EffectType.orangespark, b);
		}
	},
	lock(3f, 10){
		void draw(){
			super.draw();
			
			if(b.life > 30 && b.life < 34){
				Effects.effect(EffectType.swave, b);
				b.velocity.setLength(7f);
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
