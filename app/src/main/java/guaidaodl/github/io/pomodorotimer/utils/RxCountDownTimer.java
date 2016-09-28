package guaidaodl.github.io.pomodorotimer.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public class RxCountDownTimer {
    /**
     * 创建一个新的倒计时器。从 Time 开始。每隔一秒发送一个消息。
     * @param time 倒计时的时长，单位是秒。
     * @return
     */
    public static Observable<String> newCountDownTimer(final int time) {

        return Observable.interval(1, TimeUnit.SECONDS)
                .take(time)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long secondCount) {
                        final long remainSecond = time - secondCount - 1;

                        long minute = remainSecond / 60;
                        long second = remainSecond % 60;

                        return String.format("%02d:%02d", minute, second);
                    }
                });
    }
}
