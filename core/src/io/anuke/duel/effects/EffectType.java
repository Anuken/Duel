package io.anuke.duel.effects;

import com.badlogic.gdx.graphics.Color;

import io.anuke.duel.entities.effect.Effect;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.graphics.Hue;

public enum EffectType{
	spikes{
		{lifetime=10;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(new Color(0x75ffafff), Color.ROYAL, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			
			float add = e.life*3;
			
			float len1 = 3+add, len2 = 11+add;
			
			Draw.spike(e.x, e.y, len1, len2, 10);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	smspikes{
		{lifetime=10;}
		
		Color color = new Color(0x51ffbbff);
		
		public void draw(Effect e){
			Draw.color(color);
			Draw.thickness(2f);
			
			float add = e.life*2;
			
			Draw.spike(e.x, e.y, add, 3+add, 10);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	sspikes{
		{lifetime=30;}
		
		public void draw(Effect e){
			Draw.color(Color.ROYAL);
			Draw.thickness(3f);
			//Draw.alpha(1f-e.life/lifetime+0.4f);
			
			float add = e.life/1.2f;
			
			Draw.spike(e.x, e.y, 6+add, 12+add, 8);
			//Draw.circle(e.x, e.y, -2+add);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	ospikes{
		{lifetime=30;}
		
		public void draw(Effect e){
			Draw.color(Color.ORANGE);
			Draw.thickness(3f);
			//Draw.alpha(1f-e.life/lifetime+0.4f);
			
			float add = e.life/1.2f;
			
			Draw.spike(e.x, e.y, 6+add, 12+add, 8);
			//Draw.circle(e.x, e.y, -2+add);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	wave{
		{lifetime=10;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ORANGE, Color.SCARLET, e.life/lifetime));
			Draw.thickness(3f + e.life/lifetime);
			Draw.circle(e.x, e.y, 5f + e.life*5f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	inwave{
		{lifetime=20;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.WHITE, Color.ROYAL, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 40f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	rinwave{
		{lifetime=20;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.WHITE, Color.ORANGE, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 40f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	redwave{
		{lifetime=20;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.RED, Color.ORANGE, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 30f - e.life*1.5f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	inspike{
		{lifetime=20;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ROYAL, Color.WHITE, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 40f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	rinspike{
		{lifetime=20;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ORANGE, Color.WHITE, e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 40f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	lspike{
		{lifetime=33;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ROYAL, Color.WHITE, e.life/lifetime));
			Draw.thickness(5f - e.life/lifetime);
			Draw.circle(e.x, e.y, 60f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	rlspike{
		{lifetime=33;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ORANGE, Color.WHITE, e.life/lifetime));
			Draw.thickness(5f - e.life/lifetime);
			Draw.circle(e.x, e.y, 60f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	swave{
		{lifetime=15;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.WHITE, new Color(0x51ffbbff), e.life/lifetime));
			Draw.thickness(3f - e.life/lifetime);
			Draw.circle(e.x, e.y, 30f - e.life*2f);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	portalwave{
		{lifetime=60;}
		
		public void draw(Effect e){
			Draw.color(Hue.mix(Color.ROYAL, new Color(0.02f, 0.05f, 0.1f, 1f), e.life/lifetime));
			Draw.thickness(7f - e.life/lifetime);
			Draw.circle(e.x, e.y, 60f - e.life);
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	spark{
		{lifetime=13;}
		
		public void draw(Effect e){
			frames(e, 5);
		}
	},
	particle{
		{lifetime=13;}
		
		Color color = new Color(0x483bc3ff);
		
		public void draw(Effect e){
			Draw.color(color);
			Draw.thickness(1f);
			
			float add = e.life;
			
			Draw.spike(e.x, e.y, add, 3 + add, 6);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	orangespark{
		{lifetime=13;}
		
		public void draw(Effect e){
			
			float add = e.life;
			
			Draw.color(Color.WHITE);
			
			Draw.spike(e.x, e.y, add, 3 + add, 6);
			
			Draw.color();
		}
	},
	rspark{
		{lifetime=13;}
		
		public void draw(Effect e){
			Draw.color(Color.ROYAL);
			Draw.thickness(2f);
			
			float add = e.life*2;
			
			Draw.spike(e.x, e.y, add, 3 + add, 8);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	},
	shield{
		{lifetime=30;}
		
		public void draw(Effect e){
			Draw.alpha(1f-e.life/e.lifetime);
			Draw.tint(Color.ROYAL);
			Draw.thickness(4f);
			
			Draw.circle(e.x, e.y, 100);
			Draw.tint(Color.ORANGE);
			
			Draw.spike(e.x, e.y, e.life*4, e.life*4+20, 20);
			
			Draw.thickness(1f);
			
			Draw.color();
		}
	};
	
	public float lifetime;
	
	public void draw(Effect e){
		
	}
	
	void frames(Effect e, int frames){
		Draw.rect(name() + ((int)((frames)*(e.life/lifetime))+1), e.x, e.y);
	}
	
	public float lifetime(){
		return lifetime;
	}
}
