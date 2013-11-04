package com.conley.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class RainDrop {

	Texture dropImage;
	Sound dropSound;
	Array<Rectangle> raindrops;
	long lastDropTime;

	public RainDrop() {
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		dropSound = Gdx.audio.newSound(Gdx.files.internal("droplet.wav"));

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	public void disposeImage() {
		dropImage.dispose();
	}

	public void disposeSound() {
		dropSound.dispose();
	}

	public void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	public long getLastDropTime() {
		return lastDropTime;
	}

	public void setLastDropTime(long newTime) {
		lastDropTime = newTime;
	}

	public Array<Rectangle> getRaindrops() {
		return raindrops;
	}

	public Sound getSound() {
		return dropSound;
	}

	public Texture getImage() {
		return dropImage;
	}
}
