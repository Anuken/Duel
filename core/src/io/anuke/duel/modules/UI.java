package io.anuke.duel.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import io.anuke.SceneModule;
import io.anuke.duel.Duel;
import io.anuke.scene.builders.*;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.Dialog;
import io.anuke.scene.utils.CursorManager;

public class UI extends SceneModule<Duel>{
	boolean playing = false;
	
	public UI(){
		Styles.load((styles = new Styles(Gdx.files.internal("ui/uiskin.json"))));
		
		setup();
	}
	
	void setup(){
		SceneBuild.begin(scene);
		
		new table(){{
			get().background("button");
			
			new button("Play", ()->{
				playing = true;
				CursorManager.restoreCursor();
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
			
		}}.end();
		
		new table(){{
			atop();
			
			new label("Duel"){{
				get().setFontScale(2f);
			}}.align(Align.top);
			
		}}.end();
		
		SceneBuild.end();
	}
	
	@Override
	public void update(){
		if(!playing)
		act();
	}
}
