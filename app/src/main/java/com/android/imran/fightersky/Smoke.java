package com.android.imran.fightersky;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Imran on 3/31/2017.
 */

public class Smoke extends GameObject {


    public int r;

    private Animation animation = new Animation();

    public Smoke(int x, int y) {
        r =5;
        super.x = x;
        super.y = y;

    }

    public void UpdateRoundCircle() {
        x -= 10;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawCircle(x - r, y - r, r, paint);
        canvas.drawCircle(x - r + 2, y - r - 2, r, paint);
        canvas.drawCircle(x - r + 10, y + r - 10, r, paint);
    }
}
