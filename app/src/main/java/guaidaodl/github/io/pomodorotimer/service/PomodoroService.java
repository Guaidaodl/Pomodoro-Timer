package guaidaodl.github.io.pomodorotimer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class PomodoroService extends Service {
    private List<TomatoStateListener> mListeners = new LinkedList<>();

    /** 当前番茄的总时长，单位是秒 */
    private int mTomatoTime = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.startService(intent);
    }

    public static void bind(@NonNull Context context, @NonNull ServiceConnection conn) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public static void unbind(@NonNull Context context, @NonNull ServiceConnection conn) {
        context.unbindService(conn);
    }

    private MediaPlayer mMediaPlayer;

    /** 定时器的订阅者 */
    @Nullable
    private TimeSuscriber mTimeSuscriber;

    private PomodoroBinder mBinder = new PomodoroBinder();

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
    }

    public class PomodoroBinder extends Binder {
        /**
         * 开始一个新的番茄定时器。如果已经有先有的定时器，则会先取消原来的定时器。
         *
         * @param minutes 番茄定时器的时间长度
         */
        public void startNewTomato(int minutes) {
            if (mTimeSuscriber != null) {
                mTimeSuscriber.unsubscribe();
            }
            mTomatoTime = minutes * 60;
            mTimeSuscriber = new TimeSuscriber();
            Observable.interval(1, TimeUnit.SECONDS)
                    .take(minutes * 60)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mTimeSuscriber);

            for (TomatoStateListener listener : mListeners) {
                listener.onTomatoStart();
            }
        }

        /**
         * 停止当前的计时器。如果当前没有运行中的计时器，则不做反应。
         */
        public void stopTomato() {
            if (mTimeSuscriber != null) {
                mTimeSuscriber.unsubscribe();
                mTimeSuscriber = null;
            }
        }

        public void registerTimeChangeListener(TomatoStateListener listener) {
            // 补发一个番茄钟已经开始的消息。
            if (mTimeSuscriber != null) {
                listener.onTomatoStart();
            }
            mListeners.add(listener);
        }

        public void unregisterTimeChangeListener(TomatoStateListener listener) {
            mListeners.remove(listener);
        }
    }

    /**
     * 番茄状态的 Listener, 所有的回调都会在主线程调用。
     */
    public interface TomatoStateListener {
        void onTomatoStart();
        void onTomatoFinish();
        void onTomatoTimeChange(int remainTime, int tomatoTime);
    }

    private class TimeSuscriber extends Subscriber<Long> {

        @Override
        public void onCompleted() {
            for (TomatoStateListener listener : mListeners) {
                listener.onTomatoFinish();
            }
            mTimeSuscriber.unsubscribe();
            mTimeSuscriber = null;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Long time) {
            /* 当前番茄的剩余时间，单位是秒 */
            int remainTime = mTomatoTime - time.intValue() - 1;
            for (TomatoStateListener listener : mListeners) {
                listener.onTomatoTimeChange(remainTime, mTomatoTime);
            }
        }
    }
}
