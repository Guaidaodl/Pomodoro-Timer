/*
 * Copyright (c) 2016.  Guaidaodl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.guaidaodl.pomodorotimer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.guaidaodl.pomodorotimer.BuildConfig;
import io.github.guaidaodl.pomodorotimer.Injection;
import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.data.TomatoRepository;
import io.github.guaidaodl.pomodorotimer.utils.NotificationHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class PomodoroService extends Service {
    @SuppressWarnings("PointlessBooleanExpression")
    private static final boolean QUICK_TEST = true && BuildConfig.DEBUG;

    private static final String ACTION_START_TIMER = "io.github.guaidaodl.pomodorotimer.startTimer";
    private static final String ACTION_STOP_TIMER = "io.github.guaidaodl.pomodorotimer.stopTimer";

    private static final String EXTRAS_TOMATO_MINUTE = "TOMATO_MINUTE";

    private List<WeakReference<TomatoStateListener>> mListeners = new LinkedList<>();

    /** 当前番茄的总时长，单位是秒 */
    private int mTomatoTime = 0;

    @Nullable private MediaPlayer mMediaPlayer;

    /** 定时器的订阅者 */
    @Nullable
    private TimeSubscriber mTimeSuscriber;

    private PomodoroBinder mBinder = new PomodoroBinder();

    public static void start(Context context) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.startService(intent);
    }

    public static Intent getIntentForStopTimer(Context context) {
        return new Intent(context, PomodoroService.class)
                .setAction(ACTION_STOP_TIMER);
    }

    public static Intent getIntentForStartTimer(Context context) {
        return new Intent(context, PomodoroService.class)
                .setAction(ACTION_START_TIMER);
    }

    public static void bind(@NonNull Context context, @NonNull ServiceConnection conn) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public static void unbind(@NonNull Context context, @NonNull ServiceConnection conn) {
        context.unbindService(conn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();
        switch (action) {
            case ACTION_STOP_TIMER:
                stopTomato();
                break;
            case ACTION_START_TIMER:
                int minutes = intent.getIntExtra(EXTRAS_TOMATO_MINUTE, 25);
                startNewTomato(minutes);
                break;
            default:
                throw new IllegalStateException("unrecognized action :" + action);
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onDestroy() {
        super.onDestroy();
        if (mTimeSuscriber != null) {
            mTimeSuscriber.unsubscribe();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 当番茄中被中断或者完成的时候调用。
     */
    public void onTimerEnd() {
        if (mTimeSuscriber != null) {
            mTimeSuscriber.unsubscribe();
            mTimeSuscriber = null;
        }
        stopBGM();
        stopForeground(true);
    }

    private void playBGM() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    private void stopBGM() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public class PomodoroBinder extends Binder {
        public PomodoroService getService() {
            return PomodoroService.this;
        }
    }

    /**
     * 开始一个新的番茄定时器。如果已经有先有的定时器，则会先取消原来的定时器。
     *
     * @param minutes 番茄定时器的时间长度
     */
    public void startNewTomato(int minutes) {
        if (mTimeSuscriber != null) {
            mTimeSuscriber.unsubscribe();
        }
        if (QUICK_TEST) {
            mTomatoTime = 5;
        } else {
            mTomatoTime = minutes * 60;
        }
        mTimeSuscriber = new TimeSubscriber();
        Observable.interval(1, TimeUnit.SECONDS)
            .take(mTomatoTime)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mTimeSuscriber);

        for (WeakReference<TomatoStateListener> listenerRef : mListeners) {
            if (listenerRef.get() != null) {
                listenerRef.get().onTomatoStart();
            }
        }

        playBGM();
        NotificationHelper.cancelBreakNotification(getApplicationContext());
        NotificationHelper.startPomodoroTimerForeground(PomodoroService.this);
    }

    /**
     * 停止当前的计时器。如果当前没有运行中的计时器，则不做反应。
     */
    public void stopTomato() {
        onTimerEnd();

        // notify listeners
        for (WeakReference<TomatoStateListener> listenerRef : mListeners) {
            if (listenerRef.get() != null) {
                listenerRef.get().onTomatoStop();
            }
        }
    }

    /**
     * 注册一个 Listner，每一个调用该方法的地方，都应该有一个配对的 {@link #unregisterTimeChangeListener}
     * 的调用。
     *
     * 为了防止没有释放，listener 在内部会以弱引用的方式保存，listener 的持有者必须自己保证在 unregister
     * 之前，对象都是存活的。
     */
    public void registerTimeChangeListener(@NonNull TomatoStateListener listener) {
        // 补发一个番茄钟已经开始的消息。
        if (mTimeSuscriber != null) {
            listener.onTomatoStart();
        }
        mListeners.add(new WeakReference<>(listener));
    }

    public void unregisterTimeChangeListener(@NonNull TomatoStateListener listener) {
        WeakReference<TomatoStateListener> findRef = null;
        for (WeakReference<TomatoStateListener> listenerRef : mListeners) {
            if (listenerRef.get() == listener) {
                findRef = listenerRef;
                break;
            }
        }

        if (findRef != null) {
            mListeners.remove(findRef);
        }
    }

    /**
     * 番茄状态的 Listener, 所有的回调都会在主线程调用。
     */
    public interface TomatoStateListener {
        void onTomatoStart();
       void onTomatoStop();
        void onTomatoComplete();
        void onTomatoTimeChange(int remainTime, int tomatoTime);
    }

    private class TimeSubscriber extends Subscriber<Long> {
        @Override
        public void onCompleted() {
            onTimerEnd();

            // notify listeners
            for (WeakReference<TomatoStateListener> listenerRef : mListeners) {
                if (listenerRef.get() != null) {
                    listenerRef.get().onTomatoComplete();
                }
            }
            // save data
            long endTime = System.currentTimeMillis();
            long startTime = endTime - mTomatoTime * 1000;
            TomatoRepository tomatoRepository = Injection.provideTaskRespository();
            tomatoRepository.newTomato(startTime, endTime);

            // notify user
            NotificationHelper.showBreakNotification(getApplicationContext());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            stopBGM();
        }

        @Override
        public void onNext(Long time) {
            /* 当前番茄的剩余时间，单位是秒 */
            int remainTime = mTomatoTime - time.intValue() - 1;
            for (WeakReference<TomatoStateListener> listenerRef : mListeners) {
                if (listenerRef.get() != null) {
                    listenerRef.get().onTomatoTimeChange(remainTime, mTomatoTime);
                }
            }
        }
    }
}
