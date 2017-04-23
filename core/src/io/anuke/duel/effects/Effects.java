package io.anuke.duel.effects;

import java.util.function.Consumer;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Effect;
import io.anuke.duel.modules.Renderer;

public class Effects{
	
	public static void shake(float intensity, float duration){
		rend().shakeintensity = intensity;
		rend().shaketime = duration;
	}
	
	public static void effect(EffectType type, float x, float y){
		new Effect(type).set(x, y).add();
	}
	
	public static void overlay(float life, Consumer<Float> draw){
		rend().overlays.add(new Overlay(life, draw));
	}
	
	private static Renderer rend(){
		return Duel.module(Renderer.class);
	}
}
