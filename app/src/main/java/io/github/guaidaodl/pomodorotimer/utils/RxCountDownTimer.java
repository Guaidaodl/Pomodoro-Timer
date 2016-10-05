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

package io.github.guaidaodl.pomodorotimer.utils;

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
