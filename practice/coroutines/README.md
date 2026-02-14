# Задание Coroutines

***[Read in English](#coroutines-assignment)***

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
- Создайте внутреннее поле `_stateFlow` типа `MutableStateFlow<LoginViewState>` и экспортируйте его как `state`.
- Напишите внутреннюю функцию `loginFlow`, которая будет запускать сетевую операцию и возвращать `Flow<LoginViewState>`.
- Используйте билдер `flow` для создания холодного потока изменения состояния.
- По мере выполнения операции, посылайте новое состояние при помощи функции `emit`.
- Подпишитесь на `loginFlow` в функции `login` и обновляйте значение `_stateFlow` внутри `collect`.

## Задание 3. Speed-test

![Speed-test](/readme/speedtest.png)

В этом задании вам нужно сделать эмулятор speed-test при помощи `coroutines`.
Мы будем тренироваться запускать несколько корутин одновременно и усреднять их время выполнения.
В коде [NetworkViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/network/NetworkViewModel.kt) определена функция `emulateBlockingNetworkRequest`,
которая эмулирует сетевой запрос. Функция возвращает время выполнения запроса в миллисекундах или ошибку.
Вам нужно реализовать метод модели `startTest`, который запускает несколько запросов одновременно и усредняет время выполнения.

- Используйте scope, привязанный к viewModel.
- Запускайте несколько параллельных корутин.
- Отфильтруйте удачные результаты.
- Усредните время выполнения запросов.
- Результат поместите в переменную `_result`.
- На время выполнения операции, показывайте индикатор загрузки, устанавливая значение `_running` в `true`.

---

# Coroutines Assignment

This repository contains an assignment on the topic of Coroutines.
Each task is performed in a separate fragment of the main screen.

## Task 1. Timer

![Timer](/readme/timer.png)

In this task, you need to create a timer using `coroutines`.
The code in [TimerFragment.kt](src/main/kotlin/com/motorro/coroutines/ui/timer/TimerFragment.kt) has `startTimer` and `stopTimer` functions to
start and stop the timer.
Write the code for a coroutine that will increment a counter every few milliseconds (of your choice) and update the value of the `time` variable.
Use a scope tied to the fragment's view.

## Task 1.2 - Converting the timer to Kotlin Flow

In this task, you need to rewrite the example using Kotlin Flow.

- Change the `time` variable to `timeFlow` of type `MutableStateFlow<Duration>`.
- Create `timeFlow` in `onViewCreated`, taking into account the initial value in the `bundle`.
- Subscribe to `timeFlow` in `onViewCreated` and update the value of the `time` text field.
- Use `repeatOnLifecycle` to track the timer.
- In the timer coroutine, use `emit` to update the value of `timeFlow`.

## Task 2.1 Login

![Login](/readme/login.png)

In this task, you need to create a login form using `coroutines`.
The state of [LoginFragment.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginFragment.kt) is defined in [LoginViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewModel.kt)
using the `state` property and can take one of the following values from [LoginViewState](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewState.kt):

- `LoginViewState.Login` - login form. Optionally contains a login error.
- `LoginViewState.LoggingIn` - login process.
- `LoginViewState.Content` - content after login.
- `LoginViewState.LoggingOut` - logout process.

Implement the `login` and `logout` functions in the [LoginViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/login/LoginViewModel.kt) code.
Requirements:

- Use an instance of [LoginApi](src/main/kotlin/com/motorro/coroutines/ui/login/LoginApi.kt) to run login and logout operations.
- Note that if you run a network operation on the main thread, the application will throw an error.
- Use a scope tied to the viewModel.
- Show a loading indicator during login and logout by switching the state to `LoggingIn` and `LoggingOut` respectively.
- On successful login, switch the state to `Content`.
- On login error, switch the state to `Login` and pass the error in the state.

## Task 2.2 - Login with Kotlin Flow

In this task, you need to rewrite the example using Kotlin Flow.

- Change the type of the `state` property to `StateFlow`.
- Create an internal `_stateFlow` field of type `MutableStateFlow<LoginViewState>` and expose it as `state`.
- Write an internal `loginFlow` function that will run a network operation and return a `Flow<LoginViewState>`.
- Use the `flow` builder to create a cold flow of state changes.
- As the operation progresses, send a new state using the `emit` function.
- Subscribe to `loginFlow` in the `login` function and update the value of `_stateFlow` inside `collect`.

## Task 3. Speed-test

![Speed-test](/readme/speedtest.png)

In this task, you need to create a speed-test emulator using `coroutines`.
We will practice running several coroutines simultaneously and averaging their execution time.
The code in [NetworkViewModel.kt](src/main/kotlin/com/motorro/coroutines/ui/network/NetworkViewModel.kt) defines the `emulateBlockingNetworkRequest` function,
which emulates a network request. The function returns the request execution time in milliseconds or an error.
You need to implement the `startTest` model method, which runs several requests simultaneously and averages the execution time.

- Use a scope tied to the viewModel.
- Run several parallel coroutines.
- Filter out the successful results.
- Average the request execution times.
- Place the result in the `_result` variable.
- During the operation, show a loading indicator by setting the value of `_running` to `true`.
