package com.motorro.background

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class ServiceMonitorTest {
    private lateinit var dispatcher: TestDispatcher
    private lateinit var clock: Clock
    private lateinit var monitor: ServiceMonitor

    private val timeout = 1.seconds
    private val serviceId1 = ServiceMonitor.ServiceId("service1")
    private val serviceId2 = ServiceMonitor.ServiceId("service2")

    @Before
    fun init() {
        dispatcher = StandardTestDispatcher()
        clock = mockk {
            every { now() } answers {
                Instant.fromEpochMilliseconds(dispatcher.scheduler.currentTime)
            }
        }
        monitor = ServiceMonitor.Impl(clock)
    }

    private fun test(block: suspend TestScope.() -> Unit) = runTest(
        context = dispatcher,
        testBody = block
    )

    @Test
    fun startsWithNotRunningIfNoData() = test {
        val status = backgroundScope.async {
            monitor.monitorRunning(serviceId1, timeout).first()
        }

        assertFalse(status.await())
    }

    @Test
    fun startsWithRunningIfHasStartedStatus() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        val status = backgroundScope.async {
            monitor.monitorRunning(serviceId1, timeout).first()
        }

        assertTrue(status.await())
    }

    @Test
    fun startsWithRunningIfServiceReportedWithinTimeout() = test {
        val status = backgroundScope.async {
            monitor.monitorRunning(serviceId1, timeout).first()
        }

        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)

        assertTrue(status.await())
    }

    @Test
    fun startsWithStoppedIfServiceHasVoidStatus() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        advanceTimeBy(timeout + 100.milliseconds)
        val status = backgroundScope.async {
            monitor.monitorRunning(serviceId1, timeout).first()
        }

        assertFalse(status.await())
    }

    @Test
    fun changesToNotRunningIfNoUpdatesFromService() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        val statuses = mutableListOf<Boolean>()
        backgroundScope.launch {
            monitor.monitorRunning(serviceId1, timeout).collect {
                statuses.add(it)
            }
        }

        advanceTimeBy(timeout + 100.milliseconds)

        assertEquals(2, statuses.size)
        assertTrue(statuses[0])
        assertFalse(statuses[1])
    }

    @Test
    fun staysRunningIfServiceUpdatedWithinTimeout() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        val statuses = mutableListOf<Boolean>()
        backgroundScope.launch {
            monitor.monitorRunning(serviceId1, timeout).collect {
                statuses.add(it)
            }
        }

        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)
        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)
        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)
        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)

        assertEquals(1, statuses.size)
        assertTrue(statuses[0])
    }

    @Test
    fun timesOutIfServiceDoesNotUpdateWithinTimeout() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        val statuses = mutableListOf<Boolean>()
        backgroundScope.launch {
            monitor.monitorRunning(serviceId1, timeout).collect {
                statuses.add(it)
            }
        }

        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)

        advanceTimeBy(timeout + 100.milliseconds)

        assertEquals(2, statuses.size)
        assertTrue(statuses[0])
        assertFalse(statuses[1])
    }

    @Test
    fun stopsIfServiceStops() = test {
        monitor.update(serviceId1, ServiceMonitor.Status.STARTED)
        monitor.update(serviceId2, ServiceMonitor.Status.STARTED)

        val statuses = mutableListOf<Boolean>()
        backgroundScope.launch {
            monitor.monitorRunning(serviceId1, timeout).collect {
                statuses.add(it)
            }
        }

        advanceTimeBy(timeout / 2)
        monitor.update(serviceId1, ServiceMonitor.Status.RUNNING)
        monitor.update(serviceId1, ServiceMonitor.Status.STOPPED)

        advanceTimeBy(timeout)

        assertEquals(2, statuses.size)
        assertTrue(statuses[0])
        assertFalse(statuses[1])
    }
}