package ru.merionet.core.error

/**
 * Marks the implementing object (usually an error) with a 'retriable' flag
 * Used to distinguish errors that could be retried from those which fatally terminate the logic flow
 */
interface WithRetry {
    /**
     * If true - the error may be temporary and worth to retry
     */
    val retriable: Boolean
}