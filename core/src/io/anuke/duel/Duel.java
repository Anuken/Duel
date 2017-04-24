package io.anuke.duel;

import com.badlogic.gdx.graphics.Color;

import io.anuke.duel.entities.Enemy;
import io.anuke.duel.entities.Player;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.duel.modules.Control;
import io.anuke.duel.modules.Renderer;
import io.anuke.duel.modules.UI;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.entities.EntityHandler;
import io.anuke.ucore.modules.ModuleController;

public class Duel extends ModuleController<Duel>{
	public static Player player;
	public static Enemy enemy;
	
	public static int battle = 1;
	public static int health = 1000;
	public static final Color ecolor = Color.ORANGE;
	public static final Color pcolor = Color.ROYAL;
	
	@Override
	public void init(){
		addModule(new Control());
		addModule(new Renderer());
		addModule(new UI());
		
		player = new Player();
		enemy = new Enemy();
		
		restart();
	}
	
	public static void restart(){
		enemy.y = 200;
		enemy.x = player.x = 0;
		player.y = -200;
		
		enemy.health = (int)(health*1.5f);
		player.health = health;
		
		EntityHandler.instance().entities.clear();
		EntityHandler.instance().entitiesToAdd.clear();
		player.add();
		enemy.add();
	}
	
	public static Entity other(Entity e){
		return e == player() ? enemy() : player();
	}
	
	public static Player player(){
		return player;
	}
	
	public static Enemy enemy(){
		return enemy;
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
