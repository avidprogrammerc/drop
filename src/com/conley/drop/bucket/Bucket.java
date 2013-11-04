/***********************************************************************
 * This class represents the Bucket in the game
 * The constructor allows for custom placement on the game screen
 * 
 * @author Chris Conley
 * @version      2013.11.03
 ***********************************************************************/

package com.conley.drop.bucket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bucket {
	Texture bucketImage;
	Rectangle bucket;

	public Bucket(float startX, float startY) {
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		bucket = new Rectangle();
		bucket.x = startX; // 800 / 2 - 64 / 2;
		bucket.y = startY;
		bucket.width = 64;
		bucket.height = 64;
	}

	public Texture getBucketImage() {
		return bucketImage;
	}

	public void disposeImage() {
		bucketImage.dispose();
	}

	public float getX() {
		return bucket.x;
	}

	public float getY() {
		return bucket.y;
	}

	public void setX(float newXPos) {
		bucket.x = newXPos;
	}

	public void setY(float newYPos) {
		bucket.y = newYPos;
	}

	public Rectangle getRectangle() {
		return bucket;
	}
}