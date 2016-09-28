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

import guaidaodl.github.io.pomodorotimer.utils.RxCountDownTimer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by linyb on 28/09/2016.
 */

public class PomodoroService extends Service {
    private final static String TAG = "PomodoroService";
    private final static int DEFAULT_TOMATO_TIME = 25 * 60;

    public static void start(Context context) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.startService(intent);
    }

    public static void bind(@NonNull Context context, @NonNull ServiceConnection conn) {
        Intent intent = new Intent(context, PomodoroService.class);

        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private MediaPlayer mMediaPlayer;

    /** 定时器的订阅者 */
    @Nullable
    private TimeSuscriber mTimeSuscriber;

    private PomodoroBinder mBinder = new PomodoroBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public class PomodoroBinder extends Binder {
        /**
         * 开始一个新的番茄定时器。如果已经有先有的定时器，则会先取消原来的定时器。
         */
        public void startNewTomato() {
            if (mTimeSuscriber != null) {
                mTimeSuscriber.unsubscribe();
            }
            mTimeSuscriber = new TimeSuscriber();
            RxCountDownTimer.newCountDownTimer(DEFAULT_TOMATO_TIME)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mTimeSuscriber);
        }

        /**
         * 停止当前的计时器。如果当前没有运行中的计时器，则不做反应。
         */
        public void stopTomato() {
            if (mTimeSuscriber != null) {
                mTimeSuscriber.unsubscribe();
            }
        }
    }

    private class TimeSuscriber extends Subscriber<String> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(String time) {
            Log.d(TAG, time);
        }
    };
}
