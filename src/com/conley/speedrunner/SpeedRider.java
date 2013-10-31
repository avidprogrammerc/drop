/***********************************************************************
 * This is the core file for the Speed Runner game
 * This file should only really be modified.
 * 
 * @author     Chris Conley
 * @version    2013.10.31
 ***********************************************************************/

package com.conley.speedrunner;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SpeedRider implements ApplicationListener {
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;

	OrthographicCamera camera;
	SpriteBatch batch;

	Rectangle bucket;
	Array<Rectangle> raindrops;

	long lastDropTime;

	int numMisses;
	int numCatches;
	String score;
	String misses;
	BitmapFont font;
	FreeTypeFontGenerator generator;

	@Override
	public void create() {
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("droplet.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 0;
		bucket.width = 64;
		bucket.height = 64;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		numMisses = 0;
		numCatches = 0;
		score = "Score: 0";
		misses = "Missed catches: 0";
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Track.ttf"));
		font = generator.generateFont(14);
		font.setColor(Color.YELLOW);
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 300 * Gdx.graphics.getDeltaTime();

		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;
		

		if (numCatches < 10)
			if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
				spawnRaindrop();
		if (numCatches >= 10)
			if (TimeUtils.nanoTime() - lastDropTime > 750000000)
				spawnRaindrop();
		if (numCatches >= 20)
			if (TimeUtils.nanoTime() - lastDropTime > 500000000)
				spawnRaindrop();
		if (numCatches >= 30)
			if (TimeUtils.nanoTime() - lastDropTime > 250000000)
				spawnRaindrop();

		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			if (numCatches < 10)
				raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (numCatches >= 10)
				raindrop.y -= 300 * Gdx.graphics.getDeltaTime();
			if (numCatches >= 30)
				raindrop.y -= 315 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) {
				iter.remove();
				numMisses++;
				misses = "Missed catches: " + numMisses;
			}
			if (raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
				numCatches++;
				score = "Score: " + numCatches;
			}
		}

		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		batch.begin();
		font.draw(batch, score, 25, 460);
		batch.end();

		batch.begin();
		font.draw(batch, misses, 25, 445);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
}
