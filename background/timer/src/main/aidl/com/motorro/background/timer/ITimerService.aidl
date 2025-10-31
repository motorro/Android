package com.motorro.background.timer;

import com.motorro.background.timer.ITimerCallback;
import com.motorro.background.timer.ITimerState;

/**
 * Timer service
 */
interface ITimerService {
    ITimerState getState();

    void subscribe(ITimerCallback callback);
    void unsubscribe(ITimerCallback callback);

    void start();
    void stop();
    void toggle();
}

