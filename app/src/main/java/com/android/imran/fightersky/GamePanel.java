package com.android.imran.fightersky;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Imran on 12/2/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;


    private MainThread mThread;
    private Background bg;

    private Player mPlayer;

    public GamePanel(Context context) {

        super(context);
        getHolder().addCallback(this);
        mThread = new MainThread(getHolder(), this);
        setFocusable(true);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                mThread.setRunning(false);
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        mPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter),65,25,3);
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mPlayer.getPlaying()) {
                mPlayer.setPlaying(true);
            } else {
                mPlayer.setUp(true);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mPlayer.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {

        if (mPlayer.getPlaying()) {
            bg.update();
            mPlayer.update();
        }

    }


    @Override
    public void draw(Canvas canvas) {

        final float ScalefactorX = getWidth() / (WIDTH * 1.f);
        final float ScalefactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int saveState = canvas.save();
            canvas.scale(ScalefactorX, ScalefactorY);
            bg.draw(canvas);
            mPlayer.draw(canvas);
            canvas.restoreToCount(saveState);


            // Log.v("Pictures", "Width and height are " + WIDTH + "--" + HEIGHT);
        }

    }

}
