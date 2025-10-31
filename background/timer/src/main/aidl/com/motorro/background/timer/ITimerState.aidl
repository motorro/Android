package com.motorro.background.timer;

/**
 * Timer state
 */
interface ITimerState {
    boolean isRunning();
    long getEpochMillis();
}