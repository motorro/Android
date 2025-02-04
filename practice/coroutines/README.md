# Задание Coroutines

В этом репозитории находится задание по теме Coroutines.
Каждое из заданий выполняется в отдельном фрагменте главного экрана.

## Задание 1. Таймер

![Таймер](/readme/timer.png)

В этом задании вам нужно сделать таймер при помощи `coroutines`.
В коде [TimerFragment.kt](src/main/kotlin/com/motorro/coroutines/ui/timer/TimerFragment.kt) есть функции `startTimer` и `stopTimer` для 
запуска и останова таймера.
Напишите код корутины, которая будет увеличивать счетчик раз в несколько миллисекунд (по вашему выбору) и обновлять значение переменной `time`.
Используйте scope, привязанный ко view фрагмента.

## Задание 1.2 - Перевод таймера на Kotlin Flow

В этом задании вам нужно переписать пример с использованием Kotlin Flow.

- Измените переменную `time` на `timeFlow` типа `MutableStateFlow<Duration>`.
- Создавайте `timeFlow` в `onViewCreated` с учетом изначального значения в `bundle`.
- Подпишитесь на `timeFlow` в `onViewCreated` и обновляйте значение текстового поля `time`.
- Используйте `repeatOnLifecycle` для отслеживания таймера.
- В корутине таймера используйте `emit` для обновления значения `timeFlow`.

## Задание 2.1 Login

![Login](/readme/login.png)

В этом задании вам нужно сделать форму логина при помощи `coroutines`.
Состояние [LoginFragment.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginFragment.kt) определяется в [LoginViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewModel.kt)
при помощи свойства `state` и может принимать одно из следующих значений [LoginViewState](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewState.kt):

- `LoginViewState.Login` - форма входа. Опционально содержит ошибку входа
- `LoginViewState.LoggingIn` - процесс входа
- `LoginViewState.Content` - контент после входа
- `LoginViewState.LoggingOut` - процесс выхода

Реализуйте функции `login` и `logout` в коде [LoginViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewModel.kt).
Требования:

- Используйте экземпляр [LoginApi](src/main/kotlin/com/motorro/coroutines/ui/login/LoginApi.kt) для запуска операций входа и выхода.
- Обратите внимание, что если запустить сетевую операцию на основном потоке, то приложение выдаст ошибку.
- Используйте scope, привязанный к viewModel.
- При входе и выходе показывайте индикатор загрузки, переключая состояние на `LoggingIn` и `LoggingOut` соответственно.
- При успешном входе переключайте состояние на `Content`.
- При ошибке входа переключайте состояние на `Login` и передавайте ошибку в состоянии.

## Задание 2.2 - Login с использованием Kotlin Flow

В этом задании вам нужно переписать пример с использованием Kotlin Flow.

- Измените тип свойства `state` на `StateFlow`.
- Создайте внутреннее поле `stateFlow` типа `MutableStateFlow<LoginViewState>` и экспортируйте его как `state`.
- Напишите внутреннюю функцию `loginFlow`, которая будет запускать сетевую операцию и возвращать `Flow<LoginViewState>`.
- Используйте билдер `flow` для создания холодного потока изменения состояния.
- По мере выполнения операции, посылайте новое состояние при помощи функции `emit`.
- Подпишитесь на `loginFlow` в функции `login` и обновляйте значение `stateFlow` внутри `collect`.
