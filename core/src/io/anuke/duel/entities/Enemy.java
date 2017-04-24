package io.anuke.duel.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Laser;
import io.anuke.duel.modules.Control;
import io.anuke.ucore.util.Timers;

public class Enemy extends Fighter{
	Player player;
	Vector2 vector = new Vector2(0.5f, 0.5f);
	boolean away = true;
	boolean flip = false;
	float angle = 90;
	float standtime = 0;
	float time;
	
	void combat(){
		
		if(rand(0.09)){
			attack(Attacks.cannon);
		}
		
		if(Duel.battle > 7)
		if(rand(0.09)){
			attack(Attacks.cannon);
		}
		
		if(Duel.battle > 8)
		if(rand(0.0008)){
			attack(Attacks.lasercannon);
		}
		
		if(rand(0.03)){
			attack(Attacks.lock);
		}
		
		if(rand(0.009)){
			attack(Attacks.shot);
		}
		
		if(rand(0.01)){
			attack(Attacks.multishot);
		}
		
		if(Duel.battle > 2)
		if(rand(0.004)){
			attack(Attacks.shadow);
		}
		
		if(Duel.battle > 1)
		if(rand(0.01)){
			attack(Attacks.tricannon);
		}
		
		
		if(Duel.battle > 2)
		if(rand(0.01)){
			attack(Attacks.mark);
		}
		
		
		if(Duel.battle > 2)
		if(rand(0.001)){
			attack(Attacks.laserballs);
		}
		
		
		if(Duel.battle > 3)
		if(rand(0.001)){
			attack(Attacks.split);
		}
		
		
		if(Duel.battle > 2)
		if(rand(0.002)){
			for(int i = 0; i < 3; i ++){
				Timers.run(i*8, ()->{
					attack(Attacks.balls);
				});
			}
		}
		
		
		if(Duel.battle > 3)
		if(rand(0.002)){
			for(int i = 0; i < 5; i ++){
				Timers.run(i*10, ()->{
					attack(Attacks.split);
				});
			}
		}
		
		if(Duel.battle > 4)
		if(rand(0.002)){
			for(int i = 0; i < 5; i ++){
				Timers.run(i*15, ()->{
					attack(Attacks.lock);
					attack(Attacks.swarm);
				});
			}
			standtime = 70;
		}
		
		if(Duel.battle > 2)
		if(rand(0.002)){
			for(int i = 0; i < 7; i ++){
				Timers.run(i*10, ()->{
					attack(Attacks.tricannon);
				});
			}
		}
		
		if(Duel.battle > 4)
		if(rand(0.04) && Duel.module(Control.class).getNear(x, y, 100) > 60){
			attack(Attacks.reverseshield);
		}
		
		if(Duel.battle > 4)
		if(rand(0.005)){
			attack(Attacks.portal);
		}
		
		if(Duel.battle > 5)
		if(rand(0.004)){
			attack(Attacks.megaportal);
		}
		
		if(Duel.battle > 6)
		if(rand(0.002)){
			attack(Attacks.laserportal);
		}
		
		if(Duel.battle > 7)
		if(rand(0.003)){
			attack(Attacks.lasersphere);
		}
		
		if(Duel.battle > 6)
			if(rand(0.004)){
				attack(Attacks.cannonsphere);
			}
		
		if(Duel.battle > 6)
		if(rand(0.004)){
			attack(Attacks.laserballs);
		}
		
		
		if(Duel.battle > 3)
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
		
		//if(standtime <= 0){
			x += vector.x*delta();
			y += vector.y*delta();
		//}else{
		//	standtime -= delta();
		//}
		
		time += vector.x < 0 ? delta()*4 : vector.x > 0 ? -delta()*4 : 0;
		
		combat();
	}
	
	boolean rand(double d){
		return Math.random()<d*delta()*(1f+(Duel.battle-1f)/16f);
	}
	
	@Override
	public void added(){
		player = Duel.player;
	}
}
