package io.anuke.duel.modules;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.anuke.duel.Duel;
import io.anuke.duel.effects.Effects;
import io.anuke.duel.entities.*;
import io.anuke.duel.entities.Player.AttackInfo;
import io.anuke.scene.Element;
import io.anuke.scene.Scene;
import io.anuke.scene.SceneModule;
import io.anuke.scene.builders.*;
import io.anuke.scene.event.InputEvent;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.*;
import io.anuke.scene.ui.layout.Table;
import io.anuke.scene.utils.CursorManager;
import io.anuke.scene.utils.DragAndDrop;
import io.anuke.scene.utils.DragAndDrop.Payload;
import io.anuke.scene.utils.DragAndDrop.Source;
import io.anuke.scene.utils.DragAndDrop.Target;
import io.anuke.scene.utils.HandCursorListener;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Timers;

public class UI extends SceneModule<Duel>{
	boolean playing = false;
	boolean dead = false;
	BooleanSupplier visible = ()->{return !playing;};
	BooleanSupplier nvisible = ()->{return playing;};
	Dialog settings;
	Dialog restart;
	public Dialog next;
	Dialog info;
	AttackIndicator[] indicators = new AttackIndicator[4];
	Table selectable;
	Preferences prefs;
	boolean played = false; //TODO put this back
	boolean countdown = false;
	public boolean won = false;
	
	public UI(){
		Styles.load((styles = new Styles(Gdx.files.internal("ui/uiskin.json"))));
		Styles.styles.getFont("default-font").getData().setScale(0.75f);
		Styles.styles.getFont("default-font").setUseIntegerPositions(false);
		prefs = Gdx.app.getPreferences("io.anuke.duel");
	}
	
