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

package io.github.guaidaodl.pomodorotimer.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.guaidaodl.pomodorotimer.data.realm.Tomato;
import io.github.guaidaodl.pomodorotimer.data.realm.TomatoRealmDataSource;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class TomatoRepository implements TomatoDataSource {
    private static TomatoRepository INSTANCE = null;

    public synchronized static TomatoRepository getInstance(@NonNull TomatoRealmDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TomatoRepository(dataSource);
        }
        return INSTANCE;
    }

    private final TomatoRealmDataSource mLocalDataSource;

    private TomatoRepository(@NonNull TomatoRealmDataSource dataSource){
        mLocalDataSource = checkNotNull(dataSource);
    }

    @NonNull
    @Override
    public Tomato newTomato(long startTime, long endTime) {
        return mLocalDataSource.newTomato(startTime, endTime);
    }

    @Override
    public Observable<? extends List<Tomato>> getAllTomatos() {
        return mLocalDataSource.getAllTomatos();
    }

    @Override
    public Observable<? extends List<Tomato>> getTomatoWithStartTimeBetween(long t1, long t2) {
        return mLocalDataSource.getTomatoWithStartTimeBetween(t1, t2);
    }

    @Override
    public Observable<Integer> getTomatosCount() {
        return mLocalDataSource.getTomatosCount();
    }
}
