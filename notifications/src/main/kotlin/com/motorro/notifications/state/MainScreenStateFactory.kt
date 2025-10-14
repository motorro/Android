package com.motorro.notifications.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.notifications.api.MainScreenPageData
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject
import javax.inject.Provider

/**
 * State factory
 */
interface MainScreenStateFactory {
    /**
     * Checks for notification permissions
     */
    fun init(): MainScreenState = permissionsCheck()

    /**
     * Checks for all required permissions
     */
    fun permissionsCheck(): MainScreenState

    /**
     * Asks for notification permission
     */
    fun askingForPermissions(): MainScreenState

    /**
     * Creates notification channels
     */
    fun creatingNotificationChannels(): MainScreenState

    /**
     * Navigates to content
     */
    fun content(): MainScreenState

    /**
     * Runs page flow
     * @param page Page to run
     * @param data Page data
     * @return Page state
     */
    fun page(page: MainScreenPageData, data: Any? = null): MainScreenState

    /**
     * Terminates
     */
    fun terminated(): MainScreenState

    /**
     * [MainScreenStateFactory] Implementation
     */
    class Impl @Inject constructor(
        private val createCheck: Provider<NotificationCheckState.Factory>,
        private val createCreateChannels: Provider<CreateNotificationChannelsState.Factory>,
        private val pages: @JvmSuppressWildcards ImmutableList<MainScreenStateApi<*, *>>
    ) : MainScreenStateFactory {

        private val context = object : MainScreenContext {
            override val factory: MainScreenStateFactory = this@Impl
        }

        override fun permissionsCheck(): NotificationCheckState = createCheck.get()(context)

        override fun askingForPermissions() = GettingNotificationEnabledState(context)

        override fun creatingNotificationChannels() = createCreateChannels.get()(context)

        override fun content(): MainScreenState = page(pages.first().data)

        override fun page(page: MainScreenPageData, data: Any?): MainScreenState = pages.first { page == it.data }.let { api ->
            object : ProxyMachineState<MainScreenGesture, MainScreenViewState, Any, Any>(api.getInitialViewState()) {
                @Suppress("UNCHECKED_CAST")
                override fun init() = api.init(data) as CommonMachineState<Any, Any>

                override fun doProcess(gesture: MainScreenGesture) {
                    when(gesture) {
                        is MainScreenGesture.Back -> {
                            val currentIndex = pages.indexOfFirst { it.data == page }
                            if (0 == currentIndex) {
                                setMachineState(terminated())
                            } else {
                                setMachineState(page(pages[currentIndex - 1].data))
                            }
                        }
                        is MainScreenGesture.Navigate -> setMachineState(page(gesture.page))
                        else -> super.doProcess(gesture)
                    }
                }

                override fun mapGesture(parent: MainScreenGesture): Any? = api.mapGesture(parent)

                override fun mapUiState(child: Any) = MainScreenViewState.Page(
                    page,
                    child
                )
            }
        }

        override fun terminated() = TerminatedState(context)
    }
}