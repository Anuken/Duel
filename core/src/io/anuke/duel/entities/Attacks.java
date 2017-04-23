package io.anuke.duel.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.effect.*;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.entities.EntityHandler;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Timers;

public enum Attacks{
	swarm(100, "Shoots a swarm of seeking bullets."){
		void impl(){
			for(int i = 0; i < 15; i ++)
				shoot(BulletType.swarm, Mathf.random(360f));
			
			Effects.effect(EffectType.rspark, entity);
		}
	}, 
	cannon(10, "Shoots a powerful fast-moving bullet."){
		void impl(){
			
			Effects.effect(EffectType.inspike, entity);
			
			Timers.run(20, ()->{
				shoot(BulletType.blast, enemyAngle());
			});
		}
	},
	mark(100, "Shoots multiple fast-moving bullets in sequence."){
		void impl(){
			
			Effects.effect(EffectType.inspike, entity);
			
			Timers.run(20, ()->{
				for(int i = 0; i < 50; i ++)
				shoot(BulletType.blast, Mathf.random(360));
			});
		}
	},
	shadow(100, "Shoots a swarm of dividing, seeking bullets."){
		void impl(){
			Effects.effect(EffectType.inwave, entity, other.x, other.y);
			
			Effects.effect(EffectType.inspike, entity);
			
			for(int i = 0; i < 15; i ++){
				Bullet bullet = new Bullet(entity, BulletType.shadow, Mathf.random(360)).set(entity.x, entity.y).add();
				bullet.velocity.setLength(Mathf.random(4, 8));
			}
			
		}
	},
	tricannon(30, "Shoots three powerful fast-moving bullets."){
		void impl(){
			
			Effects.effect(EffectType.inspike, entity);
			
			for(int i = 0; i < 7; i ++)
			Timers.run(15+i*2, ()->{
				shoot(BulletType.blast, enemyAngle()+Mathf.random(-5, 5));
			});
		}
	},
	shot(100, "Shoots a swarm of various seeking bullets."){
		void impl(){
			
			for(int i = 0; i < 7; i ++)
			Timers.run(5+i*2, ()->{
				for(int j = 0; j < 5; j ++)
				shoot(Math.random() < 0.5 ? BulletType.lock : BulletType.swarm, 180+enemyAngle()+Mathf.random(-20, 20));
			});
			
			Effects.effect(EffectType.rspark, entity);
		}
	},
	lock(100, "Shoots a circle of seeking bullets."){
		void impl(){
			int shots = 20;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.lock, angle);
			}
		}
	},
	portal(100, "Opens a portal that targets the enemy."){
		void impl(){
			vector.setToRandomDirection().setLength(30);
			
			for(int i = 0; i < 2; i ++){
				new Portal(entity).set(x+vector.x, y+vector.y).add();
				vector.rotate(180);
			}
		}
	}, 
	megaportal(100, "Opens a large portal that targets the enemy."){
		void impl(){
			new MegaPortal(entity).set(x+vector.x, y+vector.y).add();
		}
	},
	laserportal(100, "Opens a laser portal that targets the enemy."){
		void impl(){
			new LaserPortal(entity).set(x+vector.x, y+vector.y).add();
		}
	}, 
	split(100, "Shoots 3 bullets that each shoot 3 lasers."){
		void impl(){
			int shots = 3;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.split1, angle+90);
			}
		}
	}, 
	balls(100, "acannon", "Shoots slow-moving projectiles in a circle."){
		void impl(){
			int shots = 30;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.ball, angle);
			}
		}
	}, 
	laserballs(100, "Shoots laser-firing projectiles in a circle."){
		void impl(){
			int shots = 5;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.laserball, angle);
			}
		}
	}, 
	reverseshield(100, "Takes control of all nearby bullets and makes them target the enemy."){
		void impl(){
			
			Effects.effect(EffectType.shield, entity);
			
			for(Entity entity : EntityHandler.instance().getEntities()){
				if(entity instanceof Bullet && Vector2.dst(x, y, entity.x, entity.y) < 100){
					((Bullet)entity).velocity.setAngle(Duel.angleTo(entity, other));
					((Bullet)entity).owner = this.entity;
					Effects.effect(EffectType.redwave, entity);
				}
			}
		}
	}, 
	trilaser(100, "Shoots three lasers in sequence."){
		void impl(){
			for(int i = 0; i < 3; i ++){
				vector.setToRandomDirection().setLength(70);
				
				Effects.effect(entity instanceof Player ? EffectType.lspike : EffectType.rlspike, entity, vector.x+x, vector.y+y);
				new Laser(entity,vector.x+x, vector.y+y, other.x, other.y).add();
			}
		}
	};
	static Vector2 vector = new Vector2();
	
	Entity entity, other;
	float x, y;
	
	public float cooldown;
	public String desc, icon;
	
	
	private Attacks(float cooldown, String icon, String desc){
		this.cooldown = cooldown;
		this.desc = desc;
		this.icon = icon;
	}
	
	private Attacks(float cooldown, String desc){
		this.cooldown = cooldown;
		this.desc = desc;
		this.icon = "a"+name();
	}
	
	
	void use(Entity entity){
		this.entity = entity;
		this.other = Duel.other(entity);
		this.x = entity.x;
		this.y = entity.y;
		impl();
	}
	
	void shoot(BulletType type, float rot){
		new Bullet(entity, type, rot).set(entity.x, entity.y).add();
	}
	
	float enemyAngle(){
		return Duel.angleTo(entity);
	}
	
	abstract void impl();
}
