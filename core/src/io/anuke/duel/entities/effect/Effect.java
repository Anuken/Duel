package io.anuke.duel.entities.effect;

import io.anuke.duel.effects.EffectType;
import io.anuke.ucore.entities.Entity;

public class Effect extends Entity{
	EffectType type;
	public float life;
	public float lifetime;
	
	public Effect(EffectType type){
		lifetime = type.lifetime;
		this.type = type;
	}
	
	@Override
	public void update(){
		life += delta();
		
		if(life > lifetime)
			remove();
	}
	
	@Override
	public void draw(){
		type.draw(this);
	}
}
