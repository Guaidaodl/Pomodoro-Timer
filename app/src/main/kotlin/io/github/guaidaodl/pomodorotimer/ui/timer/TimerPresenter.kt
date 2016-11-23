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
 */

package io.github.guaidaodl.pomodorotimer.ui.timer

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import io.github.guaidaodl.pomodorotimer.service.PomodoroService

/**
 * Created by Guaidaodl on 22/11/2016.
 */
class TimerPresenter : TimerContract.Presenter {
    private var mPomodoroService: PomodoroService? = null

    private val mPomodoroServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mPomodoroService = (service as PomodoroService.PomodoroBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun bindToTimerSevice(context: Context) {
        PomodoroService.bind(context, mPomodoroServiceConnection)
    }

    override fun startTimer() {
    }

    override fun stopTimer() {
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    private fun bindToService() {
    }
}