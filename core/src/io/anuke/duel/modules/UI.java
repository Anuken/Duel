package io.anuke.duel.modules;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

import io.anuke.SceneModule;
import io.anuke.duel.Duel;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.Damageable;
import io.anuke.duel.entities.Player;
import io.anuke.duel.entities.Player.AttackInfo;
import io.anuke.scene.Element;
import io.anuke.scene.builders.*;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.*;
import io.anuke.scene.ui.layout.Table;
import io.anuke.scene.utils.CursorManager;
import io.anuke.scene.utils.HandCursorListener;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Timers;

public class UI extends SceneModule<Duel>{
	boolean playing = false;
	boolean dead = false;
	BooleanSupplier visible = ()->{return !playing;};
	BooleanSupplier nvisible = ()->{return playing;};
	Dialog settings;
	Dialog restart;
	Dialog next;
	Preferences prefs;
	int battle = 0;
	boolean countdown = false;
	
	public UI(){
		Styles.load((styles = new Styles(Gdx.files.internal("ui/uiskin.json"))));
		prefs = Gdx.app.getPreferences("io.anuke.duel");
	}
	
	@Override
	public void init(){
		setup();
		addPref("Screen Shake", 4, 0, 16, i->{
			return (float)(i/4f) + "x";
		});
	}
	
	public Preferences getPrefs(){
		return prefs;
	}
	
	void addPref(String name, float def, float min, float max, StringProcessor s){
		String parsed = name.toLowerCase().replace(" ", "");
		Table table = settings.getContentTable();
		Slider slider = new Slider(min, max, 1f, false);
		slider.setValue(prefs.getInteger(parsed, (int)def));
		Label label = new Label(name);
		slider.changed(()->{
			label.setText(name + ": " + s.get((int)slider.getValue()));
			prefs.putInteger(parsed, (int)slider.getValue());
			prefs.flush();
		});
		slider.change();
		table.add(label).minWidth(330);
		table.add(slider);
		table.addButton("Reset", ()->{
			slider.setValue(def);
			slider.change();
		});
		table.row();
	}
	
	void setup(){
		next = new Dialog("victory!");
		next.getTitleLabel().setColor(Color.CORAL);
		next.padTop(next.getPadTop()-20);
		next.getContentTable().pad(50);
		next.text("Select a loadout.");
		next.setMovable(false);
		
		restart = new Dialog("you died.");
		restart.getTitleLabel().setColor(Color.CORAL);
		restart.text("Press [YELLOW][[ENTER][WHITE] to restart.");
		restart.setMovable(false);
		restart.padTop(restart.getPadTop()-20);
		restart.getContentTable().pad(50);
		restart.getContentTable().row();
		restart.getContentTable().addButton("back to Menu", ()->{
			restart();
			dead = false;
			playing = false;
			restart.hide();
		}).padTop(10f);
		
		
		SceneBuild.begin(scene);
		
		settings = new Dialog("Settings");
		settings.getTitleLabel().setColor(Color.CORAL);
		settings.addCloseButton();
		
		new table(){{
			get().background("button");
			get().setVisible(visible);
			
			get().addChild(new Background());
			
			new button("Play", ()->{
				play();
			}).width(200);
			
			row();
			
			new button("About", ()->{
				new Dialog("About"){{
					addCloseButton();
					getTitleLabel().setColor(Color.CORAL);
					
					text("Made by [YELLOW]Anuken[WHITE] for the [GREEN]LD38[] jam.\n"
							+ "\nTools used:"
							+ "\n- [ORANGE]libGDX[] base game library"
							+ "\n- [PURPLE]jukedeck.com[] for music"
							+ "\n- [RED]1001fonts.com[] for fonts"
							);
				}}.show(scene);
			}).width(200);
			
			row();
			
			new button("Settings", ()->{
				settings.show(scene);
			}).width(200);
			
		}}.end();
		
		new table(){{
			get().setVisible(visible);
			atop();
			
			new label("- [ORANGE]Duel [WHITE]-"){{
				get().setFontScale(2f);
			}}.align(Align.top);
			
		}}.end();
		
		new table(){{
			abottom();
			/*
			get().add(new HealthBar(getModule(Renderer.class).enemy));
			row();
			get().add().growY();
			row();
			get().add(new HealthBar(getModule(Renderer.class).player));
			*/
			
			get().add(new HealthBar(Duel.player()));
			get().add().growX();
			for(int i = 0; i < 4; i ++){
				get().add(new AttackIndicator(i)).size(52).align(Align.bottom);
			}
			get().add().growX();
			get().add(new HealthBar(Duel.enemy()));
			get().setVisible(nvisible);
			row();
		}};
		
		SceneBuild.end();
	}
	
	void play(){
		restart();
		playing = true;
		CursorManager.restoreCursor();
	}
	