	@Override
	public void init(){
		setup();
		addPref("Screen Shake", 4, 0, 16, i->{
			return (float)(i/4f) + "x";
		});
		
		addPref("Volume", 5, 0, 10, i->{
			return (int)(i/5f*100) + "%";
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
	
	void updateAttacks(){
		Array<Attacks> available = Duel.player.allattacks;
		DragAndDrop dragAndDrop = new DragAndDrop();
		
		int w = 5;
		int height = (int)((float)available.size/w+1);
		
		selectable.clear();
		for(int y = 0; y < height; y ++){
			for(int x = 0; x < w; x ++){
				int index = y*w + x;
				
				if(index >= available.size){
					selectable.add();
				}else{
					AttackSlot info = new AttackSlot(available.get(index), index);
					selectable.add(info).size(48).pad(8);
					if(index == available.size-1 && next.getTitleLabel().getText().toString().equals("Victory!"))
						info.isnew = true;
					
					dragAndDrop.addSource(new Source(info) {
						public Payload dragStart (InputEvent event, float x, float y, int pointer) {
							Payload payload = new Payload();
							payload.setObject(info);
				
							
							Table table = new Table();
							table.background("button");
							table.pad(9);
							table.add(new Label("[YELLOW]"+info.attack.name()));
							table.pack();
							
							payload.setDragActor(table);
							return payload;
						}
					});
				}
			}
			selectable.row();
		}
		
		for(int i = 0; i < 4; i ++){
			dragAndDrop.addTarget(new Target(indicators[i]) {
				public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
					getActor().setColor(Color.PURPLE);
					return true;
				}

				public void reset (Source source, Payload payload) {
					getActor().setColor(Color.ROYAL);
				}

				public void drop (Source source, Payload payload, float x, float y, int pointer) {
					AttackSlot slot = (AttackSlot)payload.getObject();
					int index = slot.index;
					int toindex = ((AttackIndicator)getActor()).index;
					AttackInfo info = Duel.player.attacks[toindex];
					Attacks attack = slot.attack;
					
					
					Duel.player.allattacks.set(index, info.attack);
					slot.attack = info.attack;
					Duel.player.attacks[toindex].attack = attack;
					slot.isnew = false;
				}
			});
		}
		next.pack();
		
	}
	
	void setup(){
		
		info = new Dialog("info"){
			protected void result(Object o){
				play();
			}
		};
		info.getTitleLabel().setColor(Color.CORAL);
		info.padTop(info.getPadTop()-20);
		info.getContentTable().pad(20);
		info.text("How to play: "
				+ "\n[YELLOW][[A][] to use attack 1"
				+ "\n[YELLOW][[S][] to use attack 2"
				+ "\n[YELLOW][[D][] to use attack 3"
				+ "\n[YELLOW][[SPACE][] to use attack 4"
				+ "\n[YELLOW][[ARROW KEYS][] to move"
				+ "\n[YELLOW][[SHIFT][] to dash"
				+ "\nDestroy the orange enemy to progress."
				+ "\n\nPress [ORANGE][[SPACE][] to continue."
				);
		info.setMovable(false);
		info.key(Keys.SPACE, true);
		
		next = new Dialog("Victory!"){
			public Dialog show(Scene scene){
				super.show(scene);
				updateAttacks();
				setPosition(Math.round((getStage().getWidth() - getWidth()) / 2), Math.round((getStage().getHeight() - getHeight()) / 2));
				return this;
			}
		};
		next.getTitleLabel().setColor(Color.CORAL);
		next.padTop(next.getPadTop()-20);
		next.getContentTable().pad(50);
		next.getContentTable().padBottom(20);
		
		next.getButtonTable().addButton("Go", ()->{
			
			next.hide();
			restart();
			
			won = false;
			dead = false;
		}).pad(5);
		
		next.setMovable(false);
		
		for(int i = 0; i < 4; i ++)
			indicators[i] = new AttackIndicator(i);
		
		
		selectable = new Table();
		
		TooltipManager.getInstance().animations = false;
		TooltipManager.getInstance().initialTime = 0;
		
		Table atable = new Table();
		for(int i = 0; i < 4; i ++){
			atable.add(indicators[i]).size(48);
		}
		
		next.text("Select a loadout.");
		next.getContentTable().row();
		next.getContentTable().add(selectable);
		next.getContentTable().row();
		next.getContentTable().add(atable).padTop(30);
		next.pack();
		
		
		restart = new Dialog("you died.");
		restart.getTitleLabel().setColor(Color.CORAL);
		restart.text("Press [YELLOW][[SPACE][WHITE] to restart.").padBottom(10);
		restart.setMovable(false);
		restart.padTop(restart.getPadTop()-20);
		restart.getContentTable().pad(50);
		restart.getContentTable().row();
		restart.getContentTable().addButton("Change Loadout", ()->{
			next.getTitleLabel().setText("Loadout");
			restart.hide();
			next.show(scene);
			dead = false;
			won = true;
		}).padTop(10f);
		restart.getContentTable().row();
		restart.getContentTable().addButton("back to Menu", ()->{
			restart();
			dead = false;
			playing = false;
			restart.hide();
		});
		
		
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
			
			add(new HealthBar(Duel.player()));
			get().add().growX();
			for(int i = 0; i < 4; i ++){
				get().add(new AttackIndicator(i)).size(52).align(Align.bottom);
			}
			get().add().growX();
			add(new HealthBar(Duel.enemy()));
			get().setVisible(nvisible);
			row();
		}};
		
		SceneBuild.end();
	}
	
