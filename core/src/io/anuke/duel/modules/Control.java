package io.anuke.duel.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.Collidable;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.entities.EntityHandler;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.QuadTree;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;
import io.anuke.ucore.util.Timers;

public class Control extends Module<Duel>{
	IntSet collided = new IntSet();
	QuadTree<Collidable> tree;
	Array<Collidable> out = new Array<>();

	public Control() {
		UInput.addProcessor(this);

		KeyBinds.bind(
			"up", Keys.UP, 
			"down", Keys.DOWN, 
			"left", Keys.LEFT, 
			"right", Keys.RIGHT,
			"dash", Keys.SHIFT_LEFT,
			"dodge", Keys.SHIFT_LEFT,
			"weapon1", Keys.A,
			"weapon2", Keys.S,
			"weapon3", Keys.D,
			"weapon4", Keys.SPACE,
			"weapon5", Keys.F
		);
		
		tree = new QuadTree<Collidable>(3, new Rectangle(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	}

	@Override
	public void update(){
		Timers.update(delta());
		
		if(getModule(UI.class).dead) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		collided.clear();
		
		tree.clear();

		for(Entity entity : EntityHandler.instance().getEntities()){
			if(entity instanceof Collidable)
			tree.insert((Collidable)entity);
		}
		
		for(Entity entity : EntityHandler.instance().getEntities()){
			if(!(entity instanceof Collidable)) continue;
			if(collided.contains((int)entity.id)) continue;
				
			((QuadTreeObject)entity).getBoundingBox(Rectangle.tmp);
			out.clear();
			tree.getMaybeIntersecting(out, Rectangle.tmp);
			
			for(Collidable c : out){
				if(checkCollide(entity, (Entity)c))
					collided.add(((int)((Entity)c).id));
			}
		}
	}
	
	public int getNear(float x, float y, float size){
		Rectangle.tmp.setSize(size);
		Rectangle.tmp.setCenter(x, y);
		out.clear();
		tree.getMaybeIntersecting(out, Rectangle.tmp);
		return out.size;
	}
	
	public boolean checkCollide(Entity entity, Entity other){
		Collidable a = (Collidable) entity;
		Collidable b = (Collidable) other;
		
		if(a.collides(other) 
				&& b.collides(entity)
				 && Mathf.intersect(entity.x, entity.y, a.hitboxSize()/2, other.x, other.y, b.hitboxSize()/2)){
			a.collision(other);
			b.collision(entity);
			return true;
		}
		
		return false;
	}
	
	public void resize(int width, int height){
		tree = new QuadTree<Collidable>(3, new Rectangle(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	}
}
