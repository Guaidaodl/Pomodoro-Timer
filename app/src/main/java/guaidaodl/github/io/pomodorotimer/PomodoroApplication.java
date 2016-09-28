package guaidaodl.github.io.pomodorotimer;

import android.app.Application;

import guaidaodl.github.io.pomodorotimer.service.PomodoroService;

public class PomodoroApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PomodoroService.start(this);
    }
}
