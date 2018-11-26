package edu.lhup.com.sudokuch3;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

    private static MediaPlayer mp = null;

    public static void play(Context context, int resid){

        stop(context);

        mp = MediaPlayer.create(context, resid);

        if(Prefs.getMusic(context)){
            mp = MediaPlayer.create(context, resid);
            mp.setLooping(true);
            mp.start();
        }

        mp.setLooping(true);
        mp.start();

    }

    protected static void stop(Context context) {
        if(mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }

    }
}
