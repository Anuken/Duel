package io.anuke.duel.effects;

import java.util.function.Consumer;

public class Overlay{
	public float lifetime;
	public float time;
	public Consumer<Float> draw;
	
	public Overlay(float life, Consumer<Float> draw){
		lifetime = life;
		this.draw = draw;
	}
	
	public void draw(){
		draw.accept(time);
	}
}
