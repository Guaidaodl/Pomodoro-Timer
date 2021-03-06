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
import rx.Observable;

/**
 * Main entry to tomato data.
 *
 * Because of realm, you should not set observable schedular. All the query is
 * async default.
 */
public interface TomatoDataSource {
    /**
     * Create a new Tomato with the
     * @param startTime the start time of tomato.
     * @param endTime   then end time of tomato.
     */
    @NonNull
    Tomato newTomato(long startTime, long endTime);

    Observable<? extends List<Tomato>> getAllTomatos();

    Observable<? extends List<Tomato>> getTomatoWithStartTimeBetween(long startTime, long endTime);

    Observable<Integer> getTomatosCount();
}
