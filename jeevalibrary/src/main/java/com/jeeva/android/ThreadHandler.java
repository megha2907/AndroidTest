package com.jeeva.android;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jeeva on 5/11/15.
 */
public class ThreadHandler {

    private static ThreadHandler mThreadHandler;

    private Map<Integer, Thread> mBackgroundTasks = new HashMap<>();

    private Random mRandom = new Random();

    private ThreadHandler() {

    }

    public static ThreadHandler getInstance() {
        if (mThreadHandler == null) {
            mThreadHandler = new ThreadHandler();
        }
        return mThreadHandler;
    }

    /**
     * To do the task in background
     *
     * @param runnable
     * @return
     */
    public int doInBackground(final Runnable runnable) {
        final int taskId = generateTaskId();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                mBackgroundTasks.remove(taskId);
            }
        });
        mBackgroundTasks.put(taskId, thread);
        thread.start();
        return taskId;
    }


    /**
     * To do the task on main thread
     *
     * @param runnable
     * @return
     */
    public int doInForeground(final Runnable runnable) {
        final int taskId = generateTaskId();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
        return taskId;
    }

    public void abortTask(int taskId) {
        if (mBackgroundTasks.containsKey(taskId)) {
            mBackgroundTasks.get(taskId).interrupt();
        }
    }

    /**
     * To generate new task id
     *
     * @return
     */
    public int generateTaskId() {
        int taskId = mRandom.nextInt(10000);
        if (mBackgroundTasks.containsKey(taskId)) {
            generateTaskId();
        }
        return taskId;
    }

}
