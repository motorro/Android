package ru.merionet.tasks.app.state

class AppError(
    context: AppContext,
    val error: Exception
) : BaseAppState(context) {

}