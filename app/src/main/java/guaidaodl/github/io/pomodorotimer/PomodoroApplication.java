package guaidaodl.github.io.pomodorotimer;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import guaidaodl.github.io.pomodorotimer.service.PomodoroService;

public class PomodoroApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
        PomodoroService.start(this);
    }
}
