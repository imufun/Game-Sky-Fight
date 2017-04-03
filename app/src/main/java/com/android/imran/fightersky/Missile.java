package com.android.imran.fightersky;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Imran on 4/3/2017.
 */

public class Missile extends GameObject {
    private Bitmap SpriteSheet;
    private int score;
    private int speed;
    Random rand = new Random();
    Animation animation = new Animation();


    public Missile(Bitmap res, int x, int y, int w, int h, int s, int NumberFrames) {
        super.x = x;
        super.y = y;
        WIDTH = w;
        HEIGHT = h;
        score = s;

        speed = 7 + (int) (rand.nextDouble() * score / 30);
        if (speed > 40) speed = 40;
        Bitmap[] images = new Bitmap[NumberFrames];
        SpriteSheet = res;

        for (int i = 0; i < images.length; i++) {
            images[i] = Bitmap.createBitmap(SpriteSheet, 0, i * HEIGHT, WIDTH, HEIGHT);
        }
        animation.setFrames(images);
        animation.setDelay(100 - speed);
    }

    public void update() {
        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e) {
        }
    }


    public int getWidth() {
        return WIDTH - 10;

    }
}
