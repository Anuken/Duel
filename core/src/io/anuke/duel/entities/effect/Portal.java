package io.anuke.duel.entities.effect;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.EffectType;
import io.anuke.duel.effects.Effects;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Mathf;

public class Portal extends TimedEntity{
	float warmup = EffectType.portalwave.lifetime;
	Entity owner;

	public Portal(Entity owner) {
		this.owner = owner;
		lifetime = 200;
	}

	@Override
	public void draw(){
		Draw.color(0.1f, 0.1f, 0.3f);
		
		float f = 20f;
		if(life > lifetime-f)
			Draw.alpha(1f-(life-(lifetime-f))/f);
		
		Draw.thickness(4);
		Draw.spike(x, y, 20 - (life - warmup) / 2.5f, 40 - (life - warmup) / 2.5f, 10, (id % 2 - 0.5f) * life * 2f);
		Draw.spike(x, y, 30, 40, 10, -(id % 2 - 0.5f) * life * 2f);
		Draw.circle(x, y, 20);
		Draw.thickness(1f);
		Draw.color();
	}

	public void update(){
		if(life <= 1f)
			Effects.effect(EffectType.portalwave, x, y);

		super.update();

		if(life > warmup && Math.random() < 0.5*delta()){
			float i = 2;
			float r = 14f;
			new Bullet(owner, BulletType.particle, Duel.angleTo(this, Duel.other(owner)) + Mathf.random(-i, i)).set(x + Mathf.random(-r, r), y + Mathf.random(-r, r)).add();
			Effects.shake(3f, 2f);
		}
	}
}
