package com.motorro.multithreading

import android.util.Log

inline fun log(block: () -> String) {
    Log.i("MT", "Thread: ${Thread.currentThread().name}: " + block())
}