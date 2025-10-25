package com.motorro.notifications.state

import android.content.Intent
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.core.log.Logging
import com.motorro.notifications.api.MainScreenPageData
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState
import com.motorro.notifications.data.NotificationAction
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
    fun init(intent: Intent): MainScreenState = permissionsCheck(intent)

    /**
     * Checks for all required permissions
     */
    fun permissionsCheck(intent: Intent): MainScreenState

    /**
     * Asks for notification permission
     */
    fun askingForPermissions(intent: Intent): MainScreenState

    /**
     * Creates notification channels
     */
    fun creatingNotificationChannels(intent: Intent): MainScreenState

    /**
     * Registers FCM
     */
    fun gettingFcmToken(intent: Intent): MainScreenState

    /**
     * FCM registration error
     */
    fun fcmRegistrationError(error: Throwable, intent: Intent): MainScreenState

    /**
     * Checks the start action
     */
    fun startUp(intent: Intent): MainScreenState

    /**
     * Processes action
     */
    fun handlingAction(startAction: NotificationAction): MainScreenState

    /**
     * Creates state if page matches [action]
     */
    fun pageForAction(action: NotificationAction): MainScreenState?

    /**
     * Runs page flow
     * @param page Page to run
     * @param action Page data
     * @return Page state
     */
    fun page(page: MainScreenPageData, action: NotificationAction? = null): MainScreenState

    /**
     * Navigates to content
     */
    fun mainContent(): MainScreenState

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
        private val createRegisterFcm: Provider<GettingFcmTokenState.Factory>,
        private val createHandlingAction: Provider<HandlingActionState.Factory>,
        private val pages: @JvmSuppressWildcards ImmutableList<MainScreenStateApi<*, *>>,
    ) : MainScreenStateFactory, Logging {

        private val context = object : MainScreenContext {
            override val factory: MainScreenStateFactory = this@Impl
        }

        override fun permissionsCheck(intent: Intent): MainScreenState = createCheck.get()(context, intent)

        override fun askingForPermissions(intent: Intent): MainScreenState = GettingNotificationEnabledState(context, intent)

        override fun creatingNotificationChannels(intent: Intent): MainScreenState = createCreateChannels.get()(context, intent)

        override fun gettingFcmToken(intent: Intent): MainScreenState = createRegisterFcm.get()(context, intent)

        override fun fcmRegistrationError(error: Throwable, intent: Intent): MainScreenState  = ErrorState(
            context,
            error,
            { terminated() },
            { gettingFcmToken(intent) }
        )

        override fun startUp(intent: Intent) = StartupState(context, intent)

        override fun handlingAction(startAction: NotificationAction) = createHandlingAction.get()(
            context,
            startAction
        )

        override fun pageForAction(action: NotificationAction): MainScreenState? =
            pages.firstOrNull { page -> page.data.matchesAction(action) }
            ?.data
            ?.also { i { "Found page for action: $action: $it" } }
            ?.let { page -> page(page, action)}

        override fun page(page: MainScreenPageData, action: NotificationAction?): MainScreenState = pages.first { page == it.data }.let { api ->
            object : ProxyMachineState<MainScreenGesture, MainScreenViewState, Any, Any>(api.getInitialViewState()) {
                @Suppress("UNCHECKED_CAST")
                override fun init() = api.init(action) as CommonMachineState<Any, Any>

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

        override fun mainContent(): MainScreenState = page(pages.first().data)

        override fun terminated() = TerminatedState(context)
    }
}