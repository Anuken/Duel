package io.anuke.duel.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.Overlay;
import io.anuke.duel.entities.*;
import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.entities.EntityHandler;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.Mathf;

public class Renderer extends RendererModule<Duel>{
	private boolean debug = false;
	private GifRecorder recorder = new GifRecorder(batch);
	private Array<Overlay> removal = new Array<Overlay>();
	
	public Player player;
	public Enemy enemy;
	public Array<Overlay> overlays = new Array<>();
	public float shakeintensity, shaketime;
	
	
	public Renderer(){
		cameraScale = 1;
		
		atlas = new Atlas(Gdx.files.internal("sprites/duel.atlas"));
		font = new BitmapFont(Gdx.files.internal("fonts/prose.fnt"));
		player = new Player().add();
		
		font.getData().setScale(1/2f);
		
		enemy = new Enemy().add();
		
		setPixelation();
	}
	
	@Override
	public void update(){
		EntityHandler.instance().update();
		
		for(Entity e : EntityHandler.instance().getEntities()){
			clampBounds(e);
		}
		
		camera.position.set(0, 0, 0);
		if(shaketime > 0){
			camera.position.add(Mathf.random(-shakeintensity, shakeintensity), Mathf.random(-shakeintensity, shakeintensity), 0);
			shaketime -= delta();
		}
		
		camera.update();
		
		clearScreen();
		
		beginPixel();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		//drawBackground();
		
		for(Entity e : EntityHandler.instance().getEntities()){
			e.draw();
		}
		
		if(debug){
			Draw.color(Color.GREEN);
			for(Entity e : EntityHandler.instance().getEntities()){
				if(e instanceof Collidable){
					Collidable c = (Collidable)e;
					Draw.linerect(e.x-c.hitboxSize()/2, e.y-c.hitboxSize()/2, c.hitboxSize(), c.hitboxSize());
				}
			}
			Draw.color();
		}
		
		removal.clear();
		
		for(Overlay o : overlays){
			o.draw();
			o.time += delta();
			if(o.time > o.lifetime)
				removal.add(o);
		}
		
		overlays.removeAll(removal, true);
		
		batch.end();
		
		endPixel();
		
		recorder.update();
		
		font.getData().setScale(1f);
		batch.begin();
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 0, Gdx.graphics.getHeight());
		batch.end();
		font.getData().setScale(0.5f);
	}
	
	void clampBounds(Entity entity){
		float minx = -gwidth()/2, maxx = gwidth()/2, miny = -gheight()/2, maxy = gheight()/2;
		
		if(entity instanceof Damageable){
			entity.x = Mathf.clamp(entity.x, minx, maxx);
			entity.y = Mathf.clamp(entity.y, miny, maxy);
		}else{
			if(!Mathf.between(entity.x, minx, maxx) || !Mathf.between(entity.y, miny, maxy))
				entity.remove();
		}
		
	}
	
	void drawBackground(){
		Draw.rect("background", 0, 0);
	}
}
