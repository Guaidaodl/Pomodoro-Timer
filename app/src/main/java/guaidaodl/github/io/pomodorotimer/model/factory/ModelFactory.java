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

package guaidaodl.github.io.pomodorotimer.model.factory;

import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;

import guaidaodl.github.io.pomodorotimer.model.realm.Tomato;
import io.realm.Realm;

public class ModelFactory {
    private static final ModelFactory INSTANCE = new ModelFactory();

    public static ModelFactory getInstance() {
        return INSTANCE;
    }

    private Realm mRealm = Realm.getDefaultInstance();
    private ModelFactory() {
    }


    /**
     * 创建一个番茄。指定 startTime 和 endTime。
     *
     * @param startTime 开始的时间, 从 1971 年开始的毫秒数
     * @param endTime   结束的时间
     *
     * @return 创建的 Tomato
     */
    public Tomato newTomato(long startTime, long endTime) {
        if (startTime <= 0) {
            throw new IllegalArgumentException("start time must bigger than zero, startTime = "
                                                +  startTime);
        }

        if (startTime > endTime) {
            throw new IllegalArgumentException("End time must bigger than start time" +
                    "actual startTime = " + startTime + " endTime = " + endTime);
        }

        mRealm.beginTransaction();
        Tomato tomato = mRealm.createObject(Tomato.class);
        tomato.setStartTime(startTime);
        tomato.setEndTime(endTime);
        tomato.setBreakCount(0);

        mRealm.commitTransaction();
        return tomato;
    }

    @VisibleForTesting
    public void closeRealm() {
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
