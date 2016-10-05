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

package io.github.guaidaodl.pomodorotimer;

import io.github.guaidaodl.pomodorotimer.data.TomatoRepository;
import io.github.guaidaodl.pomodorotimer.data.realm.TomatoRealmDataSource;
import io.github.guaidaodl.pomodorotimer.utils.shedulers.BaseSchedulerProvider;
import io.github.guaidaodl.pomodorotimer.utils.shedulers.SchedulerProvider;

/**
 * Created by Guaidaodl on 05/10/2016.
 */
public class Injection {
    public static TomatoRepository provideTaskRespository() {
        return TomatoRepository.getInstance(TomatoRealmDataSource.getInstance());
    }

    public static BaseSchedulerProvider providerBaseSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
