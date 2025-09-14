package com.motorro.statemachine.register.state

import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.register.data.RegisterDataState

/**
 * Register flow state factory
 */
internal interface RegisterFlowStateFactory {
    /**
     * Registration data form
     */
    fun dataForm(data: RegisterDataState? = null): BaseRegisterState

    /**
     * Eula form
     */
    fun eulaForm(data: RegisterDataState): BaseRegisterState

    /**
     * Running registration
     */
    fun registering(data: RegisterDataState): BaseRegisterState

    /**
     * Registration error
     */
    fun registrationError(data: RegisterDataState, error: AppException): BaseRegisterState
}

internal class RegisterFlowStateFactoryImpl(flowHost: AuthFlowHost) : RegisterFlowStateFactory {
    private val context: RegisterContext = object : RegisterContext {
        override val flowHost: AuthFlowHost = flowHost
        override val factory: RegisterFlowStateFactory = this@RegisterFlowStateFactoryImpl
    }

    override fun dataForm(data: RegisterDataState?) = RegisterFormState(
        context,
        data ?: RegisterDataState()
    )

    override fun eulaForm(data: RegisterDataState) = RegisterEulaState(
        context,
        data
    )

    override fun registering(data: RegisterDataState) = RegisteringState.Factory()(
        context,
        data
    )

    override fun registrationError(data: RegisterDataState, error: AppException) = RegisterErrorState(
        context,
        data,
        error
    )
}