/*
 *  Copyright (c) 2016.  Guaidaodl
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
 *
 */

package io.github.guaidaodl.pomodorotimer.data.realm;

import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.github.guaidaodl.pomodorotimer.data.TomatoDataSource;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

public class TomatoRealmDataSource implements TomatoDataSource {
    private static final TomatoRealmDataSource INSTANCE = new TomatoRealmDataSource();

    public static TomatoRealmDataSource getInstance() {
        return INSTANCE;
    }

    private TomatoRealmDataSource(){
    }

    /**
     * 创建一个番茄。指定 startTime 和 endTime。
     *
     * @param startTime 开始的时间, 从 1971 年开始的毫秒数
     * @param endTime   结束的时间
     *
     * @return 创建的 Tomato
     */
    @Override
    public Tomato newTomato(long startTime, long endTime) {
        Realm realm = Realm.getDefaultInstance();
        if (startTime <= 0) {
            throw new IllegalArgumentException("start time must bigger than zero, startTime = "
                    +  startTime);
        }

        if (startTime > endTime) {
            throw new IllegalArgumentException("End time must bigger than start time" +
                    "actual startTime = " + startTime + " endTime = " + endTime);
        }

        realm.beginTransaction();
        Tomato tomato = realm.createObject(Tomato.class);
        tomato.setStartTime(startTime);
        tomato.setEndTime(endTime);
        tomato.setBreakCount(0);

        realm.commitTransaction();
        return tomato;
    }

    @Override
    public Observable<? extends List<Tomato>> getAllTomatos() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Tomato.class).findAllAsync().asObservable();
    }

    @Override
    public Observable<Integer> getTomatosCount() {
        Realm realm = Realm.getDefaultInstance();

        return Observable.just(realm.where(Tomato.class).findAll().size());
    }
}
