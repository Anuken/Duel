package io.anuke.duel.entities.effect;

import com.badlogic.gdx.math.Vector2;

import io.anuke.duel.Duel;
import io.anuke.duel.Util;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.Damageable;
import io.anuke.duel.entities.Enemy;
import io.anuke.duel.entities.Player;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Mathf;

public class Laser extends Projectile{
	float charge = 25;
	float life = 60;
	float targetx, targety;
	float time;
	
	public Laser(Entity owner, float x, float y, float tx, float ty){
		super(owner);
		this.x = x;
		this.y = y;
		targetx = tx;
		targety = ty;
		vector.set(tx - x, ty - y).setLength(5000);
		targetx = x + vector.x;
		targety = y + vector.y;
		
		Entity player = Duel.other(owner);
		vector.rotate(90);
		
		
		float x1 = x, y1 = y, x2 = targetx, y2 = targety;
		float x0 = player.x, y0 = player.y;
		
		float dst = (float)(((y2-y1)*x0-(x2-x1)*y0+x2*y1-y2*x1)/Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1)));
		vector.setLength(dst);
		
		float ox = player.x+vector.x, oy = player.y+vector.y;
		
		if(Vector2.dst(ox, oy, player.x, player.y) < 20){
			
		}
		
		/*
		Effects.overlay(30, f->{
			Draw.color(Color.ROYAL);
			Draw.tcolor(1, 0.5f, 0, 1f-f/30f);
			
			Draw.alpha(1f-f/30f);
			
			//Draw.rect("mark", ox, oy);
			Draw.thickness(2f);
			
			Draw.circle(ox, oy, 10f);
			
			Draw.tint(Color.ORANGE);
			Draw.circle(ox, oy, 30f-f);
			
			//Draw.line(ox, oy, player.x, player.y);
			
			Draw.thickness(1f);
			Draw.text("!", ox, oy + 20);
			
			Draw.color();
			Draw.tcolor();
		});
		*/
		

	}
	
	public void update(){
		boolean not = time <= charge;

		time += delta();
		
		if(time > charge){
			if(not){
				Effects.shake(5, 5f);
				if(owner instanceof Player)
					Effects.effect(EffectType.sspikes, this);
				else
					Effects.effect(EffectType.ospikes, this);
			}
			
			Entity target = Duel.other(owner);
			
			if(Util.intersects(target, x, y, targetx, targety)){
				((Damageable)target).damage(5);
				Effects.effect(EffectType.particle, owner, target.x, target.y);
			}
		}
		
		if(time > life)
			remove();
	}
	
	public void draw(){
		if(time < charge){
			if(owner instanceof Enemy){
				Draw.color(1, 0.3f, 0, time/charge);
			}else{
				Draw.color(0.3f, 0.4f, 1f, time/charge);
			}
			Draw.line(x, y, targetx, targety);
			Draw.laser("slaser", "slaserend", x, y, targetx, targety);
			Draw.color();
		}else{
			
			Draw.alpha(Mathf.clamp((1f-(Mathf.clamp((time-charge)/(life-charge))))*2f));
			if(owner instanceof Enemy){
				Draw.color(Duel.ecolor);
			}else{
				Draw.color(Duel.pcolor);
			}
			
			Draw.laser("laser", "laserend", x, y, targetx, targety);
			Draw.color();
			Draw.laser("elaser", "elaserend", x, y, targetx, targety);
		}
	}

	@Override
	public int damage(){
		return 2;
	}
}
