package minidoom.entity.items;

import kuusisto.tinysound.Sound;
import minidoom.entity.Entity;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;

public abstract class Item extends Entity {
	protected Sound sound;

	public Item(GameEngine engine, int entityID, Sprite sprite, boolean visible, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
	}

	public abstract void onPickup(Player player);

	@Override
	public void kill() {
		if (sound != null)
			sound.play();
		engine.addItemRemoval(this);
	}
}
