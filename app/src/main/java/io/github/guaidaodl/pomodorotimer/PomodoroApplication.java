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

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.github.guaidaodl.pomodorotimer.data.TomatoRepository;
import io.github.guaidaodl.pomodorotimer.data.realm.TomatoRealmDataSource;
import io.github.guaidaodl.pomodorotimer.service.PomodoroService;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PomodoroApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        // Debug tools
        LeakCanary.install(this);

        // Database
        setUpRealm();

        PomodoroService.start(this);
    }

    private void setUpRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("Pomodoro.realm")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(configuration);

        // In first launch, getDefaultInstance will create a realm file
        Realm.getDefaultInstance();
    }
}
