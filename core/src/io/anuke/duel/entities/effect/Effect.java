package io.anuke.duel.entities.effect;

import io.anuke.duel.effects.EffectType;
import io.anuke.ucore.entities.Entity;

public class Effect extends Entity{
	public EffectType type;
	public Entity owner;
	public float life;
	public float lifetime;
	
	public Effect(Entity owner, EffectType type){
		lifetime = type.lifetime;
		this.type = type;
		this.owner = owner;
	}
	
	@Override
	public void update(){
		life += delta();
		
		if(life > lifetime)
			remove();
	}
	
	@Override
	public void draw(){
		type.set(this);
		type.draw();
	}
}
