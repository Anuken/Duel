package io.anuke.duel.effects;

import java.util.function.Consumer;

import io.anuke.duel.Duel;
import io.anuke.duel.entities.effect.Effect;
import io.anuke.duel.entities.effect.Projectile;
import io.anuke.duel.modules.Renderer;
import io.anuke.ucore.entities.Entity;

public class Effects{
	
	public static void shake(float intensity, float duration){
		rend().shakeintensity = Math.max(intensity, rend().shakeintensity);
		rend().shaketime = Math.max(duration, rend().shaketime);
	}
	
	public static void effect(EffectType type, Entity entity){
		new Effect(entity, type).set(entity.x, entity.y).add();
	}
	
	public static void effect(EffectType type, Entity entity, float x, float y){
		new Effect(entity, type).set(x, y).add();
	}
	
	public static void effect(EffectType type, Projectile entity){
		new Effect(entity.owner, type).set(entity.x, entity.y).add();
	}
	
	public static void overlay(float life, Consumer<Float> draw){
		rend().overlays.add(new Overlay(life, draw));
	}
	
	private static Renderer rend(){
		return Duel.module(Renderer.class);
	}
}
