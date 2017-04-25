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
	swarm(40, "Shoots a swarm of seeking bullets."){
		void impl(){
			for(int i = 0; i < 30; i ++)
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
	cannonsphere(200, "alasersphere", "Shoots a circles of fast-moving bullets."){
		void impl(){
			int shots = 60;
			for(int i = 0; i < shots; i ++){
				int index = i;
				Timers.run(i, ()->{
					shoot(BulletType.blast, index*360f/shots*3f);
				});
			}
		}
	},
	mark(100, "Shoots multiple fast-moving bullets in a circle."){
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
	multishot(40, "ashadow", "Shoots a barrage of small bullets."){
		void impl(){
			
			for(int i = 0; i < 20; i ++)
			Timers.run(i, ()->{
				for(int j = 0; j < 2; j ++)
				shoot(BulletType.particle, enemyAngle()+Mathf.random(-4, 4));
				Effects.shake(3, 2);
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
	lock(120, "Shoots a circle of seeking bullets."){
		void impl(){
			int shots = 20;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.lock, angle);
			}
		}
	},
	portal(120, "Opens a portal that targets the enemy."){
		void impl(){
			vector.setToRandomDirection().setLength(30);
			
			for(int i = 0; i < 2; i ++){
				new Portal(entity).set(x+vector.x, y+vector.y).add();
				vector.rotate(180);
			}
		}
	}, 
	megaportal(200, "Opens a large portal that targets the enemy."){
		void impl(){
			new MegaPortal(entity).set(x+vector.x, y+vector.y).add();
		}
	},
	laserportal(310, "alasersphere", "Opens a laser portal that targets the enemy."){
		void impl(){
			new LaserPortal(entity).set(x+vector.x, y+vector.y).add();
		}
	}, 
	split(130, "Shoots 3 bullets that each shoot 3 lasers."){
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
	laserballs(140, "Shoots laser-firing projectiles in a circle."){
		void impl(){
			int shots = 5;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.laserball, angle);
			}
		}
	}, 
	reverseshield(230, "Takes control of all nearby bullets and makes them target the enemy."){
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
	trilaser(110, "Shoots three lasers in sequence."){
		void impl(){
			for(int i = 0; i < 3; i ++){
				vector.setToRandomDirection().setLength(70);
				
				Effects.effect(entity instanceof Player ? EffectType.lspike : EffectType.rlspike, entity, vector.x+x, vector.y+y);
				new Laser(entity,vector.x+x, vector.y+y, other.x, other.y).add();
			}
		}
	},
	lasercannon(220, "Shoots many lasers in a burst."){
		void impl(){
			for(int i = 0; i < 25; i ++){
				Timers.run(i*3, ()->{
					Effects.effect(entity instanceof Player ? EffectType.lspike : EffectType.rlspike, entity, entity.x, entity.y);
					new Laser(entity, entity.x, entity.y, other.x, other.y).add();
				});
			}
		}
	},
	lasersphere(250, "Shoots lasers in a circular pattern."){
		void impl(){
			int lasers = 15;
			vector.set(0, 1);
			for(int i = 0; i < lasers; i ++){
				vector.setAngle(i*360f/lasers);

				float mx = vector.x;
				float my = vector.y;
				Timers.run(i*5, ()->{
					Effects.effect(entity instanceof Player ? EffectType.lspike : EffectType.rlspike, entity, entity.x, entity.y);
					new Laser(entity, entity.x, entity.y, entity.x + mx, entity.y + my).add();
					//new Laser(entity, entity.x, entity.y, entity.x - mx, entity.y + my).add();
				});
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
