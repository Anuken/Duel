package io.anuke.duel.entities;

import com.badlogic.gdx.graphics.Color;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Bullet;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.graphics.Hue;

public abstract class Fighter extends Entity implements Collidable, Damageable{
	int health = Duel.health;
	float hittime = 5f;
	float hit = 0;
	float time;
	
	public void attack(Attacks attack){
		attack.use(this);
	}
	
	@Override
	public void draw(){
		Draw.color(this instanceof Player ? Duel.pcolor : Duel.ecolor);
		Draw.thickness(8);
		Draw.circle(x, y, 15);
		Draw.thickness(4);
		Draw.color(Color.WHITE);
		if(hit > 0){
			Draw.color(Hue.mix(Color.WHITE, Color.FIREBRICK, hit/hittime));
			hit -= delta();
		}
		Draw.circle(x, y, 20);
		Draw.spike(x, y, 10, 25, 8, time);
		Draw.thickness(1);
	}
	
	@Override
	public boolean collides(Entity other){
		return other instanceof Bullet;
	}

	@Override
	public void collision(Entity other){
		damage(((Bullet)other).damage());
	}

	@Override
	public float hitboxSize(){
		return 16;
	}

	@Override
	public void damage(int amount){
		health -= amount;
		hit = hittime;
		
	}

	@Override
	public int health(){
		return health;
	}
}
