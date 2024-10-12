package ru.merionet.core.log

import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

interface Logging {
    
    val tag: String get() = javaClass.simpleName

    fun d(throwable: Throwable? = null, message: () -> String) {
        Napier.log(LogLevel.DEBUG, tag, throwable, message())
    }
    fun i(throwable: Throwable? = null, message: () -> String) {
        Napier.log(LogLevel.INFO, tag, throwable, message())
    }
    fun w(throwable: Throwable? = null, message: () -> String) {
        Napier.log(LogLevel.WARNING, tag, throwable, message())
    }
    fun e(throwable: Throwable? = null, message: () -> String) {
        Napier.log(LogLevel.ERROR, tag, throwable, message())
    }
}