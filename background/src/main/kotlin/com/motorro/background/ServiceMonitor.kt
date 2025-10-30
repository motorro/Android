package com.motorro.background

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

/**
 * Service monitoring
 */
interface ServiceMonitor {

    /**
     * Status of services
     */
    suspend fun monitorRunning(serviceId: ServiceId, timeout: Duration = 1.seconds): Flow<Boolean>

    /**
     * Update service status (call from the service)
     */
    suspend fun update(serviceId: ServiceId, status: Status)

    @JvmInline
    value class ServiceId(val value: String)

    enum class Status(val running: Boolean) {
        STARTED(true),
        RUNNING(true),
        STOPPED(false)
    }

    class Impl @Inject constructor(private val clock: Clock) : ServiceMonitor {
        private val _status = MutableStateFlow<Map<ServiceId, Pair<Status, Instant>>>(emptyMap())

        @OptIn(ExperimentalCoroutinesApi::class)
        override suspend fun monitorRunning(serviceId: ServiceId, timeout: Duration): Flow<Boolean> = _status
            .transformLatest {
                // Take current status or wait for heartbeat
                val currentStatus = it[serviceId]
                when {
                    null != currentStatus -> {
                        val currentTime = clock.now()
                        val (status, lastHeartbeat) = currentStatus
                        when  {
                            status.running && currentTime - lastHeartbeat < timeout -> emit(status)
                            else -> emit(Status.STOPPED)
                        }
                    }
                    else -> {
                        delay(timeout)
                        emit(Status.STOPPED)
                    }
                }
            }
            .flatMapLatest { status ->
                if(status.running) {
                    flow {
                        emit(true)
                        delay(timeout)
                        emit(false)
                    }
                } else {
                    flowOf(false)
                }
            }
            .distinctUntilChanged()

        override suspend fun update(serviceId: ServiceId, status: Status) {
            _status.update { it + (serviceId to (status to clock.now())) }
        }
    }
}