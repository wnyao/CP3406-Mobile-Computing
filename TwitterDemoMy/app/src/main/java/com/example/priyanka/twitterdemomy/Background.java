package com.example.priyanka.twitterdemomy;

/**
 * Created by Priyanka on 16/5/2017.
 */

class Background {
    public static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
