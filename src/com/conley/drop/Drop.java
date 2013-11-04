/***********************************************************************
 * This is the core file for the Speed Runner game.
 * All game play is done through this class
 * 
 * @author     Chris Conley
 * @version    2013.11.04
 ***********************************************************************/

package com.conley.drop;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.conley.drop.bucket.Bucket;
import com.conley.drop.dropTypes.AcidRainDrop;
import com.conley.drop.dropTypes.RainDrop;

public class Drop extends Game {
	Bucket bucket;
	RainDrop rainDrop;
	AcidRainDrop acidRainDrop;

	Music rainMusic;

	OrthographicCamera camera;
	SpriteBatch batch;

	int numMisses;
	int numCatches;
	String score;
	String misses;
	BitmapFont font;
	FreeTypeFontGenerator generator;

	@Override
	public void create() {
		bucket = new Bucket(800 / 2 - 64 / 2, 0);
		rainDrop = new RainDrop();
		acidRainDrop = new AcidRainDrop();

		// load the rain background "music"
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

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
		rainDrop.disposeImage();
		bucket.disposeImage();
		rainDrop.disposeSound();
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
		batch.draw(bucket.getBucketImage(), bucket.getX(), bucket.getY());
		batch.end();

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.setX(touchPos.x - 64 / 2);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.setX(bucket.getX() - 300 * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.setX(bucket.getX() + 300 * Gdx.graphics.getDeltaTime());

		if (bucket.getX() < 0)
			bucket.setX(0);
		if (bucket.getX() > 800 - 64)
			bucket.setX(800 - 64);

		if (numCatches < 10)
			if (TimeUtils.nanoTime() - rainDrop.getLastDropTime() > 1000000000) {
				rainDrop.spawnRaindrop();
			}
		if (numCatches >= 10)
			if (TimeUtils.nanoTime() - rainDrop.getLastDropTime() > 750000000) {
				rainDrop.spawnRaindrop();
			}
		if (numCatches >= 20)
			if (TimeUtils.nanoTime() - rainDrop.getLastDropTime() > 500000000) {
				rainDrop.spawnRaindrop();
			}
		if (numCatches >= 30)
			if (TimeUtils.nanoTime() - rainDrop.getLastDropTime() > 250000000) {
				rainDrop.spawnRaindrop();
			}

		if (TimeUtils.nanoTime() - acidRainDrop.getLastDropTime() > 750000000) {
			acidRainDrop.spawnRaindrop();
		}

		Iterator<Rectangle> iter = rainDrop.getRaindrops().iterator();
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
			if (raindrop.overlaps(bucket.getRectangle())) {
				rainDrop.getSound().play();
				iter.remove();
				numCatches++;
				score = "Score: " + numCatches;
			}
		}

		Iterator<Rectangle> aciditer = acidRainDrop.getRaindrops().iterator();
		while (aciditer.hasNext()) {
			Rectangle raindrop = aciditer.next();
			raindrop.y -= 400 * Gdx.graphics.getDeltaTime();
			if (raindrop.overlaps(bucket.getRectangle())) {
				rainDrop.getSound().play();
				aciditer.remove();
				numCatches--;
				score = "Score: " + numCatches;
			}
		}

		batch.begin();
		batch.draw(bucket.getBucketImage(), bucket.getX(), bucket.getY());
		for (Rectangle raindrop : rainDrop.getRaindrops()) {
			batch.draw(rainDrop.getImage(), raindrop.x, raindrop.y);
		}
		for (Rectangle raindrop : acidRainDrop.getRaindrops()) {
			batch.draw(acidRainDrop.getImage(), raindrop.x, raindrop.y, 64f,
					64f);
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
}