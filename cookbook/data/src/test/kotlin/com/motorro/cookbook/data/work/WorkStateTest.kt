package com.motorro.cookbook.data.work

import androidx.work.WorkInfo
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WorkStateTest {

    @Test
    fun `given list of statuses when last is running then status is loading`() {
        val workInfos = listOf(
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.RUNNING,
                tags = emptySet()
            ),
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.SUCCEEDED,
                tags = emptySet()
            )
        )

        val combinedWorkInfo = workInfos.getCombinedWorkInfo()
        assertEquals(WorkState.Loading, combinedWorkInfo)
    }

    @Test
    fun `given list of statuses when one is failed with unauthorized exception then status is error`() {
        val error = UnauthorizedException()
        val errorData = error.toErrorData()
        val workInfos = listOf(
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.SUCCEEDED,
                tags = emptySet()
            ),
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.FAILED,
                tags = emptySet(),
                outputData = errorData
            )
        )

        val combinedWorkInfo = workInfos.getCombinedWorkInfo()

        assertIs<WorkState.Error>(combinedWorkInfo)
        assertEquals(UnauthorizedException::class.simpleName, combinedWorkInfo.className)
        assertEquals(error.message, combinedWorkInfo.message)
        assertEquals(error.isFatal, combinedWorkInfo.isFatal)
    }

    @Test
    fun `given list of statuses when none is running or failed then status is idle`() {
        val workInfos = listOf(
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.SUCCEEDED,
                tags = emptySet()
            ),
            WorkInfo(
                id = UUID.randomUUID(),
                state = WorkInfo.State.CANCELLED,
                tags = emptySet()
            )
        )

        val combinedWorkInfo = workInfos.getCombinedWorkInfo()

        assertEquals(WorkState.Idle, combinedWorkInfo)
    }

    @Test
    fun `when work state is loading returns LceState loading`() {
        val data = "some data"
        assertEquals(LceState.Loading(null), WorkState.Loading.toLce<String>(null))
        assertEquals(LceState.Loading(data), WorkState.Loading.toLce(data))
    }

    @Test
    fun `when work state is idle returns LceState content or loading`() {
        val data = "some data"
        assertEquals(LceState.Loading(null), WorkState.Idle.toLce<String>(null))
        assertEquals(LceState.Content(data), WorkState.Idle.toLce(data))
    }

    @Test
    fun `when work state is error with unauthorized exception returns LceState error with unauthorized exception`() {
        val errorMessage = "Unauthorized"
        val error = WorkState.Error(UnauthorizedException::class.simpleName!!, errorMessage, true)
        val lceState = error.toLce<String>(null)
        assertIs<LceState.Error<String, CoreException>>(lceState)
        assertIs<UnauthorizedException>(lceState.error)
        assertEquals(errorMessage, lceState.error.message)
        assertEquals(true, lceState.error.isFatal)
    }

    @Test
    fun `when work state is error with generic exception returns LceState error with core exception`() {
        val errorMessage = "Generic error message"
        val error = WorkState.Error("SomeOtherException", errorMessage, false)
        val lceState = error.toLce<String>(null)
        assertIs<LceState.Error<String, CoreException>>(lceState)
        assertIs<CoreException>(lceState.error)
        assertEquals(errorMessage, lceState.error.message)
        assertEquals(false, lceState.error.isFatal)
    }
}
