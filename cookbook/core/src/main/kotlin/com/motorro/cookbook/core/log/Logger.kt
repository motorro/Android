package com.motorro.cookbook.core.log

/**
 * Log level
 */
enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
}

/**
 * Logs messages
 */
fun interface Log {
    /**
     * Log message
     * @param tag Logging tag
     * @param level Message level
     * @param throwable Exception if supplied
     * @param message Log message builder
     */
    fun log(tag: String, level: LogLevel, throwable: Throwable?, message: () -> String)
}

/**
 * Global logging object
 */
object Logger : Log {

    private var log: Log? = null

    /**
     * Sets concrete logger
     */
    fun setLogger(log: Log) {
        this.log = log
    }

    override fun log(tag: String, level: LogLevel, throwable: Throwable?, message: () -> String) {
        log?.log(tag, level, throwable, message)
    }
}

/**
 * Implement to get tagged logs
 */
interface Logging {

    val loggingTag: String get() = javaClass.simpleName

    fun d(throwable: Throwable? = null, message: () -> String) {
        Logger.log(loggingTag, LogLevel.DEBUG, throwable, message)
    }
    fun i(throwable: Throwable? = null, message: () -> String) {
        Logger.log(loggingTag, LogLevel.INFO, throwable, message)
    }
    fun w(throwable: Throwable? = null, message: () -> String) {
        Logger.log(loggingTag, LogLevel.WARN, throwable, message)
    }
    fun e(throwable: Throwable? = null, message: () -> String) {
        Logger.log(loggingTag, LogLevel.ERROR, throwable, message)
    }
}