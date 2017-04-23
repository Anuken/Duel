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
	swarm{
		void impl(){
			for(int i = 0; i < 15; i ++)
				shoot(BulletType.swarm, Mathf.random(360f));
			
			Effects.effect(EffectType.rspark, entity);
		}
	}, 
	cannon{
		void impl(){
			
			Effects.effect(EffectType.inspike, entity);
			
			Timers.run(20, ()->{
				shoot(BulletType.blast, enemyAngle());
			});
		}
	},
	shadow{
		void impl(){
			Effects.effect(EffectType.inwave, entity, other.x, other.y);
			
			Effects.effect(EffectType.inspike, entity);
			
			for(int i = 0; i < 15; i ++){
				Bullet bullet = new Bullet(entity, BulletType.shadow, Mathf.random(360)).set(entity.x, entity.y).add();
				bullet.velocity.setLength(Mathf.random(4, 8));
			}
			
		}
	},
	tricannon{
		void impl(){
			
			Effects.effect(EffectType.inspike, entity);
			
			for(int i = 0; i < 7; i ++)
			Timers.run(15+i*2, ()->{
				shoot(BulletType.blast, enemyAngle()+Mathf.random(-5, 5));
			});
		}
	},
	shot{
		void impl(){
			
			for(int i = 0; i < 7; i ++)
			Timers.run(5+i*2, ()->{
				for(int j = 0; j < 5; j ++)
				shoot(Math.random() < 0.5 ? BulletType.lock : BulletType.swarm, 180+enemyAngle()+Mathf.random(-20, 20));
			});
			
			Effects.effect(EffectType.rspark, entity);
		}
	},
	lock{
		void impl(){
			int shots = 20;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i;
				shoot(BulletType.lock, angle);
			}
		}
	},
	portal{
		void impl(){
			vector.setToRandomDirection().setLength(30);
			
			for(int i = 0; i < 2; i ++){
				new Portal(entity).set(x+vector.x, y+vector.y).add();
				vector.rotate(180);
			}
		}
	}, 
	split{
		void impl(){
			int shots = 4;
			
			for(int i = 0; i < shots; i ++){
				float angle = 360f/shots*i+45;
				shoot(BulletType.split1, angle);
			}
		}
	}, 
	reverseshield{
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
	trilaser{
		void impl(){
			for(int i = 0; i < 3; i ++){
				vector.setToRandomDirection().setLength(70);
				
				Effects.effect(entity instanceof Player ? EffectType.lspike : EffectType.rlspike, entity, vector.x+x, vector.y+y);
				new Laser(entity,vector.x+x, vector.y+y, other.x, other.y).add();
			}
		}
	};
	Entity entity, other;
	float x, y;
	
	static Vector2 vector = new Vector2();
	
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