	void play(){
		if(!played){
			info.show(scene);
			dead = true;
			played = true;
		}else{
			restart();
			playing = true;
			CursorManager.restoreCursor();
		}
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
			
			Draw.color(Color.ROYAL);
			Draw.thickness(8f);
			Draw.spike(0, -20, 70, 200, 10, Timers.time()/2f);
			Draw.color();
			Draw.thickness(1f);
			
			Draw.tscl(2f);
			Draw.tcolor(Color.ORANGE);
			Draw.text("Battle " + Duel.battle, 0, 60);
			Draw.tcolor();
			Draw.text((int)((time-f)/(time/5)) + "", 0, 0);
			
			Draw.tscl(1f);
		});
	}
	
	@Override
	public void update(){
		//if(UInput.keyDown(Keys.R))
		//	Duel.enemy.health = 0;
		
		if(Duel.player().health() <= 0 && !dead && !won){
			restart.show(scene);
			dead = true;
		}
		
		if(dead && UInput.keyDown(Keys.SPACE) && !won){
			restart();
		}
		
		if(Duel.enemy.health() <= 0 && !won){
			nextBattle();
			next.getTitleLabel().setText("Victory!");
			next.show(scene);
			Duel.enemy().remove();
			won = true;
			
		}
		
		act();
	}
	
	void nextBattle(){
		Duel.battle ++;
		Duel.health += 500;
		Duel.player.allattacks.add(Attacks.values()[Mathf.random(Attacks.values().length-1)]);
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
	
	class AttackSlot extends Element{
		Attacks attack;
		Label text;
		int index;
		boolean isnew;
		
		public AttackSlot(Attacks attack, int idx){
			this.attack = attack;
			this.index = idx;
			
			text = new Label("you should not see this text");
			text.setFontScale(0.5f);
			Table table = new Table();
			table.background("button");
			table.pad(9);
			table.add(text);
			
			Tooltip tool = new Tooltip(table);
			tool.setInstant(true);
			
			addListener(tool);
			addListener(new HandCursorListener());
		}
		
		public void draw(Batch batch, float alpha){
			
			text.setText("[ORANGE]Attack: [GREEN]\"" + attack.name()  
			+"\"\n[WHITE]" + attack.desc 
			+ "\n[YELLOW]Drag to attack bar to assign.");
			
			
			Draw.color(Color.ROYAL);
			Draw.thickness(4);
			Draw.alpha(alpha);
			Draw.linerect(getX(), getY(), getWidth(), getHeight());
			Draw.crect(attack.icon, getX()+4, getY()+4, getWidth()-8, getHeight()-8);
			Draw.thickness(1);
			Draw.color();
			
			if(isnew){
				BitmapFont font = Styles.styles.getFont("default-font");
				
				//Styles.styles.getDrawable("button").draw(batch, getX(), getY() + getHeight()-24, 60, 24);
				
				font.setColor(0, 1, 0, alpha);
				font.getData().setScale(0.5f);
				font.draw(batch, "new!", getX()+4, getY() + getHeight()-6);
				font.getData().setScale(0.75f);
				font.setColor(Color.WHITE);
			}
		}
	}
	
	class AttackIndicator extends Element{
		int index;
		Label text;
		
		public AttackIndicator(int index){
			this.index = index;
			text = new Label("WOW");
			text.setFontScale(0.5f);
			Table table = new Table();
			table.add(text);
			table.pad(9);
			table.background("button");
			Tooltip tool = new Tooltip(table);
			tool.setInstant(true);
			TooltipManager.getInstance().animations = false;
			TooltipManager.getInstance().initialTime = 0;
			addListener(tool);
			addListener(new HandCursorListener());
			
			setColor(Color.ROYAL);
		}
		
		public void draw(Batch batch, float alpha){
			AttackInfo info = Duel.player().attacks[index];
			
			text.setText("[ORANGE]Attack: [GREEN]\"" + info.attack.name()  
			+"\"\n[WHITE]" + info.attack.desc 
					+"\n[YELLOW]Press [["+Keys.toString(KeyBinds.get("weapon" + (index+1)))+"] to use.");
			
			Draw.color(getColor());
			Draw.thickness(4);
			Draw.alpha(alpha);
			Draw.linerect(getX(), getY(), getWidth(), getHeight());
			if(info.cooldown <= 0) Draw.color(Color.ORANGE);
			Draw.alpha(alpha);
			Draw.crect(info.attack.icon, getX()+4, getY()+4, getWidth()-8, getHeight()-8);
			Draw.thickness(1);
			Draw.color();
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
			
			Styles.styles.getDrawable("white").draw(batch, getX(), getY(), getWidth(),
					((float)health/Duel.health)*getHeight() * (entity instanceof Enemy ? (1/1.5f) : 1f));
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
