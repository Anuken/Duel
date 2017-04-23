package io.anuke.duel.entities.effect;

import io.anuke.ucore.entities.Entity;

public class TimedEntity extends Entity{
	public float life;
	public float lifetime;
	
	@Override
	public void update(){
		life += delta();
		
		if(life > lifetime)
			remove();
	}
	
}