	void restart(){
		Duel.restart();
		dead = false;
		restart.hide();
		countdown = false;
		
		float time = 200;
		
		Timers.run(time, ()->{
			countdown = true;
		});
		
		Effects.overlay(time, f->{
			Draw.tscl(2f);
			Draw.text((int)((time-f)/(time/5)) + "", 0, 0);
			Draw.tscl(1f);
			
			Draw.color(Color.ROYAL);
			Draw.thickness(8f);
			Draw.spike(0, -20, 70, 200, 10, Timers.time()/2f);
			Draw.color();
			Draw.thickness(1f);
		});
	}
	
	@Override
	public void update(){
		if(UInput.keyDown(Keys.R))
			Duel.enemy.health = 0;
		
		if(Duel.player().health() <= 0 && !dead){
			restart.show(scene);
			dead = true;
			
			if(UInput.keyDown(Keys.ENTER)){
				restart();
			}
		}
		
		if(Duel.enemy.health() <= 0 && !dead){
			next.show(scene);
			dead = true;
		}
		
		act();
	}
	
	@Override
	public void dispose(){
		prefs.flush();
	}
	
	static interface StringProcessor{
		String get(int in);
	}
	
	class Background extends Element{
		{
			setFillParent(true);
		}
		
		public void draw(){
			if(!playing){
				float rad = 260;
				Draw.color(Color.ROYAL);
				Draw.thickness(6);
				Draw.circle(gwidth()/2, gheight()/2, rad);
				Draw.spike(gwidth()/2, gheight()/2, rad-30, rad+30, 20, Timers.time()/2f);
				Draw.thickness(1f);
				Draw.color();
			}
		}
	}
	
	class AttackIndicator extends Element{
		int index;
		Label text;
		
		public AttackIndicator(int index){
			this.index = index;
			Tooltip tool = new Tooltip((text = new Label("WOW")));
			tool.setInstant(true);
			TooltipManager.getInstance().animations = false;
			TooltipManager.getInstance().initialTime = 0;
			addListener(tool);
			addListener(new HandCursorListener());
			
			Styles.styles.getFont("default-font").getData().setScale(0.75f);
			//text.setFontScale(0.75f);
			//text.setWrap(true);
			
			setColor(Color.ROYAL);
		}
		
		public void draw(Batch batch, float alpha){
			AttackInfo info = Duel.player().attacks[index];
			
			//Styles.styles.getDrawable("button").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			text.setText("[ORANGE]Attack: [GREEN]\"" + info.attack.name()  
			+"\"\n[WHITE]" + info.attack.desc 
					+"\n[YELLOW]Press [["+Keys.toString(KeyBinds.get("weapon" + (index+1)))+"] to use.");
			
			Draw.color(getColor());
			Draw.thickness(4);
			Draw.linerect(getX(), getY(), getWidth(), getHeight());
			if(info.cooldown <= 0) Draw.color(Color.ORANGE);
			Draw.crect(info.attack.icon, getX()+4, getY()+4, getWidth()-8, getHeight()-8);
			Draw.thickness(1);
			Draw.color();
			
			if(info.cooldown - Gdx.graphics.getDeltaTime()*60 <= 0 && info.cooldown > 0){
				//setColor(Color.WHITE);
				//addAction(Actions.color(Color.WHITE, 60f));
			}
			
			
			/*
			Draw.color(0, 0, 0, 0.1f);
			Draw.crect("white", getX()+4, getY()+4, getWidth()-8, (getHeight()-8)*(info.cooldown/(float)info.attack.cooldown));
			Draw.color();
			
			DrawContext.font.getData().setScale(1f);
			Draw.text(info.charges + "", getX(), getY()+getHeight(), Align.left);
			Draw.tcolor();
			*/
		}
	}
	
	class HealthBar extends Element{
		Damageable entity;
		
		public HealthBar(Entity entity){
			this.entity = (Damageable)entity;
		}
		
		public void draw(Batch batch, float alpha){
			Draw.color(Color.BLACK);
			Draw.crect("white", getX(), getY(), getWidth(), getHeight());
			
			int health = entity.health();
			
			Draw.color(entity instanceof Player ? Color.ROYAL : Color.ORANGE);
			
			Draw.thickness(4);
			Draw.linerect(getX(), getY(), getWidth(), getHeight());
			
			Styles.styles.getDrawable("white").draw(batch, getX(), getY(), getWidth(),( (float)health/Duel.health)*getHeight());
			//Styles.styles.getDrawable("white").draw(batch, getX(), getY(), ( (float)health/Duel.health)*getWidth(), getHeight());
			
			Draw.thickness(1f);
			Draw.color();
		}
		
		public float getPrefWidth(){
			return 50;
		}
		
		public float getPrefHeight(){
			return Gdx.graphics.getHeight();
		}
	}
}
