package io.anuke.duel.modules;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

import io.anuke.SceneModule;
import io.anuke.duel.Duel;
import io.anuke.duel.entities.Damageable;
import io.anuke.duel.entities.Player;
import io.anuke.scene.Element;
import io.anuke.scene.builders.*;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.Dialog;
import io.anuke.scene.ui.Label;
import io.anuke.scene.ui.Slider;
import io.anuke.scene.ui.layout.Table;
import io.anuke.scene.utils.CursorManager;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Entity;

public class UI extends SceneModule<Duel>{
	boolean playing = false;
	BooleanSupplier visible = ()->{return !playing;};
	BooleanSupplier nvisible = ()->{return playing;};
	
	Dialog settings;
	Preferences prefs;
	
	public UI(){
		Styles.load((styles = new Styles(Gdx.files.internal("ui/uiskin.json"))));
		prefs = Gdx.app.getPreferences("io.anuke.duel");
	}
	
	@Override
	public void init(){
		setup();
		addPref("Screen Shake", 3, 0, 12);
	}
	
	public Preferences getPrefs(){
		return prefs;
	}
	
	void addPref(String name, float def, float min, float max){
		String parsed = name.toLowerCase().replace(" ", "");
		Table table = settings.getContentTable();
		Slider slider = new Slider(min, max, 1f, false);
		slider.setValue(prefs.getInteger(parsed, (int)def));
		Label label = new Label(name);
		slider.changed(()->{
			label.setText(name + ": " + (int)slider.getValue());
			prefs.putInteger(parsed, (int)slider.getValue());
			prefs.flush();
		});
		slider.change();
		table.add(label).padRight(60).minWidth(190);
		table.add(slider);
		table.addButton("Reset", ()->{
			slider.setValue(def);
			slider.change();
		});
		table.row();
	}
	
	void setup(){
		SceneBuild.begin(scene);
		
		settings = new Dialog("Settings");
		settings.getTitleLabel().setColor(Color.CORAL);
		settings.addCloseButton();
		
		new table(){{
			get().background("button");
			get().setVisible(visible);
			
			new button("Play", ()->{
				play();
			}).width(200);
			
			row();
			
			new button("About", ()->{
				new Dialog("About"){{
					addCloseButton();
					getTitleLabel().setColor(Color.CORAL);
					
					text("Some random placeholder text that nobody will read, "
							+ "\nbut might as well add it anyway just in case. "
							+ "\nAlso, sample text sample text sample text.");
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
			
			new label("Duel"){{
				get().setFontScale(2f);
			}}.align(Align.top);
			
		}}.end();
		
		new table(){{
			abottom();
			get().add(new HealthBar(getModule(Renderer.class).player));
			get().add().growX();
			get().add(new HealthBar(getModule(Renderer.class).enemy));
			
			get().setVisible(nvisible);
		}};
		
		SceneBuild.end();
	}
	
	void play(){
		playing = true;
		CursorManager.restoreCursor();
	}
	
	@Override
	public void update(){
		act();
	}
	
	@Override
	public void dispose(){
		prefs.flush();
	}
	
	static class HealthBar extends Element{
		Damageable entity;
		
		public HealthBar(Entity entity){
			this.entity = (Damageable)entity;
		}
		
		public void draw(Batch batch, float alpha){
			Styles.styles.getDrawable("button").draw(batch, getX(), getY(), getWidth(), getHeight());
			
			int health = entity.health();
			
			Draw.color(entity instanceof Player ? Color.ROYAL : Color.ORANGE);
			
			Styles.styles.getDrawable("white").draw(batch, getX(), getY(), getWidth(),( (float)health/Duel.health)*getHeight());
			
			Draw.color();
		}
		
		public float getPrefWidth(){
			return 50;
		}
		
		public float getPrefHeight(){
			return 200f;
		}
	}
}
