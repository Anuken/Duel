package io.anuke.duel.entities.effect;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.Player;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Mathf;

public class LaserPortal extends TimedEntity{
	float warmup = EffectType.portalwave.lifetime;
	Entity owner;

	public LaserPortal(Entity owner) {
		this.owner = owner;
		lifetime = 300;
	}

	@Override
	public void draw(){
		Draw.color(owner instanceof Player ? Duel.pcolor : Duel.ecolor);
		
		float f = 20f;
		if(life > lifetime-f)
			Draw.alpha(1f-(life-(lifetime-f))/f);
		
		Draw.thickness(5);
		Draw.spike(x, y, 40 - (life - warmup) / 2.5f, 80 - (life - warmup) / 2.5f, 8, (id % 2 - 0.5f) * life * 2f);
		Draw.spike(x, y, 50, 80, 8, -(id % 2 - 0.5f) * life * 2f);
		Draw.circle(x, y, 40);
		Draw.thickness(1f);
		Draw.color();
	}

	public void update(){
		if(life <= 1f)
			Effects.effect(EffectType.portalwave, owner, x, y);

		super.update();

		if(life > warmup && Math.random() < 0.3*delta()){
			float r = 20f;
			float d = 50f;
			new Laser(owner, x + Mathf.random(-r, r), y + Mathf.random(-r, r), Duel.other(owner).x+ Mathf.random(-d, d), Duel.other(owner).y + Mathf.random(-d, d)).add();
			
			//new Bullet(owner, BulletType.particle, Duel.angleTo(this, Duel.other(owner)) + Mathf.random(-i, i)).set(x + Mathf.random(-r, r), y + Mathf.random(-r, r)).add();
			Effects.shake(6f, 3f);
		}
	}
}
