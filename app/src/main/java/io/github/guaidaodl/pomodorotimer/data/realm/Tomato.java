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

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Tomato extends RealmObject {
    /** 番茄开始的时间 */
    @Index private long mStartTime;

    /** 番茄结束的时间 */
    private long mEndTime;

    /** 该番茄中发生打断的次数 */
    private long mBreakCount;

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;
    }

    public long getBreakCount() {
        return mBreakCount;
    }

    public void setBreakCount(long breakCount) {
        mBreakCount = breakCount;
    }

}
