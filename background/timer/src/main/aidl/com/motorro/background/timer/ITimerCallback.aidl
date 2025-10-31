package com.motorro.background.timer;

import com.motorro.background.timer.ITimerState;

/**
 * Timer calback
 */
interface ITimerCallback {
    void onStateChange(in ITimerState state);
}
