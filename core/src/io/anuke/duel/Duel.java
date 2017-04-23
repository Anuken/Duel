package io.anuke.duel;

import io.anuke.duel.entities.Enemy;
import io.anuke.duel.entities.Player;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.duel.modules.Control;
import io.anuke.duel.modules.Renderer;
import io.anuke.duel.modules.UI;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.modules.ModuleController;

public class Duel extends ModuleController<Duel>{
	public static final int health = 10000;
	
	@Override
	public void init(){
		addModule(Control.class);
		addModule(Renderer.class);
		addModule(UI.class);
	}
	
	public static Entity other(Entity e){
		return e == player() ? enemy() : player();
	}
	
	public static Player player(){
		return module(Renderer.class).player;
	}
	
	public static Enemy enemy(){
		return module(Renderer.class).enemy;
	}
	
	public static float angleTo(Entity entity){
		Entity other = other(entity);
		Projectile.vector.set(other.x - entity.x, other.y - entity.y);
		return Projectile.vector.angle();
	}
	
	public static float angleTo(Entity entity, Entity other){
		Projectile.vector.set(other.x - entity.x, other.y - entity.y);
		return Projectile.vector.angle();
	}
}
