/***********************************************************************
 * This class represents the rain drops in the game
 * It uses an Array to store the raindrops in.
 * 
 * @author Chris Conley
 * @version      2013.11.03
 ***********************************************************************/

package com.conley.drop.dropTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class AcidRainDrop {

	Texture dropImage;
	Sound dropSound;
	Array<Rectangle> raindrops;
	long lastDropTime;
	Sprite sprite;

	public AcidRainDrop() {
		Texture.setEnforcePotImages(false);
		dropImage = new Texture(Gdx.files.internal("acidRain.png"));
		sprite = new Sprite(dropImage);
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

	public Sprite getImage() {
		return sprite;
	}
}
